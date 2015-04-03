package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.MoleculeEditor;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.*;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.ui.MainWindow;

/**
 * @author waiyan
 * Main class of the physics engine
 */
public class World {
	
	
	public static double COEFFICENT_OF_RESTITUTION = 1;
	public static int particleCount = 0;
	public static boolean simulationLvlAtomic = false;
	public static boolean simulationLvlPartial = true;
	public static boolean electricForceActive = true;
	public static boolean LJForceActive = true;
	public static String simulationStatus = "running";
	public static CountDownLatch countDownLatch;
	public static boolean allParticlesActive = true;
	public static boolean debugRotate = false;
	public static int frameTime_as = 50;
	
	public static double distance_metric = DISTANCE.m.value();
	public static double time_metric = TIME.as.value();
	public static double mass_metric = MASS.kg.value();
	
	public static ArrayList<AbstractParticle> activeParticles = new ArrayList<>();
	public static ArrayList<AbstractParticle> allAtoms = new ArrayList<>();
	public static ArrayList<AbstractParticle> allMolecules = new ArrayList<>();
	public static ArrayList<AbstractParticle[]> potentialContacts = new ArrayList<>();
	public static ForceRegistry registry = new ForceRegistry();
	public static OctTree octTree = new OctTree();
	public static NarrowCollisionDetector detector = new NarrowCollisionDetector();
	public static ContactResolver resolver = new ContactResolver();

	public static ArrayList<String> inputFilePaths = new ArrayList<>();
	public static MoleculeEditor editor;
	public static MainWindow window;
	public static PrintStream originalStream;
	public static PrintStream dummyStream;
	
	public static boolean displayUI = true;
	
	public static long startTime;
	public static long endTime;
	public static long duration;
	
	// Only Needed for restart
	public static HashMap <Integer,Vector3D> initialPositions;
	
