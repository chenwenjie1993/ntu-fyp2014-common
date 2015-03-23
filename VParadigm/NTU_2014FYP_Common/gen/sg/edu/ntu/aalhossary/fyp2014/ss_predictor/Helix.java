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
	
	public static double Cal_euc_distance (float [] f){
		double e_dist = 0;
		e_dist = Math.sqrt(((f[0]*f[0])+(f[1]*f[1])+(f[2]*f[2])));
		return e_dist;
	}
	
	public double getLength (float[] start,float[] end){
		double length = 0;
		float[] diff = FindDifference(end, start);	
		length = Cal_euc_distance(diff);
		
		return length;
	}
	
	
}
