package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.Residue;

public class ThreeTenHelix extends Helix {
	
public void setResidues (STRIDE_Output stride, Object o){
		
		if(stride.prediction.equals("310Helix")){
			this.residues=IOutility.createObjectsFromModel(stride,o);
		}
		
	}
public float [] getOrientation(ArrayList <Residue> residues){
	
	int size = residues.size()-3;
	ArrayList <float[]> total = new ArrayList <float[]>();
	float [] avg = new float [3];
	float sum0 = 0; float sum1 = 0; float sum2 = 0;
	Residue[] key = new Residue[size];
	Residue[] value = new Residue[size];
	for(int i = 0; i< size; i++){	
			key[i]=residues.get(i);
			value[i]=residues.get(i+3);
			total.add(FindDifference(value[i].getAtom("N").getCoordinates(),key[i].getAtom("O").getCoordinates()));
		}
	for(Residue res: key){
		System.out.println(res.getResidueSeqNum());
	}
	for(Residue res1: value){
		System.out.println(res1.getResidueSeqNum());
	}
	
	for(float[] f1:total){

	sum0 += f1[0];
	sum1 += f1[1];
	sum2 += f1[2];
	
	avg[0] = sum0 /total.size();
	avg[1] = sum1 /total.size();
	avg[2] = sum2 /total.size();
	}
	
	
	
	return avg;
}
}
