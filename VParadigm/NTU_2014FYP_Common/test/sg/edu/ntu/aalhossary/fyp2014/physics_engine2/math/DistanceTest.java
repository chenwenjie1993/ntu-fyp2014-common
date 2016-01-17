package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math;

import static org.junit.Assert.*;

import org.junit.Test;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

public class DistanceTest {

	@Test
	public void testDistance3D() {
		Vector3D a = new Vector3D(0, 0, 0);
		Vector3D b = new Vector3D(1, 2, 3);
		assertEquals(new Vector3D(1, 2, 3), Distance.distance3D(a, b));
	}

}
