package sg.edu.ntu.aalhossary.fyp2014.commonmodel;

import java.util.*;

public class Chain implements Particle {

	protected String name;
	public Collection<Residue> residues;
	public Collection<Atom> atomSeq;

	public Collection<Residue> getResidues() {
		return this.residues;
	}

	public Chain() {
		// TODO - implement Chain.Chain
		throw new UnsupportedOperationException();
	}

	public String getChainName() {
		// TODO - implement Chain.getChainName
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param chainID
	 */
	public void setChainName(String chainID) {
		// TODO - implement Chain.setChainName
		throw new UnsupportedOperationException();
	}

	public java.util.ArrayList<Atom> getAtoms() {
		// TODO - implement Chain.getAtoms
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param chain
	 */
	public void setChainSequence(org.biojava.bio.structure.Chain chain) {
		// TODO - implement Chain.setChainSequence
		throw new UnsupportedOperationException();
	}

}