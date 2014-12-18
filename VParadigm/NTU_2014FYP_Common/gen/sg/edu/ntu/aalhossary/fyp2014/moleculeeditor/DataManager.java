package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import org.biojava.bio.structure.Structure;

public class DataManager {

	public DataManager() {
	}

	public static Structure readFile(java.lang.String fileName) {
		Structure struc = null;
		
		if(fileName.endsWith(".pdb")){
			struc = FileReader.readPDBFile(fileName); 
		}
		else if(fileName.endsWith(".mol2")){
			struc = FileReader.readMol2File(fileName);
		}
		else if(fileName.endsWith(".cif")){
			struc = FileReader.readMmcifFile(fileName);
		}
		return struc;
	}

	public void writeFile(java.lang.String fileName, Structure struc) {
		String strucToPdb = struc.toPDB();
		// implement writer to write to file
		FileWriter.writePDBFile(fileName, strucToPdb);
	}

}