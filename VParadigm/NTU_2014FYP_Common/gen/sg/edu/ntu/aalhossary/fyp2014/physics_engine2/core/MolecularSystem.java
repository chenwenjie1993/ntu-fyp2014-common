package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.*;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.DataManager;;

public class MolecularSystem {
	public ArrayList<AbstractParticle> particles;
	public ArrayList<Interaction> interactions;
	public ArrayList<ArrayList<AbstractParticle>> rlist;
	public boolean[][] exclude;
	private double timeDelta = 0.0002;
	private Map<String, Object> params;
	private final double R = 1;
	private int updateRlistStep = 20;
	
	private static final Logger log = Logger.getLogger("main");
	
	
	private double t = 0;
	private double Ekin = -1;
	private double Ep = -1;
	private double E = -1;
	private double T = 300;
	private double k = 8.314510e-3;
	private double dT = 0;
	
	public MolecularSystem() {
		AbstractParticle.resetCount();
	}
	
	public void initRlist() {
		System.out.println("Rebuild rlist");
		if (particles == null || particles.size() == 0) {
			return;
		}
		rlist = new ArrayList<ArrayList<AbstractParticle>>();
		for (int i=0; i<particles.size(); i++) {
			ArrayList<AbstractParticle> l = new ArrayList<AbstractParticle>();
			rlist.add(l);
		}
	}
	
	public void updateRlist() {
		initRlist();
		int n = particles.size();
		
		for (int i=0; i<n; i++) {
			for (int j=i+1; j<n; j++) {
				AbstractParticle p1 = particles.get(i);
				AbstractParticle p2 = particles.get(j);
				double dist = p1.getPosition().subtractAndReturn(p2.getPosition()).getMagnitude();
				if (dist < R && !exclude[i][j]) {
					rlist.get(i).add(p2);
					rlist.get(j).add(p1);
				}
			}
		}
	}
	
	public double getTimeDelta() {
		return timeDelta;
	}

	public void setTimeDelta(double duration) {
		this.timeDelta = duration;
	}
		
	public void nextFrame() {
//		if (t == 0) {
//			printLog();
//		}
		
		
		t += timeDelta;
		
		updatePotentialEnergy();
		updateSystemEnergy();
		integrate();
//		System.out.println(particles);
		
		printLog();
	}
	
	public void updateSystemEnergy() {
		
		Ekin = 0;
		for (AbstractParticle particle: particles) {
			double v = particle.getVelocity().getMagnitude();
			Ekin += 0.5 * particle.getMass() * v * v;
		}
		
		Ep = 0;
		for (Interaction interaction: interactions) {
			Ep += interaction.getPotentialEnergy();
		}
		
		System.out.println("Ekin: " + Ekin);
		System.out.println("Ep: " + Ep);
		System.out.println("Total Energy: " + (Ekin+Ep));
		
		// E is calculated only the first time and used as target energy
		if (E < 0) {
			E = Ep + Ekin;
			System.out.println("Target energy: " + E);
			return;
		}
		
		rescale();
		
//		dT = T;
//		T = 2 * Ekin / (particles.size() - 3) / k;
//		dT = T - dT;
//		System.out.println("T: " + T);
	}
	
	public void rescale() {
		double Ekin2 = E - Ep;
		System.out.println("Ideal Ek: " + Ekin2);
		if (Ekin2 >= Ekin || Ekin2 <= 0) {
			System.out.println("No need for scaling.");
			return;
		}
		
		double scale = 1;
		while (true) {
			scale -= 0.001;
			double t = 0;
			for (AbstractParticle particle: particles) {
				Vector3D v = new Vector3D();
				v.add(particle.getVelocity());
				v.scale(scale);
				double l = v.getMagnitude();
				t += 0.5 * particle.getMass() * l * l;
			}
			if (t <= Ekin2) {
				System.out.println("Scaled Ek: " + t);
				System.out.println("Scale: " + scale);
				for (AbstractParticle particle: particles) {
					particle.getVelocity().scale(scale);
				}
				return;
			}
		}
	}
	
	public void updatePotentialEnergy() {
		
		for (Interaction interaction: interactions) {
			if (interaction instanceof Bond) {
				if ((Boolean) params.get("Bond")) {
		   			interaction.calcPotentialEnergyTerm();
				}
			}
			else if (interaction instanceof Angle) {
				if ((Boolean) params.get("Angle")) {
		   			interaction.calcPotentialEnergyTerm();
				}
			}
			else if (interaction instanceof ProperDihedral) {
				if ((Boolean) params.get("ProperDihedral")) {
		   			interaction.calcPotentialEnergyTerm();
				}
			}
			else if (interaction instanceof ImproperDihedral) {
				if ((Boolean) params.get("ImproperDihedral")) {
		   			interaction.calcPotentialEnergyTerm();
				}
			}
   		}
		
		if ((Boolean) params.get("LennardJones") || (Boolean) params.get("Electrostatic")) {
			int n = rlist.size();
			for (int i=0; i<n; i++) {
				for (AbstractParticle p: rlist.get(i)) {
					if (p.getGUID()-1 > i) {
						if ((Boolean) params.get("LennardJones")) {
							Interaction ljInteraction = new LennardJonesPotential((Atom)particles.get(i), (Atom)p);
							ljInteraction.calcPotentialEnergyTerm();
						}
						if ((Boolean) params.get("Electrostatic")) {
							Interaction elecInteraction = new ElectrostaticPotential((Atom)particles.get(i), (Atom)p);
							elecInteraction.calcPotentialEnergyTerm();
						}
					}
				}
			}
		}
		
//		if ((Boolean) params.get("LennardJones")) {
//   			interaction.calcPotentialEnergyTerm();
//		}
//		else if (interaction instanceof ElectrostaticPotential) {
//			if ((Boolean) params.get("Electrostatic")) {
//	   			interaction.calcPotentialEnergyTerm();
//			}
//		}
		
		
   	}
	
