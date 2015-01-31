package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.io.IOException;
import java.util.logging.Level;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.MMCIFFileReader;
import org.biojava.bio.structure.io.PDBFileReader;

public class FileReader {

	public static Structure readPDBFile(String fileName) {
		// wrapper class for parsing a PDB file.
		PDBFileReader pdbreader = new PDBFileReader();
		Structure struc = null; 
		//Model model = null;
		
		try{
			// Access to the data of a PDB file.
			struc = pdbreader.getStructure(fileName);	    
		 } catch (Exception e){
			 java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MoleculeEditor.class.getName());
			 logger.log(Level.SEVERE, "Error converting to structure object from file path");
		 }
		return struc;
	}

	public static Structure readMol2File(String fileName) {
		// TODO - implement FileReader.readMol2File
		throw new UnsupportedOperationException();
	}

	public static Structure readMmcifFile(String fileName) {
		MMCIFFileReader pdbreader = new MMCIFFileReader();
		Structure struct=null;
        try {
                struct = pdbreader.getStructure(fileName);

        } catch (IOException e) {
        	java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MoleculeEditor.class.getName());
			 logger.log(Level.SEVERE, "Error converting to structure object from file path");
        }
		return struct;
	}

}