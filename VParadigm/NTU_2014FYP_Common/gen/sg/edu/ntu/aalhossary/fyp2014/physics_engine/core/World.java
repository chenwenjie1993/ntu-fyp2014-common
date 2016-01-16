package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.MoleculeEditor;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.*;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.ui.MainWindow;

/**
 * @author waiyan
 * Main class of the physics engine
 */
public class World {
	
	enum SimulationLevel {Atomic, Molecular, Partial};
	public static double COEFFICENT_OF_RESTITUTION = 1;
	
	@Option(name = "-l", aliases = "-level", usage = "Simulation Level")
	public static SimulationLevel simlvl = SimulationLevel.Atomic;
	
	public static boolean simulationLvlAtomic = true;
	public static boolean simulationLvlPartial = false;
	
	public static boolean electricForceActive = true;
	public static boolean LJForceActive = true;
	
	public static int frameTime_as = 50;
	public static boolean debugRotate = false;
	
	@Argument(usage = "inputs of the Engine", metaVar="INPUTS")
	public static ArrayList<String> inputFilePaths = new ArrayList<>();
	
	@Option(name="-o", aliases = "-output", usage="output of the engine", metaVar="OUTPUT")
	public static String outputFilePath = "output.txt";
	
	@Option(name="-gui",usage="enable GUI")
	public static boolean displayUI = false;
	
	public static int particleCount = 0;
	public static String simulationStatus = "running";
	public static CountDownLatch countDownLatch;
	public static boolean allParticlesActive = true;
	
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
	public static HashMap <Integer,Vector3D> initialPositions;
	public static MoleculeEditor editor;
	public static MainWindow window;
	public static PrintStream outputStream;
	public static PrintStream defaultStream;
	public static PrintStream dummyStream;
	public static long startTime;
	public static long endTime;
	public static long duration;
	
	public static void main (String[] args) throws Exception{
		
		new World().parseArguments(args);
			
		defaultStream = System.out;
		outputStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFilePath)));
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
		System.out.println("Time taken to load particles: " + duration/1000);
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
			 System.setOut(dummyStream);
			 window = new MainWindow();
			 window.getMediator().displayParticles(octTree.getAllParticles());
			 System.setOut(outputStream);
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
		else {
			System.setOut(outputStream);
		}
		
		int i = 0;
		
		while(true) {
	
			System.out.println("\nFrame: " + i);
//			startTime = System.nanoTime();
			
			// Applying Forces		
			registry.updateAllForces(activeParticles);
			
			for(AbstractParticle particle: activeParticles){
				if(simulationLvlAtomic)
					particle.integrate(frameTime_as*time_metric);	
				else {
					if(!simulationLvlPartial)
						particle.integrate(i*frameTime_as*time_metric);
					else
						particle.integrate(frameTime_as*time_metric);
				}
				printParticleStatus(particle);
			}
			
//			endTime = System.nanoTime();
//			duration = (endTime - startTime);  
//			System.out.println("Time taken to update forces: " + duration/1000);
//			startTime = System.nanoTime();

			// Update tree only after integration
			octTree.updateAllActiveParticles();
			
//			endTime = System.nanoTime();
//			duration = (endTime - startTime);  
//			System.out.println("Time taken for broad phase: " + duration/1000);
//			startTime = System.nanoTime();
			
			// Collision Detection 
			detector.detectCollision(octTree, activeParticles, potentialContacts);    	 
			
//			endTime = System.nanoTime();
//			duration = (endTime - startTime);  
//			System.out.println("Time taken for narrow phase: " + duration/1000);
//			startTime = System.nanoTime();
			
			// Resolve Collisions and set active particles
			resolver.resolveContacts(potentialContacts);	
			
//			endTime = System.nanoTime();
//			duration = (endTime - startTime);  
//			System.out.println("Time taken to resolve: " + duration/1000);
//			startTime = System.nanoTime();
			
			if(displayUI){
			//	AbstractParticle [] temp_particles = activeParticles.toArray(new AbstractParticle[activeParticles.size()]);
			//	editor.getMediator().notifyUpdated(temp_particles);
				if(i%100 == 0){
					System.setOut(dummyStream);
					// Test Rotation
					if(debugRotate && (simulationLvlAtomic || !simulationLvlAtomic && simulationLvlPartial))
						testRotate(rotateTestAtoms);
					
					window.getMediator().displayParticles(octTree.getAllParticles());
					System.setOut(outputStream);
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
				break;
			}
		}
		outputStream.flush();
		
		if(!displayUI)
			new UpdateRegistry().displayParticles(octTree.getAllParticles());
		
		System.setOut(defaultStream);
		endTime = System.nanoTime();
		duration = (endTime - startTime); 
		System.out.println("Time taken to end: " + duration/1000);
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
	
	@SuppressWarnings("deprecation")
	public void parseArguments(String[] args){
		 CmdLineParser parser = new CmdLineParser(this);
		 parser.setUsageWidth(100);
		 try {
			 parser.parseArgument(args);
			 System.out.println("Output will be logged at " + outputFilePath);
			 
			if(inputFilePaths.isEmpty())
				 inputFilePaths.add("lysine.pdb");
			 
			 switch (simlvl) {
				case Atomic: 	simulationLvlAtomic = true;
								break;
				case Molecular: simulationLvlAtomic = false;
								simulationLvlPartial = false;
								break;
				case Partial:	simulationLvlAtomic = false;
								simulationLvlPartial = true;
								break;
			}	
		 }
		 catch(CmdLineException e) {
			 System.err.println(e.getMessage());
			 System.err.println("java World [options...] arguments...");
			 parser.printUsage(System.err);
			 System.err.println();
			 System.exit(0);
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
					allAtoms.add(particle);
					atoms.add((Atom)particle);
					line = br.readLine();
				}
				Molecule molecule = new Molecule(atoms);
				allMolecules.add(molecule);
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