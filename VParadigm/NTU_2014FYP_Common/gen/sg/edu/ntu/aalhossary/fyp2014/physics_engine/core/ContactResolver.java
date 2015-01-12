package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;
import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

public class ContactResolver {

	public void resolveContacts(ArrayList<AbstractParticle[]> potentialContacts){
		
		if(potentialContacts.isEmpty())
			return;
		
		World.activeParticles.clear();
		World.oldPositions.clear();
		
		for (AbstractParticle[] potentialContact : potentialContacts){
			AbstractParticle a1 = potentialContact[0];
			AbstractParticle a2 = potentialContact[1];
			Vector3D v1 = a1.calculateVelocityChange(a2.getMass(), a2.getVelocity(), World.COEFFICENT_OF_RESTITUTION);
			Vector3D v2 = a2.calculateVelocityChange(a1.getMass(), a1.getVelocity(), World.COEFFICENT_OF_RESTITUTION);
			
//			double diff_x = a1.getPosition().x - a2.getPosition().x;
//			double diff_y = a1.getPosition().y - a2.getPosition().y;
//			double diff_z = a1.getPosition().z - a2.getPosition().z;
//			
//			// absolute values
//			double cap_x = diff_x / World.time_metric;
//			double cap_y = diff_y / World.time_metric;
//			double cap_z = diff_z / World.time_metric;
//			
//			cap_x = cap_y = cap_z = 3000;
//			
//			System.out.println("original Imparting velocity: " + v1.print() + "\t" + v2.print());
//			
//			v1.x = limitVelocity (v1.x, cap_x);
//			v1.y = limitVelocity (v1.y, cap_y);
//			v1.z = limitVelocity (v1.z, cap_z);
//			v2.x = limitVelocity (v2.x, cap_x);
//			v2.y = limitVelocity (v2.y, cap_y);
//			v2.z = limitVelocity (v2.z, cap_z);
//				
			
			a1.setVelocity(v1.x, v1.y, v1.z);
			a2.setVelocity(v2.x, v2.y, v2.z);
			System.out.println("Imparting velocity: " + v1.print() + "\t" + v2.print());
			World.markAsActive(a1);
			World.markAsActive(a2);
		}
	}
	
	private double limitVelocity (double original, double limit){
		if(original<0) {
			double temp = Math.min(limit, -original);
			return -temp;
		}
		return Math.min(original, limit);	
	}
}