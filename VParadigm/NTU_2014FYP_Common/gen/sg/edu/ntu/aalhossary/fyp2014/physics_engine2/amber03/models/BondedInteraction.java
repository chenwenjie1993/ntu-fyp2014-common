package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Interaction;

public abstract class BondedInteraction implements Interaction {
	List<Atom> atoms;
	double Ep = 0;
	
	public BondedInteraction(List<Atom> atoms) {
		this.atoms = atoms;
	}
	
	public double getPotentialEnergy() {
		return Ep;
	}
}
