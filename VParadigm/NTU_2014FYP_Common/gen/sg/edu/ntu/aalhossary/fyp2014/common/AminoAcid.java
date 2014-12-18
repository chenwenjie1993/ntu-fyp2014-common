package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.*;

/**
 * @author Xiu Ting
 * AminoAcid.java is child of Residue.java
 * 
 */

public class AminoAcid extends Residue {

	char aminoChar;
	public ArrayList<Atom> atoms;
	
	// constructor
	public AminoAcid() {
		super();
	}
	
	// get amino acid character eg. 'A'
	public char getAminoChar() {
		return this.aminoChar;
	}

	// set the amino acid character. Used by Chain.java
	public void setAminoChar(char aminoChar) {
		this.aminoChar = aminoChar;
	}
}