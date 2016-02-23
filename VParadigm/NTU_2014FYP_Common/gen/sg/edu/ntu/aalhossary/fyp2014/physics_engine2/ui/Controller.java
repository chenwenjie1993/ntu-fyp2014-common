package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.util.Map;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TypologyBuilder;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.MolecularSystem;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Simulator;

public abstract class Controller {
	public Map<String, Object> config;
	public String status = "Pending";
	protected MolecularSystem m;
	public int currentFrame = 0;
	public int totalFrame = 0;
	protected Simulator sim = new Simulator();

	
	public Controller(Map<String, Object> config) {
		this.config = config;
		totalFrame = (Integer) config.get("frame");
	}
	
	public void start() {
		TypologyBuilder tb = new TypologyBuilder();
   		m = tb.build(config);
   		m.setTimeDelta((Double) config.get("timeDelta"));
	};
}
