package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.io.PDBFileParser;
import org.biojava.bio.structure.io.PDBFileReader;
import org.biojava3.core.util.InputStreamProvider;
import org.biojava3.structure.StructureIO;

import sg.edu.ntu.aalhossary.fyp2014.common.AminoAcid;
import sg.edu.ntu.aalhossary.fyp2014.common.Chain;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.common.Molecule;
import sg.edu.ntu.aalhossary.fyp2014.common.Residue;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.DataManager;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;

public class IOutility {

	/**
	 * 
	 * @param pathname
	 * @return inputStream or null if file does not exist
	 */
static final Map<String, String> map = new HashMap<>();
	
	
	static {
		map.put("GLY", "G");
		map.put("ALA", "A");
		map.put("VAL", "V");
		map.put("LEU", "L");
		map.put("ILE", "I");
		map.put("PRO", "P");
		map.put("PHE", "F");
		map.put("TYR", "Y");
		map.put("TRP", "W");
		map.put("SER", "S");
		map.put("THR", "T");
		map.put("CYS", "C");
		map.put("MET", "M");
		map.put("ASN", "N");
		map.put("GLN", "Q");
		map.put("LYS", "K");
		map.put("ARG", "R");
		map.put("HIS", "H");
		map.put("ASP", "D");
		map.put("GLU", "E");
	}
	public static InputStream fastafileToInputStream (String pathname){
	File f = new File(pathname);
	InputStream inputStream = null;
		if (f.exists()){
			InputStreamProvider isp = new InputStreamProvider();
			try {
				 inputStream = isp.getInputStream(pathname);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return inputStream;
	}

	public static String pdbFileTofastaString(String pathname){
			
			StringBuilder sb = new StringBuilder();
			sb.append(">").append("\n");
			PDBFileReader pdbreader = new PDBFileReader();
			Structure structure = null;
			String sequence = null;
			try{
				structure=pdbreader.getStructure(pathname);
				for(org.biojava.bio.structure.Chain c :structure.getChains()){
					sequence = c.getAtomSequence();
					sb.append(sequence);
					}
				}catch (IOException e){
				e.printStackTrace();
			}
			return sb.toString();
		
		}

	public static String pdbStringTofastaString(String pdbString){
		
		InputStream is = new ByteArrayInputStream(pdbString.getBytes(StandardCharsets.UTF_8));
		PDBFileParser pdbreader = new PDBFileParser();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		sb.append(">").append("\n");
		Structure structure = null;
		String sequence = null;
		try{
			structure=pdbreader.parsePDBFile(br);
			for(org.biojava.bio.structure.Chain c :structure.getChains()){
				sequence = c.getAtomSequence();
				sb.append(sequence);
				}
			}catch (IOException e){
			e.printStackTrace();
		}
	//	System.out.println(sb.toString());
		return sb.toString();
		
	}

	public static InputStream stringToInputStream(String input){
		InputStream inputStream = null;
		
			if(input==null){
				throw new IllegalStateException("Input String not yet set");
				
			}else{
				inputStream = new ByteArrayInputStream(input.getBytes());
			}
				
		return inputStream;
	}



	public static String ObjectstoFasta(ArrayList<Chain> chains){
		StringBuilder sb = new StringBuilder();
		sb.append(">").append("\n");
		char x;
		for (Chain c:chains){
			for(Residue res:c.getResidues()){
			AminoAcid aa = (AminoAcid)res;	
			x = aa.getAminoChar();	
			sb.append(x);
			}
		}
		return sb.toString();
	}

	public static String ObjectstoPdbString(ArrayList<Model> models){
		
		return DataManager.modelToPDB(models);
	
	}
	
	public static void WriteToFile (ArrayList<Object> output) throws IOException{
		
		if(output.get(0) instanceof STRIDE_Output){
			File file = new File("/Users/benkong/temp/IUPred_Output.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			ArrayList<String> temp = new ArrayList<String>();
			for(Object o:output){
				String string = ((STRIDE_Output)o).toString();
				temp.add(string);
				for(String s:temp){
					bw.write(s);
					bw.close();
				}
			}		
		}else if(output.get(0) instanceof IUPRED_Output){
			File file = new File("/Users/benkong/temp/IUPRred_Output.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			ArrayList<String> temp = new ArrayList<String>();
			for(Object o:output){
				String string = ((IUPRED_Output)o).toString();
				temp.add(string);
				for(String s:temp){
					bw.write(s);
					bw.close();
				}
			}	
		}	
	}
	@SuppressWarnings({ "deprecation" })
	public static ArrayList<AminoAcid> createAminoAcid(String pathname,STRIDE_Output stride){
		
		 PDBFileReader pdbreader = new PDBFileReader();
		 Structure pdb = null;
		 
		 ArrayList<AminoAcid> regions = new ArrayList<AminoAcid>();
		 
		 try {
			pdb = pdbreader.getStructure(pathname);
	
				org.biojava.bio.structure.Chain c = pdb.getChainByPDB(stride.chain);
				for(Group a:c.getGroupsByPDB(stride.startpos, stride.endpos)){
					AminoAcid aa = new AminoAcid();
					aa.setName(a.getPDBName());
					aa.setAminoChar(map.get(a.getPDBName()).charAt(0));
					Chain chain = new Chain();
					aa.setParent(chain);
					chain.setChainName(a.getChainId());
					aa.setResidueSeqNum(Integer.parseInt(a.getPDBCode()));
					aa.setAtomList(a.getAtoms());	
					regions.add(aa);
				}
			
		} catch (IOException | StructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return regions;
		
	}
	
	public static void createObjectsFromPDB(ArrayList<STRIDE_Output>output, String pathname){
		ArrayList<ArrayList<AminoAcid>> PaminoAcid=new ArrayList<ArrayList<AminoAcid>>();
		for(STRIDE_Output o : output){
			PaminoAcid.add(IOutility.createAminoAcid(pathname,o));
		}
		
		
	}
	public static ArrayList<Residue> createObjectsFromModel(STRIDE_Output output, Object input){
		Model m = (Model)input;
		ArrayList<Residue> Presidues=new ArrayList<Residue>();
			for(Molecule mol:m.getMolecules()){
				for(Chain c:mol.getChains()){
					if(c.name.equals(output.chain)){
						Presidues = c.getResidues(output.startpos, output.endpos);
					}	
				}
			}	
					
		return Presidues;
		
	}
}
