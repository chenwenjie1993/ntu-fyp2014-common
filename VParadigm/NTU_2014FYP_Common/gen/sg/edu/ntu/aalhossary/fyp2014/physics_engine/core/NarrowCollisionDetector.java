package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;
import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

public class NarrowCollisionDetector {
	
	public void detectCollision(ArrayList<AbstractParticle> particles) {
		World.potentialContacts.clear();
		
		for (int i=0; i<particles.size(); i++){
			AbstractParticle a = particles.get(i);
			for (int j=i+1; j<particles.size(); j++){
				AbstractParticle b = particles.get(j);
				double bond_length = 0.28E-9;
				double diff_x = a.getPosition().x - b.getPosition().x;
				double diff_y = a.getPosition().y - b.getPosition().y;
				double diff_z = a.getPosition().z - b.getPosition().z;
				double min_dist = Math.min(Math.abs(diff_x), Math.min(Math.abs(diff_y), Math.abs(diff_z)));
				
				if(min_dist <= bond_length){
					System.out.println("The two particles collided.");
					AbstractParticle [] potentialContact = {a,b};
					World.potentialContacts.add(potentialContact);
				}
			}
		}
		/*
		for (AbstractParticle a: particles){
			for (AbstractParticle b: particles){
				if(a == b)
					continue;
				else {
					double bond_length = 0.28E-9;
					double diff_x = a.getPosition().x - b.getPosition().x;
					double diff_y = a.getPosition().y - b.getPosition().y;
					double diff_z = a.getPosition().z - b.getPosition().z;
					double min_dist = Math.min(Math.abs(diff_x), Math.min(Math.abs(diff_y), Math.abs(diff_z)));
					
					if(min_dist <= bond_length){
						System.out.println("The two particles collided.");
						AbstractParticle [] potentialContact = {a,b};
						World.potentialContacts.add(potentialContact);
					}
				}
			}
		}
		*/
	}
}