package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

public class World {
	public static double machineEpsilon;
	
	private static ArrayList<AbstractParticle> abstractParticles = new ArrayList<>();;
	private static ForceRegistry registry = new ForceRegistry();
	private static BroadPhaseCollisionDetector detector = new BroadPhaseCollisionDetector();
	private static ContactResolver resolver = new IterativeContactResolver();


	public static void main (String[] args){
		
		machineEpsilon = calculateMachineEpsilon();
		AbstractParticle a1 = new Atom("011");	//Na
		AbstractParticle a2 = new Atom("017");	//Cl
		
		
		a1.setPosition(0, 0, 0);
		a1.setVelocity(0, 0, 0);
		a1.setAcceleration(0, 0, 0);
		// find a way to get oxidation state/ net charge
		a1.setNetCharge(1);
	
		// actual is 564.02 pm
		
		a2.setPosition(5e-10, 5e-10, 5e-10);
		a2.setVelocity(0, 0, 0);
		a2.setAcceleration(0, 0, 0);
		a2.setNetCharge(-1);
	
	
		
		Vector3D eF = Force.getElectricForce(a1, a2) ;
		System.out.println("Electric (Coulomb) Force between atoms is: " + eF.print());
		registry.add(a1, eF);
		registry.add(a2, eF.getNegativeVector());
		
		Vector3D vdwF = Force.getLennardJonesPotential(a1, a2);
		System.out.println("vdW Force between atoms is: " + vdwF.print());
		registry.add(a1, vdwF);
		registry.add(a2, vdwF.getNegativeVector());
	
		System.out.println("Time \t a1\t\t\t \t  a2\t\t\t");
		for(int i=0; i<1000; i++){
			registry.updateAllForces();
			System.out.print(i + "\t");
			a1.integrate(i*1e-18);
			a2.integrate(i*1e-18);
			printParticleStatus(a1);
			System.out.print("\t");
			printParticleStatus(a2);
			System.out.println();
		
			if(a1.getBoundingPrimitive().overlap(a2.getBoundingPrimitive())){
				System.out.println("THe two particles collided");
				break;

			}
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
		//System.out.println(p.velocity.print());	
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