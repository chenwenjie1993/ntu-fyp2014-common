package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.util.ArrayList;
import java.util.Arrays;

import sg.edu.ntu.aalhossary.fyp2014.common.Residue;

public class Helix {
	
	ArrayList <Residue> residues;
	float [] orientation;
	
	public static float[]  FindDifference (float[]f1, float[]f2){
		float[] length = new float[3];
		length [0] = f1[0]-f2[0];
		length [1] = f1[1]-f2[1];
		length [2] = f1[2]-f2[2];
		
		return length;
	}
	

	
	public float [] getOrientation(ArrayList <Residue> residues){
		ArrayList <float[]> total = new ArrayList <float[]>();
		float [] avg = new float [3];
		float sum0 = 0; float sum1 = 0; float sum2 = 0;
		ArrayList <Residue>temp = (ArrayList<Residue>) residues.clone();
		for(Residue res : residues){
			for(Residue res1: temp){
			if(residues.indexOf(res)-temp.indexOf(res1)==1){
				total.add(this.FindDifference(res.getAtom("N").getCoordinates(),res1.getAtom("C").getCoordinates()));
			}
			}
		}
		for(float[] f1:total){
		System.out.println(Arrays.toString(f1));
		
		sum0 += f1[0];
		sum1 += f1[1];
		sum2 += f1[2];
		
		avg[0] = sum0 /total.size();
		avg[1] = sum1 /total.size();
		avg[2] = sum2 /total.size();
		}
		System.out.println(sum0+" "+sum1+" "+sum2);
		
		
		return avg;
	}
	
	public double getLength (float[] start,float[] end,float[] orientation){
		double length = 0;
		float[] last =  FindDifference(end, orientation);
		float [] first =FindDifference(start, orientation);
		float [] diff = FindDifference(last, first);
		length = Cal_euc_distance(diff);
		
		return length;
	}
	
	public static double Cal_euc_distance (float [] f){
		double e_dist = 0;
		e_dist = Math.sqrt(((f[0]*f[0])+(f[1]*f[1])+(f[2]*f[2])));
		return e_dist;
	}

}
