package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

public interface EventListener {
	
	public void onRestart();
	
	public void onStop();
	
	public void onPause();
	
	public void onResume();
	
	public int getStatus();
}
