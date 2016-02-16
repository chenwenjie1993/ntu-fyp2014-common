package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public class Angle extends BondedInteraction {
	public double th0, cth;
	protected Vector3D v_ji, v_jk;
	protected double theta, cosTheta, cosThetaSqr;
	
	public Angle(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<String> query = new ArrayList<String>();
		for (Atom atom : atoms) {
			query.add(atom.type);
		}
		List<Double> params = db.getAngleParams(query);
		th0 = params.get(0);
		cth = params.get(1);
		
		v_ji = atoms.get(0).getPosition().subtractAndReturn(atoms.get(1).getPosition());
		v_jk = atoms.get(2).getPosition().subtractAndReturn(atoms.get(1).getPosition());
		cosTheta = Geometry.cosTheta(v_ji, v_jk);
		cosThetaSqr = cosTheta * cosTheta;
		theta = Math.acos(cosTheta);
	}
	
	@Override
	public void updatePotentialEnergy() {
		v_ji = atoms.get(0).getPosition().subtractAndReturn(atoms.get(1).getPosition());
		v_jk = atoms.get(2).getPosition().subtractAndReturn(atoms.get(1).getPosition());
		cosTheta = Geometry.cosTheta(v_ji, v_jk);
		cosThetaSqr = cosTheta * cosTheta;
		theta = Math.acos(cosTheta);
		
		double dVdt = - cth * (theta - th0);
		
//		Vector3D energy = new Vector3D();
		Vector3D force_i = new Vector3D();
		Vector3D force_j = new Vector3D();
		Vector3D force_k = new Vector3D();
		
//		int  m;
        double st, sth;
        double cik, cii, ckk;
        double nrkj2, nrij2;
        double nrkj_1, nrij_1;
//        double f_i, f_j, f_k;
		
        st  = dVdt / Math.sqrt(1 - cosThetaSqr);
        sth = st * cosThetaSqr;
        
        nrij2 = v_ji.getMagnitude();
        nrkj2 = v_jk.getMagnitude();

        nrij_1 = 1 / Math.sqrt(nrij2);
        nrkj_1 = 1 / Math.sqrt(nrkj2);

        cik = st*nrij_1*nrkj_1;
        cii = sth*nrij_1*nrij_1;
        ckk = sth*nrkj_1*nrkj_1;
        
        force_i.x = - (cik * v_jk.x - cii * v_ji.x);
        force_i.y = - (cik * v_jk.y - cii * v_ji.y);
        force_i.z = - (cik * v_jk.z - cii * v_ji.z);
        
        force_k.x = - (cik * v_ji.x - cii * v_jk.x);
        force_k.y = - (cik * v_ji.y - cii * v_jk.y);
        force_k.z = - (cik * v_ji.z - cii * v_jk.z);
        
        force_j = force_i.getNegativeVector().addAndReturn(force_k.getNegativeVector());
        
//        atoms.get(0).addForce(force_i.getNegativeVector());
//        atoms.get(1).addForce(force_j.getNegativeVector());
//        atoms.get(2).addForce(force_k.getNegativeVector());
        
//        for (m = 0; m < DIM; m++)
//        {           
//            f_i[m]    = -(cik*r_kj[m] - cii*r_ij[m]);
//            f_k[m]    = -(cik*r_ij[m] - ckk*r_kj[m]);
//            f_j[m]    = -f_i[m] - f_k[m];
//            f[ai][m] += f_i[m];
//            f[aj][m] += f_j[m];
//            f[ak][m] += f_k[m];
//        }
	}
	
}
