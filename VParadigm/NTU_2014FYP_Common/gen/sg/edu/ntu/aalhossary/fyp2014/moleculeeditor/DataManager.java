package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.util.ArrayList;

import org.biojava.bio.structure.Structure;
import org.jmol.api.JmolViewer;

import sg.edu.ntu.aalhossary.fyp2014.common.Model;

public class DataManager {

	public DataManager() {
		
	}

	public Structure readFile(java.lang.String fileName) {
		Structure struc = null;
		
		if(fileName.endsWith(".pdb") || fileName.endsWith(".pdbqt")){
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