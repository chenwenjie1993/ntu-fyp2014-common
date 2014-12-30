package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Xiu Ting
 *
 */
public class Molecule extends sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle {

	protected String name;
	protected Model parent;
	protected float molecularMass;
	protected String formula;
	protected float internalEnergy;
	protected ArrayList<Chain> chains;
	public Interaction interaction;

	public Molecule() {
		super();
		chains = new ArrayList<Chain>();
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Model getParent(){
		return parent;
	}
	
	public void setParent(Model model){
		this.parent = model;
	}
	public ArrayList<Chain> getChains() {
		return this.chains;
	}
	
	/**
	 * 
	 * @param chains
	 */
	protected void setChains(java.util.List<org.biojava.bio.structure.Chain> chains) {
		AbstractParticle chain;
		for(int i=0;i<chains.size();i++){
			chain = new Chain();
			((Chain)chain).setChainName(chains.get(i).getChainID());
			((Chain)chain).setParent(this);
			((Chain)chain).setChainPosition(i);
			((Chain)chain).setChainSequence(chains.get(i));
			this.chains.add((Chain) chain);
		}
	}

	public Interaction getInteraction() {
		return this.interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}

	public void setAtomHash(HashMap<String, Atom> atomHash, String modelName) {
		for(int i=0;i<chains.size();i++){
			chains.get(i).setAtomHash(atomHash, modelName);
		}
	}

}