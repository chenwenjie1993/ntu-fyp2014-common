package sg.edu.ntu.aalhossary.fyp2014.commonmodel;

public class Atom implements Particle {

	public java.lang.String name;
	public java.lang.Object symbol;
	public float atomicMass;
	public float atomicNum;
	public int ionCharge;
	public int chainSeqNum;
	public Interaction interaction;
	public Chain chain;
	public Residue residue;

	public int getChainSeqNum() {
		return this.chainSeqNum;
	}

	public void setChainSeqNum(int chainSeqNum) {
		this.chainSeqNum = chainSeqNum;
	}

	public Atom() {
		// TODO - implement Atom.Atom
		throw new UnsupportedOperationException();
	}

	public String getName() {
		// TODO - implement Atom.getName
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		// TODO - implement Atom.setName
		throw new UnsupportedOperationException();
	}

}