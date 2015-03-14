package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

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
	public static boolean simulationLvlAtomic = true;
	public static boolean electricForceActive = true;
	public static boolean LJForceActive = true;
	public static String simulationStatus = "running";
	public static CountDownLatch countDownLatch;
	public static boolean restartTimeOut = true;
	public static boolean allParticlesActive = true;
	
	public static double distance_metric = DISTANCE.m.value();
	public static double time_metric = TIME.as.value();
	public static double mass_metric = MASS.kg.value();
	
	public static ArrayList<AbstractParticle> activeParticles = new ArrayList<>();
	public static ArrayList<Vector3D> oldPositions= new ArrayList<>();
	public static ArrayList<AbstractParticle[]> potentialContacts = new ArrayList<>();
	public static ForceRegistry registry = new ForceRegistry();
	public static OctTree octTree = new OctTree();
	public static NarrowCollisionDetector detector = new NarrowCollisionDetector();
	public static ContactResolver resolver = new ContactResolver();

	public static MoleculeEditor editor;
	public static MainWindow window;
	public static PrintStream originalStream;
	public static PrintStream dummyStream;
	
//	public static boolean displayUI = false;
	public static boolean displayUI = true;
	
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
		
		octTree.insert(a1);
		octTree.insert(a2);
		octTree.insert(a3);
		octTree.insert(a4);
		octTree.insert(a5);
		octTree.insert(a6);
		octTree.insert(a7);
		octTree.insert(a8);
		octTree.insert(a9);
	
		
		AbstractParticle a10 = new Atom ("Cl");
		a10.setPosition(13.5e-10, -13.5e-10, 0);
		a10.setNetCharge(-1);	
		octTree.insert(a10);
		
		AbstractParticle a11 = new Atom ("Na");
		a11.setPosition(10e-10, -10e-10, 0);
		a11.setNetCharge(1);	
		octTree.insert(a11);
		
		ArrayList<AbstractParticle> rotateTestAtoms = new ArrayList<>();
		rotateTestAtoms.add(a6);
		rotateTestAtoms.add(a7);
		rotateTestAtoms.add(a8);
		rotateTestAtoms.add(a9);
		
		// insert as molecule
	/*	
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
		octTree.insert(molecule);
		
		ArrayList <Atom> atomList2 = new ArrayList<>();
		atomList2.add((Atom)a10);
		atomList2.add((Atom)a11);
		Molecule molecule2 = new Molecule(atomList2);
		octTree.insert(molecule2);
	*/	
		
//		AbstractParticle a12 = new Atom ("Na");
//		a12.setPosition(10e-10, -10e-10, 0);
//		a12.setNetCharge(1);	
//		octTree.insert(a12);
	
