package sg.edu.ntu.aalhossary.fyp2014.physics_engine.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.MoleculeEditor;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.model.*;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.view.View;

public abstract class AbstractController implements Controller{
	enum SimulationLevel {Atomic, Molecular, Partial};
	public static double COEFFICENT_OF_RESTITUTION = 1;
	
	@Option(name = "-l", aliases = "-level", usage = "Simulation Level")
	public static SimulationLevel simlvl = SimulationLevel.Atomic;
	
	public static boolean simulationLvlAtomic = true;
	public static boolean simulationLvlPartial = false;
	
	public static boolean electricForceActive = true;
	public static boolean LJForceActive = true;
	
	public static int frameTime_as = 1000;
	public static boolean debugRotate = false;
	
	@Argument(usage = "inputs of the Engine", metaVar="INPUTS")
	public static ArrayList<String> inputFilePaths = new ArrayList<>();
	
//	@Option(name="-o", aliases = "-output", usage="output of the engine", metaVar="OUTPUT")
	
	
//	@Option(name="-gui",usage="enable GUI")
//	public static boolean displayUI = true;
//	
//	public static int particleCount = 0;
	public static String simulationStatus = "running";
	public static CountDownLatch countDownLatch;
	public static boolean allParticlesActive = true;
	
	
	public static MoleculeEditor editor;
	public static View window;
//	public static PrintStream outputStream;
//	public static PrintStream defaultStream;
//	public static PrintStream dummyStream;
	public static long startTime;
	public static long endTime;
	public static long duration;
	
	
	public void start() {
		
		boolean displayUI = true;
		
		if(inputFilePaths.isEmpty())
			 inputFilePaths.add("lysine.pdb");
		
		startTime = System.nanoTime();
		for (String filePath: inputFilePaths){
			parsePDB(filePath);
		}
		
		endTime = System.nanoTime();
		duration = (endTime - startTime);  
		System.out.println("Time taken to load particles: " + duration/1000);
		startTime = System.nanoTime();
	
			
		ArrayList<AbstractParticle> rotateTestAtoms = new ArrayList<>();
//		if(debugRotate) {
//			rotateTestAtoms.add(allAtoms.get(5));
//			rotateTestAtoms.add(allAtoms.get(6));
//			rotateTestAtoms.add(allAtoms.get(7));
//			rotateTestAtoms.add(allAtoms.get(8));
//		}	

		// check simulation level
		checkSimulationLevel();
		
		// checks which particles will be involved in calculations
		checkForActiveParticles();
				
		if(displayUI){
//			 System.setOut(dummyStream);
			 window = new View();
			 window.getMediator().displayParticles(World.octTree.getAllParticles(), true);
//			 System.setOut(outputStream);
			 World.initialPositions = new HashMap<>();
			 for (AbstractParticle particle: World.allAtoms){
				 Vector3D temp = particle.getPosition();
				 World.initialPositions.put(particle.getGUID(), new Vector3D(temp.x, temp.y, temp.z));
			 }
			 for (AbstractParticle particle: World.allMolecules){
				 Vector3D temp = particle.getPosition();
				 World.initialPositions.put(particle.getGUID(), new Vector3D(temp.x, temp.y, temp.z));
			 }
		}
		else {
//			System.setOut(outputStream);
		}
		
		int i = 0;
		
		while (true) {
	
			System.out.println("Frame: " + i);
//			startTime = System.nanoTime();
			
			// Applying Forces		
			World.registry.updateAllForces(World.activeParticles);
			
			for(AbstractParticle particle: World.activeParticles){
//				System.out.println(i);
				if(simulationLvlAtomic)
					particle.integrate(frameTime_as*World.time_metric);	
				else {
					if(!simulationLvlPartial)
						particle.integrate(i*frameTime_as*World.time_metric);
					else
						particle.integrate(frameTime_as*World.time_metric);
				}
				printParticleStatus(particle);
			}
			
//			endTime = System.nanoTime();
//			duration = (endTime - startTime);  
//			System.out.println("Time taken to update forces: " + duration/1000);
//			startTime = System.nanoTime();

			// Update tree only after integration
			World.octTree.updateAllActiveParticles();
			
//			endTime = System.nanoTime();
//			duration = (endTime - startTime);  
//			System.out.println("Time taken for broad phase: " + duration/1000);
//			startTime = System.nanoTime();
			
			// Collision Detection 
//			detector.detectCollision(octTree, activeParticles, potentialContacts);    	 
			
//			endTime = System.nanoTime();
//			duration = (endTime - startTime);  
//			System.out.println("Time taken for narrow phase: " + duration/1000);
//			startTime = System.nanoTime();
			
			// Resolve Collisions and set active particles
//			resolver.resolveContacts(potentialContacts);	
			
//			endTime = System.nanoTime();
//			duration = (endTime - startTime);  
//			System.out.println("Time taken to resolve: " + duration/1000);
//			startTime = System.nanoTime();
			
			if (displayUI) {
			//	AbstractParticle [] temp_particles = activeParticles.toArray(new AbstractParticle[activeParticles.size()]);
			//	editor.getMediator().notifyUpdated(temp_particles);
				if(i%100 == 0){
//					System.setOut(dummyStream);
					// Test Rotation
					if(debugRotate && (simulationLvlAtomic || !simulationLvlAtomic && simulationLvlPartial))
						testRotate(rotateTestAtoms);
					
					window.getMediator().displayParticles(World.octTree.getAllParticles(), true);
//					System.setOut(outputStream);
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
					try {
						countDownLatch.await(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			i++;
			if(i > 100 && simulationLvlAtomic || i>200 && !simulationLvlAtomic) {
				simulationStatus = "restart";
				break;
			}
		}
//		outputStream.flush();
		
		if(!displayUI)
			new UpdateRegistry().displayParticles(World.octTree.getAllParticles(), false);
		
//		System.setOut(defaultStream);
		endTime = System.nanoTime();
		duration = (endTime - startTime); 
		System.out.println("Time taken to end: " + duration/1000);
	}
	

	public static void markAsActive(AbstractParticle particle){
		
		for (int index =0; index< World.activeParticles.size(); index++){
			if (World.activeParticles.get(index).getGUID() == particle.getGUID()) {
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
		World.octTree.getAllParticles(particles);
		
		for(AbstractParticle particle: particles)
			markAsActive(particle);
	}
	
	public static void resetActiveParticlesVelocities(){
		for(AbstractParticle particle: World.activeParticles)
			particle.setVelocity(0, 0, 0);
	}
	
	private static void printParticleStatus(AbstractParticle p){
		System.out.print("Particle " + p.getGUID() + "\t" +p.getPosition().print()+"\n");
		//System.out.print("Particle " + p.getGUID() + "\t" + p.getVelocity().print()+"\t");
	}
	
	private static void restartSimulation(){
		for (AbstractParticle particle: World.octTree.getAllParticles()) {
			Vector3D pos = World.initialPositions.get(particle.getGUID());
			if (particle instanceof Atom)
				particle.setPosition(pos.x, pos.y, pos.z);
			else if (particle instanceof Molecule)
				((sg.edu.ntu.aalhossary.fyp2014.physics_engine.model.Molecule) particle).setPositionAsCentroid();
			particle.setVelocity(0, 0, 0);
		}
		
		checkForActiveParticles();
	}
	
	private static void checkSimulationLevel(){
		World.octTree.clear();
		if(simulationLvlAtomic) {
			for(AbstractParticle particle: World.allAtoms) {
				World.octTree.insert(particle);
			}
		}
		else {
			for(AbstractParticle particle: World.allMolecules) {
				World.octTree.insert(particle);
			}
			if(simulationLvlPartial){
				World.octTree.clear();
				for(AbstractParticle particle: ((sg.edu.ntu.aalhossary.fyp2014.physics_engine.model.Molecule)World.allMolecules.get(0)).getAtoms()) {
					World.octTree.insert(particle);
				}
				
				for(int i=1; i<World.allMolecules.size(); i++) {
					World.octTree.insert(World.allMolecules.get(i));
				}
			}
		}
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
			InputStream ir = World.class.getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(ir));
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
					World.allAtoms.add(particle);
					atoms.add((Atom)particle);
					line = br.readLine();
				}
				Molecule molecule = new Molecule(atoms);
				World.allMolecules.add(molecule);
				line = br.readLine();
			}
			
			
			
			/*
			
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
			*/
			 
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
}
