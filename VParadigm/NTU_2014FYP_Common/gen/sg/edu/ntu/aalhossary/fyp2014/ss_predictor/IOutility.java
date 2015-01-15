package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileParser;
import org.biojava.bio.structure.io.PDBFileReader;
import org.biojava3.core.util.InputStreamProvider;
import org.biojava3.structure.StructureIO;

import sg.edu.ntu.aalhossary.fyp2014.common.AminoAcid;
import sg.edu.ntu.aalhossary.fyp2014.common.Chain;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.common.Residue;

public class IOutility {

	/**
	 * 
	 * @param pathname
	 * @return inputStream or null if file does not exist
	 */
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
			
	//		InputStream is = this.fastafileToInputStream(pathname);
			PDBFileReader pdbreader = new PDBFileReader();
			Structure structure = null;
			String fasta = null;
			try{
				structure=pdbreader.getStructure(pathname);
				for(org.biojava.bio.structure.Chain c :structure.getChains()){
					fasta = c.getAtomSequence();
					}
				}catch (IOException e){
				e.printStackTrace();
			}
			return fasta;
		
		}

	public static String pdbStringTofastaString(String pdbString){
		
		InputStream is = new ByteArrayInputStream(pdbString.getBytes());
		PDBFileParser pdbreader = new PDBFileParser();
		Structure structure = null;
		String fasta = null;
		try{
			structure=pdbreader.parsePDBFile(is);
			for(org.biojava.bio.structure.Chain c :structure.getChains()){
				fasta = c.getAtomSequence();
				}
			}catch (IOException e){
			e.printStackTrace();
		}
		return fasta;
	
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

	public static String PDBtoFasta (String pathname){
		String fasta = null;
		
		try{
		Structure s = StructureIO.getStructure("pathname");
	    for (org.biojava.bio.structure.Chain c : s.getChains()) {
	        fasta = c.getAtomSequence();
	    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return fasta;
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

	public static String ObjectstoPdbFile(ArrayList<Model> models){
		throw new UnsupportedOperationException("not yet implemented");//TODO
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
		}else if(output.get(0) instanceof IUPred_Output){
			File file = new File("/Users/benkong/temp/IUPRred_Output.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			ArrayList<String> temp = new ArrayList<String>();
			for(Object o:output){
				String string = ((IUPred_Output)o).toString();
				temp.add(string);
				for(String s:temp){
					bw.write(s);
					bw.close();
				}
			}	
		}	
	}
}
