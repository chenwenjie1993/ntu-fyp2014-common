package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Angle;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Atom;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Bond;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.ElectrostaticPotential;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.ImproperDihedral;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.LennardJonesPotential;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.ProperDihedral;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.MolecularSystem;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Interaction;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.util.FileReader;

public class TypologyBuilder {
	MolecularSystem m = new MolecularSystem();
	public MolecularSystem build(String dir) {
		readTopology(dir + "topol.top");
		readPosition(dir + "conf.gro");
		return m;
	}
	
	private void readTopology(String fileName) {
		m.particles = new ArrayList<AbstractParticle>();
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
		
		generateNonBondedInteraction();
	}
	
	private void readPosition(String fileName) {
		List<String> fileAsList = FileReader.readFile(fileName);
		int count = Integer.valueOf(fileAsList.get(1).trim());
		loadPosition(fileAsList.subList(2, count+2));
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
				List<Atom> atoms = new ArrayList<Atom>();
				atoms.add(atom1);
				atoms.add(atom2);
				m.interactions.add(new Bond(atoms));
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
				List<Atom> atoms = new ArrayList<Atom>();
				atoms.add(atom1);
				atoms.add(atom2);
				atoms.add(atom3);
				m.interactions.add(new Angle(atoms));
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
				List<Atom> atoms = new ArrayList<Atom>();
				atoms.add(atom1);
				atoms.add(atom2);
				atoms.add(atom3);
				atoms.add(atom4);
				m.interactions.add(new ProperDihedral(atoms));
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
				List<Atom> atoms = new ArrayList<Atom>();
				atoms.add(atom1);
				atoms.add(atom2);
				atoms.add(atom3);
				atoms.add(atom4);
				m.interactions.add(new ImproperDihedral(atoms));
			}
		}
	}
	
	public void loadPosition(List<String> position) {
		for (int i=0; i<position.size(); i++) {
			String[] t = position.get(i).split(" +");
//			System.out.println(Arrays.toString(t));
			double x = Double.parseDouble(t[4]);
			double y = Double.parseDouble(t[5]);
			double z = Double.parseDouble(t[6]);
			((AbstractParticle) m.particles.get(i)).setPosition(x, y, z, 0);
		}
	}
	
	public void generateNonBondedInteraction() {
		int count = m.particles.size();
		for (int i=0; i<count-1; i++) {
			for (int j=i+1; j<count; j++) {
				ElectrostaticPotential e = new ElectrostaticPotential((Atom)m.particles.get(i), (Atom)m.particles.get(j));
				LennardJonesPotential l = new LennardJonesPotential((Atom)m.particles.get(i), (Atom)m.particles.get(j));
				m.interactions.add(e);
				m.interactions.add(l);
			}
		}
	}
}
