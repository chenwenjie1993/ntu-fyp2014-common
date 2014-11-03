package sg.edu.ntu.aalhossary.fyp2014.physics_engine.junit;

import static org.junit.Assert.*;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Vector3D;

import org.junit.Before;
import org.junit.Test;

public class Vector3DTest {

	private Vector3D v1;
	private Vector3D v2;
	private Vector3D v3;
	private double epsilon = 1e-6;
	
	@Before
	public void setUp() throws Exception {
		v1 = new Vector3D (5.0, -2.0, 9.5);
		v2 = new Vector3D (10.1, 4.0, 4.5);
		v3 = new Vector3D (0, 0, 0);
	}
	
	@Test
	public void testAdd() {
		v1.add(v2);	
		assertEquals(v1.x, 15.1, epsilon);
		assertEquals(v1.y, 2.0, epsilon);
		assertEquals(v1.z, 14.0, epsilon);
	}
	
	@Test
	public void testSubtract() {
		v1.subtract(v2);	
		assertEquals(v1.x, -5.1, epsilon);
		assertEquals(v1.y, -6.0, epsilon);
		assertEquals(v1.z, 5.0, epsilon);
	}
	
	@Test
	public void testScale() {
		v1.scale(0.5);
		assertEquals(v1.x, 2.5, epsilon);
		assertEquals(v1.y, -1, epsilon);
		assertEquals(v1.z, 4.75, epsilon);
	}
	
	@Test
	public void testAddScaledVector() {
		v1.addScaledVector(v2,-1);
		assertEquals(v1.x, -5.1, epsilon);
		assertEquals(v1.y, -6.0, epsilon);
		assertEquals(v1.z, 5.0, epsilon);
	}
	
	@Test
	public void testGetComponentProduct() {
		v3 = v1.getComponentProduct(v2);
		assertEquals(v3.x, 50.5, epsilon);
		assertEquals(v3.y, -8.0, epsilon);
		assertEquals(v3.z, 42.75, epsilon);
	}

	@Test
	public void testGetCrossProduct() {
		v3 = v1.getCrossProduct(v2);
		assertEquals(v3.x, -47, epsilon);
		assertEquals(v3.y, 73.45, epsilon);
		assertEquals(v3.z, 40.2, epsilon);
	}

	@Test
	public void testGetScalarProduct() {
		double scalar_product = v1.getScalarProduct(v2);
		assertEquals(scalar_product, 85.25, epsilon);
	}
	
	@Test
	public void testGetMagnitude() {
		double mag = v1.getMagnitude();
		assertEquals(mag, 10.920164, epsilon);
	}
	
	@Test
	public void testGetUnitVector() {
		v3 = v2.getUnitVector();
		assertEquals(v3.x, 0.85896, epsilon);
		assertEquals(v3.y, 0.340182, epsilon);
		assertEquals(v3.z, 0.382705, epsilon);
	}
	
	@Test
	public void testNormalize() {
		v1.normalize();
		assertEquals(v1.x, 0.457869, epsilon);
		assertEquals(v1.y, -0.183147, epsilon);
		assertEquals(v1.z, 0.86995, epsilon);
		v3.normalize();
		assertEquals(v3.x, 0, epsilon);
		assertEquals(v3.y, 0, epsilon);
		assertEquals(v3.z, 0, epsilon);
	}
	
}