	public void integrate() {
		double lambda = 1;
//		t = (300 - T) / dT * timeDelta;
//		System.out.println("t: " + t);
//		lambda = Math.sqrt(1 + timeDelta * 0.5 / t * (300 / T / (t - 0.5 * timeDelta) - 1));
//		lambda = 1 + (300 / T - 1);
//		
//		if (lambda > 1.25) {
//			lambda = 1.25;
//		}
//		else if (lambda < 0.8) {
//			lambda = 0.8;
//		}
//		System.out.println("lambda: " + lambda);
		
		/**
		 * Velocity Verlet Algorithm
		 */
		for (AbstractParticle particle: particles) {
			Vector3D r = particle.getPosition();
			Vector3D v = particle.getVelocity();
			Vector3D a = particle.getAcceleration();
			Vector3D a2 = particle.getAccumulatedAcceleration();
//			log.info("[ACC]" + a2);
						
			Vector3D dr = new Vector3D();
			dr.addScaledVector(v, timeDelta);
			dr.addScaledVector(a, 0.5 * timeDelta * timeDelta);
			r.add(dr);
			
			Vector3D dv = new Vector3D();
			dv.addScaledVector(a, 0.5 * timeDelta);
			dv.addScaledVector(a2, 0.5 * timeDelta);
			v.add(dv);
//			v.scale(0.99);
//			v.scale(lambda);
						
//			Vector3D dv = new Vector3D();
//			dv.addScaledVector(a, timeDelta);
//			v.add(dv);
//			
//			Vector3D dr = new Vector3D();
//			dr.addScaledVector(v, timeDelta);
//			r.add(dr);
			
			particle.setAcceleration(a2.x, a2.y, a2.z);
			particle.clearAccumulator();
			
//			if (particle.getGUID() == 3) {
//				System.out.println(dr);
//			}
			
//			System.out.println("Positon: " + particle.getPosition());
//			System.out.println("Velocity: " + particle.getVelocity());
		}
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public int getUpdateRlistStep() {
		return updateRlistStep;
	}

	public void setUpdateRlistStep(int updateRlistStep) {
		this.updateRlistStep = updateRlistStep;
	}
	
	public void printLog() {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("water, t=%8.4f", t);
		log.info(formatter.toString());
		
		sb = new StringBuilder();
		formatter = new Formatter(sb, Locale.US);
		formatter.format("%5d", particles.size());
		log.info(formatter.toString());
		
		for (AbstractParticle particle: particles) {
			sb = new StringBuilder();
			formatter = new Formatter(sb, Locale.US);
			
			int rId = 0;
			String rName = "";
			String aName = "";
			int aId = 0;
			double pX = 0;
			double pY = 0;
			double pZ = 0;
			double vX = 0;
			double vY = 0;
			double vZ = 0;
			
			rName = "HOH";
			
//			if (particle.getGUID() <= 12) {
//				rId = 1;
//				rName = "ALA";
//			}
//			else if (particle.getGUID() <= 32) {
//				rId = 2;
//				rName = "PHE";
//			}
//			else {
//				rId = 3;
//				rName = "SER";
//			}
			
			aName = ((Atom) particle).getName();
			aId = particle.getGUID();
			pX = particle.getPosition().x;
			pY = particle.getPosition().y;
			pZ = particle.getPosition().z;
			vX = particle.getVelocity().x;
			vY = particle.getVelocity().y;
			vZ = particle.getVelocity().z;
			
			log.info(formatter.format("%5d%-5s%5s%5d%8.3f%8.3f%8.3f%8.4f%8.4f%8.4f",
					rId,
					rName,
					aName,
					aId,
					pX,
					pY,
					pZ,
					vX,
					vY,
					vZ).toString());
			
		}
		log.info("   0.80649   0.62365   1.03123");
	}
	
	public String getPDB() {
		StringBuffer sb = new StringBuffer();
		
		for (AbstractParticle p: particles) {
			sg.edu.ntu.aalhossary.fyp2014.common.Atom a = new sg.edu.ntu.aalhossary.fyp2014.common.Atom();
			a.setSymbol(((Atom)p).getType());
			a.setChainSeqNum(1);
			Vector3D v = new Vector3D();
			v.add(p.getPosition());
			v.scale(10);
			DataManager.toPDB(a, p.getGUID(), v, 1, sb);
		}
		return sb.toString();
	}
}
