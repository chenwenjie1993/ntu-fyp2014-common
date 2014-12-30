package sg.edu.ntu.aalhossary.fyp2014.common;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.MoleculeEditor;

public class TestDisplayParticles {
	public static void main(String[] args){
		
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
	}
}
