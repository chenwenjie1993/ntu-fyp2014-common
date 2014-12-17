package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

public interface BoundingPrimitive {

	public boolean overlap(BoundingPrimitive other);
	public void updateCentre(double x, double y, double z);
}