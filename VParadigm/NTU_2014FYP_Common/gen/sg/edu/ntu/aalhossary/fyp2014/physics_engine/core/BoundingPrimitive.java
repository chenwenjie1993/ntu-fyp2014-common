package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

public abstract class BoundingPrimitive {

	protected Vector3D centre;
	public abstract boolean overlap(BoundingPrimitive other);
	
	public void updateCentre(double x, double y, double z, int metric){
		centre.x = x; centre.y =y; centre.z=z; centre.metric=metric;
	}
}