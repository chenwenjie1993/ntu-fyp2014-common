package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.ArrayList;
import java.util.List;

public class Residue implements Particle {

	protected String name;
	protected int chainSeqNum;
	protected int moleculeSeqNum;
	public Chain chain;
	public ArrayList<Atom> atoms;
	public ArrayList<Bond> bonds;
	
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
	
	public int getResidueSeqNum() {
		return this.chainSeqNum;
	}

	/**
	 * 
	 * @param seqNum
	 */
	public void setResidueSeqNum(int seqNum) {
		this.chainSeqNum = seqNum;
	}
	
	public ArrayList<Atom> getAtomList(){
		return atoms;
	}

	public void setAtomList(List<org.biojava.bio.structure.Atom> atoms) {
		Atom atom;
		for(int i=0;i<atoms.size();i++){
			atom = new Atom();
			atom.setName(atoms.get(i).getName());
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
			atoms.get(i).addBond(bond);
			atoms.get(i+1).addBond(bond);
		}
	}

	@Override
	public Atom getAtom(int pos) {
		// TODO Auto-generated method stub
		return null;
	}
}