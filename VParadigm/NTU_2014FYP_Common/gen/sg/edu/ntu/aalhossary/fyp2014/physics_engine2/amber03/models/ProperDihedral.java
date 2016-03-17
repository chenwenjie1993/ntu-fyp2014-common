package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public class ProperDihedral extends Dihedral {
	double mult;

	public ProperDihedral(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<String> query = new ArrayList<String>();
		for (Atom atom : atoms) {
			query.add(atom.type);
		}
//		System.out.println(query);
		List<Double> params = db.getProperDihedralParams(query);
		phi0 = Math.toRadians(params.get(0));
		kd = params.get(1);
		mult = params.get(2);
		
		updateProperties();
		
		System.out.println("phi0: " + String.valueOf(phi0) + " phi: " + String.valueOf(phi) + " kd: " + kd + " mult: " + mult);

	}

	@Override
	public void calcPotentialEnergyTerm() {
		updateProperties();
		
		double mdphi, sdphi;
		mdphi = mult * phi - phi0;
		sdphi = Math.sin(mdphi);
		dVdphi = - 0.5 * kd * mult * sdphi;
		
		calcForce();
	}
}

/*
 * 
real dopdihs(real cpA, real cpB, real phiA, real phiB, int mult,
             real phi, real lambda, real *V, real *F)
{
    real v, dvdlambda, mdphi, v1, sdphi, ddphi;
    real L1   = 1.0 - lambda;
    real ph0  = (L1*phiA + lambda*phiB)*DEG2RAD;
    real dph0 = (phiB - phiA)*DEG2RAD;
    real cp   = L1*cpA + lambda*cpB;

    mdphi =  mult*phi - ph0;
    sdphi = sin(mdphi);
    ddphi = -cp*mult*sdphi;
    v1    = 1.0 + cos(mdphi);
    v     = cp*v1;

    dvdlambda  = (cpB - cpA)*v1 + cp*dph0*sdphi;

    *V = v;
    *F = ddphi;

    return dvdlambda;

}
*/

