package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Interaction;

public abstract class NonBondedInteraction implements Interaction {
	Atom i, j;
	
	public NonBondedInteraction(Atom i, Atom j) {
		this.i = i; this.j = j;
	}
}
