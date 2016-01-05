package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.typology_db;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class TypologyDatabaseTest {
	private static TypologyDatabase db;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = TypologyDatabase.getInstance();
	}
	
//	@Test
//	public void testVdParams() {
//		List<Double> H0 = new ArrayList<Double>();
//		List<Double> Cs = new ArrayList<Double>();
//		List<Double> OW_spc = new ArrayList<Double>();
//		List<Double> Zn = new ArrayList<Double>();
//		
//		H0.add(2.47135e-01);
//		H0.add(6.56888e-02);
//		
//		Cs.add(6.04920e-01);
//		Cs.add(3.37230e-04);
//		
//		OW_spc.add(3.16557e-01);
//		OW_spc.add(6.50629e-01);
//
//		Zn.add(1.95998e-01);
//		Zn.add(5.23000e-02);
//		
//		assertEquals(H0, db.getVdParams("H0"));
//		assertEquals(Cs, db.getVdParams("Cs"));
//		assertEquals(OW_spc, db.getVdParams("OW_spc"));
//		assertEquals(Zn, db.getVdParams("Zn"));
//	}
	
	@Test
	public void testBondParams() {		
		List<String> CT_H0_key = new ArrayList<String>();
		List<String> CT_F_key = new ArrayList<String>();
		List<Double> CT_H0_value = new ArrayList<Double>();
		List<Double> CT_F_value = new ArrayList<Double>();
		
		CT_H0_key.add("CT");
		CT_H0_key.add("H0");
		
		CT_F_key.add("CT");
		CT_F_key.add("F");
		
		CT_H0_value.add(0.10900);
		CT_H0_value.add(284512.0);
		
		CT_F_value.add(0.13800);
		CT_F_value.add(307105.6);

		assertEquals(CT_H0_value, db.getBondParams(CT_H0_key));
		assertEquals(CT_F_value, db.getBondParams(CT_F_key));
	}
	
	@Test
	public void testAngleParams() {
		List<String> H0_CT_H0_key = new ArrayList<String>();
		List<String> N_C_N_key = new ArrayList<String>();
		List<Double> H0_CT_H0_value = new ArrayList<Double>();
		List<Double> N_C_N_value = new ArrayList<Double>();
		
		H0_CT_H0_key.add("H0");
		H0_CT_H0_key.add("CT");
		H0_CT_H0_key.add("H0");
		
		N_C_N_key.add("N");
		N_C_N_key.add("C");
		N_C_N_key.add("N");
		
		H0_CT_H0_value.add(109.500);
		H0_CT_H0_value.add(292.880);
		
		N_C_N_value.add(120.000);
		N_C_N_value.add(585.760);

		assertEquals(H0_CT_H0_value, db.getAngleParams(H0_CT_H0_key));
		assertEquals(N_C_N_value, db.getAngleParams(N_C_N_key));
	}
	
	@Test
	public void testImproperDihedralParams() {
		List<String> X_O2_C_O2_key = new ArrayList<String>();
		List<String> X_X_CA_H5_key = new ArrayList<String>();
		List<Double> X_O2_C_O2_value = new ArrayList<Double>();
		List<Double> X_X_CA_H5_value = new ArrayList<Double>();
		
		X_O2_C_O2_key.add("H0");
		X_O2_C_O2_key.add("O2");
		X_O2_C_O2_key.add("C");
		X_O2_C_O2_key.add("O2");
		
		X_X_CA_H5_key.add("H0");
		X_X_CA_H5_key.add("H0");
		X_X_CA_H5_key.add("CA");
		X_X_CA_H5_key.add("H5");
		
		X_O2_C_O2_value.add(180.000);
		X_O2_C_O2_value.add(43.93200);
		
		X_X_CA_H5_value.add(180.000);
		X_X_CA_H5_value.add(4.60240);

		assertEquals(X_O2_C_O2_value, db.getImproperDihedralParams(X_O2_C_O2_key));
		assertEquals(X_X_CA_H5_value, db.getImproperDihedralParams(X_X_CA_H5_key));
	}
	
	@Test
	public void testProperDihedralParams() {
		List<String> X_C_C_X_key = new ArrayList<String>();
		List<Double> X_C_C_X_value = new ArrayList<Double>();
		
		X_C_C_X_key.add("H0");
		X_C_C_X_key.add("C");
		X_C_C_X_key.add("C");
		X_C_C_X_key.add("O2");
		
		X_C_C_X_value.add(180.000);
		X_C_C_X_value.add(15.16700);
		X_C_C_X_value.add(2.0);

		assertEquals(X_C_C_X_value, db.getProperDihedralParams(X_C_C_X_key));
	}

}
