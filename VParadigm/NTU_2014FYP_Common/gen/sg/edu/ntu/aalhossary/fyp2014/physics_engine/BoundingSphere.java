package sg.edu.ntu.aalhossary.fyp2014.physics_engine;
public class BoundingSphere implements BoundingPrimitive {

	private double radius;
	private Vector3D centre;
	
	public BoundingSphere(){
		radius = 0;
		centre = new Vector3D();
	}
	
	public BoundingSphere(double radius, Vector3D centre){
		this.radius = radius;
		this.centre = new Vector3D(centre);
	}
	
	public boolean overlap(BoundingPrimitive other) {
		
		// The primitives overlap if the position difference (distance) is less than the sum of two half_sizes
		
		if(other instanceof BoundingBox) {
			BoundingBox bBox = (BoundingBox)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bBox.getCentre());
			double distanceSquared = temp.getSquaredMagnitude();
			return distanceSquared < (this.radius + bBox.getHalfSize()) * (this.radius + bBox.getHalfSize());
		}
		
		else if(other instanceof BoundingSphere) {
			BoundingSphere bSphere = (BoundingSphere)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bSphere.getCentre());
			double distanceSquared = temp.getSquaredMagnitude();
			return distanceSquared < (this.radius + bSphere.getRadius()) * (this.radius + bSphere.getRadius());
		}
		
		return false;
	}
	
	public Vector3D getCentre(){
		return centre;
	}
	
	public double getRadius(){
		return radius;
	}
}