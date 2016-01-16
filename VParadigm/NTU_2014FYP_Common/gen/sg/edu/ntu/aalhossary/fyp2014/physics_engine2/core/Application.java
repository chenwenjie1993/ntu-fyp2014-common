package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TypologyBuilder;

public class Application {

	public static void main(String[] args) {
		String dir = "res/test/amber03/";
   		TypologyBuilder tb = new TypologyBuilder();
   		MolecularSystem m = tb.build(dir);
   		m.calculateParticleEnergyPotential();
   		for (Particle p: m.particles) {
   			System.out.println(p.getPotentialEnergy().toString());
   		}
   	}

}
