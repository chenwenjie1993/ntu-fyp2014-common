package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

public abstract class Geometry {
	
	public static Vector3D vector(Vector3D a, Vector3D b) {
		return b.subtractAndReturn(a);
	}
		
	public static double cosTheta(Vector3D v_ji, Vector3D v_jk) {
		return (v_ji.x * v_jk.x + v_ji.y * v_jk.y + v_ji.z * v_jk.z) / v_ji.getMagnitude() / v_jk.getMagnitude();
	}
	
	public static double theta(double cos) {
		return Math.acos(cos);
	}
	
	public static double phi(Vector3D m, Vector3D n) {
		double p = theta(cosTheta(m, n));
		if (p < 0) {
			p *= -1;
		}
		return p;
	}
}
