package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

public class ForceRegistry {
	private HashMap<AbstractParticle, ArrayList<Vector3D>> registrations = new HashMap<AbstractParticle,ArrayList<Vector3D>>();
	
	public HashMap<AbstractParticle, ArrayList<Vector3D>> get(){
		return registrations;
	}
	
	public void add(AbstractParticle abstractParticle, Vector3D force) {
		
		if(registrations.get(abstractParticle) == null){
			ArrayList<Vector3D> forces = new ArrayList<Vector3D>();
			forces.add(force);
			registrations.put(abstractParticle, forces);
		}
		else
			registrations.get(abstractParticle).add(force);
	}

	public void updateForce(AbstractParticle abstractParticle, Vector3D oldForce, Vector3D newForce) {
		ArrayList<Vector3D> forces = registrations.get(abstractParticle);
		if(forces!=null){
			int index = forces.indexOf(oldForce);
			forces.get(index).x = newForce.x;
			forces.get(index).y = newForce.y;
			forces.get(index).z = newForce.z;
			
		}
	}
	/***
	 * Apply forces to particles
	 */
	public void updateAllForces(){

		// electric force, vdw force etc are not stored in force registry
		for(AbstractParticle particle: World.activeParticles){
			for(AbstractParticle particle2: World.activeParticles){
				if(!particle.equals(particle2)){
					Force electricForce = new ElectricForce(particle,particle2);
					Force lennardJonesForce = new LennardJonesForce (particle, particle2);
					
					// calculating the total force here saves computation then letting the force registry to handle
					Vector3D totalForce = electricForce.getForce();
					totalForce.add(lennardJonesForce.getForce());
					
					particle.addForce(totalForce);
					particle2.addForce(totalForce.getNegativeVector());
				}
			}
		}
		
		// apply forces that are in the force registry
		applyForces();
	}
	
	public void remove(AbstractParticle abstractParticle, Vector3D force) {
		ArrayList<Vector3D> forces = registrations.get(abstractParticle);
		if(forces!=null){
			forces.remove(force);
			Vector3D cancelForce = new Vector3D (-force.x, -force.y, -force.z, force.metric);
			abstractParticle.addForce(cancelForce);
		}	
	}

	public void removeAllForceFrom (AbstractParticle abstractParticle) {
		ArrayList<Vector3D> forces = registrations.get(abstractParticle);
		if(forces!=null)
			forces.clear();
		abstractParticle.clearAccumulator();
	}
	
	public void clear(){
		registrations.clear();
	}
	
	public void applyForce(AbstractParticle abstractParticle){
		ArrayList<Vector3D> forces = registrations.get(abstractParticle);
		if(forces != null)
			for (Vector3D force: forces)
				abstractParticle.addForce(force);
	}
	
	public void applyForces(){
		for (Entry<AbstractParticle, ArrayList<Vector3D>> entry: registrations.entrySet()){
			AbstractParticle abstractParticle = entry.getKey();
			applyForce(abstractParticle);
		}
	}

}