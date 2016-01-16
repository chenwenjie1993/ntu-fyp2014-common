package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import sg.edu.ntu.aalhossary.fyp2014.common.BoundingPrimitive;
import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

/**
 * @author waiyan
 * BoundingBox is to be used for residues
 */
public class BoundingBox extends BoundingPrimitive {

	private double x_length;
	private double y_length;
	private double z_length;
	
	public BoundingBox(double x, double y, double z, Vector3D centre){
		x_length = x;
		y_length = y;
		z_length = z;
		super.centre = new Vector3D(centre);
	}

	/**
	 * Determines if two BoundingPrimitives are overlapping
	 */
	public boolean overlap(BoundingPrimitive other) {
		
		// The primitives overlap if the position difference (distance) is less than the sum of two half_sizes
		
		if(other instanceof BoundingCube) {
			BoundingCube bCube = (BoundingCube)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bCube.getCentre());
			double distanceSquared = temp.getSquaredMagnitude();
			boolean x_con = distanceSquared <= Math.pow(this.x_length + bCube.getHalfSize(), 2);
			boolean y_con = distanceSquared <= Math.pow(this.y_length + bCube.getHalfSize(), 2);
			boolean z_con = distanceSquared <= Math.pow(this.z_length + bCube.getHalfSize(), 2);
			return x_con || y_con || z_con;
		}
		
		else if(other instanceof BoundingSphere) {
			BoundingSphere bSphere = (BoundingSphere)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bSphere.getCentre());
			double distanceSquared = temp.getSquaredMagnitude();
			boolean x_con = distanceSquared <= Math.pow(this.x_length + bSphere.getRadius(), 2);
			boolean y_con = distanceSquared <= Math.pow(this.y_length + bSphere.getRadius(), 2);
			boolean z_con = distanceSquared <= Math.pow(this.z_length + bSphere.getRadius(), 2);
			return x_con || y_con || z_con;
		}
		
		else if(other instanceof BoundingBox){
			BoundingBox bBox = (BoundingBox)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bBox.getCentre());
			double distanceSquared = temp.getSquaredMagnitude();
			boolean x_con = distanceSquared <= Math.pow(this.x_length + bBox.getXLength(), 2);
			boolean y_con = distanceSquared <= Math.pow(this.y_length + bBox.getYLength(), 2);
			boolean z_con = distanceSquared <= Math.pow(this.z_length + bBox.getZLength(), 2);
			return x_con || y_con || z_con;
		}
		
		return false;
	}
	
	public Vector3D getCentre(){
		return centre;
	}
	
	public double getXLength(){
		return x_length;
	}
	
	public double getYLength(){
		return y_length;
	}
	
	public double getZLength(){
		return z_length;
	}
}