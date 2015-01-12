package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;
import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

public class NarrowCollisionDetector {
	
	public void detectCollision() {
		ArrayList<AbstractParticle> particles = World.activeParticles;
		World.potentialContacts.clear();
		
		for (int i=0; i< particles.size(); i++){
			AbstractParticle a = particles.get(i);
			ArrayList <AbstractParticle> neighbouringParticles = new ArrayList<>();
			World.octTree.retrieve(neighbouringParticles, a);
			filterNeighbouringParticles(neighbouringParticles, particles, i);
			
		
			for (AbstractParticle b: neighbouringParticles){
	
				double diff_x = a.getPosition().x - b.getPosition().x;
				double diff_y = a.getPosition().y - b.getPosition().y;
				double diff_z = a.getPosition().z - b.getPosition().z;
				double min_dist = Math.min(Math.abs(diff_x), Math.min(Math.abs(diff_y), Math.abs(diff_z)));
				
				double r1 = ((BoundingSphere)(a.getBoundingPrimitive())).getRadius();
				double r2 = ((BoundingSphere)(b.getBoundingPrimitive())).getRadius();
				//bond_length = r1+r2;//r1+r2;
				
				double bond_length = 0.28E-9;
				if(min_dist <= bond_length){
					System.out.println("The two particles collided." + min_dist);
					AbstractParticle [] potentialContact = {a,b};
					World.potentialContacts.add(potentialContact);
//					if(diff_x<0) {
//						diff_x = 0.5* (bond_length + diff_x);
//						a.movePositionBy(-diff_x, 0, 0, a.getPosition().metric);
//						b.movePositionBy(diff_x, 0, 0, b.getPosition().metric);
//					}
//					else {
//						diff_x = 0.5* (bond_length - diff_x);
//						a.movePositionBy(diff_x, 0, 0, a.getPosition().metric);
//						b.movePositionBy(-diff_x, 0, 0, b.getPosition().metric);
//					}
//					if(diff_y<0){
//						diff_y = 0.5* (bond_length + diff_y);
//						a.movePositionBy(0, -diff_y, 0, a.getPosition().metric);
//						b.movePositionBy(0, diff_y, 0, b.getPosition().metric);
//					}
//					else {
//						diff_y = 0.5* (bond_length - diff_y);
//						a.movePositionBy(0, diff_y, 0, a.getPosition().metric);
//						b.movePositionBy(0, -diff_y, 0, b.getPosition().metric);
//					}
//					if(diff_z<0){
//						diff_z = 0.5* (bond_length + diff_z);
//						a.movePositionBy(0, 0, -diff_z, a.getPosition().metric);
//						b.movePositionBy(0, 0, diff_z, b.getPosition().metric);
//					}
//					else {
//						diff_z = 0.5* (bond_length - diff_z);
//						a.movePositionBy(0, 0, diff_z, a.getPosition().metric);
//						b.movePositionBy(0, 0, -diff_z, b.getPosition().metric);
//					}
//					
//					
//					diff_x = a.getPosition().x - b.getPosition().x;
//					diff_y = a.getPosition().y - b.getPosition().y;
//					diff_z = a.getPosition().z - b.getPosition().z;
//					min_dist = Math.min(Math.abs(diff_x), Math.min(Math.abs(diff_y), Math.abs(diff_z)));
//					
//					System.out.println("CORRECTED: " + min_dist);
//				
				}
			}
		}
	}
	
	private void filterNeighbouringParticles (ArrayList<AbstractParticle> neighbouringParticles, ArrayList<AbstractParticle> processedParticles, int index){
		for (int j=0; j< index; j++){
			for(AbstractParticle a: neighbouringParticles){
				if(a.getPosition() == processedParticles.get(j).getPosition()){
					neighbouringParticles.remove(a);
					break;
				}
			}
		}
	}

}