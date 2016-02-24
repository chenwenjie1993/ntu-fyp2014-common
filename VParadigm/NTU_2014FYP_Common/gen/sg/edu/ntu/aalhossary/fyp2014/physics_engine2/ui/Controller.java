package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.util.Map;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TypologyBuilder;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.MolecularSystem;

public abstract class Controller {
	public Map<String, Object> config;
	public String status = "Pending";
	protected MolecularSystem m;
	public int currentFrame = 0;
	public int totalFrame = 0;
	TypologyBuilder tb;

	
	public Controller(Map<String, Object> config) {
		this.config = config;
		totalFrame = (Integer) config.get("frame");
	}
	
	public void buildTopology() {
		System.out.println("Building topology...");
		tb = new TypologyBuilder();
		m = tb.build(config);
   		m.setTimeDelta((Double) config.get("timeDelta"));
	}
	
	public void start() {
   		buildTopology();
	};
}
