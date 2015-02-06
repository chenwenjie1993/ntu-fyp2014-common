package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.File;
import java.util.ArrayList;

import org.biojava.bio.structure.Structure;

import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.DataManager;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry;

public class test {

	public static void main(String[] args) {
		
		File file = new File("/Users/benkong/documents/fyp/stride/1PGB.pdb");
		Structure struc = DataManager.readFile(file.getAbsolutePath());
		UpdateRegistry updateReg = new UpdateRegistry();
		updateReg.createUserModel(struc);
		ArrayList<Model> models = (ArrayList<Model>)updateReg.getModelList();
		System.out.println(models.size());

	}
}
