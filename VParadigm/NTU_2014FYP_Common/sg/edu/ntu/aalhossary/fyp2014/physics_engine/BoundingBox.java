package sg.edu.ntu.aalhossary.fyp2014.physics_engine;

public class BoundingBox implements BoundingPrimitive {

	private double half_size;
	private Vector3D centre;

	public Vector3D getCentre() {
		return this.centre;
	}

	public BoundingBox() {
		// TODO - implement BoundingBox.BoundingBox
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param half_size
	 * @param centre
	 */
	public BoundingBox(double half_size, Vector3D centre) {
		// TODO - implement BoundingBox.BoundingBox
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param other
	 */
	public boolean overlap(BoundingPrimitive other) {
		// TODO - implement BoundingBox.overlap
		throw new UnsupportedOperationException();
	}

	public double getHalfSize() {
		// TODO - implement BoundingBox.getHalfSize
		throw new UnsupportedOperationException();
	}

}