package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.common.TestDisplayParticles;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.MoleculeEditor;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.*;


public class World {
	public static double machineEpsilon;
	
	public static double distance_metric = DISTANCE.m.value();
	public static double time_metric = TIME.as.value();
	public static double mass_metric = MASS.kg.value();
	
	
	public static ArrayList<AbstractParticle> particles = new ArrayList<>();
	public static ArrayList<AbstractParticle[]> potentialContacts = new ArrayList<>();
	public static ForceRegistry registry = new ForceRegistry();
	public static NarrowCollisionDetector detector = new NarrowCollisionDetector();
	public static ContactResolver resolver = new ContactResolver();

	public static MoleculeEditor editor = new MoleculeEditor();

	public static void main (String[] args){
		
		machineEpsilon = calculateMachineEpsilon();
		AbstractParticle a1 = new Atom("011");	//Na
		AbstractParticle a2 = new Atom("017");	//Cl
		
		particles.add(a1);
		particles.add(a2);
		
		
		a1.setPosition(0, 0, 0);
		a1.setVelocity(0, 0, 0);
		a1.setAcceleration(0, 0, 0);
		a1.setNetCharge(1);		// find a way to get oxidation state/ net charge
		
		a2.setPosition(1e-10, 1e-10, 1e-10);
		a2.setVelocity(0, 0, 0);
		a2.setAcceleration(0, 0, 0);
		a2.setNetCharge(-1);
		
		// Registering forces for the first time
		Vector3D electricForce = Force.getElectricForce(a1, a2) ;
		System.out.println("Electric (Coulomb) Force between atoms is: " + electricForce.print());
		registry.add(a1, electricForce);
		registry.add(a2, electricForce.getNegativeVector());
		
		Vector3D vdwForce = Force.getLennardJonesPotential(a1, a2);
		System.out.println("vdW Force between atoms is: " + vdwForce.print());
		registry.add(a1, vdwForce);
		registry.add(a2, vdwForce.getNegativeVector());
	
		editor.getMediator().displayParticles(a1, a2);

		
		System.out.println("Time \t a1\t\t\t \t  a2\t\t\t");
		for(int i=0; i<10000; i++){
			
			// Applying Forces
			registry.applyForces();
			a1.integrate(i*time_metric);
			a2.integrate(i*time_metric);
			
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
			
			// Collision Detection and Resolution
			detector.detectCollision(particles);
			resolver.resolveContacts(potentialContacts);
			
			AbstractParticle[] particles = {a1, a2};
			editor.getMediator().notifyUpdated(particles);
			editor.getMediator().displayParticles(a1, a2);
			
			
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	//	TestDisplayParticles.showTest();
		
	}
	
	private static void printParticleStatus(AbstractParticle p){
		System.out.print(p.getPosition().print()+"\t");
		//System.out.println(p.getVelocity().print());	
		//System.out.println("Acceleration: " + p.acceleration.print());

	}
	
	private static double calculateMachineEpsilon(){
		double machEps = 1.0;
		do{
           machEps /= 2.0;	// operator / is used instead of bit shifting since we do not know the CPU architecture
		} while ((double) (1.0 + (machEps / 2.0)) != 1.0);
        return (machEps);
	}
}