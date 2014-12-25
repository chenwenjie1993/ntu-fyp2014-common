package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;


// Bounding Sphere is to be used for atoms and molecules
public class BoundingSphere extends BoundingPrimitive {

	private double radius;
	
	public BoundingSphere(double radius, Vector3D centre){
		this.radius = radius;
		super.centre = new Vector3D(centre);
	}
	
	public boolean overlap(BoundingPrimitive other) {
		
		// The primitives overlap if the position difference (distance) is less than the sum of two half_sizes
	
		if(other instanceof BoundingCube) {
			BoundingCube bCube = (BoundingCube)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bCube.getCentre());
			double distanceSquared = temp.getSquaredMagnitude();
			return distanceSquared <= (this.radius + bCube.getHalfSize()) * (this.radius + bCube.getHalfSize());
		}
		
		else if(other instanceof BoundingSphere) {
			BoundingSphere bSphere = (BoundingSphere)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bSphere.getCentre());
			double distanceSquared = temp.getSquaredMagnitude();
			return distanceSquared <= (this.radius + bSphere.getRadius()) * (this.radius + bSphere.getRadius());
		}
		
		else if(other instanceof BoundingBox){
			BoundingBox bBox = (BoundingBox)other; 
			Vector3D temp = new Vector3D(centre);
			temp.subtract(bBox.getCentre());
			double distanceSquared = temp.getSquaredMagnitude();
			boolean x_con = distanceSquared <= Math.pow(this.radius + bBox.getXLength(), 2);
			boolean y_con = distanceSquared <= Math.pow(this.radius + bBox.getYLength(), 2);
			boolean z_con = distanceSquared <= Math.pow(this.radius + bBox.getZLength(), 2);
			return x_con || y_con || z_con;
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