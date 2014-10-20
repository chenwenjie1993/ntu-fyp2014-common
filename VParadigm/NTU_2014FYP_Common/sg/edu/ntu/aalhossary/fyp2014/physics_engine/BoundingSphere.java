package sg.edu.ntu.aalhossary.fyp2014.physics_engine;

public class BoundingSphere implements BoundingPrimitive {

	private double radius;
	private Vector3D centre;

	public double getRadius() {
		return this.radius;
	}

	public Vector3D getCentre() {
		return this.centre;
	}

	public BoundingSphere() {
		// TODO - implement BoundingSphere.BoundingSphere
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param radius
	 * @param centre
	 */
	public BoundingSphere(double radius, Vector3D centre) {
		// TODO - implement BoundingSphere.BoundingSphere
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param other
	 */
	public boolean overlap(BoundingPrimitive other) {
		// TODO - implement BoundingSphere.overlap
		throw new UnsupportedOperationException();
	}

}