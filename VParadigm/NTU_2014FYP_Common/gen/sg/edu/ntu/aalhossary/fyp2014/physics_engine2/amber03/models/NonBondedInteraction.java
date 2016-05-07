package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Interaction;

public abstract class NonBondedInteraction implements Interaction {
	Atom i, j;
	double Ep = 0;
	
	public NonBondedInteraction(Atom i, Atom j) {
		this.i = i; this.j = j;
	}
	
	public double getPotentialEnergy() {
		return Ep;
	}
}
