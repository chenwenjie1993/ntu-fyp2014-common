package sg.edu.ntu.aalhossary.fyp2014.physics_engine;

public interface BoundingPrimitive {

	/**
	 * 
	 * @param other
	 */
	boolean overlap(BoundingPrimitive other);

}