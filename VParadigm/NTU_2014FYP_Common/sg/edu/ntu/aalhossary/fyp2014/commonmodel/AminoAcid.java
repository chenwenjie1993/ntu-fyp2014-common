package sg.edu.ntu.aalhossary.fyp2014.commonmodel;

import java.util.*;

public class AminoAcid extends Residue {

	char aminoChar;
	public java.util.ArrayList atoms;
	public Collection<Atom> attribute;

	public char getAminoChar() {
		return this.aminoChar;
	}

	public void setAminoChar(char aminoChar) {
		this.aminoChar = aminoChar;
	}

	public AminoAcid() {
		// TODO - implement AminoAcid.AminoAcid
		throw new UnsupportedOperationException();
	}

}