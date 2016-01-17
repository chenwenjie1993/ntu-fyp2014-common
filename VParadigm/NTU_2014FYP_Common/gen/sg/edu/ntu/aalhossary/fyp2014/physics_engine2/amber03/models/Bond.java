package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Distance;

public class Bond extends BondedInteraction {
	public double b0, kb;
	
	public Bond(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<String> query = new ArrayList<String>();
		for (Atom atom : atoms) {
			query.add(atom.type);
		}
		List<Double> params = db.getBondParams(query);
		b0 = params.get(0);
		kb = params.get(1);
	}

	@Override
	public void updatePotentialEnergy() {
		Vector3D dist = Distance.distance3D(atoms.get(0).getPosition(), atoms.get(1).getPosition());
		Vector3D vector = atoms.get(1).getPosition().subtractAndReturn(atoms.get(0).getPosition());
		vector.normalize();
		vector.scale(b0);
		
		Vector3D energy = new Vector3D();
		double temp_x = dist.x - vector.x;
		temp_x *= temp_x;
		energy.x = 0.5 * kb * temp_x;
		
		double temp_y = dist.y - vector.y;
		temp_y *= temp_y;
		energy.y = 0.5 * kb * temp_y;
		
		double temp_z = dist.z - vector.z;
		temp_z *= temp_z;
		energy.z = 0.5 * kb * temp_z;
		
		atoms.get(0).potentialEnergy.add(energy);
		atoms.get(1).potentialEnergy.add(energy);
	}

}
