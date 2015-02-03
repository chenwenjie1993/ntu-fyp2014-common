package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmol.minimize.MinAngle;
import org.jmol.minimize.forcefield.CalculationsMMFF;

import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Bond;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;

public class Minimizer {
	protected static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MoleculeEditor.class.getName());
	protected static HashMap<String,Integer>mmffType;
	protected static HashMap<String,double[]>bondStretch;
	protected static HashMap<String,double[]>angleBend;
	protected static HashMap<String,double[]>stretchBend;
	protected static HashMap<String,Double>OOP;
	protected static HashMap<String,double[]>torsion;
	protected static HashMap<String,double[]>vdw;
	protected static HashMap<String,double[]>electroInter;
	
	/**
	 * Variables needed for calculations
	 */
	public static double CS = 0.0;
	private Model model;
	
	public double bondStretchVal=0.0;
	public double angleBendVal=0.0;
	public double stretchBendVal=0.0;
	public double OOPVal=0.0;
	public double torsionVal=0.0;
	public double vdwVal=0.0;
	public double electroInterVal=0.0;
	
	public void setupMinimizing(Model model){
		this.model = model;
		
		readMMFF94Types("res/res/MMFF94SYMB.txt");
		assignMMFFTypes();
		
		readBondStretch("res/res/MMFF94-bond-stretching-interaction.txt");
		for(int i=0;i<model.getBonds().length;i++){
			Bond bond = model.getBonds()[i];
			bondStretchVal += calBondStretch(bond.getAtom1(), bond.getAtom2());
		}
		
		// calculation of Angle Bend
		readAngleBend("res/res/MMFF94-angle-bending.txt");
		Atom atm3;
		for(Atom atm : model.getAtomHash().values()){
			for(int i=0;i<atm.getBond().size();i++){
				Atom atm1 = atm.getBond().get(i).getAtom1();
				Atom atm2 = atm.getBond().get(i).getAtom2();
				if (atm1.getBond().size() > 1) {
			        Atom[] atomList = getBondedAtom(atm1);
			        for (int j=0;j<atomList.length;j++){
			        	if((atm3 = atomList[j]).getAtomSeqNum()>atm1.getAtomSeqNum()){
			        		System.out.println(atm1.getAtomSeqNum()+"\t"+ atm2.getAtomSeqNum()+"\t"+ atm3.getAtomSeqNum());
			        		angleBendVal += calAngleBend(atm1, atm2, atm3);
			        	}
			        }  		
				}
				if (atm2.getBond().size() > 1) {
					Atom[] atomList = getBondedAtom(atm2);
			        for (int j=0;j<atomList.length;j++)
			        	if ((atm3 = atomList[j]).getAtomSeqNum() < atm2.getAtomSeqNum() && atm3.getAtomSeqNum() > atm1.getAtomSeqNum()){ 
			        		System.out.println(atm3.getAtomSeqNum()+"\t"+ atm1.getAtomSeqNum()+"\t"+ atm2.getAtomSeqNum());
			        		angleBendVal += calAngleBend(atm3, atm1, atm2);
			        	}
			      }
			}
		}
		
		// calculation of Stretch Bend
		readStretchBend("res/res/MMFF94-stretch-bend-interactions.txt");
		
		// calculation of Out of Plane Bending
		readOOP("res/res/MMFF94-out-of-plane bending-interaction.txt");
		
		// calculation of torsion
		readTorsion("res/res/MMFF94-torsion interactions.txt");
		
		// calculation of van der waals interactions
		readVDW("res/res/MMFF94-van der Waals interactions.txt");
		
		// calculation of electrostatic interaction
		readElectroInter("res/res/MMFF94-partial-bond-charge-increment.txt");
		
		double energy = angleBendVal;// + calAngleBend() + calStrechBend() + calOOP() + calTorsion() + calVDWI() + calElectroInter();
		System.out.println("Energy" + energy);
	}

	private Atom[] getBondedAtom(Atom atm1) {
		ArrayList<Bond> bonds = atm1.getBond();
		ArrayList<Atom> atoms = new ArrayList<Atom>();
		for(int i=0;i<bonds.size();i++){
			if(bonds.get(i).getAtom1().getAtomSeqNum()==atm1.getAtomSeqNum())
				atoms.add(bonds.get(i).getAtom2());
			else if(bonds.get(i).getAtom2().getAtomSeqNum()==atm1.getAtomSeqNum())
				atoms.add(bonds.get(i).getAtom1());
		}
		return ((Atom[])atoms.toArray(new Atom[atoms.size()]));
	}

	private static double calBondStretch(Atom atm1, Atom atm2){
		String key;
		if(atm1.getmmType()<=atm2.getmmType())
			key = "0"+atm1.getmmType() +atm2.getmmType();
		else
			key = "0"+atm2.getmmType() +atm1.getmmType();
		double[] values = bondStretch.get(key);
		double kb = values[0];
		double r0 = values[1];
		double rab = distance(atm1.getCoordinates(), atm2.getCoordinates());
		
		double delta = rab - r0; 
	    double delta2 = delta * delta;
		
		double energy = 143.9325/2 * kb * delta2 * (1 + CS*delta + (7/12)*(CS*CS)*(delta2));
		return energy;
	}
	
	

	private static double calAngleBend(Atom atm1, Atom atm2, Atom atm3){
		double energy=0.0;
		String key = ""+atm1.getmmType()+atm2.getmmType()+atm3.getmmType();
		double[] values = angleBend.get(key);
	    double ka = values[0];
	    double t0 = values[1];
	    
		if (t0 == 180) {
			energy = 143.932 * ka * (1 + Math.cos(theta));
		} 
		else {
			energy = (0.043844/2) * ka * Math.pow(dt, 2) * (1 + CB * dt); // 0.043844/2
		}
		return value;
	}
	private static double calStrechBend(){
		double value = 2.51210 ;
		return value;
	}
	private static double calOOP(){
		double value = 143.9325 ;
		return value;
	}
	private static double calTorsion(){
		double value = 143.9325 ;
		return value;
	}
	private static double calVDWI(){
		double value = 143.9325;
		return value;
	}
	private static double calElectroInter(){
		double value = 143.9325;
		return value;
	}
	
	private static double distance(float[] a, float[] b) {
		double dx = a[0] - b[0];
		double dy = a[1] - b[1];
		double dz = a[2] - b[2];
		return (dx * dx + dy * dy + dz * dz);
	}
	
	private static void readMMFF94Types(String fileLocation) {
		String line;
		Pattern pattern = Pattern.compile("\\s\\s[\\w\\W]+\\s+\\d+\\s[\\w\\W]+$");
		Matcher matcher;
		mmffType = new HashMap<String,Integer>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
			while((line = reader.readLine()) != null){
				if(line.startsWith("1.")|| line.startsWith("*"))
					continue;
				
				matcher = pattern.matcher(line);
				if(matcher.find()){
					String[] splitLine = line.split("\\s+"); // [1] formula [2] type
					mmffType.put(splitLine[1], Integer.valueOf(splitLine[2]));
				}
			}
			reader.close();
		}
		catch(NumberFormatException e){
			logger.log(Level.WARNING, "Problem converting type string to int");
		}
		catch(IOException e){
			logger.log(Level.WARNING, "Loading mmffType file Failed");
		}
	}
	
	private void assignMMFFTypes() {
		HashMap<String, Atom> atomHash = model.getAtomHash();
		for(Atom atm : atomHash.values()){
			String atmMMType = AtomToMMType.setMMType(atm);
			int type = mmffType.get(atmMMType);
			atm.setmmType(type);
		}
		
	}
	
	private void readBondStretch(String file) {
		String line;
		bondStretch = new HashMap<String,double[]>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				if(line.startsWith("6.")|| line.startsWith("*"))
					continue;
				try{
					String[] splitLine = line.split("\\s+"); // [0]-[2] type [3] kb [4] r0
					String key = splitLine[0]+splitLine[1]+splitLine[2];
					double[] value = {Double.parseDouble(splitLine[3]),Double.parseDouble(splitLine[4])};
					bondStretch.put(key,value);
				}
				catch(NumberFormatException e){
					logger.log(Level.WARNING, "Problem converting string to double");
				}
			}
			reader.close();
		}
		catch(IOException e){
			logger.log(Level.WARNING, "Loading bond stretch file Failed");
		}
	}
	
	private void readAngleBend(String file){
		String line;
		angleBend = new HashMap<String,double[]>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				if(line.startsWith("8.")|| line.startsWith("*"))
					continue;
				try{
					String[] splitLine = line.split("\\s+"); // [0]-[2] type [3] kb [4] t0
					String key = splitLine[0]+splitLine[1]+splitLine[2]+splitLine[3];
					double[] value = {Double.parseDouble(splitLine[4]),Double.parseDouble(splitLine[5])};
					angleBend.put(key,value);
				}
				catch(NumberFormatException e){
					logger.log(Level.WARNING, "Problem converting string to double");
				}
			}
			reader.close();
		}
		catch(IOException e){
			logger.log(Level.WARNING, "Loading angle bend file Failed");
		}
	}
	
	private void readStretchBend(String file) {
		String line;
		stretchBend = new HashMap<String,double[]>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				if(line.startsWith("9.")|| line.startsWith("*"))
					continue;
				try{
					String[] splitLine = line.split("\\s+"); // [0]-[3] type [4] kbaIJK [5] kbaKJI
					String key = splitLine[1]+splitLine[2]+splitLine[3];
					double[] value = {Double.parseDouble(splitLine[4]),Double.parseDouble(splitLine[5])};
					stretchBend.put(key,value);
				}
				catch(NumberFormatException e){
					logger.log(Level.WARNING, "Problem converting string to double");
				}
			}
			reader.close();
		}
		catch(IOException e){
			logger.log(Level.WARNING, "Loading stretch bend file Failed");
		}
	}

	private void readOOP(String file) {
		String line;
		OOP = new HashMap<String,Double>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				if(line.startsWith("11.")|| line.startsWith("*"))
					continue;
				try{
					String[] splitLine = line.split("\\s+"); // [0]-[3] type [4] koop 
					String key = splitLine[1]+splitLine[2]+splitLine[3];
					double value = Double.parseDouble(splitLine[4]);
					OOP.put(key,value);
				}
				catch(NumberFormatException e){
					logger.log(Level.WARNING, "Problem converting string to double");
				}
			}
			reader.close();
		}
		catch(IOException e){
			logger.log(Level.WARNING, "Loading OOP file Failed");
		}
	}

	private void readTorsion(String file) {
		String line;
		torsion = new HashMap<String,double[]>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				if(line.startsWith("12.")|| line.startsWith("*"))
					continue;
				try{
					String[] splitLine = line.split("\\s+"); // [0]-[4] type [5] V1 [6] V2 [7] V3
					String key = splitLine[1]+splitLine[2]+splitLine[3]+splitLine[4];
					double[] value = {Double.parseDouble(splitLine[5]),Double.parseDouble(splitLine[6]),Double.parseDouble(splitLine[7])};
					torsion.put(key,value);
				}
				catch(NumberFormatException e){
					logger.log(Level.WARNING, "Problem converting string to double");
				}
			}
			reader.close();
		}
		catch(IOException e){
			logger.log(Level.WARNING, "Loading torsion file Failed");
		}
	}

	private void readVDW(String file) {
		String line;
		vdw = new HashMap<String,double[]>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				if(line.startsWith("13.")|| line.startsWith("*"))
					continue;
				try{
					String[] splitLine = line.split("\\s+"); // [0] type [1] alpha-i [2] N-i [3] A-i [4] G-i
					String key = splitLine[1];
					double[] value = {Double.parseDouble(splitLine[1]),Double.parseDouble(splitLine[2]),
										Double.parseDouble(splitLine[3]),Double.parseDouble(splitLine[4])};
					vdw.put(key,value);
				}
				catch(NumberFormatException e){
					logger.log(Level.WARNING, "Problem converting string to double");
				}
			}
			reader.close();
		}
		catch(IOException e){
			logger.log(Level.WARNING, "Loading van der Waals file Failed");
		}
	}

	private void readElectroInter(String file) {
		String line;
		electroInter = new HashMap<String,double[]>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				if(line.startsWith("15.")|| line.startsWith("*"))
					continue;
				try{
					String[] splitLine = line.split("\\s+"); // [0] type [1] pbci [2] fcadj
					String key = splitLine[1];
					double[] value = {Double.parseDouble(splitLine[2]),Double.parseDouble(splitLine[3])};
					electroInter.put(key,value);
				}
				catch(NumberFormatException e){
					logger.log(Level.WARNING, "Problem converting string to double");
				}
			}
			reader.close();
		}
		catch(IOException e){
			logger.log(Level.WARNING, "Loading electrostatic interaction file Failed");
		}
	}

}
