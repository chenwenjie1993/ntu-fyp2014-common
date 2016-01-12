package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;
import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

/**
 * @author waiyan
 * Narrow Phase Collision Detector determines which particles are about to be collided
 */
public class NarrowCollisionDetector {
	
	/**
	 * Detect which particles are about to be collided 
	 */
	public void detectCollision(OctTree octTree, ArrayList<AbstractParticle> activeParticles, ArrayList<AbstractParticle[]> potentialContacts) {
		
		potentialContacts.clear();
		ArrayList<Integer> processedParticles = new ArrayList<>();
		
		for (int i=0; i< activeParticles.size(); i++){
			
			AbstractParticle a = activeParticles.get(i);
			processedParticles.add(a.getGUID());
			
			ArrayList <AbstractParticle> neighbouringParticles = octTree.retrieve(a);
			ArrayList<AbstractParticle> filteredParticles = filterNeighbouringParticles(neighbouringParticles, processedParticles);
			
			for (AbstractParticle b: filteredParticles){
	
				double scale1 = Math.pow(10, a.getPosition().metric);
				double scale2 = Math.pow(10, b.getPosition().metric);
				double diff_x = a.getPosition().x * scale1 - b.getPosition().x * scale2;
				double diff_y = a.getPosition().y * scale1 - b.getPosition().y * scale2;
				double diff_z = a.getPosition().z * scale1 - b.getPosition().z * scale2;
				
				double min_dist = Math.sqrt(diff_x*diff_x + diff_y*diff_y + diff_z*diff_z);
	//			double min_dist = Math.min(Math.abs(diff_x), Math.min(Math.abs(diff_y), Math.abs(diff_z)));
				
				double r1 = ((BoundingSphere)(a.getBoundingPrimitive())).getRadius();
				double r2 = ((BoundingSphere)(b.getBoundingPrimitive())).getRadius();
				
	//			double bond_length = 0.28E-9;
				double bond_length = r1+r2;
				
				if(min_dist <= bond_length){
				
		//			System.out.println("The two particles collided: " + a.getGUID() +" and " + b.getGUID());
					AbstractParticle [] potentialContact = {a,b};
					potentialContacts.add(potentialContact);
				}
			}
		}
	}
	
	/**
	 * Filter processed particles from the neighbouring particles
	 * @param neighbouringParticles
	 * @param activeParticles
	 * @param index
	 */
	private ArrayList<AbstractParticle> filterNeighbouringParticles (ArrayList<AbstractParticle> neighbouringParticles,  ArrayList<Integer> processedParticles){
        ArrayList<AbstractParticle> filteredParticles = new ArrayList<>();
        for(AbstractParticle a: neighbouringParticles){
        	if(!processedParticles.contains(a.getGUID()))
        		filteredParticles.add(a);
         }
        return filteredParticles;
	}
}