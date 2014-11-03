package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.ArrayList;

public class Atom implements Particle {

	public java.lang.String name;
	public java.lang.Object symbol;
	public float atomicMass;
	public float atomicNum;
	public int ionCharge;
	public int chainSeqNum;
	public int atomSeqNum;
	public double[] coordinates;
	public Interaction interaction;
	public Chain chain;
	public Residue residue;
	public ArrayList<Bond> bonds;
	
	public Atom() {
		bonds = new ArrayList<Bond>();
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public int getChainSeqNum() {
		return this.chainSeqNum;
	}

	public void setChainSeqNum(int chainSeqNum) {
		this.chainSeqNum = chainSeqNum;
	}
	
	public int getAtomSeqNum() {
		return this.atomSeqNum;
	}

	public void setAtomSeqNum(int atomSeqNum) {
		this.atomSeqNum = atomSeqNum;
	}

	public double[] getAtomCoordinates(){
		return coordinates;
	}
	
	public void setCoordinates(double[] coords) {
		coordinates = coords;
	}

	public void addBond(Bond bond) {
		bonds.add(bond);
	}
	
	public ArrayList<Bond> getBond(){
		return bonds;
	}

	@Override
	public Atom getAtom(int pos) {
		// TODO Auto-generated method stub
		return null;
	}

}