	public static void main (String[] args) throws Exception{
		
		originalStream = System.out;
		dummyStream    = new PrintStream (new OutputStream(){
		    @Override
			public void write(int b) {
		        //NO-OP
		    }
		});
		
		inputFilePaths.add("res/physics/input.pdb");
		for (String filePath: inputFilePaths){
			parsePDB(filePath);
		}
	/*	
		AbstractParticle a1 = new Atom("H");
		AbstractParticle a2 = new Atom("H");
		AbstractParticle a3 = new Atom("N");
		AbstractParticle a4 = new Atom ("C");
		AbstractParticle a5 = new Atom ("H");
		AbstractParticle a6 = new Atom ("C");
		AbstractParticle a7 = new Atom ("O");
		AbstractParticle a8 = new Atom ("O");
		AbstractParticle a9 = new Atom ("H");
		a3.setPosition(0, 0, 0);
		a3.setNetCharge(-3);		// find a way to get oxidation state/ net charge	
		a2.setPosition(-2.8e-10, 2.8e-10, 0);
		a2.setNetCharge(1);
		a1.setPosition(-2.8e-10, -2.8e-10, 0);
		a1.setNetCharge(1);
		a4.setPosition(4e-10, 0, 0);
		a4.setNetCharge(4);
		a5.setPosition(4e-10, 3e-10, 0);
		a5.setNetCharge(-1);
		a6.setPosition(7.5e-10, 0, 0);
		a6.setNetCharge(4);
		a7.setPosition(10e-10, 3e-10, 0);
		a7.setNetCharge(-2);
		a8.setPosition(10e-10, -3e-10, 0);
		a8.setNetCharge(-2);
		a9.setPosition(10e-10, 6e-10, 0);
		a9.setNetCharge(1);
		
		AbstractParticle a10 = new Atom ("Cl");
		a10.setPosition(2e-10, -9e-10, 0);
		a10.setNetCharge(-1);	
		
		AbstractParticle a11 = new Atom ("Na");
		a11.setPosition(6.5e-10, -9e-10, 0);
		a11.setNetCharge(1);	
		
		allAtoms.add(a1);
		allAtoms.add(a2);
		allAtoms.add(a3);
		allAtoms.add(a4);
		allAtoms.add(a5);
		allAtoms.add(a6);
		allAtoms.add(a7);
		allAtoms.add(a8);
		allAtoms.add(a9);
		allAtoms.add(a10);
		allAtoms.add(a11);
		
		
		ArrayList<Atom> atomList = new ArrayList<>();
		atomList.add((Atom)a1);
		atomList.add((Atom)a2);
		atomList.add((Atom)a3);
		atomList.add((Atom)a4);
		atomList.add((Atom)a5);
		atomList.add((Atom)a6);
		atomList.add((Atom)a7);
		atomList.add((Atom)a8);
		atomList.add((Atom)a9);
		Molecule molecule = new Molecule(atomList);
		allMolecules.add(molecule);
		
		ArrayList <Atom> atomList2 = new ArrayList<>();
		atomList2.add((Atom)a10);
//		atomList2.add((Atom)a11);
		Molecule molecule2 = new Molecule(atomList2);
		allMolecules.add(molecule2);
		
		atomList2 = new ArrayList<>();
//		atomList2.add((Atom)a10);
		atomList2.add((Atom)a11);
		Molecule molecule3 = new Molecule(atomList2);
		allMolecules.add(molecule3);
		
			
		ArrayList<AbstractParticle> rotateTestAtoms = new ArrayList<>();
		if(debugRotate) {
			rotateTestAtoms.add(a6);
			rotateTestAtoms.add(a7);
			rotateTestAtoms.add(a8);
			rotateTestAtoms.add(a9);
		}	
*/
		// check simulation level
		checkSimulationLevel();
		
		// checks which particles will be involved in calculations
		checkForActiveParticles();
		
		if(displayUI){
			 System.setOut(dummyStream);
			 window = new MainWindow();
			 window.getMediator().displayParticles(octTree.getAllParticles());
			 System.setOut(originalStream);
			 initialPositions = new HashMap<>();
			 for (AbstractParticle particle: allAtoms){
				 Vector3D temp = particle.getPosition();
				 initialPositions.put(particle.getGUID(), new Vector3D(temp.x, temp.y, temp.z));
			 }
			 for (AbstractParticle particle: allMolecules){
				 Vector3D temp = particle.getPosition();
				 initialPositions.put(particle.getGUID(), new Vector3D(temp.x, temp.y, temp.z));
			 }
		}		
		
		System.out.println("Time \t a1\t\t\t \t  a2\t\t\t");
		int i = 0;
		
		while(true) {
	
			startTime = System.nanoTime();
			
			// Applying Forces		
			registry.updateAllForces(activeParticles);
			
			endTime = System.nanoTime();
			duration = (endTime - startTime);  
			System.out.println("Time taken to calculate forces: " + duration);
			startTime = System.nanoTime();
			
			System.out.println("\n" + i);
			for(AbstractParticle particle: activeParticles){
				if(simulationLvlAtomic)
					particle.integrate(frameTime_as*time_metric);	
				else {
					if(!simulationLvlPartial)
						particle.integrate(i*frameTime_as*time_metric);
					else
						particle.integrate(frameTime_as*time_metric);
				}
			//	printParticleStatus(particle);
			}
			
			endTime = System.nanoTime();
			duration = (endTime - startTime);  
			System.out.println("Time taken to integrate: " + duration);
			startTime = System.nanoTime();

			// Update tree only after integration
			octTree.updateAllActiveParticles();
			
			endTime = System.nanoTime();
			duration = (endTime - startTime);  
			System.out.println("Time taken for broad phase: " + duration);
			startTime = System.nanoTime();
			
			// Collision Detection 
			detector.detectCollision(octTree, activeParticles, potentialContacts);    	 
			
			endTime = System.nanoTime();
			duration = (endTime - startTime);  
			System.out.println("Time taken for narrow phase: " + duration);
			startTime = System.nanoTime();
			
			// Resolve Collisions and set active particles
			resolver.resolveContacts(potentialContacts);	
			
			endTime = System.nanoTime();
			duration = (endTime - startTime);  
			System.out.println("Time taken to resolve: " + duration);
			startTime = System.nanoTime();
			
			if(displayUI){
			//	AbstractParticle [] temp_particles = activeParticles.toArray(new AbstractParticle[activeParticles.size()]);
			//	editor.getMediator().notifyUpdated(temp_particles);
				if(i%100 == 0){
					System.setOut(dummyStream);
					// Test Rotation
				//	if(debugRotate)
				//		testRotate(rotateTestAtoms);
					
					window.getMediator().displayParticles(octTree.getAllParticles());
					System.setOut(originalStream);
				}
				
				if(simulationStatus.equals("restart")){
					checkSimulationLevel();
					restartSimulation();
					simulationStatus = "running";
					i=0;
				}
				
				else if(simulationStatus.equals("changed")){
					
					checkSimulationLevel();
					restartSimulation();
					simulationStatus = "running";
					//i=0;
				}
				
				else if(simulationStatus.equals("paused")){
					i--;
					countDownLatch = new CountDownLatch(1);
					countDownLatch.await(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				}
			}
			
			i++;
			if(i > 10000 && simulationLvlAtomic || i>20000 && !simulationLvlAtomic) {
				simulationStatus = "restart";
			//		break;
			}
		}
//		endTime = System.nanoTime();
//		duration = (endTime - startTime);  
//		System.out.println("Time taken to end: " + duration);
		
	}
	
	public static void markAsActive(AbstractParticle particle){
		
		for (int index =0; index< World.activeParticles.size(); index++){
			if(World.activeParticles.get(index).getGUID() == particle.getGUID()) {
				return;
			}
		}

		World.activeParticles.add(particle);
		Vector3D temp = new Vector3D();
		temp.x = particle.getPosition().x;
		temp.y = particle.getPosition().y;
		temp.z = particle.getPosition().z;
	}
	
	public static void checkForActiveParticles(){
		
		World.activeParticles.clear();
		ArrayList <AbstractParticle> particles = new ArrayList<>();
		octTree.getAllParticles(particles);
		
		for(AbstractParticle particle: particles)
			markAsActive(particle);
	}
	
	public static void resetActiveParticlesVelocities(){
		for(AbstractParticle particle: activeParticles)
			particle.setVelocity(0, 0, 0);
	}
	
	private static void printParticleStatus(AbstractParticle p){
		System.out.print("Particle " + p.getGUID() + "\t" +p.getPosition().print()+"\n");
		//System.out.print("Particle " + p.getGUID() + "\t" + p.getVelocity().print()+"\t");
	}
	
	private static void restartSimulation(){
		for (AbstractParticle particle: octTree.getAllParticles()) {
			Vector3D pos = initialPositions.get(particle.getGUID());
			if (particle instanceof Atom)
				particle.setPosition(pos.x, pos.y, pos.z);
			else if (particle instanceof Molecule)
				((Molecule) particle).setPositionAsCentroid();
			particle.setVelocity(0, 0, 0);
		}
		
		checkForActiveParticles();
	}
	
	private static void checkSimulationLevel(){
		octTree.clear();
		if(simulationLvlAtomic) {
			for(AbstractParticle particle: allAtoms) {
				octTree.insert(particle);
			}
		}
		else {
			for(AbstractParticle particle: allMolecules) {
				octTree.insert(particle);
		//		octTree.remove(allMolecules.get(0));
			}
			if(simulationLvlPartial){
				octTree.clear();
				for(AbstractParticle particle: ((Molecule)allMolecules.get(0)).getAtoms()) {
					octTree.insert(particle);
				}
				
				for(int i=1; i<allMolecules.size(); i++) {
					octTree.insert(allMolecules.get(i));
				}
			}
		}
	}
	
	public static void setCommand(String command) throws Exception{
		
	}
	
	private static void testRotate (ArrayList<AbstractParticle> particles){
		Vector3D axis = new Vector3D(1,0,0);
		for(AbstractParticle particle: particles) {
			particle.rotateAroundAxis(axis, 1);
		//	markAsActive(particle);
		}
	}
	
	private static void parsePDB (String filePath){
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();	// MODEL
			while(line!=null){
				ArrayList <Atom> atoms = new ArrayList<>();
				line = br.readLine();		// HETATM
				while(!line.equals("ENDMDL")){
					System.out.println("Parsing " + line);
					double x = Double.parseDouble(line.substring(30, 38)) * 1e-10;
					double y = Double.parseDouble(line.substring(38, 46)) * 1e-10;
					double z = Double.parseDouble(line.substring(46, 54)) * 1e-10;
					int charge = Integer.parseInt(line.substring(78));
					AbstractParticle particle = new Atom(line.substring(12, 16).replace(" ", ""));
					particle.setPosition(x, y, z);
					particle.setNetCharge(charge);
					allAtoms.add(particle);
					atoms.add((Atom)particle);
					line = br.readLine();
				}
				Molecule molecule = new Molecule(atoms);
				allMolecules.add(molecule);
				line = br.readLine();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}