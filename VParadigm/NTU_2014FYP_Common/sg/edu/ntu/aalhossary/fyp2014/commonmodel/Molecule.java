package sg.edu.ntu.aalhossary.fyp2014.commonmodel;

import java.util.*;

public class Molecule implements Particle {

	protected String name;
	protected float molecularMass;
	protected String formula;
	protected float internalEnergy;
	protected Collection<Chain> chains;
	public Interaction interaction;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Chain> getChains() {
		return this.chains;
	}

	public Interaction getInteraction() {
		return this.interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}

	public Molecule() {
		// TODO - implement Molecule.Molecule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param chains
	 */
	protected void setChains(java.util.List<org.biojava.bio.structure.Chain> chains) {
		// TODO - implement Molecule.setChains
		throw new UnsupportedOperationException();
	}

}