package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

public class TopologyBuilder {
//	private Map<String, Object> config; 
	private Map<String, Object> params;
	MolecularSystem m = new MolecularSystem();
	final double T = 300;
	final double K = 8.314510e-3;
	
	public MolecularSystem build(Map<String, Object> config) {
//		this.config = config;
		String dir = (String) config.get("dir") + "/" + (String) config.get("name") + "/";
		params = (Map<String, Object>) config.get("ffParams");
		m.particles = new ArrayList<AbstractParticle>();
		m.interactions = new ArrayList<Interaction>();
		readNameAndPosition(dir + (String) config.get("name") + ".gro");
		readTopology(dir + (String) config.get("name") +".top");
//		initVelocity();
		return m;
	}
	
	private void readTopology(String fileName) {
		
		List<String> fileAsList = FileReader.readFile(fileName);
		int atoms = fileAsList.indexOf("[ atoms ]");
		int bonds = fileAsList.indexOf("[ bonds ]");
		int pairs = fileAsList.indexOf("[ pairs ]");
		int angles = fileAsList.indexOf("[ angles ]");
		int properDihedrals = fileAsList.indexOf("[ dihedrals ]");
		int improperDihedrals = fileAsList.lastIndexOf("[ dihedrals ]");
		int position_restraints = fileAsList.lastIndexOf("[ position_restraints ]");
		
		loadAtoms(fileAsList.subList(atoms+1, bonds));
		
		System.out.println("Loading bonds...");
		loadBonds(fileAsList.subList(bonds+1, pairs));
		
		System.out.println("Loading pairs...");
		loadPairs(fileAsList.subList(pairs+1, angles));
		
		System.out.println("Loading angles...");
		loadAngles(fileAsList.subList(angles+1, properDihedrals));

		System.out.println("Loading ImproperDihedral...");
		loadProperDihedrals(fileAsList.subList(properDihedrals+1, improperDihedrals));

		System.out.println("Loading ProperDihedral...");
		loadImproperDihedrals(fileAsList.subList(improperDihedrals+1, position_restraints));
		
		updateExclude();
//		generateNonBondedInteraction();
	}
	
	private void updateExclude() {
		int n = m.particles.size();
//		boolean visited[] = new boolean[n];
		
		for (int i=0; i<n; i++) {
			Atom atom1 = (Atom) m.particles.get(i);
			ArrayList<Atom> neighbor = atom1.getNeighbor();
			for (Atom atom2: neighbor) {
				m.exclude[atom1.getGUID()-1][atom2.getGUID()-1] = true;
				ArrayList<Atom> neighbor2 = atom2.getNeighbor();
				for (Atom atom3: neighbor2) {
					m.exclude[atom1.getGUID()-1][atom3.getGUID()-1] = true;
				}
			}
		}
		
//		for (int i=0; i<n; i++) {
//			for (int j=0; j<n; j++) {
//				if (m.exclude[i][j]) {
//					System.out.println(i + " " + j);
//				}
//			}
//		}
		
	}

	private void readNameAndPosition(String fileName) {
		List<String> fileAsList = FileReader.readFile(fileName);
		int count = Integer.valueOf(fileAsList.get(1).trim());
		loadNameAndPosition(fileAsList.subList(2, count+2));
	}
	
	private void loadAtoms(List<String> atoms) {
//		System.out.println(atoms.toString());
		int i = 0;
		for (String s : atoms) {
			String[] t = s.split(" +");
			if (t.length > 8 && !t[0].contains(";")) {
//				System.out.println(Arrays.toString(t));
				String type = t[2];
				double charge = Double.parseDouble(t[7]);
				double mass = Double.parseDouble(t[8]);
				Atom atom = (Atom) m.particles.get(i);
				atom.setType(type);
				atom.setCharge(charge);
				atom.setMass(mass);
//				System.out.println(i + " " + atom);
				i++;
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
				atom1.addNeighbor(atom2);
				atom2.addNeighbor(atom1);
			}
		}
	}
	
	private void loadPairs(List<String> pairs) {
//		System.out.println(pairs.toString());
		int n = m.particles.size();
		m.exclude = new boolean[n][n];
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				m.exclude[i][j] = false;
			}
		}
		
		for (String s : pairs) {
			String[] t = s.split(" +");
			if (t.length > 3 && !t[0].contains(";")) {
//				System.out.println(Arrays.toString(t));
				int n1 = Integer.parseInt(t[1])-1;
				int n2 = Integer.parseInt(t[2])-1;
				
				m.exclude[n1][n2] = true;
				m.exclude[n2][n1] = true;
//				m.interactions.add(new ElectrostaticPotential(atom1, atom2));
//				m.interactions.add(new LennardJonesPotential(atom1, atom2));
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
	
	public void loadNameAndPosition(List<String> s) {
		for (int i=0; i<s.size(); i++) {
			String[] t = s.get(i).split(" +");
//			System.out.println(Arrays.toString(t));
			double x = Double.parseDouble(t[4]);
			double y = Double.parseDouble(t[5]);
			double z = Double.parseDouble(t[6]);
			
			Atom atom = new Atom();
			atom.setPosition(x, y, z, 0);
			atom.setName(t[2]);
			
			if (t.length > 9) {
				double v_x = Double.parseDouble(t[7]);
				double v_y = Double.parseDouble(t[8]);
				double v_z = Double.parseDouble(t[9]);
				atom.setVelocity(v_x, v_y, v_z);
			}
			m.particles.add(atom);
		}
	}
	
	public void generateNonBondedInteraction() {
		int count = m.particles.size();
		if ((Boolean) params.get("Electrostatic")) {
			for (int i=0; i<count-1; i++) {
				for (int j=i+1; j<count; j++) {
					ElectrostaticPotential e = new ElectrostaticPotential((Atom)m.particles.get(i), (Atom)m.particles.get(j));
					m.interactions.add(e);
				}
			}
		}
		
		if ((Boolean) params.get("LennardJones")) {
			for (int i=0; i<count-1; i++) {
				for (int j=i+1; j<count; j++) {
					LennardJonesPotential l = new LennardJonesPotential((Atom)m.particles.get(i), (Atom)m.particles.get(j));
					m.interactions.add(l);
				}
			}
		}
		
	}
	
	public void initVelocity() {
		Random rand = new Random();
		double p, m1, m2, v1, v2;
		AbstractParticle a1, a2;
		for (int i=0; i<m.particles.size()/2; i++) {
			p = 0.15 * rand.nextDouble() + 1;
			a1 = m.particles.get(i);
			m1 = a1.getMass();
			a2 = m.particles.get(m.particles.size()-i-1);
			m2 = a2.getMass();
			v1 = Math.sqrt(2 * K * T * Math.log(p) / m1 / Math.sqrt(m1 / (2 * Math.PI * K * T)));
			v2 = Math.sqrt(2 * K * T * Math.log(p) / m2 / Math.sqrt(m2 / (2 * Math.PI * K * T)));
			a1.setVelocity(v1, v1, v1);
			a2.setVelocity(-v2, -v2, -v2);
		}
	}
}
