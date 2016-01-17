package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

public class Distance {
	//TODO: Is this small enough?
	private static double epsilon = 1e-18;
	
	public static Vector3D distance3D(Vector3D a, Vector3D b) {
		Vector3D diff = a.subtractAndReturn(b);
		if (diff.x < -epsilon) {
			diff.x = -diff.x;
		}
		if (diff.y < -epsilon) {
			diff.y = -diff.y;
		}
		if (diff.z < -epsilon) {
			diff.z = -diff.z;
		}
		return diff;
	}
}
