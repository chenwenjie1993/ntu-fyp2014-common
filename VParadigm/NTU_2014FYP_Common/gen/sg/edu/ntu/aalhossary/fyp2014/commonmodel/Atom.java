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
	
	public Atom() {
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

	

	

	

}