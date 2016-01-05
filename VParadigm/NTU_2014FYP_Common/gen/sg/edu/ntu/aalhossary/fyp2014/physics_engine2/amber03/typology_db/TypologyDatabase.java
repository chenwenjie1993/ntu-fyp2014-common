package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.typology_db;

import java.util.*;
import java.util.logging.Logger;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.util.*;

public class TypologyDatabase {
	private static final Logger log = Logger.getLogger( TypologyDatabase.class.getName() );
	
	private static TypologyDatabase instance = null;
	private HashMap<String, List<Double>> vdParams;
	private HashMap<List<String>, List<Double>> bondParams;
	private HashMap<List<String>, List<Double>> angleParams;
	private HashMap<List<String>, List<Double>> properDihedralParams;
	private HashMap<List<String>, List<Double>> improperDihedralParams;
	
	private TypologyDatabase() {
		log.info("Loading Typology Database");
		loadBondedParams();
		loadNonBondedParams();
	}
	
	public static TypologyDatabase getInstance() {
		if (instance == null) {
			instance = new TypologyDatabase();
		}
		return instance;
	}
	
	public List<Double> getVdParams(String atom) {
		return vdParams.get(atom);
	}
	
	public List<Double> getBondParams(List<String> atoms) {
		return bondParams.get(atoms);
	}
	
	public List<Double> getAngleParams(List<String> atoms) {
		return angleParams.get(atoms);
	}
	
	public List<Double> getImproperDihedralParams(List<String> atoms) {
		List<String> key = new ArrayList<String>(atoms);
		List<Double> value = improperDihedralParams.get(key);
		if (value == null) {
			key.set(0, "X");
			value = improperDihedralParams.get(key);
		}
		if (value == null) {
			key.set(1, "X");
			value = improperDihedralParams.get(key);
		}
		return value;
	}
	
	public List<Double> getProperDihedralParams(List<String> atoms) {
		List<String> key = new ArrayList<String>(atoms);
		List<Double> value = properDihedralParams.get(key);
		if (value == null) {
			key.set(0, "X");
			key.set(3, "X");
			value = properDihedralParams.get(key);
		}
		return value;
	}
	
	private void loadNonBondedParams() {
		String fileName = "res/amber03/ffnonbonded.itp";
		List<String> fileAsList = FileReader.readFile(fileName);
		int atomtypes = fileAsList.indexOf("[ atomtypes ]");
		loadVdParams(fileAsList.subList(atomtypes+1,  fileAsList.size()));
	}
	
	private void loadBondedParams() {
		String fileName = "res/amber03/ffbonded.itp";
		List<String> fileAsList = FileReader.readFile(fileName);
		int bondtypes = fileAsList.indexOf("[ bondtypes ]");
		int constrainttypes = fileAsList.indexOf("[ constrainttypes ]");
		int angletypes = fileAsList.indexOf("[ angletypes ]");
		int improperDihedral = fileAsList.indexOf("[ dihedraltypes ]");
		int properDihedral = fileAsList.lastIndexOf("[ dihedraltypes ]");
		
//		System.out.println(bondtypes);
//		System.out.println(constrainttypes);
//		System.out.println(angletypes);
//		System.out.println(improperDihedral);
//		System.out.println(properDihedral);
		
		loadBondParams(fileAsList.subList(bondtypes+1, constrainttypes));
		loadAngleParams(fileAsList.subList(angletypes+1, improperDihedral));
		loadImproperDihedralParams(fileAsList.subList(improperDihedral+1, properDihedral));
		loadProperDihedralParams(fileAsList.subList(properDihedral+1, fileAsList.size()));
	}
	
	private void loadVdParams(List<String> atoms) {
		vdParams = new HashMap<String, List<Double>>();
		for (String s : atoms) {
			String[] t = s.split(" +");
			if (t.length > 6 && !t[0].contains(";")) {
				String key = t[0];
				System.out.println(key);
				List<Double> value = new ArrayList<Double>();
				value.add(Double.parseDouble(t[5]));
				value.add(Double.parseDouble(t[6]));
				System.out.println(value);
				vdParams.put(key, value);
			}
		}
	}
	
	private void loadBondParams(List<String> bonds) {
		bondParams = new HashMap<List<String>, List<Double>>();
		for (String s : bonds) {
			String[] t = s.split(" +");
			if (t.length > 5 && !t[0].contains(";")) {
				List<String> key = new ArrayList<String>();
				key.add(t[1]);
				key.add(t[2]);
//				System.out.println(key);
				List<Double> value = new ArrayList<Double>();
				value.add(Double.parseDouble(t[4]));
				value.add(Double.parseDouble(t[5]));
//				System.out.println(value);
				bondParams.put(key, value);
			}
		}
	}
	
	private void loadAngleParams(List<String> angles) {
		angleParams = new HashMap<List<String>, List<Double>>();
		for (String s : angles) {
			String[] t = s.split(" +");
			if (t.length > 5 && !t[0].contains(";")) {
				List<String> key = new ArrayList<String>();
				key.add(t[0]);
				key.add(t[1]);
				key.add(t[2]);
//				System.out.println(key);
				List<Double> value = new ArrayList<Double>();
				value.add(Double.parseDouble(t[4]));
				value.add(Double.parseDouble(t[5]));
//				System.out.println(value);
				angleParams.put(key, value);
			}
		}
	}
	
	private void loadImproperDihedralParams(List<String> dihedrals) {
		improperDihedralParams = new HashMap<List<String>, List<Double>>();
		for (String s : dihedrals) {
			String[] t = s.split(" +");
			if (t.length > 6 && !t[0].contains(";")) {
				List<String> key = new ArrayList<String>();
				key.add(t[0]);
				key.add(t[1]);
				key.add(t[2]);
				key.add(t[3]);
//				System.out.println(key);
				List<Double> value = new ArrayList<Double>();
				value.add(Double.parseDouble(t[5]));
				value.add(Double.parseDouble(t[6]));
//				System.out.println(value);
				improperDihedralParams.put(key, value);
			}
		}
	}

	private void loadProperDihedralParams(List<String> dihedrals) {
		properDihedralParams = new HashMap<List<String>, List<Double>>();
		for (String s : dihedrals) {
			String[] t = s.split(" +");
			if (t.length > 8 && !t[0].contains(";")) {
				List<String> key = new ArrayList<String>();
				key.add(t[1]);
				key.add(t[2]);
				key.add(t[3]);
				key.add(t[4]);
//				System.out.println(key);
				List<Double> value = new ArrayList<Double>();
				value.add(Double.parseDouble(t[6]));
				value.add(Double.parseDouble(t[7]));
				value.add(Double.parseDouble(t[8]));
//				System.out.println(value);
				properDihedralParams.put(key, value);
			}
		}
	}
}
