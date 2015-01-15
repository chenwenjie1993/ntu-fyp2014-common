package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.util.HashMap;
import java.util.Map;

public class SequenceOutput {
	
	String position;
	String aminoacid;
	float score;
	
	public void outputposition (String Rposition){
		this.position = Rposition;
	}
	
	public void mapAminoAcid (String alphabet){
		Map<String, String> map = new HashMap<>();
		map.put("G", "GLY");
		map.put("A", "ALA");
		map.put("V", "VAL");
		map.put("L", "LEU");
		map.put("I", "ILE");
		map.put("P", "PRO");
		map.put("F", "PHE");
		map.put("Y", "TYR");
		map.put("W", "TRP");
		map.put("S", "SER");
		map.put("T", "THR");
		map.put("C", "CYS");
		map.put("M", "MET");
		map.put("N", "ASN");
		map.put("Q", "GLN");
		map.put("K", "LYS");
		map.put("R", "ARG");
		map.put("H", "HIS");
		map.put("D", "ASP");
		map.put("E", "GLU");
	}
	
	public void outputscore (float Rscore){
		this.score=Rscore;
	}

}
