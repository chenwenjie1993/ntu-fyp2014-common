package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ForceRegistry {
	private HashMap<AbstractParticle, ArrayList<Force>> registrations = new HashMap<AbstractParticle,ArrayList<Force>>();
	
	public void add(AbstractParticle abstractParticle, Force force) {
		
		if(registrations.get(abstractParticle) == null){
			ArrayList<Force> forces = new ArrayList<Force>();
			forces.add(force);
			registrations.put(abstractParticle, forces);
		}
		else
			registrations.get(abstractParticle).add(force);
	}

	public void remove(AbstractParticle abstractParticle, Force force) {
		ArrayList<Force> forces = registrations.get(abstractParticle);
		if(forces!=null){
			forces.remove(force);
			Force cancelForce = new Force (-force.x, -force.y, -force.z);
			abstractParticle.addForce(cancelForce);
		}	
	}

	public void removeAllForceFrom (AbstractParticle abstractParticle) {
		ArrayList<Force> forces = registrations.get(abstractParticle);
		if(forces!=null)
			forces.clear();
		abstractParticle.clearAccumulator();
	}
	
	public void clear(){
		registrations.clear();
	}
	
	public void updateForce(AbstractParticle abstractParticle){
		ArrayList<Force> forces = registrations.get(abstractParticle);
		if(forces != null)
			for (Force force: forces)
				abstractParticle.addForce(force);
	}
	
	public void updateAllForces(){
		for (Entry<AbstractParticle, ArrayList<Force>> entry: registrations.entrySet()){
			AbstractParticle abstractParticle = entry.getKey();
			updateForce(abstractParticle);
		}
	}

}