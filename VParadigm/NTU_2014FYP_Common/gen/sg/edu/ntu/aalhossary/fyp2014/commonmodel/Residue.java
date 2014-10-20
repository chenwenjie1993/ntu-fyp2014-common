package sg.edu.ntu.aalhossary.fyp2014.commonmodel;

public class Residue implements Particle {

	protected String name;
	protected int chainSeqNum;
	public Chain chain;
	public Atom _unnamed_Atom_;
	
	public Residue() {
		name = null;
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

}