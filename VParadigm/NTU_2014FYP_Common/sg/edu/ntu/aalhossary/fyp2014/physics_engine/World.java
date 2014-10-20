package sg.edu.ntu.aalhossary.fyp2014.physics_engine;

import java.util.*;

public class World {

	public static double machineEpsilon;
	private static Collection<AbstractParticle> abstractParticles = new ArrayList<>();
	private static ForceRegistry registry = new ForceRegistry();
	private static BroadPhaseCollisionDetector detector = new BroadPhaseCollisionDetector();
	private static ContactResolver resolver = new IterativeContactResolver();

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO - implement World.main
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param p
	 */
	private static void printParticleStatus(AbstractParticle p) {
		// TODO - implement World.printParticleStatus
		throw new UnsupportedOperationException();
	}

	private static double calculateMachineEpsilon() {
		// TODO - implement World.calculateMachineEpsilon
		throw new UnsupportedOperationException();
	}

}