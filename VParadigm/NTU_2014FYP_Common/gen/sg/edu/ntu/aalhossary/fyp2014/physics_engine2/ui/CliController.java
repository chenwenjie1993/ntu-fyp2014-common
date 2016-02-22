package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.util.Map;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.MolecularSystem;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public class CliController extends Controller {
	public CliController(Map<String, Object> config) {
		super(config);
	}
	
	@Override
	public void start() {
		super.start();
//		for (AbstractParticle particle: m.particles) {
//			System.out.println(particle.getPosition().toString());
//		}
		System.out.println(Geometry.vector(m.particles.get(0).getPosition(), m.particles.get(1).getPosition()).getMagnitude());
//		System.out.println(m.particles.get(0).getAcceleration());
	}
}
