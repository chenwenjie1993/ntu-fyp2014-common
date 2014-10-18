package ModelTest;

public class Molecule implements Particle {

	protected java.lang.String name;
	protected float molecularMass;
	protected java.lang.String formula;
	protected float internalEnergy;
	protected java.util.ArrayList chains;
	public Interaction interaction;

	/**
	 * 
	 * @param struc
	 */
	public Molecule(org.biojava.bio.structure.Structure struc) {
		// TODO - implement Molecule.Molecule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param interaction
	 */
	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}

	public Interaction getInteraction() {
		return this.interaction;
	}

	/**
	 * 
	 * @param chains
	 */
	private void setChains(java.util.List chains) {
		// TODO - implement Molecule.setChains
		throw new UnsupportedOperationException();
	}

	public java.util.ArrayList getChains() {
		return this.chains;
	}

}