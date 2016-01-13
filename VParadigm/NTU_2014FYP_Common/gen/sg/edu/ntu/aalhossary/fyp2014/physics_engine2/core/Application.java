package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.typology.TypologyBuilder;

public class Application {

	public static void main(String[] args) {
		String fileName = "res/test/amber03/topol.top";
		TypologyBuilder tb = new TypologyBuilder();
		Molecule m = tb.build(fileName);
		m.calculateParticleEnergyPotential();
		for (Particle p: m.particles) {
			System.out.println(p.getEnergy().toString());
		}
	}

}
