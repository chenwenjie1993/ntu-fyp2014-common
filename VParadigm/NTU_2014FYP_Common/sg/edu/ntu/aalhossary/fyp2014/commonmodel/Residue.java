package sg.edu.ntu.aalhossary.fyp2014.commonmodel;

public class Residue implements Particle {

	protected String name;
	protected int chainSeqNum;
	public Chain chain;
	public Atom _unnamed_Atom_;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Residue() {
		// TODO - implement Residue.Residue
		throw new UnsupportedOperationException();
	}

	public int getResidueSeqNum() {
		// TODO - implement Residue.getResidueSeqNum
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param seqNum
	 */
	public void setResidueSeqNum(int seqNum) {
		// TODO - implement Residue.setResidueSeqNum
		throw new UnsupportedOperationException();
	}

}