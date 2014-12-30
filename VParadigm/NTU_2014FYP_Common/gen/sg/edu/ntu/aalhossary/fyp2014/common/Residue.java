package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Xiu Ting
 *
 */
public class Residue extends sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle {

	protected String name;
	protected int chainSeqNum;
	protected int moleculeSeqNum;
	protected Chain chain;
	protected ArrayList<Atom> atoms;
	protected ArrayList<Bond> bonds;
	
	public Residue() {
		super();
		name = null;
		atoms = new ArrayList<Atom>();
		bonds = new ArrayList<Bond>();
		
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Chain getParent(){
		return chain;
	}
	
	public void setParent(Chain chain) {
		this.chain = chain;
	}
	
	public int getResidueSeqNum() {
		return this.chainSeqNum;
	}

	public void setResidueSeqNum(int seqNum) {
		this.chainSeqNum = seqNum;
	}
	
	public int getChainPosition(){
		return chain.position;
	}
	
	public ArrayList<Atom> getAtomList(){
		return atoms;
	}

	public void setAtomList(List<org.biojava.bio.structure.Atom> atoms) {
		AbstractParticle atom;
		for(int i=0;i<atoms.size();i++){
			atom = new Atom();
			((Atom)atom).setParent(this);
			((Atom)atom).setSymbol(atoms.get(i).getName());
			((Atom)atom).setChainSeqNum(chainSeqNum);
			((Atom)atom).setAtomSeqNum(atoms.get(i).getPDBserial());
			((Atom)atom).setCoordinates();
			this.atoms.add(((Atom)atom));
		}
		setBondsBetweenAtoms();
	}
	
	private void setBondsBetweenAtoms(){
		Bond bond;
		// loop for each atom in residue
		for(int i=0;i<atoms.size()-1;i++){
			// compare the current atom with next atom.
			bond = new Bond(atoms.get(i), atoms.get(i+1));
			// add bond to the array of bonds between atom for this residue
			bonds.add(bond);
			// add the bond to atom
			atoms.get(i).setBond(bond);
			atoms.get(i+1).setBond(bond);
		}
	}

	public void setAtomHash(HashMap<String, Atom> atomHash, String modelName) {
		for(int i=0;i<atoms.size();i++){
			atomHash.put(modelName+atoms.get(i).atomSeqNum, atoms.get(i));
		}
	}
	
	public String getModelName(){
		return chain.getModelName();
	}
}