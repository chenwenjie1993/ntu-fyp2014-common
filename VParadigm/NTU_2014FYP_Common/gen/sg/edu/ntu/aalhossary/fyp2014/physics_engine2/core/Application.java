package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.util.*;

public class Application {

	public static void main(String[] args) {
		String fileName = "res/test/amber03/topol.top";
		readTopology(fileName);
	}

	public static void readTopology(String fileName) {
		List<String> fileAsList = FileReader.readFile(fileName);
		int atoms = fileAsList.indexOf("[ atoms ]");
		int bonds = fileAsList.indexOf("[ bonds ]");
		int pairs = fileAsList.indexOf("[ pairs ]");
		int angles = fileAsList.indexOf("[ angles ]");
		int properDihedrals = fileAsList.indexOf("[ dihedrals ]");
		int improperDihedrals = fileAsList.lastIndexOf("[ dihedrals ]");
		int position_restraints = fileAsList.lastIndexOf("[ position_restraints ]");
		
		loadAtoms(fileAsList.subList(atoms+1, bonds));
		loadBonds(fileAsList.subList(bonds+1, pairs));
		loadAngles(fileAsList.subList(angles+1, properDihedrals));
		loadProperDihedrals(fileAsList.subList(properDihedrals+1, improperDihedrals));
		loadImproperDihedrals(fileAsList.subList(improperDihedrals+1, position_restraints));
	}
	
	public static void loadAtoms(List<String> atoms) {
//		System.out.println(atoms.toString());
	}
	
	public static void loadBonds(List<String> bonds) {
//		System.out.println(bonds.toString());
	}
	
	public static void loadAngles(List<String> angles) {
//		System.out.println(angles.toString());
	}
	
	public static void loadProperDihedrals(List<String> dihedrals) {
//		System.out.println(dihedrals.toString());
	}
	
	public static void loadImproperDihedrals(List<String> dihedrals) {
//		System.out.println(dihedrals.toString());
	}
}
