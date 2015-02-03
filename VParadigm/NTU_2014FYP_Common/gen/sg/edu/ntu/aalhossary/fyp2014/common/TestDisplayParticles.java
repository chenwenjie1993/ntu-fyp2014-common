package sg.edu.ntu.aalhossary.fyp2014.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.structure.Structure;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.DataManager;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.MoleculeEditor;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry;

public class TestDisplayParticles {
	public static void main(String[] args){
		
		//showTest();
		showTest2();
		//test3();
	}
	
	public static void test3(){
		File file = new File("res/res/4hhb.pdb");
		Structure struc = DataManager.readFile(file.getAbsolutePath());
		UpdateRegistry updateReg = new UpdateRegistry();
		updateReg.createUserModel(struc);
		ArrayList<Model> models = (ArrayList<Model>)updateReg.getModelList();
	}
	
	public static void showTest2() {
		MoleculeEditor editor = new MoleculeEditor();
		List<Model>models = new ArrayList<Model>();
		UpdateRegistry.displayModels(models, editor.getViewer());
		AbstractParticle p1 = new Atom();
		AbstractParticle p2 = new Atom();
		ArrayList<AbstractParticle> particles = new ArrayList<AbstractParticle>();
		particles.add(p1);
		particles.add(p2);
		editor.getMediator().displayParticles(particles);
		//editor.getMediator().notifyUpdated(particles);
	}

	public static void showTest() {
		MoleculeEditor editor = new MoleculeEditor();
		// coordinates of atoms must be within -50 to 50
		// NOTE FROM JMOL
		/* Moves the center of rotation to the specified value. 
		 * A value of 100 will move the molecule completely out of the window. 
		 * The value represents the percentage of the display window, and 0 is the window center. 
		 * A value of 50 will move the center of the molecule to the edge of the window. 
		 * Positive values are to the right of center for X and below center for Y.
		 */
		AbstractParticle p1 = new Atom();
		p1.movePositionBy(0, 10, 5, 0);
		AbstractParticle p2 = new Atom();
		p2.movePositionBy(-5, 0, -5, 0);
		editor.getMediator().displayParticles(p1, p2);
		
		// use this to update the moved particles
		AbstractParticle[] particles = {p1, p2};
		//editor.getMediator().notifyUpdated(particles);
	}
	
	// Convert particles to model format
	/* [] of particles
	 * Create new Model() object.
	 * For each particles, separate to different level (molecule/chain/etc)
	 * (1) If particle is Atom, 
	 * 
	 */
}
