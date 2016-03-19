package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.util.Map;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyBuilder;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.MolecularSystem;

public abstract class Controller {
	public static int STOPPED = -1;
	public static int PAUSED = 0;
	public static int RUNNING = 1;
	
	public Map<String, Object> config;
	public int status = STOPPED;
	protected MolecularSystem m;
	public int currentFrame = 0;
	public int totalFrame = 0;
	TopologyBuilder tb;

	
	public Controller(Map<String, Object> config) {
		this.config = config;
		totalFrame = (Integer) config.get("frame");
		init();
	}
	
	public void buildTopology() {
		System.out.println("Building topology...");
		tb = new TopologyBuilder();
		m = tb.build(config);
   		m.setTimeDelta((Double) config.get("timeDelta"));
   		Map<String, Object> params = (Map<String, Object>) config.get("ffParams");
		m.setParams(params);
	}
	
	public void init() {
		buildTopology();
	}
	
	public void start() {
   		
	}
}
