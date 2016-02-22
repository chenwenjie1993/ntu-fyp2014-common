package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

public class Simulator {
	public void nextFrame(MolecularSystem m) {
		m.updateEnergyPotential();
		m.integrate();
	}
}
