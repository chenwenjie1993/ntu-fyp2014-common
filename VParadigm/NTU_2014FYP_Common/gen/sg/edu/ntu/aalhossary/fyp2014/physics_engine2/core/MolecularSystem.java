package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import java.util.ArrayList;

public class MolecularSystem extends sg.edu.ntu.aalhossary.fyp2014.common.Molecule {
	public ArrayList<Particle> particles;
	public ArrayList<Interaction> interactions;
	
	public void calculateParticleEnergyPotential() {
		for (Interaction interaction: interactions) {
   			interaction.updatePotentialEnergy();
   		}
   	}
}