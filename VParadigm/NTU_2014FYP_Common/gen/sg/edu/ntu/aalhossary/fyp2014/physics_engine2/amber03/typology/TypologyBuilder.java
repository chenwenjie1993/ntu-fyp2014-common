package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.typology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Angle;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Atom;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Bond;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.ImproperDihedral;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.ProperDihedral;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.*;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.util.FileReader;

public class TypologyBuilder {
	Molecule m = new Molecule();
	public Molecule build(String fileName) {
		readTopology(fileName);
		return m;
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
				String type = t[2];
				double charge = Double.parseDouble(t[7]);
				double mass = Double.parseDouble(t[8]);
				m.particles.add(new Atom(type, charge, mass));
			}
		}
	}
	
	private void loadBonds(List<String> bonds) {
//		System.out.println(bonds.toString());
		
		for (String s : bonds) {
			String[] t = s.split(" +");
			if (t.length > 3 && !t[0].contains(";")) {
//				System.out.println(Arrays.toString(t));
				Atom atom1 = (Atom) m.particles.get(Integer.parseInt(t[1])-1);
				Atom atom2 = (Atom) m.particles.get(Integer.parseInt(t[2])-1);
				m.interactions.add(new Bond(atom1, atom2));
			}
		}
	}
	
	private void loadAngles(List<String> angles) {
//		System.out.println(angles.toString());
		
		for (String s : angles) {
			String[] t = s.split(" +");
			if (t.length > 4 && !t[0].contains(";")) {
//				System.out.println(Arrays.toString(t));
				Atom atom1 = (Atom) m.particles.get(Integer.parseInt(t[1])-1);
				Atom atom2 = (Atom) m.particles.get(Integer.parseInt(t[2])-1);
				Atom atom3 = (Atom) m.particles.get(Integer.parseInt(t[3])-1);
				m.interactions.add(new Angle(atom1, atom2, atom3));
			}
		}
	}
	
	private void loadProperDihedrals(List<String> dihedrals) {
//		System.out.println(dihedrals.toString());
		
		for (String s : dihedrals) {
			String[] t = s.split(" +");
			if (t.length > 5 && !t[0].contains(";")) {
//				System.out.println(Arrays.toString(t));
				Atom atom1 = (Atom) m.particles.get(Integer.parseInt(t[1])-1);
				Atom atom2 = (Atom) m.particles.get(Integer.parseInt(t[2])-1);
				Atom atom3 = (Atom) m.particles.get(Integer.parseInt(t[3])-1);
				Atom atom4 = (Atom) m.particles.get(Integer.parseInt(t[4])-1);
				m.interactions.add(new ProperDihedral(atom1, atom2, atom3, atom4));
			}
		}
	}
	
	public void loadImproperDihedrals(List<String> dihedrals) {
//		System.out.println(dihedrals.toString());
		for (String s : dihedrals) {
			String[] t = s.split(" +");
			if (t.length > 5 && !t[0].contains(";")) {
//				System.out.println(Arrays.toString(t));
				Atom atom1 = (Atom) m.particles.get(Integer.parseInt(t[1])-1);
				Atom atom2 = (Atom) m.particles.get(Integer.parseInt(t[2])-1);
				Atom atom3 = (Atom) m.particles.get(Integer.parseInt(t[3])-1);
				Atom atom4 = (Atom) m.particles.get(Integer.parseInt(t[4])-1);
				m.interactions.add(new ImproperDihedral(atom1, atom2, atom3, atom4));
			}
		}
	}
}
