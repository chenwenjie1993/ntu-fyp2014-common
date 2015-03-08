package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.Residue;

public class AlphaHelix extends Helix {
	
	
	public void setResidues (STRIDE_Output stride, Object o){
		
		if(stride.prediction.equals("AlphaHelix")){
			this.residues=IOutility.createObjectsFromModel(stride,o);
		}
		
	}
	
	public ArrayList<Residue> getHelixResidues(ArrayList <Residue> residues){
		ArrayList<Residue> Hresidue = new ArrayList <Residue>();
		for(int i = 0; i < residues.size(); i++){
			if(i%4 == 0){
			Hresidue.add(residues.get(i));
			}
		}
		return Hresidue;
	}

}
