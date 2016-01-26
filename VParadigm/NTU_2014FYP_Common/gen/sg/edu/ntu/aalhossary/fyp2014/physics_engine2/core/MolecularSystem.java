package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import java.util.ArrayList;
import java.util.logging.Logger;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

public class MolecularSystem {
	public ArrayList<AbstractParticle> particles;
	public ArrayList<Interaction> interactions;
	public double duration = 1e-6;
	private static final Logger log = Logger.getLogger("main");
	
	public void updateEnergyPotential() {
		for (Interaction interaction: interactions) {
   			interaction.updatePotentialEnergy();
   		}
   	}
	
	public void integrate() {
		/**
		 * Velocity Verlet Algorithm
		 */
		for (AbstractParticle particle: particles) {
			Vector3D r = particle.getPosition();
			Vector3D v = particle.getVelocity();
			Vector3D a = particle.getAcceleration();
			Vector3D a2 = particle.getAccumulatedAcceleration();
			log.info("[ACC]" + a2);
						
			Vector3D dr = new Vector3D();
			dr.x = v.x * duration + 0.5 * a.x * duration * duration;
			dr.y = v.y * duration + 0.5 * a.y * duration * duration;
			dr.z = v.z * duration + 0.5 * a.z * duration * duration;
			r.add(dr);
			
			Vector3D dv = new Vector3D();
			dv.x = 0.5 * (a.x + a2.x) * duration;
			dv.y = 0.5 * (a.y + a2.y) * duration;
			dv.z = 0.5 * (a.z + a2.z) * duration;
			v.add(dv);
			
			particle.setAcceleration(a2.x, a2.y, a2.z);
			particle.clearAccumulator();
			
//			System.out.println("Positon: " + particle.getPosition());
//			System.out.println("Velocity: " + particle.getVelocity());
		}
	}
}
