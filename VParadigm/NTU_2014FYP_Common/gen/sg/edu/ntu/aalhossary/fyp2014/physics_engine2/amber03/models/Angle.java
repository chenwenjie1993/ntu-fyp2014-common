package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Vector3D;

public class Angle implements BondedInteraction {
	public Atom i, j, k;
	
	public Angle(Atom i, Atom j, Atom k) {
		this.i = i;
		this.j = j;
		this.k = k;
	}
	
	@Override
	public void updatePotentialEnergy() {
		
	}
	
}
