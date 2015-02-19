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
			Vector3D v1 = a1.calculateVelocityChange(a2, World.COEFFICENT_OF_RESTITUTION);
			Vector3D v2 = a2.calculateVelocityChange(a1, World.COEFFICENT_OF_RESTITUTION);	
			a1.addVelocityAccumulated(v1.x, v1.y, v1.z);
			a2.addVelocityAccumulated(v2.x, v2.y, v2.z);
			System.out.println("Imparting velocity: " + v1.print() + "\t" + v2.print());
			World.markAsActive(a1);
			World.markAsActive(a2);
		}
		
		for(AbstractParticle particle: World.activeParticles){
			particle.setVelocityAccumulated();
		}
	}
}