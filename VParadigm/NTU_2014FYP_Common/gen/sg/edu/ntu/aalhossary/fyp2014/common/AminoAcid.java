package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.*;

public class AminoAcid extends Residue {

	char aminoChar;
	public ArrayList<Atom> atoms;
	
	public AminoAcid() {
		super();
	}
	
	public char getAminoChar() {
		return this.aminoChar;
	}

	public void setAminoChar(char aminoChar) {
		this.aminoChar = aminoChar;
	}

	

}