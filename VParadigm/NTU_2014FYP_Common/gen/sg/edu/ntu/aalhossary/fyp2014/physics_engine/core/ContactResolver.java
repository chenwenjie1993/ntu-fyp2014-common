package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;
import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

/**
 *  @author waiyan
 *  Resolve the Particles that have collided
 *  Update the velocities of the Particles after impact
 */
public class ContactResolver {

	/**
	 * Resolve the pair of Particle that have collided
	 * @param potentialContacts
	 */
	public void resolveContacts(ArrayList<AbstractParticle[]> potentialContacts){
		
		if(potentialContacts.isEmpty())
			return;
		
		World.activeParticles.clear();
		
		for (AbstractParticle[] potentialContact : potentialContacts){
			AbstractParticle a1 = potentialContact[0];
			AbstractParticle a2 = potentialContact[1];
			Vector3D v1 = a1.calculateVelocityChange(a2, World.COEFFICENT_OF_RESTITUTION);
			Vector3D v2 = a2.calculateVelocityChange(a1, World.COEFFICENT_OF_RESTITUTION);	
			a1.addVelocityAccumulated(v1.x, v1.y, v1.z);
			a2.addVelocityAccumulated(v2.x, v2.y, v2.z);
	//		System.out.println("Imparting velocity: " + v1.print() + "\t" + v2.print());
			
			if(!World.allParticlesActive) {
				World.markAsActive(a1);
				World.markAsActive(a2);
			}
		}		
		
		if(World.allParticlesActive) {
			for(AbstractParticle particle: World.octTree.getAllParticles())
				World.markAsActive(particle);
		}
		
		for(AbstractParticle particle: World.activeParticles){
			particle.setVelocityAccumulated();
			if(!World.LJForceActive) {
				particle.setVelocity(0, 0, 0);
			}
		}
	}
}