/*		
		a11.setPosition(0, 0, 0);
		a10.setPosition(4.5e-10, 4.5e-10, 0);
		a12.setPosition(9e-10, 0, 0);
		
		ArrayList<Atom> atomList = new ArrayList<>();
		atomList.add((Atom)a11);
		atomList.add((Atom)a10);
		atomList.add((Atom)a12);
		Molecule m10 = new Molecule(atomList);
		octTree.insert(m10);	
		m10.setInverseInertiaTensor(new Vector3D(1e5,1e5,1e5));

*/	
		
		
		// checks which particles will be involved in calculations
		checkForActiveParticles();
		
		if(displayUI){
			 System.setOut(dummyStream);
			 window = new MainWindow();
			 window.getMediator().displayParticles(octTree.getAllParticles());
			 System.setOut(originalStream);
			 initialPositions = new HashMap<>();
			 for (AbstractParticle particle: octTree.getAllParticles()){
				 Vector3D temp = particle.getPosition();
				 initialPositions.put(particle.getGUID(), new Vector3D(temp.x, temp.y, temp.z));
			 }
		}		
		
		System.out.println("Time \t a1\t\t\t \t  a2\t\t\t");
		int i = 0;

		while(true) {
	
			// Applying Forces		
			registry.updateAllForces(activeParticles);
		
			System.out.println("\n" + i);
			for(AbstractParticle particle: activeParticles){
				particle.integrate(50*time_metric);	
			//	printParticleStatus(particle);
			}

			// Update tree only after integration
			octTree.updateAllActiveParticles();
			
			// Collision Detection 
			detector.detectCollision(octTree, activeParticles, potentialContacts);    	 
			
			// Resolve Collisions and set active particles
			resolver.resolveContacts(potentialContacts);	
			
			if(displayUI){
			//	AbstractParticle [] temp_particles = activeParticles.toArray(new AbstractParticle[activeParticles.size()]);
			//	editor.getMediator().notifyUpdated(temp_particles);
				if(i%100 == 0){
					System.setOut(dummyStream);
					// Test Rotation
					 testRotate(rotateTestAtoms);
					
					window.getMediator().displayParticles(octTree.getAllParticles());
					System.setOut(originalStream);
				}
				
				if(simulationStatus.equals("restart")){
					
					for (AbstractParticle particle: octTree.getAllParticles()) {
				
						Vector3D pos = initialPositions.get(particle.getGUID());
						if (particle instanceof Atom)
							particle.setPosition(pos.x, pos.y, pos.z);
						else if (particle instanceof Molecule)
							((Molecule) particle).setPositionAsCentroid();
						particle.setVelocity(0, 0, 0);
					}
					
					checkForActiveParticles();
					
					i=0;
					simulationStatus = "running";
				}
				
				else if(simulationStatus.equals("paused")){
					i--;
					countDownLatch = new CountDownLatch(1);
					countDownLatch.await(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				}
			}
			
			i++;
			
		//	if(i==5)
		//		System.exit(-1);
			
			if(i == 10000 && restartTimeOut)
				simulationStatus = "restart";
		}
	}
	
	public static void markAsActive(AbstractParticle particle){
		
		for (int index =0; index< World.activeParticles.size(); index++){
			if(World.activeParticles.get(index).getGUID() == particle.getGUID()) {
				World.oldPositions.get(index).x = particle.getPosition().x;
				World.oldPositions.get(index).y = particle.getPosition().y;
				World.oldPositions.get(index).z = particle.getPosition().z;
				return;
			}
		}

		World.activeParticles.add(particle);
		Vector3D temp = new Vector3D();
		temp.x = particle.getPosition().x;
		temp.y = particle.getPosition().y;
		temp.z = particle.getPosition().z;
		World.oldPositions.add(temp);

	}
	
	public static void checkForActiveParticles(){
		
		World.activeParticles.clear();
		World.oldPositions.clear();
		
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
		//System.out.print(p.getVelocity().print()+"\t");
	}
	
	public static void setCommand(String command) throws Exception{
		String [] args = command.split(" ");
		
		if(args[0].equalsIgnoreCase("create")){
			if(args[1].equalsIgnoreCase("atom")){
				AbstractParticle a1 = new Atom (args[2]);
				octTree.insert(a1);
				int i = 3;
				while(i<args.length){
					if(args[i].equalsIgnoreCase("-pos")){
						String pos[] = args[i+1].split(",");
						a1.setPosition(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]));
					}
					else if (args[i].equalsIgnoreCase("-vel")){
						String vel[] = args[i+1].split(",");
						a1.setVelocity(Double.parseDouble(vel[0]), Double.parseDouble(vel[1]), Double.parseDouble(vel[2]));
					}
					else if (args[i].equalsIgnoreCase("-acc")){
						String acc[] = args[i+1].split(",");
						a1.setAcceleration(Double.parseDouble(acc[0]), Double.parseDouble(acc[1]), Double.parseDouble(acc[2]));
					}
					else {
						i--;	// else increment one, decrement one instead to cancel it out with i+=2
					}
					i += 2;
				}
				
			}
			else if(args[1].equalsIgnoreCase("molecule")){
				int count = Integer.parseInt(args[2]);
				for (int i=0; i<count; i++)	{
					
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

}