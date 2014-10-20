package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.io.IOException;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.MMCIFFileReader;
import org.biojava.bio.structure.io.PDBFileReader;

public class FileReader {

	/**
	 * 
	 * @param fileName
	 */
	public static Structure readPDBFile(String fileName) {
		// wrapper class for parsing a PDB file.
		PDBFileReader pdbreader = new PDBFileReader();
		Structure struc = null; 
		//Model model = null;
		
		try{
			// Access to the data of a PDB file.
			struc = pdbreader.getStructure(fileName);
		    /* System.out.println(struc.getModel(0));
	    
		    for (org.biojava.bio.structure.Chain c: struc.getChains()){
	            System.out.println("Chain " + c.getChainID() + " details:");
	            System.out.println("Atom ligands: " + c.getAtomLigands());
	            System.out.println(c.getAtomSequence());
	         }*/
		    
		 } catch (Exception e){
		     e.printStackTrace();
		 }
		return struc;
	}

	/**
	 * 
	 * @param fileName
	 */
	public static Structure readMol2File(String fileName) {
		// TODO - implement FileReader.readMol2File
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param fileName
	 */
	public static Structure readMmcifFile(String fileName) {
		MMCIFFileReader pdbreader = new MMCIFFileReader();
		Structure struct=null;
        try {
                struct = pdbreader.getStructure(fileName);

                // you can convert it to a PDB file...
                //System.out.println(struct.toPDB());
        } catch (IOException e) {
                e.printStackTrace();
        }
		return struct;
	}

}