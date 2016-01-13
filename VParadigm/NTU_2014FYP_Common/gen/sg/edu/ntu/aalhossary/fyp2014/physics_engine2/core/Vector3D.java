package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

public class Vector3D extends sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Vector3D {
	public Vector3D() {
		super();
	}
	
	public Vector3D(double x, double y, double z) {
		super(x, y, z);
	}
	
	public String toString() {
		return "(" + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + ")";
	}
}
