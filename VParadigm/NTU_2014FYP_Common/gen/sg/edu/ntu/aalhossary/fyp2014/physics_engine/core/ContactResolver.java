package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;
import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

public class ContactResolver {

	public void resolveContacts(ArrayList<AbstractParticle[]> potentialContacts){
		for (AbstractParticle[] potentialContact : potentialContacts){
			AbstractParticle a1 = potentialContact[0];
			AbstractParticle a2 = potentialContact[1];
			Vector3D v1 = a1.calculateVelocityChange(a2.getMass(), a2.getVelocity());
			Vector3D v2 = a2.calculateVelocityChange(a1.getMass(), a1.getVelocity());
			a1.setVelocity(v1.x, v1.y, v1.z);
			a2.setVelocity(v2.x, v2.y, v2.z);
			System.out.println("Imparting velocity: " + v1.print() + "\t" + v2.print());
		}
	}
}