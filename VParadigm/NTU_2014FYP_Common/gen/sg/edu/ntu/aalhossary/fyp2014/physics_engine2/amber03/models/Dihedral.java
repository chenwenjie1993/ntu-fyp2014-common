package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public abstract class Dihedral extends BondedInteraction {
	public double phi0, kd;
	protected Vector3D v_ji, v_jk, v_lk, m, n;
	protected double phi, dp, dVdphi;
	
	public Dihedral(List<Atom> atoms) {
		super(atoms);
	}

	public abstract void updatePotentialEnergy();
	
	public void updateProperties() {
		v_ji = atoms.get(0).getPosition().subtractAndReturn(atoms.get(1).getPosition());
		v_jk = atoms.get(2).getPosition().subtractAndReturn(atoms.get(1).getPosition());
		v_lk = atoms.get(2).getPosition().subtractAndReturn(atoms.get(3).getPosition());
		
	    m = v_ji.getCrossProduct(v_jk);
	    n = v_jk.getCrossProduct(v_lk);
	    
	    phi = Geometry.phi(m, n);
		dp = phi - phi0;
	}
	
	protected void applyForce() {
		Vector3D f_i, f_j, f_k, f_l;
	    Vector3D uvec, vvec, svec;
	    double iprm, iprn, nrkj, nrkj2, nrkj_1, nrkj_2;
	    double a, b, p, q;
		
		iprm  = m.getSquaredMagnitude();
	    iprn  = n.getSquaredMagnitude();
	    nrkj2 = v_jk.getSquaredMagnitude();
	    
	    if ((iprm > 1e-8) && (iprn > 1e-8)) {
	        nrkj_1 = 1/Math.sqrt(nrkj2);
	        nrkj_2 = nrkj_1*nrkj_1;
	        nrkj   = nrkj2*nrkj_1;
	        // f_i
	        a      = -dVdphi*nrkj/iprm;
	        f_i = new Vector3D();
	        f_i.addScaledVector(m, a);
	        // f_l
	        b     = dVdphi*nrkj/iprn;
	        f_l = new Vector3D();
	        f_l.addScaledVector(n, b);
	        
	        p     = v_ji.getScalarProduct(v_jk);
	        p    *= nrkj_2;
	        q     = v_lk.getScalarProduct(v_jk);
	        q    *= nrkj_2;
	        uvec = new Vector3D();
	        uvec.addScaledVector(f_i, p);
	        vvec = new Vector3D();
	        vvec.addScaledVector(f_l, q);
	        
	        // f_j
	        svec = Geometry.vector(vvec, uvec);
	        f_j = Geometry.vector(svec, f_i);
	        // f_k
	        f_k = new Vector3D();
	        f_k.add(f_l);
	        f_k.add(svec);
	        
	        atoms.get(0).addForce(f_i);
	        atoms.get(1).addForce(f_j.getNegativeVector());
	        atoms.get(2).addForce(f_k.getNegativeVector());
	        atoms.get(3).addForce(f_l);
	    }
	}
}
