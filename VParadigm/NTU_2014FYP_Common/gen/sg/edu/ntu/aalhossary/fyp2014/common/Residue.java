package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.ArrayList;
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
		Atom atom;
		for(int i=0;i<atoms.size();i++){
			atom = new Atom();
			atom.setParent(this);
			atom.setSymbol(atoms.get(i).getName());
			atom.setChainSeqNum(chainSeqNum);
			atom.setAtomSeqNum(atoms.get(i).getPDBserial());
			atom.setCoordinates(atoms.get(i).getCoords());
			this.atoms.add(atom);
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

	@Override
	public Atom getAtom(int pos) {
		for(int i=0;i<atoms.size();i++){
			if(pos==atoms.get(i).atomSeqNum)
				return atoms.get(i);
		}
		return null;
	}
}