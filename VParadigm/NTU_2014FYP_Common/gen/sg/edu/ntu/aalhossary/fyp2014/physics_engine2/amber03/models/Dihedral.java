package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.List;

public class Dihedral extends BondedInteraction {
	public Atom i, j, k, l;
	
	public Dihedral(List<Atom> atoms) {
		super(atoms);
	}

	@Override
	public void updatePotentialEnergy() {
		
	}

}
