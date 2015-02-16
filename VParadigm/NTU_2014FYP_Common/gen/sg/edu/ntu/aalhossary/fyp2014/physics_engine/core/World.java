package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.MoleculeEditor;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.*;


public class World {
	
	public static double COEFFICENT_OF_RESTITUTION = 1;
	public static int particleCount = 0;
	public static boolean simulationLvlAtomic = true;
	public static String simulationStatus = "running";
	public static CountDownLatch countDownLatch;
	
	
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
	
	public static void main (String[] args) throws Exception{
		
		originalStream = System.out;
		dummyStream    = new PrintStream (new OutputStream(){
		    @Override
			public void write(int b) {
		        //NO-OP
		    }
		});
		
		AbstractParticle a1=null, a2=null, a3=null;
		try {
			a1 = new Atom("Na");
			a2 = new Atom("Cl");
			a3 = new Atom ("Na");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();			
		}
		
		

		a1.setPosition(0, 0, 0);
		a1.setVelocity(0, 0, 0);
		a1.setNetCharge(1);		// find a way to get oxidation state/ net charge
		
		a2.setPosition(5e-10, 5e-10, 5e-10);
		a2.setVelocity(0, 0, 0);
		a2.setNetCharge(-1);
		
		ArrayList<Atom> atomList = new ArrayList<>();
		atomList.add((Atom)a1);
		atomList.add((Atom)a2);
		Molecule m1 = new Molecule(atomList);
		
		a3.setPosition(10e-10, 10e-10, 10e-10);
		a3.setVelocity(0, 0, 0);
		a3.setNetCharge(1);
		
	//	octTree.insert(a1);
	//	octTree.insert(a2);
		octTree.insert(m1);
		octTree.insert(a3);
		
/*		AbstractParticle a4 = new Atom ("He");
		AbstractParticle a5 = new Atom ("Li");
		AbstractParticle a6 = new Atom ("Be");
		AbstractParticle a7 = new Atom ("B");
		AbstractParticle a8 = new Atom ("C");
		AbstractParticle a9 = new Atom ("N");
		AbstractParticle a10 = new Atom ("O");
		AbstractParticle a11 = new Atom ("F");
		AbstractParticle a12 = new Atom ("Ne");
		AbstractParticle a13 = new Atom ("Mg");
	*/	
	//	a3.setPosition(30, 40, 50, -10);
	/*	a4.setPosition(-30, 4, 5, -10);
		a5.setPosition(3, 40, 5, -10);
		a6.setPosition(3, 4, 50, -10);
		a7.setPosition(3, 40, 5, -10);
		a8.setPosition(3, -4, 5, -10);
		a9.setPosition(30, 40, 5, -10);
		a10.setPosition(3, 4, -5, -10);
		a11.setPosition(-3, 4, 5, -10);
		a12.setPosition(-3, 4, -5, -10);
		a13.setPosition(-30, -4, 5, -10);
	*/	
	//	octTree.insert(a3);
	/*	octTree.insert(a4);
		octTree.insert(a5);
		octTree.insert(a6);
		
		octTree.insert(a7);
		octTree.insert(a8);
		octTree.insert(a9);
		octTree.insert(a10);
		octTree.insert(a11);
		octTree.insert(a12);
		octTree.insert(a13);
		octTree.printTree(octTree);
*/
	
		
		// checks which particles will be involved in calculations
		checkForActiveParticles();
		
		if(displayUI){
			 System.setOut(dummyStream);
			 window = new MainWindow();
			 window.getMediator().displayParticles(octTree.getAllParticles());
		//	 System.setOut(originalStream);
		}		
		
		System.out.println("Time \t a1\t\t\t \t  a2\t\t\t");
		int i = 0;
		while(true) {
			
			// Applying Forces
			registry.updateAllForces();
					
			for(AbstractParticle particle: activeParticles){
				particle.integrate(i*time_metric);	
			}
			
			// Update tree only after integration
			octTree.updateAllActiveParticles();
			
		/*	
			System.out.print(i + "\t");
			printParticleStatus(m1);
			printParticleStatus(a1);
			printParticleStatus(a2);
			printParticleStatus(a3);
			System.out.println();
		*/	
			// Collision Detection 
			detector.detectCollision();    	 

			// Resolve Collisions and set active particles
			resolver.resolveContacts(potentialContacts);	
			
			
			
			if(displayUI){
				AbstractParticle [] temp_particles = activeParticles.toArray(new AbstractParticle[activeParticles.size()]);
			//	editor.getMediator().notifyUpdated(temp_particles);
				if(i%100 == 0){
					System.setOut(dummyStream);
					window.getMediator().displayParticles(octTree.getAllParticles());
				//	System.setOut(originalStream);
				}
				
				if(simulationStatus.equals("restart")){
					
					a1.setPosition(0, 0, 0);
					a2.setPosition(5e-10, 5e-10, 5e-10);
					a3.setPosition(10e-10, 10e-10, 10e-10);
					m1.setPosition(2.5e-10, 2.5e-10, 2.5e-10);
					for (AbstractParticle particle: octTree.getAllParticles())
						particle.setVelocity(0, 0, 0);
					
				//	checkForActiveParticles();
					
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
			if(i == 30001) 
				i = 0;
		}
	}
	
	public static void markAsActive(AbstractParticle particle){
		if(oldPositions.contains(particle.getPosition()))
			return;	
		World.activeParticles.add(particle);
		World.oldPositions.add(particle.getPosition());
	}
	
	private static void checkForActiveParticles(){
		
		ArrayList <AbstractParticle> particles = new ArrayList<>();
		octTree.getAllParticles(particles);
		for(AbstractParticle particle: particles)
			markAsActive(particle);
		/*
		for(AbstractParticle particle: particles){	
			if(registry.get().containsKey(particle))
				markAsActive(particle);
			else if(particle.getVelocity().getMagnitude() != 0) 
				markAsActive(particle);
			else if (particle.getAcceleration().getMagnitude() != 0) 
				markAsActive(particle);
		}
		*/
	}
	
	private static void printParticleStatus(AbstractParticle p){
		System.out.print(p.getPosition().print()+"\t");
	//	System.out.print(p.getVelocity().print()+"\t");
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
	

}