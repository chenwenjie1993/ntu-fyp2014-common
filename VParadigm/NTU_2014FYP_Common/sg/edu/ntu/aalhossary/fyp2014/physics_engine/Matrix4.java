package sg.edu.ntu.aalhossary.fyp2014.physics_engine;

public class Matrix4 {

	private double[] data = new double [12];

	/**
	 * 
	 * @param aM
	 */
	public void add(Matrix4 aM) {
		// TODO - implement Matrix4.add
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aM
	 */
	public void multiply(Matrix4 aM) {
		// TODO - implement Matrix4.multiply
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aScale
	 */
	public void multiply(double aScale) {
		// TODO - implement Matrix4.multiply
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aV
	 */
	public Vector3D transform(Vector3D aV) {
		// TODO - implement Matrix4.transform
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aM
	 */
	public Matrix4 transform(Matrix4 aM) {
		// TODO - implement Matrix4.transform
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aM
	 */
	public void setInverse(Matrix4 aM) {
		// TODO - implement Matrix4.setInverse
		throw new UnsupportedOperationException();
	}

	public double getDeterminant() {
		// TODO - implement Matrix4.getDeterminant
		throw new UnsupportedOperationException();
	}

	public Matrix4 inverse() {
		// TODO - implement Matrix4.inverse
		throw new UnsupportedOperationException();
	}

	public void invert() {
		// TODO - implement Matrix4.invert
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aQ
	 * @param aPos
	 */
	public void setOrientationAndPos(Quaternion aQ, Vector3D aPos) {
		// TODO - implement Matrix4.setOrientationAndPos
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aV
	 */
	public Vector3D transformInverse(Vector3D aV) {
		// TODO - implement Matrix4.transformInverse
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aV
	 */
	public Vector3D transformDirection(Vector3D aV) {
		// TODO - implement Matrix4.transformDirection
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aV
	 */
	public Vector3D transformInverseDirection(Vector3D aV) {
		// TODO - implement Matrix4.transformInverseDirection
		throw new UnsupportedOperationException();
	}

}