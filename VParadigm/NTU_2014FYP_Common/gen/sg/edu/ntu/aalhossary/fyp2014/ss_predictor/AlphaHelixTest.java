package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.io.PDBFileReader;
import org.junit.Test;

import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.common.Residue;
import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.DataManager;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry;

public class AlphaHelixTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/stride/4HHB.pdb";
		STRIDE_Predictor sp = new STRIDE_Predictor();
		sp.process(pathname);
		File file = new File("/Users/benkong/documents/fyp/stride/4HHB.pdb");
		Structure struc = DataManager.readFile(file.getAbsolutePath());
		UpdateRegistry updateReg = new UpdateRegistry();
		updateReg.createUserModel(struc);
		ArrayList<Model> models = (ArrayList<Model>)updateReg.getModelList();
		
		AlphaHelix alpha = new AlphaHelix();
		
		alpha.setResidues(sp.Pregion.get(0), models.get(0));
		
		//PDBFileReader pdbreader = new PDBFileReader();
		//Structure pdb = null;
		
		//pdb = pdbreader.getStructure(pathname);
		
		/*try {
			org.biojava.bio.structure.Chain c = pdb.getChainByPDB(sp.Pregion.get(0).chain);
			for(Residue res:alpha.residues){
				
			}
		} catch (StructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		ArrayList <Residue> test = alpha.getHelixResidues(alpha.residues);
		for(Residue res:test){
			System.out.println(Arrays.toString(res.getAtom("CA").getCoordinates()));
		}
		
		float [] t = alpha.getOrientation(test);
		
		
		System.out.println(Arrays.toString(t));
		
			
	
			
	
			
		
		
		
	}

}
