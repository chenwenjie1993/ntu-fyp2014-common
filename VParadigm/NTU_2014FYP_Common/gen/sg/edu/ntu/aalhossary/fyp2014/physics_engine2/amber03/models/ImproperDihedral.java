package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public class ImproperDihedral extends Dihedral {
	public double phi0, kd;
	protected Vector3D v_ji, v_jk, v_lk;
	protected double phi;

	public ImproperDihedral(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<String> query = new ArrayList<String>();
		for (Atom atom : atoms) {
			query.add(atom.type);
		}
		List<Double> params = db.getImproperDihedralParams(query);
		phi0 = params.get(0);
		kd = params.get(1);
	}

	@Override
	public void updatePotentialEnergy() {
		v_ji = atoms.get(0).getPosition().subtractAndReturn(atoms.get(1).getPosition());
		v_jk = atoms.get(2).getPosition().subtractAndReturn(atoms.get(1).getPosition());
		v_lk = atoms.get(2).getPosition().subtractAndReturn(atoms.get(3).getPosition());
		
		
		
		
		Vector3D f_i, f_j, f_k, f_l;
	    Vector3D uvec, vvec, svec, dx_jl;
	    double iprm, iprn, nrkj, nrkj2, nrkj_1, nrkj_2;
	    double a, b, p, q, toler;
	    Vector3D jt, dt_ij, dt_kj, dt_lj;

	    Vector3D m = v_ji.getCrossProduct(v_jk);
	    Vector3D n = v_jk.getCrossProduct(v_lk);
	    
	    phi = Geometry.phi(m, n);
		double dp = phi - phi0;
//		System.out.println(dp);
		double dVdphi = - kd * dp;
	    
//	    iprm  = m.getSquaredMagnitude();       /*  5    */
//	    iprn  = n.getSquaredMagnitude();       /*  5	*/
//	    nrkj2 = v_jk.getSquaredMagnitude(); /*  5	*/
//	    toler = nrkj2*GMX_REAL_EPS;
//	    if ((iprm > toler) && (iprn > toler))
//	    {
//	        nrkj_1 = 1/Math.sqrt(nrkj2); /* 10	*/
//	        nrkj_2 = nrkj_1*nrkj_1;      /*  1	*/
//	        nrkj   = nrkj2*nrkj_1;       /*  1	*/
//	        a      = -dVdphi*nrkj/iprm;   /* 11	*/
//	        svmul(a, m, f_i);            /*  3	*/
//	        b     = dVdphi*nrkj/iprn;     /* 11	*/
//	        svmul(b, n, f_l);            /*  3  */
//	        p     = v_ji.getScalarProduct(v_jk);   /*  5	*/
//	        p    *= nrkj_2;              /*  1	*/
//	        q     = v_lk.getScalarProduct(v_jk);   /*  5	*/
//	        q    *= nrkj_2;              /*  1	*/
//	        svmul(p, f_i, uvec);         /*  3	*/
//	        svmul(q, f_l, vvec);         /*  3	*/
//	        rvec_sub(uvec, vvec, svec);  /*  3	*/
//	        rvec_sub(f_i, svec, f_j);    /*  3	*/
//	        rvec_add(f_l, svec, f_k);    /*  3	*/
//	        rvec_inc(f[i], f_i);         /*  3	*/
//	        rvec_dec(f[j], f_j);         /*  3	*/
//	        rvec_dec(f[k], f_k);         /*  3	*/
//	        rvec_inc(f[l], f_l);         /*  3	*/
//
//	        if (g)
//	        {
//	            copy_ivec(SHIFT_IVEC(g, j), jt);
//	            ivec_sub(SHIFT_IVEC(g, i), jt, dt_ij);
//	            ivec_sub(SHIFT_IVEC(g, k), jt, dt_kj);
//	            ivec_sub(SHIFT_IVEC(g, l), jt, dt_lj);
//	            t1 = IVEC2IS(dt_ij);
//	            t2 = IVEC2IS(dt_kj);
//	            t3 = IVEC2IS(dt_lj);
//	        }
//	        else if (pbc)
//	        {
//	            t3 = pbc_rvec_sub(pbc, x[l], x[j], dx_jl);
//	        }
//	        else
//	        {
//	            t3 = CENTRAL;
//	        }
//
//	        rvec_inc(fshift[t1], f_i);
//	        rvec_dec(fshift[CENTRAL], f_j);
//	        rvec_dec(fshift[t2], f_k);
//	        rvec_inc(fshift[t3], f_l);
//	    }
	}

}
