package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.List;

public class Angle extends BondedInteraction {
	public Atom i, j, k;
	
	public Angle(List<Atom> atoms) {
		super(atoms);
	}
	
	@Override
	public void updatePotentialEnergy() {
		
	}
	
}
