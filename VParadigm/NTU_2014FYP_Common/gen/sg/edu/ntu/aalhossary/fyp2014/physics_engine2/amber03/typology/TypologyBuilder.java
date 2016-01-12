package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.typology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.*;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.util.FileReader;

public class TypologyBuilder {
	Molecule m = new Molecule();
	public Molecule build(String fileName) {
		readTopology(fileName);
		return null;
	}
	
	private void readTopology(String fileName) {
		m.particles = new ArrayList<Particle>();
		m.interactions = new ArrayList<Interaction>();
		
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
	
	private void loadAtoms(List<String> atoms) {
//		System.out.println(atoms.toString());
		
		for (String s : atoms) {
			String[] t = s.split(" +");
			if (t.length > 8 && !t[0].contains(";")) {
//				System.out.println(Arrays.toString(t));
				int id = Integer.parseInt(t[1]);
				String type = t[2];
				double charge = Double.parseDouble(t[7]);
				double mass = Double.parseDouble(t[8]);
				m.particles.add(new Atom(id, type, charge, mass));
			}
		}
	}
	
	private void loadBonds(List<String> bonds) {
//		System.out.println(bonds.toString());
	}
	
	private void loadAngles(List<String> angles) {
//		System.out.println(angles.toString());
	}
	
	private void loadProperDihedrals(List<String> dihedrals) {
//		System.out.println(dihedrals.toString());
	}
	
	public void loadImproperDihedrals(List<String> dihedrals) {
//		System.out.println(dihedrals.toString());
	}
}
