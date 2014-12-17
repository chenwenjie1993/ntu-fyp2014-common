package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import sg.edu.ntu.aalhossary.fyp2014.common.Vector3D;

public class BoundingBox implements BoundingPrimitive {

	private double half_size;
	private Vector3D centre;
	
	public BoundingBox(){
		half_size = 0;
		centre = new Vector3D();
	}
	
	public BoundingBox(double half_size, Vector3D centre){
		this.half_size = half_size;
		centre = new Vector3D(centre);
	}
	
	public boolean overlap(BoundingPrimitive other) {
		
		// The primitives overlap if the position difference (distance) is less than the sum of two half_sizes
		
		if(other instanceof BoundingBox) {
			BoundingBox bBox = (BoundingBox)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bBox.centre);
			double distanceSquared = temp.getSquaredMagnitude();
			return distanceSquared < (this.half_size + bBox.half_size) * (this.half_size + bBox.half_size);
		}
		
		else if(other instanceof BoundingSphere) {
			BoundingSphere bSphere = (BoundingSphere)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bSphere.getCentre());
			double distanceSquared = temp.getSquaredMagnitude();
			return distanceSquared < (this.half_size + bSphere.getRadius()) * (this.half_size + bSphere.getRadius());
		}
		
		return false;
	}
	
	public Vector3D getCentre(){
		return centre;
	}
	
	public double getHalfSize(){
		return half_size;
	}
}