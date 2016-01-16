package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Vector3D;

public class Dihedral implements BondedInteraction {
	public Atom i, j, k, l;
	
	public Dihedral(Atom i, Atom j, Atom k, Atom l) {
		this.i = i;
		this.j = j;
		this.k = k;
		this.l = l;
	}

	@Override
	public void updatePotentialEnergy() {
		
	}

}
