package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.MolecularSystem;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public class CliController extends Controller {

	@Override
	public void progress(MolecularSystem m) {
//		for (AbstractParticle particle: m.particles) {
//			System.out.println(particle.getPosition().toString());
//		}
		System.out.println(Geometry.vector(m.particles.get(0).getPosition(), m.particles.get(1).getPosition()).getMagnitude());
//		System.out.println(m.particles.get(0).getAcceleration());
	}
}
