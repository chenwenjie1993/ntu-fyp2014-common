package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.biojava.bio.structure.Structure;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.DataManager;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.MoleculeEditor;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.*;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.ui.MainWindow;

/**
 * @author waiyan
 * Main class of the physics engine
 */
public class World {
	
	
	public static double COEFFICENT_OF_RESTITUTION = 1;
	public static int particleCount = 0;
	public static boolean simulationLvlAtomic = true;
	public static boolean simulationLvlPartial = false;
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
	public static String outputFilePath = "";
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
		
		outputFilePath = "res/physics/output.txt";
		inputFilePaths.add("res/physics/input.pdb");
		
		//originalStream = System.out;
		originalStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFilePath)));
		dummyStream    = new PrintStream (new OutputStream(){
		    @Override
			public void write(int b) {
		        //NO-OP
		    }
		});
		
		startTime = System.nanoTime();
		for (String filePath: inputFilePaths){
			parsePDB(filePath);
		}
		
		endTime = System.nanoTime();
		duration = (endTime - startTime);  
		System.out.println("Time taken to load particles: " + duration);
		startTime = System.nanoTime();
	
			
		ArrayList<AbstractParticle> rotateTestAtoms = new ArrayList<>();
		if(debugRotate) {
			rotateTestAtoms.add(allAtoms.get(5));
			rotateTestAtoms.add(allAtoms.get(6));
			rotateTestAtoms.add(allAtoms.get(7));
			rotateTestAtoms.add(allAtoms.get(8));
		}	

		// check simulation level
		checkSimulationLevel();
		
		// checks which particles will be involved in calculations
		checkForActiveParticles();
		
		if(displayUI){
			// System.setOut(dummyStream);
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
	
			System.out.println("\n" + i);
			startTime = System.nanoTime();
			
			// Applying Forces		
			registry.updateAllForces(activeParticles);
			
			endTime = System.nanoTime();
			duration = (endTime - startTime);  
			System.out.println("Time taken to calculate forces: " + duration);
			startTime = System.nanoTime();
			
	
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
				//	System.setOut(dummyStream);
					// Test Rotation
					if(debugRotate && (simulationLvlAtomic || !simulationLvlAtomic && simulationLvlPartial))
						testRotate(rotateTestAtoms);
					
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
				((sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Molecule) particle).setPositionAsCentroid();
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
			}
			if(simulationLvlPartial){
				octTree.clear();
				for(AbstractParticle particle: ((sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Molecule)allMolecules.get(0)).getAtoms()) {
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
			/*
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();	// MODEL
			while(line!=null){
				ArrayList <Atom> atoms = new ArrayList<>();
				line = br.readLine();		// HETATM
				while(!line.equals("ENDMDL") && !line.equals("END")){
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
			
			
			*/
			
			File file = new File(filePath);
			Structure struc = DataManager.readFile(file.getAbsolutePath());
			UpdateRegistry reg = new UpdateRegistry();
			reg.createUserModel(struc);
			
			List<Model> models = reg.getModelList();
			for (Model model : models) {
				//upgrade molecules
				ArrayList<sg.edu.ntu.aalhossary.fyp2014.common.Molecule> molecules = model.getMolecules();
				ArrayList<sg.edu.ntu.aalhossary.fyp2014.common.Molecule> newMolecules= new ArrayList<sg.edu.ntu.aalhossary.fyp2014.common.Molecule>(); 
				for (sg.edu.ntu.aalhossary.fyp2014.common.Molecule molecule : molecules) {
					Molecule newMolecule = new Molecule(molecule);
					newMolecules.add(newMolecule);
					for(Atom atom: newMolecule.getAtoms()) {
						allAtoms.add(atom);
					}
				}
				model.setMolecules(newMolecules);
				allMolecules.addAll(newMolecules);
			}
			
	
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}