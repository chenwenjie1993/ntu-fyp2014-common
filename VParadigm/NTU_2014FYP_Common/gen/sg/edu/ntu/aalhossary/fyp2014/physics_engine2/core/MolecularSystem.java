package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.*;

public class MolecularSystem {
	public ArrayList<AbstractParticle> particles;
	public ArrayList<Interaction> interactions;
	private double timeDelta = 0.0002;
	private Map<String, Object> params;
	
	private static final Logger log = Logger.getLogger("main");
	
	public MolecularSystem() {
		AbstractParticle.resetCount();
	}
	
	public double getTimeDelta() {
		return timeDelta;
	}

	public void setTimeDelta(double duration) {
		this.timeDelta = duration;
	}
		
	public void nextFrame() {
		updateEnergyPotential();
		integrate();
	}
	
	public void updateEnergyPotential() {
		for (Interaction interaction: interactions) {
			if (interaction instanceof Bond) {
				if ((Boolean) params.get("Bond")) {
		   			interaction.updatePotentialEnergy();
				}
			}
			else if (interaction instanceof Angle) {
				if ((Boolean) params.get("Angle")) {
		   			interaction.updatePotentialEnergy();
				}
			}
			else if (interaction instanceof ProperDihedral) {
				if ((Boolean) params.get("ProperDihedral")) {
		   			interaction.updatePotentialEnergy();
				}
			}
			else if (interaction instanceof ImproperDihedral) {
				if ((Boolean) params.get("ImproperDihedral")) {
		   			interaction.updatePotentialEnergy();
				}
			}
			else if (interaction instanceof LennardJonesPotential) {
				if ((Boolean) params.get("LennardJones")) {
		   			interaction.updatePotentialEnergy();
				}
			}
			else if (interaction instanceof ElectrostaticPotential) {
				if ((Boolean) params.get("Electrostatic")) {
		   			interaction.updatePotentialEnergy();
				}
			}
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
			dr.x = v.x * timeDelta + 0.5 * a.x * timeDelta * timeDelta;
			dr.y = v.y * timeDelta + 0.5 * a.y * timeDelta * timeDelta;
			dr.z = v.z * timeDelta + 0.5 * a.z * timeDelta * timeDelta;
			r.add(dr);
			
			Vector3D dv = new Vector3D();
			dv.x = 0.5 * (a.x + a2.x) * timeDelta;
			dv.y = 0.5 * (a.y + a2.y) * timeDelta;
			dv.z = 0.5 * (a.z + a2.z) * timeDelta;
			v.add(dv);
			
			particle.setAcceleration(a2.x, a2.y, a2.z);
			particle.clearAccumulator();
			
//			System.out.println("Positon: " + particle.getPosition());
//			System.out.println("Velocity: " + particle.getVelocity());
		}
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
