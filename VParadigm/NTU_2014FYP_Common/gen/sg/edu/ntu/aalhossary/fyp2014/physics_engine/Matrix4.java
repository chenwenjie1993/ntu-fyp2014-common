package sg.edu.ntu.aalhossary.fyp2014.physics_engine;
import java.util.ArrayList;

public class Matrix4 {
	private double [] data = new double [12];
	
	public void add(Matrix4 aM) {
		throw new UnsupportedOperationException();
	}

	public void multiply(Matrix4 aM) {
		throw new UnsupportedOperationException();
	}

	public void multiply(double aScale) {
		throw new UnsupportedOperationException();
	}

	public Vector3D transform(Vector3D aV) {
		throw new UnsupportedOperationException();
	}

	public Matrix4 transform(Matrix4 aM) {
		throw new UnsupportedOperationException();
	}

	public void setInverse(Matrix4 aM) {
		throw new UnsupportedOperationException();
	}

	public double getDeterminant() {
		throw new UnsupportedOperationException();
	}

	public Matrix4 inverse() {
		throw new UnsupportedOperationException();
	}

	public void invert() {
		throw new UnsupportedOperationException();
	}

	public void setOrientationAndPos(Quaternion aQ, Vector3D aPos) {
		throw new UnsupportedOperationException();
	}

	public Vector3D transformInverse(Vector3D aV) {
		throw new UnsupportedOperationException();
	}

	public Vector3D transformDirection(Vector3D aV) {
		throw new UnsupportedOperationException();
	}

	public Vector3D transformInverseDirection(Vector3D aV) {
		throw new UnsupportedOperationException();
	}
}