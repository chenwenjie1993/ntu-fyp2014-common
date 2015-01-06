package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;
import org.biojava3.core.util.InputStreamProvider;
import org.biojava3.structure.StructureIO;

import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;

public class InputManager {
	

	InputMethodEnum inputMethod;
	
	public InputMethodEnum getInputMethod() {
		return inputMethod;
	}

	public void setInputMethod(InputMethodEnum inputMethod) {
		//No need to check here till now. will check in next methods
		this.inputMethod = inputMethod;
	}
	
	/**
	 * 
	 * @param pathname
	 * @return inputStream or null if file does not exist
	 */
	public InputStream fastafileToInputStream (String pathname){
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
	
	public String pdbFileTofastaString(String pathname){
		InputStream is = this.fastafileToInputStream(pathname);
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		PDBFileReader pdbreader = new PDBFileReader();
		Structure structure = null;
		String line;
		String fasta = null;
		try{
			br = new BufferedReader(new InputStreamReader(is));
			while((line=br.readLine())!=null){
				sb.append(line);
			}
			String pdbfile = sb.toString();
			structure = pdbreader.getStructure(pdbfile);
			for(Chain c :structure.getChains()){
				fasta = c.getAtomSequence();
				}
			}catch (IOException e){
			e.printStackTrace();
		}
		return fasta;
	
	}

	public InputStream stringToInputStream(String input){
		InputStream inputStream = null;
		
			if(input==null){
				throw new IllegalStateException("Input String not yet set");
				
			}else{
				inputStream = new ByteArrayInputStream(input.getBytes());
			}
				
		return inputStream;
	}
	
	
	
	public String PDBtoFasta (String pathname){
		String fasta = null;
		
		try{
		Structure s = StructureIO.getStructure("pathname");
        for ( Chain c : s.getChains()) {
            fasta = c.getAtomSequence();
        }
		}catch(Exception e){
			e.printStackTrace();
		}
		return fasta;
	}  
	
	public String ObjectstoFasta(){
		return null;
		
		
	}

	/**
	 * @deprecated we may need it later
	 * @param pathname
	 * @return
	 * @throws FileNotFoundException
	 */
	public String readFile(String pathname) throws FileNotFoundException {
		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int)file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");
		try{
			while(scanner.hasNextLine()){
				fileContents.append(scanner.nextLine()).append(lineSeparator);
			}
			return fileContents.toString();
		}finally{
			scanner.close();
		}
	}
	
}