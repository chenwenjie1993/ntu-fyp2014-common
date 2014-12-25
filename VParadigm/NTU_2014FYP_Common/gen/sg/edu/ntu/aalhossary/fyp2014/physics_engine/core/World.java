package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
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
		
		a2.setPosition(5e-10, 5e-10, 5e-10);
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
			/*
			if(a1.getBoundingPrimitive().overlap(a2.getBoundingPrimitive())){
				//System.out.println("The two particles collided");
				//break;
				double bond_length = 0.28E-9;
				double diff_x = a1.getPosition().x - a2.getPosition().x;
				double diff_y = a1.getPosition().y - a2.getPosition().y;
				double diff_z = a1.getPosition().z - a2.getPosition().z;
				double min_dist = Math.min(Math.abs(diff_x), Math.min(Math.abs(diff_y), Math.abs(diff_z)));
				
				if(min_dist <= bond_length){
					System.out.println("The two particles collided- " + min_dist);
			
					Vector3D v1 = a1.calculateVelocityChange(a2.getMass(), a2.getVelocity());
					Vector3D v2 = a2.calculateVelocityChange(a1.getMass(), a1.getVelocity());
					a1.setVelocity(v1.x, v1.y, v1.z);
					a2.setVelocity(v2.x, v2.y, v2.z);
					System.out.println("a1 velocity after: "+a1.getVelocity().print());
					System.out.println("a2 velocity after: "+a2.getVelocity().print());
				
					System.out.print(i + "\t");
					printParticleStatus(a1);
					System.out.print("\t");
					printParticleStatus(a2);
					System.out.println();
				}
				
			}
		*/
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		
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