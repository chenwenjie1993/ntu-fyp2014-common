package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.common.TestDisplayParticles;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.MoleculeEditor;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.*;


public class World {
	
	
	public static double distance_metric = DISTANCE.m.value();
	public static double time_metric = TIME.as.value();
	public static double mass_metric = MASS.kg.value();
	
	public static ArrayList<AbstractParticle> activeParticles = new ArrayList<>();
	public static ArrayList<Vector3D> oldPositions= new ArrayList<>();
	public static ArrayList<AbstractParticle> test = new ArrayList<>();
	public static ArrayList<AbstractParticle[]> potentialContacts = new ArrayList<>();
	public static ForceRegistry registry = new ForceRegistry();
	public static OctTree octTree = new OctTree();
	public static NarrowCollisionDetector detector = new NarrowCollisionDetector();
	public static ContactResolver resolver = new ContactResolver();

	public static MoleculeEditor editor;
	public static boolean displayUI = true;

	public static void main (String[] args) throws Exception{
		
		AbstractParticle a1=null, a2=null;
		try {
			a1 = new Atom("Na");
			a2 = new Atom("Cl");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();			
		}

		a1.setPosition(0, 0, 0);
		a1.setVelocity(0, 0, 0);
		a1.setAcceleration(0, 0, 0);
		a1.setNetCharge(1);		// find a way to get oxidation state/ net charge
		
		a2.setPosition(5e-10, 5e-10, 5e-10);
		a2.setVelocity(0, 0, 0);
		a2.setAcceleration(0, 0, 0);
		a2.setNetCharge(-1);
		
		
		octTree.insert(a1);
		octTree.insert(a2);
		
		AbstractParticle a3 = new Atom ("H");
		AbstractParticle a4 = new Atom ("He");
		AbstractParticle a5 = new Atom ("Li");
		AbstractParticle a6 = new Atom ("Be");
		AbstractParticle a7 = new Atom ("B");
		AbstractParticle a8 = new Atom ("C");
		AbstractParticle a9 = new Atom ("N");
		AbstractParticle a10 = new Atom ("O");
		AbstractParticle a11 = new Atom ("F");
		AbstractParticle a12 = new Atom ("Ne");
		AbstractParticle a13 = new Atom ("Mg");
		
		a3.setPosition(3, 4, 5, -10);
		a4.setPosition(-30, 4, 5, -10);
		a5.setPosition(3, 40, 5, -10);
		a6.setPosition(3, 4, 50, -10);
		a7.setPosition(3, 40, 5, -10);
		a8.setPosition(3, -4, 5, -10);
		a9.setPosition(30, 40, 5, -10);
		a10.setPosition(3, 4, -5, -10);
		a11.setPosition(-3, 4, 5, -10);
		a12.setPosition(-3, 4, -5, -10);
		a13.setPosition(-30, -4, 5, -10);
		
		octTree.insert(a3);
		octTree.insert(a4);
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
		
		// Registering forces for the first time
		Vector3D electricForce = Force.getElectricForce(a1, a2) ;
		System.out.println("Electric (Coulomb) Force between atoms is: " + electricForce.print());
		registry.add(a1, electricForce);
		registry.add(a2, electricForce.getNegativeVector());
		
		Vector3D vdwForce = Force.getLennardJonesPotential(a1, a2);
		System.out.println("vdW Force between atoms is: " + vdwForce.print());
		registry.add(a1, vdwForce);
		registry.add(a2, vdwForce.getNegativeVector());
	
	
		// CALL markAsActive if velocity or acceleration is non zero or added to force registry
		//	markAsActive(a1);
		//	markAsActive(a2);
		
		// checkForActiveParticles automates the process at a heavier cost
		checkForActiveParticles();
		
		if(displayUI){
			 editor = new MoleculeEditor();
			 editor.getMediator().displayParticles(a1, a2);
		}		
		
		System.out.println("Time \t a1\t\t\t \t  a2\t\t\t");
		for(int i=0; i<10000; i++){
			
			// Applying Forces
			registry.applyForces();
			
			for(AbstractParticle particle: activeParticles)
				particle.integrate(i*time_metric);			
			
			// Update tree only after integration
			octTree.updateAllActiveParticles();
			
			// Updating Forces
			Vector3D newElectricForce = Force.getElectricForce(a1, a2);
			registry.updateForce(a2, electricForce.getNegativeVector(), newElectricForce.getNegativeVector());
			registry.updateForce(a1, electricForce, newElectricForce);		// important! update the negative first. otherwise, old value will be lost and getNegativeVector will return error
			
			Vector3D newVdWForce = Force.getLennardJonesPotential(a1, a2);
			registry.updateForce(a2, vdwForce.getNegativeVector(), newVdWForce.getNegativeVector());
			registry.updateForce(a1, vdwForce, newVdWForce);	
			
			System.out.print(i + "\t");
			printParticleStatus(a1);
			System.out.print("\t");
			printParticleStatus(a2);
			System.out.println();
			
			// Collision Detection 
			detector.detectCollision();    	 
			
			// Resolve Collisions and set active particles
			resolver.resolveContacts(potentialContacts);
			
		
			if(displayUI){
				AbstractParticle [] temp_particles = activeParticles.toArray(new AbstractParticle[activeParticles.size()]);
			//	editor.getMediator().notifyUpdated(temp_particles);
				if(i%100 == 0)
					editor.getMediator().displayParticles(a1, a2);
			}
			
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
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
		for(AbstractParticle particle: particles){	
			if(registry.get().containsKey(particle))
				markAsActive(particle);
			else if(particle.getVelocity().getMagnitude() != 0) 
				markAsActive(particle);
			else if (particle.getAcceleration().getMagnitude() != 0) 
				markAsActive(particle);
		}
	}
	
	private static void printParticleStatus(AbstractParticle p){
		System.out.print(p.getPosition().print()+"\t");
		//System.out.println(p.getVelocity().print());	
		//System.out.println("Acceleration: " + p.acceleration.print());

	}
	

}