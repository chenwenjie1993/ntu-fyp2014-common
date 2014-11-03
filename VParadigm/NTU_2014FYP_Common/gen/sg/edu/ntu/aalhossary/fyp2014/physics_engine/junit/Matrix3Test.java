package sg.edu.ntu.aalhossary.fyp2014.physics_engine.junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Matrix3;

public class Matrix3Test {

	private Matrix3 m1;
	private Matrix3 m2;
	private Matrix3 m3;
	private double epsilon = 1e-8;
	
	@Before
	public void setUp() throws Exception {
		m1 = new Matrix3(9,3,5,-6,-9,7,-1,-8,1);
		m2 = new Matrix3(1,2,3,3,2,1,2,1,3);
		m3 = new Matrix3(4,5,6,6,5,4,4,6,5);
	}

	@Test
	public void testAdd() {
		m1.add(m2);	
		Matrix3 expected = new Matrix3(10,5,8,-3,-7,8,1,-7,4);
		assertEquals("The matrices do not match.",expected,m1);
	}
	
	@Test
	public void testMultiply(){
		m1 = m2.multiplyAndReturn(m3);
		Matrix3 expected = new Matrix3(28,33,29,28,31,31,26,33,31);
		assertEquals("The matrices do not match.",expected,m1);
	}
	
	@Test
	public void testTranspose(){
		m1 = m1.transpose();
		Matrix3 expected = new Matrix3(9,-6,-1,3,-9,-8,5,7,1);
		assertEquals("The matrices do not match.",expected,m1);
	}
	
	@Test
	public void testScalarMultiply(){
		m1.scale(0.5);
		Matrix3 expected = new Matrix3(4.5,1.5,2.5,-3,-4.5,3.5,-0.5,-4,0.5);
		assertEquals("The matrices do not match.",expected,m1);
	}
	
	//@Test
	public void testInvert(){
		m1.invert();
		Matrix3 expected = new Matrix3(47,-43,66,-1,14,-93,39,69,-63);
		expected = expected.scaleAndReturn(1.0/615);
		assertEquals("The matrices do not match.",expected,m1);
		
		Matrix3 zeroMatrix = new Matrix3();
		zeroMatrix.invert();
		assertEquals("The matrices do not match", zeroMatrix, zeroMatrix);
	}
	
	@Test
	public void testInverse(){
		m2 = m1.inverse();
		Matrix3 expected = new Matrix3(47,-43,66,-1,14,-93,39,69,-63);
		expected = expected.scaleAndReturn(1.0/615);
		assertEquals("The matrices do not match.",expected,m2);
	}

}
