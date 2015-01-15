package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.util.HashMap;
import java.util.Map;

public class IUPred_Output {
	String position;
	String aminoacid;
	String score;
	
	public void outputposition (String Rposition){
		this.position = Rposition;
	}

	public void outputscore (String Rscore){
		this.score=Rscore;
	}

	public void aminoacid(String Raminoacid) {
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
		this.aminoacid= map.get(Raminoacid);
	}
	public void printRegion(){
		 System.out.println("Pos:"+position+"	AminoAcid:"+aminoacid+" Score:"+score); 
	 }
	public String toString(){
		String string=null;
		string = "Pos:"+position+"	AminoAcid:"+aminoacid+" Score:"+score+"";
		return string;
		
	}
}
