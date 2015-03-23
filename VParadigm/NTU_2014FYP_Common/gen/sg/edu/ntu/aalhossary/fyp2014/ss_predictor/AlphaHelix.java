package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sg.edu.ntu.aalhossary.fyp2014.common.Residue;

public class AlphaHelix extends Helix {
	
	
	public void setResidues (STRIDE_Output stride, Object o){
		ArrayList <Residue> temp = new ArrayList <Residue>();
		if(stride.prediction.equals("AlphaHelix")){
			this.residues=IOutility.createObjectsFromModel(stride,o);
		}
		
	}
	
	public float [] getOrientation(ArrayList <Residue> residues){
		
		int size = residues.size()-4;
		ArrayList <float[]> total = new ArrayList <float[]>();
		float [] avg = new float [3];
		float sum0 = 0; float sum1 = 0; float sum2 = 0;
		Residue[] key = new Residue[size];
		Residue[] value = new Residue[size];
		for(int i = 0; i< size; i++){	
				key[i]=residues.get(i);
				value[i]=residues.get(i+4);
				total.add(FindDifference(value[i].getAtom("N").getCoordinates(),key[i].getAtom("O").getCoordinates()));
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
