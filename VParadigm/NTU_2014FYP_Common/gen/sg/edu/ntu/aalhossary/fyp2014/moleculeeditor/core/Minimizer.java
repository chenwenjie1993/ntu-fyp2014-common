package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javajs.util.T3d;

import org.jmol.minimize.MinAtom;
import org.jmol.minimize.Util;

import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Bond;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;

public class Minimizer {
	protected static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MoleculeEditor.class.getName());
	protected HashMap<String,Integer>mmffType;
	protected HashMap<String,Double>bondCharge;
	protected HashMap<String,Double>partialCharge;
	protected HashMap<String,double[]>bondStretch;
	protected HashMap<String,double[]>angleBend;
	protected HashMap<String,double[]>stretchBend;
	protected HashMap<String,Double>OOP;
	protected HashMap<String,double[]>torsion;
	protected HashMap<String,double[]>vdw;
	protected HashMap<String,double[]>electroInter;
	
	/**
	 * Variables needed for calculations
	 */
	public static double CS = 0.0;
	public double energyInit = 0.0;
	private Model model;
	
	public double bondStretchVal=0.0;
	public double angleBendVal=0.0;
	public double stretchBendVal=0.0;
	public double OOPVal=0.0;
	public double torsionVal=0.0;
	public double vdwVal=0.0;
	public double electroInterVal=0.0;

	double[][] coordSaved;
	BufferedWriter writer;
	private UpdateRegistry updateReg;
	int currentMinStep=0;
	double dE=0.0;
	
	public Minimizer(UpdateRegistry updateReg){
		this.updateReg = updateReg;
	}
	
	public void setupMinimizing(Model model){
		this.model = model;
		//this.indivAtomEnergy = new double[7][model.getAtomHash().size()];
		
		//List<Atom> atomlist = new ArrayList<Atom>(model.getAtomHash().values());
		/*try {
			writer = new BufferedWriter(new FileWriter("res/temp/min.txt",true));*/
			//writer.write("Electrostatic Interaction [qi,qj][a,b][energy]");
			//writer.newLine();
			
		// initialise energy
		readMMFFFiles();
		
		/*writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		energyInit = calculateEnergy(false);
		System.out.println("Energy Init" + energyInit);
		try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("res/temp/JmolEnergyMin.txt",true));
			writer.write("Initial Energy:" + energyInit);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startMinimizing(){
		currentMinStep=0;
		while(steepestDescentSteps(100));
	}

	private boolean steepestDescentSteps(int n) {
		
		for (int iStep = 1; iStep <= n; iStep++) {
			currentMinStep++;
			for(Atom atm : model.getAtomHash().values()){
				setForcesUsingNumericalDerivative(atm);
			}
			updateCoordinates();
			double energy = calculateEnergy(false);
			//System.out.println("\tEnergy" + energy);
			dE = energy - energyInit;
			boolean done = isNear3(energy, energyInit, 1e-3);
			
			if (done|| currentMinStep % 10 == 0 || n <= currentMinStep) {
			 try {
			    	BufferedWriter writer = new BufferedWriter(new FileWriter("res/temp/JmolEnergyMin.txt",true));
			    	
					writer.write("\tMain Step: " + iStep + "\tEnergy:" + String.format("%.5g", energy) + "\tdE: "+String.format("%.5g", dE));
					writer.newLine();/**/
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}/**/
			//System.out.print("\tStep: " + iStep + "\tEnergyInit:" + energyInit);
			}
			energyInit = energy;
			if (done || n <= currentMinStep) {
				return false;
			}
		}
		return true;
	}

	private void setForcesUsingNumericalDerivative(Atom atom){
		double delta = 1.0e-5;
		double[] force = new double[3];
		force[0] = -getDE(atom, 0, delta);
		force[1] = -getDE(atom, 1, delta);
		force[2] = -getDE(atom, 2, delta);
	    atom.setForces(force);
	    //System.out.println("Atom="+ atom.getAtomSeqNum() + "\tforce="+force[0] + "," + force[1] + "," + force[2]+ "\n");
	}
	
	private double getDE(Atom atom, int i, double delta) {
		atom.getCoordinates()[i] += delta;
	    double e = calculateEnergy(false);
	    atom.getCoordinates()[i] -= delta;
	    //System.out.println("Final=[" + e + "," + energyInit+","+delta+"]");
		return (e - energyInit) / delta;
		
	}

	private double calculateEnergy(boolean gradient) {
		clearValues();
		if (gradient)
		      clearForces();
		for(int i=0;i<model.getBonds().length;i++){
			Bond bond = model.getBonds()[i];
			bondStretchVal += calBondStretch(gradient, bond.getAtom1(), bond.getAtom2());
			
		}
		
		boolean found=false;
		for(Atom atm : model.getAtomHash().values()){			// For each atom in the hash
			for(Atom atm2 : model.getAtomHash().values()){
				
				if(!atm.hasNeighbour(atm2)&& atm.getAtomSeqNum()<atm2.getAtomSeqNum()){
					found=false;
					for(int i=0;i<atm.getNeighbourAtoms().length;i++){
						for(int j=0;j<atm2.getNeighbourAtoms().length;j++){
							if(atm.getNeighbourAtoms()[i]==atm2.getNeighbourAtoms()[j])
								found=true;
						}
					}
					if(!found){
						vdwVal += calVDWI(gradient, atm, atm2);
						if((atm.getPartialCharge()==0.0 || atm2.getPartialCharge()==0.0) || atm.getAtomSeqNum()==atm2.getAtomSeqNum())
							continue;
						else
							electroInterVal += calElectroInter(gradient, atm, atm2);
					}
				}
			}
		}
		
		Atom atm3 = null, atm4 = null;
		Atom[] atomList, atomList2;
		for(Atom atm : model.getAtomHash().values()){			// For each atom in the hash
			for(int i=0;i<atm.getBond().size();i++){			// get the list of bonds
				Atom atm1 = atm.getBond().get(i).getAtom1();	// left atom
				Atom atm2 = atm.getBond().get(i).getAtom2();	// right atom
				//System.out.print("Atom1: "+ atm1.getAtomSeqNum() + "\tAtom2: " + atm2.getAtomSeqNum() + "\tSTART: ");
				// if left atom has more than one bonds and not the first atom
				if ((atm1.getAtomSeqNum()<atm.getAtomSeqNum())||(atm2.getAtomSeqNum()>atm.getAtomSeqNum())) {	// seq # less than curr, can continue				
			        atomList = getBondedAtom(atm1);		// give the list of atom index
				    for (int j=0;j<atomList.length;j++){
				    	if((atm3 = atomList[j]).getAtomSeqNum()!=atm2.getAtomSeqNum()){
				        	angleBendVal += calAngleBend(gradient, atm2, atm1, atm3);
				        	//stretchBendVal += calStrechBend(atm2, atm1, atm3);
				        	
				        	//System.out.print(atm2.getAtomSeqNum()+"\t"+ atm1.getAtomSeqNum()+"\t"+ atm3.getAtomSeqNum());
				        	atomList2 = getBondedAtom(atm3);
				        	if(atomList2.length==1){
				        		torsionVal += calTorsion(gradient, atm2, atm1, atm3, null);
				        		OOPVal += calOOP(gradient, atm2, atm1, atm3,null);
				        	}
				        	else{
				        		for (int k=0;k<atomList2.length;k++){
				        			atm4 = atomList2[k];
				        			torsionVal += calTorsion(gradient, atm2, atm1, atm3, atm4);
				        			OOPVal += calOOP(gradient, atm2, atm1, atm3, atm4);
				        			//System.out.println("\t"+ atm4.getAtomSeqNum());
				        		}
				        	}
				    	}
				    }
				}
				if (atm2.getAtomSeqNum()<atm.getAtomSeqNum() || (atm1.getAtomSeqNum()>atm.getAtomSeqNum())) {
					atomList = getBondedAtom(atm2);
			        for (int j=0;j<atomList.length;j++){
			        	if((atm3 = atomList[j]).getAtomSeqNum()!=atm1.getAtomSeqNum()){
			        		angleBendVal += calAngleBend(gradient, atm1, atm2, atm3);
			        		//stretchBendVal += calStrechBend(atm1, atm2, atm3);
			        		//OOPVal += calOOP(gradient, atm1, atm2, atm3);
			        		//System.out.print(atm1.getAtomSeqNum()+"\t"+ atm2.getAtomSeqNum()+"\t"+ atm3.getAtomSeqNum());
			        		atomList2 = getBondedAtom(atm3);
			        		if(atomList2.length==1)
				        		torsionVal += calTorsion(gradient, atm1, atm2, atm3, null);
			        		else{
				        		for (int k=0;k<atomList2.length;k++){
				        			atm4 = atomList2[k];
				        			torsionVal += calTorsion(gradient, atm1, atm2, atm3, atm4);
				        			//System.out.println("\t(4)"+ atm4.getAtomSeqNum());
				        		}
			        		}
			        	}
			        }
			      }
				//System.out.println();
			}
		}
		/*if(ommitStretchBend)
			stretchBendVal=0.0;
		System.out.println(bondStretchVal + "," + angleBendVal + "," + stretchBendVal + "," + OOPVal + 
				"," + torsionVal + "," + vdwVal + "," + electroInterVal);*/
		/*try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("energyMin.txt",true));
			writer.write("a: " + bondStretchVal + "\tb: " + angleBendVal+ "\tc: " + stretchBendVal+"\td: " + OOPVal+
					"\te: " + torsionVal+"\tf: " + vdwVal+"\tg: " + electroInterVal);
			writer.newLine();
			writer.close();
		} catch (IOException ee) {
			// TODO Auto-generated catch block
			ee.printStackTrace();
		}*/
		return (bondStretchVal + angleBendVal + stretchBendVal + OOPVal + torsionVal + vdwVal + electroInterVal);
	}

	private void clearForces() {
		for (Atom atm : model.getAtomHash().values())
			atm.getForces()[0] = atm.getForces()[1] = atm.getForces()[2] = 0; 
	}

	private void clearValues() {
		bondStretchVal = 0.0;
		angleBendVal = 0.0;
		stretchBendVal = 0.0;
		OOPVal = 0.0;
		torsionVal = 0.0;
		vdwVal = 0.0;
		electroInterVal = 0.0;
		//ommitStretchBend=false;
	}

	private void readMMFFFiles() {
		readMMFF94Types("res/res/MMFF94SYMB.txt");
		readBondCharge("res/res/MMFF94-bond-charge-increment.txt");
		readElectroInter("res/res/MMFF94-partial-bond-charge-increment.txt");
		assignMMFFTypes();
		readBondStretch("res/res/MMFF94-bond-stretching-interaction.txt");
		readAngleBend("res/res/MMFF94-angle-bending.txt");
		readStretchBend("res/res/MMFF94-stretch-bend-interactions.txt");
		readOOP("res/res/MMFF94-out-of-plane bending-interaction.txt");
		readTorsion("res/res/MMFF94-torsion interactions.txt");
		readVDW("res/res/MMFF94-van der Waals interactions.txt");
		
	}

	private void updateCoordinates() {
		double step = 0.23;
	    double trustRadius = 0.3; // don't move further than 0.3 Angstroms
	    double trustRadius2 = trustRadius * trustRadius;
	    
	    double e1 = calculateEnergy(false);
	    for (int iStep = 0; iStep < 10; iStep++) {
	    	//System.out.println("ISTEP:" + iStep);
		    saveCoordinates();
			 for (Atom atm : model.getAtomHash().values()){
		          double[] force = atm.getForces();
		          double[] coord = {(double)atm.getCoordinates()[0],(double)atm.getCoordinates()[1],(double)atm.getCoordinates()[2]};
		          double f2 = (force[0] * force[0] + force[1] * force[1] + force[2]
			              * force[2]);
			          if (f2 > trustRadius2 / step / step) {
			            f2 = trustRadius / Math.sqrt(f2) / step;
			         
			            force[0] *= f2;
			            force[1] *= f2;
			            force[2] *= f2;
			          }
			          
			          for (int j = 0; j < 3; ++j) {
			        	  if (Util.isFinite(force[j])) {
			              double tempStep = force[j] * step;
			              if (tempStep > trustRadius)
			                coord[j] += trustRadius;
			              else if (tempStep < -trustRadius)
			                coord[j] -= trustRadius;
			              else
			                coord[j] += tempStep;
			        	  }
			          }
			          
			          atm.setForces(force);
			          atm.setCoordinates(coord);
			 }
			 double e2 = calculateEnergy(false);
			 
			 if (isNear3(e2, e1, 1.0e-3))
			        break;
		      if (e2 > e1) {
		        step *= 0.1;
		        restoreCoordinates();
		      } else if (e2 < e1) {
		        e1 = e2;
		        step *= 2.15;
		        if (step > 1.0)
		          step = 1.0;
		      }
		      
		      updateReg.updateViewerCoord();
		      /*try {
			    	BufferedWriter writer = new BufferedWriter(new FileWriter("JmolEnergyMin.txt",true));
					writer.write("iStep=" + iStep + " " + "E= " + e1);
					writer.newLine();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	    }
	    
	}
	
	private boolean isNear3(double a, double b, double eps) {
		return (Math.abs(a - b) < eps);
	}

	private void restoreCoordinates() {
		ArrayList<Atom> tempAtomList = new ArrayList<Atom>(model.getAtomHash().values());
		for (int i = 0; i < coordSaved.length; i++) 
	    	  tempAtomList.get(i).setCoordinates(coordSaved[i]);
	}

	private void saveCoordinates() {
		ArrayList<Atom> tempAtomList = new ArrayList<Atom>(model.getAtomHash().values());
		if (coordSaved == null)
		      coordSaved = new double[tempAtomList.size()][3];
		    for (int i = 0; i < tempAtomList.size(); i++) 
		      for (int j = 0; j < 3; j++)
		        coordSaved[i][j] = tempAtomList.get(i).getCoordinates()[j];
	}

	private void addForces(Atom[] atmList, double dE) {
		for(int i=0;i<atmList.length;i++){
			atmList[i].getForces()[0] += atmList[i].getCoordinates()[0] * dE;
			atmList[i].getForces()[1] += atmList[i].getCoordinates()[1] * dE;
			atmList[i].getForces()[2] += atmList[i].getCoordinates()[2] * dE;
		}
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

	private double calBondStretch(boolean gradient, Atom atm1, Atom atm2){
		String key;
		double rab=0.0, dE=0.0;
		double[] vec = new double[3];
		if (gradient) {
		      rab = restorativeForceAndDistance(atm1.getCoordinates(), atm2.getCoordinates(), vec);
		} else {
		      rab = Math.sqrt(distance(atm1.getCoordinates(), atm2.getCoordinates()));
		}
		if (isNearZero2(rab, 1.0e-3))
		   rab = 1.0e-3;
		    
		if(atm1.getmmType()<=atm2.getmmType()){
			key = ""+atm1.getmmType() +atm2.getmmType();
			//rab = Math.sqrt(distance(atm1.getCoordinates(), atm2.getCoordinates()));
		}
		else{
			key = ""+atm2.getmmType() +atm1.getmmType();
			//rab = Math.sqrt(distance(atm2.getCoordinates(), atm1.getCoordinates()));
		}
		double[] values = bondStretch.get(key);
		double kb = values[0];
		double r0 = values[1];
		
		double delta = rab - r0; 
	    double delta2 = delta * delta;
		
		double energy = 143.9325/2 * kb * delta2 * (1 + CS*delta + (7/12)*(CS*CS)*(delta2));
		
		if (gradient) {
		      dE = 143.9325/2 * kb * delta * (2 + 3*CS*delta + 4*(CS*CS)*delta2);
		      Atom[] atmList = {atm1,atm2};
		      addForces(atmList, dE);
		}
		/*try {
			writer.write("Bond Stretch [kb,r0][delta][energy]");
			writer.newLine();
			writer.write("["+atm1.getAtomSeqNum()+","+atm2.getAtomSeqNum()+"]\t["+kb+","+r0+"]\t["+delta+"]\t["+energy+"]");
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return energy;
	}

	private double calAngleBend(boolean gradient, Atom atm1, Atom atm2, Atom atm3){
		double energy=0.0, dE=0.0, theta=0.0;
		String key = ""+atm1.getmmType()+atm2.getmmType()+atm3.getmmType();
		double[] values = angleBend.get(key);
		if(values==null)	// null values present CHECK!
			return 0.0;
	    double ka = values[0];
	    double t0 = values[1];
	    
	    // cos law
	    if (gradient) {
	        theta = restorativeForceAndAngleRadians(atm1.getCoordinates(), atm2.getCoordinates(), atm3.getCoordinates());
	      } else {
	        theta = angleRadiansABC(atm1.getCoordinates(), atm2.getCoordinates(), atm3.getCoordinates());
	      }
	    if (!isFinite(theta))
	        theta = 0.0;
	    /*double atm12 = distance(atm1.getCoordinates(),atm2.getCoordinates());
	    double atm23 = distance(atm2.getCoordinates(),atm3.getCoordinates());
	    double atm13 = distance(atm1.getCoordinates(),atm3.getCoordinates());
	    double theta = Math.acos((atm12 + atm23 - atm13 ) / 2 / Math.sqrt(atm12 * atm23));*/
	    
	    double dt = ((theta * (180.0 / Math.PI)) - t0);
	    double CB = -0.4 * (Math.PI / 180.0);
	    
		if (t0 == 180) {
			energy = 143.932 * ka * (1 + Math.cos(theta));
			if (gradient)
		        dE = -143.932 * ka * Math.sin(theta);
		} 
		else {
			energy = (0.043844/2) * ka * Math.pow(dt, 2) * (1 + CB * dt); // 0.043844/2
			if (gradient)
		        dE = 0.021922 * ka * dt * (2 + 3 * CB * dt);
		}
		if (gradient){
			Atom[] atmList = {atm1,atm2,atm3};
			addForces(atmList, dE);
		}
		/*try {
			writer.write("Angle Bend [ka,t0][theta,dt,CB][energy]");
			writer.newLine();
			writer.write("["+atm1.getAtomSeqNum()+","+atm2.getAtomSeqNum()+","+atm3.getAtomSeqNum()+"]\t["+ka+","+t0+"]\t["+theta+","+dt+","+CB+"]\t["+energy+"]");
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return energy;
	}

	private double calStrechBend(boolean gradient, Atom atm1, Atom atm2, Atom atm3){
		double energy=0.0;
		String key = ""+atm1.getmmType()+atm2.getmmType()+atm3.getmmType();
		double[] sb_values = stretchBend.get(key);
		if(sb_values==null)	// null still present CHECK!!
			return 0.0;
		double kba_ijk = sb_values[0];
		double kba_kji = sb_values[1];
		
		if(angleBend.get(key)==null)	// null still present CHECK!!
			return 0.0;
		double t0 = angleBend.get(key)[1];
		
		String keyij = ""+atm1.getmmType()+atm2.getmmType();
	    String keyjk = ""+atm2.getmmType()+atm3.getmmType();
	    if(bondStretch.get(keyij)==null||bondStretch.get(keyjk)==null)	// null still present CHECK!!
			return 0.0;
	    double rij = Math.sqrt(distance(atm1.getCoordinates(), atm2.getCoordinates())) - bondStretch.get(keyij)[1];
	    double rjk = Math.sqrt(distance(atm2.getCoordinates(), atm3.getCoordinates())) - bondStretch.get(keyjk)[1];
	
	    double atm12 = distance(atm1.getCoordinates(),atm2.getCoordinates());
	    double atm23 = distance(atm2.getCoordinates(),atm3.getCoordinates());
	    double atm13 = distance(atm1.getCoordinates(),atm3.getCoordinates());
	    double theta = Math.acos((atm12 + atm23 - atm13 ) / 2 / Math.sqrt(atm12 * atm23));
	    
		double dt = ((theta * (180.0 / Math.PI)) - t0);
		
		double constant = 2.51210 * dt;
	    double dr = kba_ijk*rij + kba_kji*rjk;
	    double delta = theta * (180.0 / Math.PI) - t0;
	    
	    energy = constant * dr * delta;
	    /*try {
			writer.write("Stretch Bend [kba_ijk,kba_kji,t0][rij,rjk][theta,dt][constant,dr,delta][energy]");
			writer.newLine();
			writer.write("["+atm1.getAtomSeqNum()+","+atm2.getAtomSeqNum()+","+atm3.getAtomSeqNum()+"]\t["+kba_ijk+","+kba_kji+","+t0+"]\t["+rij+","+rjk+"]\t["+theta+","+dt+"]\t["+constant+","+dr+","+delta+"] ["+energy+"]");
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    return energy;
	}

	private double calOOP(boolean gradient, Atom atm1, Atom atm2, Atom atm3, Atom atm4){
		double energy=0.0, dE=0.0, theta=0.0;
		String key="";
		if(atm4==null)
			key = ""+atm1.getmmType()+atm2.getmmType()+atm3.getmmType() + "0";
		else
			key = ""+atm1.getmmType()+atm2.getmmType()+atm3.getmmType()+atm4.getmmType();
		if(OOP.get(key)==null)
			return 0.0;
		double koop = OOP.get(key);
		float[] vec1 = new float[3];
		float[] vec2 = new float[3];
		float[] vec3 = new float[3];
		if (gradient) {
		      theta = restorativeForceAndOutOfPlaneAngleRadians(atm1.getCoordinates(), atm2.getCoordinates(), 
		    		  atm3.getCoordinates(), atm4.getCoordinates(), vec1, vec2, vec3);
		    } else {
		      theta = pointPlaneAngleRadians(atm1.getCoordinates(), atm2.getCoordinates(), 
		    		  atm3.getCoordinates(), atm4.getCoordinates(), vec1, vec2, vec3, false);
		    }
		/*double atm12 = distance(atm1.getCoordinates(),atm2.getCoordinates());
	    double atm23 = distance(atm2.getCoordinates(),atm3.getCoordinates());
	    double atm13 = distance(atm1.getCoordinates(),atm3.getCoordinates());
	    double theta = Math.acos((atm12 + atm23 - atm13 ) / 2 / Math.sqrt(atm12 * atm23));*/
		
		double constant = (0.043844 * (180.0 / Math.PI))/ 2 * (180.0 / Math.PI);
		energy = constant * koop * theta * theta; // theta in radians
		
		if (gradient) {
		      dE = 0.043844 * (180.0 / Math.PI) * koop * theta;
		      Atom[] atmList = {atm1,atm2,atm3};
		      addForces(atmList, dE);
		}
		
		 /*if (!isFinite(theta))
		        theta = 0.0;
		try {
			writer.write("OOP [theta,constant][energy]");
			writer.newLine();
			writer.write("["+atm1.getAtomSeqNum()+","+atm2.getAtomSeqNum()+","+atm3.getAtomSeqNum()+"]\t["+theta+","+constant+"]\t["+energy+"]");
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return energy;
	}

	private double calTorsion(boolean gradient, Atom atm1, Atom atm2, Atom atm3, Atom atm4){
		double energy = 0.0, theta=0.0, dE=0.0;
		double[] vec1= new double[3];
		double[] vec2= new double[3];
		double[] vec3= new double[3];
		String key;
		if(atm4==null)
			key = ""+atm1.getmmType()+atm2.getmmType()+atm3.getmmType() + "0";
		else
			key = ""+atm1.getmmType()+atm2.getmmType()+atm3.getmmType() + atm4.getmmType();
		if(torsion.get(key)==null)
			return 0.0;
		double[] values = torsion.get(key);
		double v1 = values[0];
	    double v2 = values[1];
	    double v3 = values[2];
		
	    
	    if (gradient) {
	        theta = restorativeForceAndTorsionAngleRadians(atm1.getCoordinates(), atm2.getCoordinates(), 
	        		atm3.getCoordinates(), atm4.getCoordinates());
	        if (!isFinite(theta))
	            theta = 0.001 * (Math.PI / 180.0);
	    } else {
	        theta = getTorsionAngleRadians(atm1.getCoordinates(), atm2.getCoordinates(), 
	        		atm3.getCoordinates(), atm4.getCoordinates(), vec1, vec2, vec3);
	    }
	    
	    /*float[] r12 = difference(atm2.getCoordinates(),atm1.getCoordinates());
	    float[] r32 = difference(atm3.getCoordinates(),atm2.getCoordinates());
	    r32 = normalize(r32);
	    r12 = cross(r12,r32);
	    float[] r43 = difference(atm4.getCoordinates(),atm3.getCoordinates());
	    r43 = cross(r32,r43);
	    double r12dotr43 = (r12[0]*r43[0]) + (r12[1]*r43[1]) + (r12[2]*r43[2]);
	    r12 = cross(r43,r12);
	    double r32dotr12 = (r32[0]*r12[0]) + (r32[1]*r12[1]) + (r32[2]*r12[2]);
	    double theta = Math.atan2(-r32dotr12,r12dotr43);*/
	    
		double cosTheta = Math.cos(theta);
		double cosTheta2 = cosTheta * cosTheta;
		
		energy = 0.5 * ((v1 * (1 + cosTheta)) + (v2 * (2 - 2 * cosTheta2))
		          + (v3 * (1 + (cosTheta * ((4 * cosTheta2) - 3)))));
		
		if (gradient) {
	        double sinTheta = Math.sin(theta);        
	        dE = 0.5 * (-v1 * sinTheta + 4 * v2 * sinTheta * cosTheta + 3 * v3 * sinTheta * (1 - 4 * cosTheta2));
	        Atom[] atmList = {atm1,atm2,atm3, atm4};
	        addForces(atmList, dE);
		}
		/*try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("min.txt",true));
			writer.write("["+atm1.getAtomSeqNum()+","+atm2.getAtomSeqNum()+","+atm3.getAtomSeqNum()+","+atm4.getAtomSeqNum()+"]\t["+v1+","+v2+","+v3+"]\t["+theta+","+cosTheta+"]\t["+energy+"]\t["+dE + "]");
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.write("Torsion [theta,cosTheta][energy]");
			writer.newLine();
			writer.write("["+atm1.getAtomSeqNum()+","+atm2.getAtomSeqNum()+","+atm3.getAtomSeqNum()+","+atm4.getAtomSeqNum()+"]\t["+v1+","+v2+","+v3+"]\t["+theta+","+cosTheta+"]\t["+energy+"]");
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return energy;
	}

	private double calVDWI(boolean gradient, Atom atm1, Atom atm2){
		double energy = 0.0, rab=0.0, dE=0.0;
		double[] vec = new double[3];
		String key1 = ""+atm1.getmmType();
		String key2 = ""+atm2.getmmType();
		if(vdw.get(key1)==null || vdw.get(key2)==null)
			return 0.0;
		double[] values1 = vdw.get(key1);
		double alpha1 = values1[0];
		double N1 = values1[1];
		double A1 = values1[2];
		double G1 = values1[3];
		
		double[] values2 = vdw.get(key2);
		double alpha2 = values2[0];
		double N2 = values2[1];
		double A2 = values2[2];
		double G2 = values2[3];
		
		if (gradient) {
		      rab = restorativeForceAndDistance(atm1.getCoordinates(), atm2.getCoordinates(), vec);
		} else {
		      rab = Math.sqrt(distance(atm1.getCoordinates(), atm2.getCoordinates()));
		}
		if (isNearZero2(rab, 1.0e-3))
		   rab = 1.0e-3;
		
		double R_ii = A1*Math.pow(alpha1, 0.25);
		double R_jj = A2*Math.pow(alpha2, 0.25);
		double gamma = (R_ii-R_jj)/(R_ii+R_jj);
		double R_ij = 0.5*(R_ii + R_jj)*(1+(0.2*(1-Math.exp(-12*gamma*gamma))));
		double emma = ((181.16*G1*G2*alpha1*alpha2)/
				(Math.pow((alpha1/N1), 0.5)+Math.pow((alpha2/N2), 0.5))) *
				(1/Math.pow(R_ij, 6));
		double r_ij = rab / R_ij;
		
		double f1 = 1.07 /(r_ij+0.07);
		double f2 = 1.12/(0.12+ Math.pow(r_ij, 7));
		
		energy = emma * Math.pow(f1, 7)  * (f2 - 2);
		
		if (gradient) {
		      dE = -7 * emma * Math.pow(f1, 7) /R_ij 
		          * (f1 / 1.07 * (f2 - 2) + f2 * f2 * Math.pow(r_ij, 6));
		      Atom[] atmList = {atm1,atm2};
		      addForces(atmList, dE);
		}
		/*try {
			writer.write("vdw [N1,G1,A1,N2,G2,A2][R_ii,R_jj,R_ij,r_ij][gamma,emma,f1,f2][energy]");
			writer.newLine();
			writer.write("["+atm1.getAtomSeqNum()+","+atm2.getAtomSeqNum()+"]"
					+ "\t["+alpha1+","+N1+","+A1+","+G1+","+alpha2+","+N2+","+A2+","+G2+"]"
							+ "\t["+R_ii+","+R_jj+","+R_ij+","+r_ij+"]"
									+ "\t["+gamma+","+emma+","+f1+","+f2+"]\t["+energy+"]");
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return energy;
	}

	private double calElectroInter(boolean gradient, Atom atm1, Atom atm2){
		double energy = 0.0;
		double qi = atm1.getPartialCharge();
		double qj = atm2.getPartialCharge();
		double constant=0.0;
		constant = (checkThirdNeighbour(atm1,atm2) ?  249.0537 : 332.0716);
		
		double a = constant*qi*qj;
		double b = Math.sqrt(distance(atm1.getCoordinates(), atm2.getCoordinates())+ 0.05);
		
		energy = a / b;
		/*try {
			writer.write("Electrostatic Interaction [qi,qj][a,b][energy]");
			writer.newLine();
			writer.write("["+atm1.getAtomSeqNum()+","+atm2.getAtomSeqNum()+"]\t["+qi+","+qj+","+constant+"]\t["+a+","+b+"]\t["+energy+"]");
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return energy;
	}

	private double restorativeForceAndDistance(float[] coord1, float[] coord2, double[] vec) {
		sub(coord1, coord2,vec);
	    double rab = length(vec);
	    if (rab < 0.1) {// atoms are too close to each other
	      randomizeUnitVector(vec);
	      rab = 0.1;
	    }
	    vec = normalize(vec);
	    setCoord(coord1,vec);
	    scale(coord1,-1); // -drab/da
	    setCoord(coord2,vec); // -drab/db
	
	    return rab;
	}

	private void randomizeUnitVector(double[] vec) {
		Random ptr = new Random();
	
	    double l;
	    do {
	    	vec[0] = ptr.nextDouble() - 0.5;
	    	vec[1] = ptr.nextDouble() - 0.5;
	    	vec[2] = ptr.nextDouble() - 0.5;
	      l = vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2];
	    } while ((l > 1.0) || (l < 1e-4));
	    vec=normalize(vec);
	}

	private double restorativeForceAndAngleRadians(float[] coord1,	float[] coord2, float[] coord3) {
		sub(coord1, coord2);
	    sub(coord3, coord2);
	
	    double length1 = length(coord1);
	    double length3 = length(coord3);
	
	    if (isNearZero(length1) || isNearZero(length3)) {
	        coord1[0]=coord1[1]=coord1[2] = 0;
	        coord2[0]=coord2[1]=coord2[2] = 0;
	        coord3[0]=coord3[1]=coord3[2] = 0;
	        return 0.0;
	    }
	    
	    // Calculate normalized bond vectors
	    double inverseLenv1 = 1.0 / length1;
	    double inverseLenv3 = 1.0 / length3;
	    
	    scale(coord1, inverseLenv1);
	    scale(coord3, inverseLenv3);
	
	    // Calculate the cross product of coord1 and coord2
	    coord2= cross(coord1, coord3);
	    
	    double length2 = length(coord2);
	    if (isNearZero(length2)) {
	    	coord1[0]=coord1[1]=coord1[2] = 0;
	        coord2[0]=coord2[1]=coord2[2] = 0;
	        coord3[0]=coord3[1]=coord3[2] = 0;
	        return 0.0;
	    }
	    scale(coord2, 1 / length2);
	
	    // Calculate the Math.cos of theta and then theta
	    double costheta = coord1[0] * coord3[0] + coord1[1] * coord3[1] + coord1[2] * coord3[2];
	    double theta;
	    if (costheta > 1.0) {
	      theta = 0.0;
	      costheta = 1.0;
	    } else if (costheta < -1.0) {
	      theta = Math.PI;
	      costheta = -1.0;
	    } else {
	      theta = Math.acos(costheta);
	    }
	    
	    coord1= cross(coord1, coord2);
	    coord1= normalize(coord1);
	    coord2= cross(coord3, coord2);
	    coord2= normalize(coord2);
	
	    scale(coord1, -inverseLenv1);
	    scale(coord2, inverseLenv3);
	
	    setCoord(coord3, coord2);
	    coord2[0] += coord1[0];
	    coord2[1] += coord1[1];
	    coord2[2] += coord1[2];
	    scale(coord2, -1);
	
		return theta;
	}

	private double angleRadiansABC(float[] coord1, float[] coord2, float[] coord3) {
		double atm12 = distance(coord1,coord2);
	    double atm23 = distance(coord2,coord3);
	    double atm13 = distance(coord1,coord3);
	    return (isNearZero2(atm12, 1e-3) || isNearZero2(atm23, 1e-3) ? 0 :
	    	Math.acos((atm12 + atm23 - atm13 ) / 2 / Math.sqrt(atm12 * atm23)));
	}

	private double vectorAngleRadians(float[] atm1, float[] atm2) {
		double l1 = length(atm1);
		double l2 = length(atm2);
		return (isNearZero(l1) || isNearZero(l2) ? 
				0 : Math.acos((atm1[0] * atm2[0] + atm1[1] * atm2[1] + atm1[2] * atm2[2]) / (l1 * l2)));
	}

	private double pointPlaneAngleRadians(float[] coord1, float[] coord2, float[] coord3, float[] coord4,
			float[] vec1, float[] vec2, float[] vec3, boolean b) {
		sub(coord2, coord3, vec1);
	    sub(coord2, coord4, vec2);
	    vec3 = cross(vec1, vec2);    
	    add(vec2, vec1);
	    sub(coord2, coord1, vec1);
	    
	    double angleA_CD = (b ? vectorAngleRadians(vec2, vec1) : Math.PI); 
	
	    double angleNorm = vectorAngleRadians(vec3, vec1);
	    
	    if (angleNorm > Math.PI / 2)
	      angleNorm = Math.PI - angleNorm;
	    double val = Math.PI / 2.0 + (angleA_CD > Math.PI / 2.0 ? 
	        -angleNorm 
	        : angleNorm) ;    
	    return val;
	}

	private double restorativeForceAndOutOfPlaneAngleRadians(float[] coord1, float[] coord2, float[] coord3, float[] coord4, 
			float[] vec1, float[] vec2, float[] vec3) {
		 sub(coord1, coord2);
		 sub(coord3, coord2);
		 sub(coord4,coord2);
		    
		 double len_ji = length(coord1);
		 double len_jk = length(coord3);
		 double len_jl = length(coord4);
		 if (isNearZero(len_ji) || isNearZero(len_jk) || isNearZero(len_jl)) {
			 coord1[0]=coord1[1]=coord1[2] = 0;
		     coord2[0]=coord2[1]=coord2[2] = 0;
		     coord3[0]=coord3[1]=coord3[2] = 0;
		     coord4[0]=coord4[1]=coord4[2] = 0;
			 return 0.0;
		 }
		 coord1 = normalize(coord1);
		 coord3 = normalize(coord3);
		 coord4 = normalize(coord4);
	
	    double cos_theta = (coord1[0] * coord3[0] + coord1[1] * coord3[1] + coord1[2] * coord3[2]);
	    double     theta = Math.acos(cos_theta);
	
	    // If theta equals 180 degree or 0 degree
	    if (isNearZero(theta) || isNearZero(Math.abs(theta - Math.PI))) {
	    	coord1[0]=coord1[1]=coord1[2] = 0;
		    coord2[0]=coord2[1]=coord2[2] = 0;
		    coord3[0]=coord3[1]=coord3[2] = 0;
		    coord4[0]=coord4[1]=coord4[2] = 0;
	      return 0.0;
	    }
	    double csc_theta = 1 / Math.sin(theta);
	
	    vec1 = cross(coord1, coord3);
	    vec2 = cross(coord3, coord4);
	    vec3 = cross(coord4, coord1);
	
	    // the angle from l to the i-j-k plane (Wilson angle):
	    
	    double sin_dl =  (vec1[0] * coord4[0] + vec1[1] * coord4[1] + vec1[2] * coord4[2]) * csc_theta;
	    double     dl = Math.asin(sin_dl);
	    double cos_dl = Math.cos(dl);
	
	    if (cos_dl < 0.0001 || isNearZero(dl) || isNearZero(Math.abs(dl - Math.PI))) {
	    	coord1[0]=coord1[1]=coord1[2] = 0;
		    coord2[0]=coord2[1]=coord2[2] = 0;
		    coord3[0]=coord3[1]=coord3[2] = 0;
		    coord4[0]=coord4[1]=coord4[2] = 0;
	      return dl;
	    }
	        
	    coord4 = scaleAdd(-sin_dl / csc_theta, coord4, vec1);
	    scale(coord4, csc_theta / len_jl);
	
	    setCoord(coord2, coord1);
	    
	    coord1 = scaleAdd(-cos_theta, coord3, coord1);
	    coord1 = scaleAdd(-sin_dl * csc_theta, coord1, vec2);
	    scale(coord1,csc_theta / len_ji);
	
	    coord3 = scaleAdd(-cos_theta, coord2, coord3);
	    coord3 = scaleAdd(-sin_dl * csc_theta, coord3, vec3);
	    scale(coord3, csc_theta / len_jk);
	
	    setCoord(coord2, coord1);
	    add(coord2, coord3);
	    add(coord2, coord4);
	    scale(coord2, -1);
	
	    return dl;
	}

	private double restorativeForceAndTorsionAngleRadians(float[] atm1, float[] atm2,	float[] atm3, float[] atm4) {
		sub(atm1, atm2, atm1);
	    sub(atm2, atm3, atm2);
	    sub(atm3, atm4, atm3);
	
	    double len_ij = length(atm1);
	    double len_jk = length(atm2);
	    double len_kl = length(atm3);
	    if (isNearZero(len_ij) || isNearZero(len_jk) || isNearZero(len_kl)) {
	    	atm1[0]=atm1[1]=atm1[2] = 0;
	    	atm2[0]=atm2[1]=atm2[2] = 0;
	    	atm3[0]=atm3[1]=atm3[2] = 0;
	    	atm4[0]=atm4[1]=atm4[2] = 0;
	        return 0.0;
	    }
	    
	    double ang = vectorAngleRadians(atm1, atm2);
	    double sin_j = Math.sin(ang);
	    double cos_j = Math.cos(ang);
	    
	    ang = vectorAngleRadians(atm2, atm3);
	    double sin_k = Math.sin(ang);
	    double cos_k = Math.cos(ang);
	
	    // normalize the bond vectors:
	    atm1 = normalize(atm1);
	    atm2 = normalize(atm2);
	    atm3 = normalize(atm3);
	
	    // use i, k, and l for temporary variables as well
	    atm1 = cross(atm1, atm2);  //a
	    atm4 = cross(atm2, atm3);  //b
	    atm3 = cross(atm1, atm4);  //c
	
	    double theta = -Math.atan2((atm3[0] * atm2[0] + atm3[1] * atm2[1] + atm3[2] * atm2[2]),
	    		(atm1[0] * atm4[0] + atm1[1] * atm4[1] + atm1[2] * atm4[2]));
	        
	    scale(atm1, (1. / len_ij / sin_j / sin_j));
	
	    scale(atm4, (-1. / len_kl / sin_k / sin_k));
	
	    setCoord(atm2, atm1);
	    scale(atm2, (-len_ij / len_jk * cos_j - 1.));
	    setCoord(atm3, atm4);
	    scale(atm3,(-len_kl / len_jk * cos_k));
	    sub(atm2, atm3);
	    
	    setCoord(atm3, atm1);
	    add(atm3, atm2);
	    add(atm3, atm4);
	    scale(atm3,-1);
	
	    return theta;
	}

	private double getTorsionAngleRadians(float[] coord1, float[] coord2, float[] coord3, float[] coord4,
			double[] vec1, double[] vec2, double[] vec3) {
		sub(coord2, coord1, vec1); 
	    sub(coord3, coord2, vec2);
	    vec2 = normalize(vec2);
	    coord1 = cross(coord1, coord2); //p1
	    sub(coord4, coord3, vec3); 
	    coord3 = cross(coord2, coord3); //p2
	    double p1dotp2 = (coord1[0] * coord3[0] + coord1[1] * coord3[1] + coord1[2] * coord3[2]);
	    vec1 = cross(vec3, vec1);
	    double theta = Math.atan2((-vec2[0] * vec1[0] + (-vec2[1]) * vec1[1] + (-vec2[2]) * vec1[2]), p1dotp2);   
	    return theta;
	}

	private boolean checkThirdNeighbour(Atom a, Atom b) {
		Atom[] atmNeigh1;
		for(int i=0;i<a.getNeighbourAtoms().length;i++){
			// get all the neighbouring atoms from current neigh atoms
			atmNeigh1 = a.getNeighbourAtoms()[i].getNeighbourAtoms();
			for(int j=0;j<atmNeigh1.length;j++){
				for(int k=0;k<b.getNeighbourAtoms().length;k++){
					if(atmNeigh1[j]==b.getNeighbourAtoms()[k])
						return true;
				}
			}
		}		
		return false;
	}

	private double distance(float[] a, float[] b) {
		double dx = a[0] - b[0];
		double dy = a[1] - b[1];
		double dz = a[2] - b[2];
		return (dx * dx + dy * dy + dz * dz);
	}
	
	private void scale(float[] coord, double value) {
		coord[0] *= value;
		coord[1] *= value;
		coord[2] *= value;
	}

	private float[] scaleAdd(double s, float[] coord, float[] vec) {
		float[] temp = {
	    (float) (s * coord[0] + vec[0]),
	    (float) (s * coord[1] + vec[1]),
	    (float) (s * coord[2] + vec[2])};
	    return temp;
	}

	private void setCoord(float[] coord, float[] coord2) {
		coord[0] = coord2[0];
		coord[1] = coord2[1];
		coord[2] = coord2[2];
	}

	private void setCoord(float[] coord, double[] coord2) {
		coord[0] = (float)coord2[0];
		coord[1] = (float)coord2[1];
		coord[2] = (float)coord2[2];
	}

	private double length(float[] coord1) {
		return Math.sqrt(coord1[0]*coord1[0]+coord1[1]*coord1[1]+coord1[2]*coord1[2]);
	}

	private double length(double[] coord1) {
		return Math.sqrt(coord1[0]*coord1[0]+coord1[1]*coord1[1]+coord1[2]*coord1[2]);
	}

	private double[] cross(double[] a, double[] b) {
		double[] temp = {(a[1]*b[2])-(a[2]*b[1]),
				(a[2]*b[0])-(a[0]*b[2]),
				(a[0]*b[1])-(a[1]*b[0])};
		return temp;
	}

	private float[] cross(float[] a, float[] b) {
		float[] temp = {(a[1]*b[2])-(a[2]*b[1]),
						(a[2]*b[0])-(a[0]*b[2]),
						(a[0]*b[1])-(a[1]*b[0])};
		return temp;
	}

	private float[] normalize(float[] a) {
		double d = Math.sqrt((a[0]*a[0])+(a[1]*a[1])+(a[2]*a[2]));
		
		a[0] /=d;
		a[1] /=d;
		a[2] /=d;
		return a;
	}

	private double[] normalize(double[] a) {
			double d = Math.sqrt((a[0]*a[0])+(a[1]*a[1])+(a[2]*a[2]));
		
		a[0] /=d;
		a[1] /=d;
		a[2] /=d;
		return a;
	}

	private void add(float[] atm1, float[] atm2) {
		atm1[0] += atm2[0];
		atm1[1] += atm2[1];
		atm1[2] += atm2[2];
	}

	private void sub(float[] coord1, float[] coord2) {
		coord1[0] -= coord2[0];
		coord1[1] -= coord2[1];
		coord1[2] -= coord2[2];
	}

	private void sub(float[] coord1, float[] coord2, double[] vec1) {
		vec1[0] = coord1[0] - coord2[0];
		vec1[1] = coord1[1] - coord2[1];
		vec1[2] = coord1[2] - coord2[2];
	}

	private void sub(float[] atm1, float[] atm2, float[] atm12) {
		atm1[0] = atm2[0] - atm12[0];
		atm1[1] = atm2[1] - atm12[1];
		atm1[2] = atm2[2] - atm12[2];
	}

	private boolean isFinite(double a) {
		return !Double.isInfinite(a) && !Double.isNaN(a);
	}

	private boolean isNearZero(double a) {
		return (Math.abs(a) < 2e-6);
	}

	private boolean isNearZero2(double a, double ep) {
		return (Math.abs(a) < ep);
	}

	private void readMMFF94Types(String fileLocation) {
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
		double charge=0.0;
		readPartialCharge("res/res/MMFF94-formalCharge.txt");
		HashMap<String, Atom> atomHash = model.getAtomHash();
		for(Atom atm : atomHash.values()){
			if (atm==null)
				continue;
			String atmMMType = AtomToMMType.setMMType(atm);
			if(atmMMType==null){
				System.out.println("Atom: " + atm.getAtomSeqNum()+ " has no mmType");
				atm.setmmType(0);
				atm.setPartialCharge(0.0);
				continue;
			}
			int type = mmffType.get(atmMMType);
			if(partialCharge.get(atmMMType)==null)
				charge=0.0;
			else
				charge = partialCharge.get(atmMMType);
			atm.setmmType(type);
			atm.setPartialCharge(charge);
			
			//System.out.println("ATOM: [" + atm.getAtomSeqNum() + "]\t Type: "+ atm.getmmType());
		}
		assignPartialCharge();
	}
	
	private void assignPartialCharge() {
		String key,keya,keyb;
		double pa,pb,bci,charge,bFactor;
		double[] partialChargeVal = new double[model.getAtomHash().size()];
		for(Bond bond : model.getBonds()){
			if(bond.getAtom1().getmmType()<bond.getAtom2().getmmType()){
				key = ""+bond.getAtom1().getmmType()+bond.getAtom2().getmmType();
				bFactor = -1;
			}
			else{
				key = ""+bond.getAtom2().getmmType()+bond.getAtom1().getmmType();
				bFactor = 1;
			}
			
			if(bondCharge.get(key)==null){
				pa = bond.getAtom1().getPartialCharge();
				pb = bond.getAtom2().getPartialCharge();
				bci = pa-pb;
			}
			else{
				bci = bFactor * bondCharge.get(key);
			}
			keya = ""+bond.getAtom1().getmmType();
			keyb = ""+bond.getAtom2().getmmType();
			charge = (electroInter.get(keyb)[1]* bond.getAtom2().getPartialCharge()) - 
					(electroInter.get(keya)[1] * bond.getAtom1().getPartialCharge()) + bci;
			pa = partialChargeVal[bond.getAtom1().getAtomSeqNum()-1] + charge;
			pb = partialChargeVal[bond.getAtom2().getAtomSeqNum()-1] - charge;
			partialChargeVal[bond.getAtom1().getAtomSeqNum()-1] = pa;
			partialChargeVal[bond.getAtom2().getAtomSeqNum()-1] = pb;
		}
		double finalCharge=0.0;
		for(Atom atm : model.getAtomHash().values()){
			finalCharge = atm.getPartialCharge() + partialChargeVal[atm.getAtomSeqNum()-1];
			atm.setPartialCharge(finalCharge);
			//System.out.printf("[%d,%f]\n", atm.getAtomSeqNum(), atm.getPartialCharge());
		}
	}

	private void readBondCharge(String file) {
		String line;
		bondCharge = new HashMap<String,Double>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				if(line.startsWith("14.")|| line.startsWith("*"))
					continue;
				try{
					String[] splitLine = line.split("\\s+");
					String key = splitLine[1] + splitLine[2];
					double value = Double.parseDouble(splitLine[3]);
					bondCharge.put(key,value);
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

	private void readPartialCharge(String file){
		String line;
		partialCharge = new HashMap<String,Double>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				if(line.startsWith("#"))
					continue;
				try{
					String[] splitLine = line.split("\\s+");
					String key = splitLine[0];
					double value = Double.parseDouble(splitLine[2])/12;
					partialCharge.put(key,value);
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
					String key = splitLine[1]+splitLine[2];
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
					String[] splitLine = line.split("\\s+"); // [1]-[3] type [4] ka [5] t0
					String key = splitLine[1]+splitLine[2]+splitLine[3];
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
					String key = splitLine[0]+splitLine[1]+splitLine[2]+splitLine[3];
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
					double[] value = {Double.parseDouble(splitLine[2]),Double.parseDouble(splitLine[3]),
										Double.parseDouble(splitLine[4]),Double.parseDouble(splitLine[5])};
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
