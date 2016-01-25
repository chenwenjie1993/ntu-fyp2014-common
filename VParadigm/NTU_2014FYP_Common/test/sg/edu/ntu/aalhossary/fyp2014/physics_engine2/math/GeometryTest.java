package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math;

import static org.junit.Assert.*;

import org.junit.Test;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

public class GeometryTest {

	@Test
	public void testVector() {
		Vector3D a = new Vector3D(0, 0, 0);
		Vector3D b = new Vector3D(1, 2, 3);
		assertEquals(new Vector3D(1, 2, 3), Geometry.vector(a, b));
	}
	
	@Test
	public void testTheta() {
		Vector3D a = new Vector3D(1, 0, 0);
		Vector3D b = new Vector3D(0, 1, 0);
		Vector3D c = new Vector3D(1, 1, 0);
		assertEquals(90.0/180*Math.PI, Geometry.theta(Geometry.cosTheta(a, b)), 1e-6);
		assertEquals(45.0/180*Math.PI, Geometry.theta(Geometry.cosTheta(a, c)), 1e-6);
	}
	
	@Test 
	public void testPhi() {
		Vector3D a = new Vector3D(1, 0, 0);
		Vector3D b = new Vector3D(0, 1, 0);
		Vector3D c = new Vector3D(0, 0, -1);
		Vector3D d = new Vector3D(1, 0, -1);
		
		Vector3D m = a.getCrossProduct(b);
		Vector3D n1 = c.getCrossProduct(b);
		Vector3D n2 = d.getCrossProduct(b);
		
		assertEquals(90.0/180*Math.PI, Geometry.phi(m, n1), 1e-6);
		assertEquals(45.0/180*Math.PI, Geometry.phi(m, n2), 1e-6);
	}

}
