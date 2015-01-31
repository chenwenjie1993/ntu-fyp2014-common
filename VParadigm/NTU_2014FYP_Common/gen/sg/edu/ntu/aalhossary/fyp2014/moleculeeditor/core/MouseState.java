package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.util.ArrayList;

public class MouseState {

	private boolean hasEnded;
	private int[] startPos;
	private ArrayList<int[]> trackPos;
	private int[] endPos;
	private String mouseMode;
	
	public MouseState(){
		this.hasEnded = false;
		this.startPos = new int[2];
		this.endPos = new int[2];
		this.trackPos = new ArrayList<int[]>();
	}
	
	public boolean hasEnded() {
		return hasEnded;
	}
	
	public int getStartPosX() {
		return startPos[0];
	}
	
	public int getStartPosY() {
		return startPos[1];
	}
	
	public void setStartPosition(int x, int y) {
		startPos[0] = x;
		startPos[1] = y;
		hasEnded=false;
	}
	
	public ArrayList<int []> getPositionList() {
		return trackPos;
	}
	
	public void setCurrentPosition(int x, int y) {
		int[] currPos = {x,y};
		trackPos.add(currPos);
	}
	
	public int getEndPosX() {
		return endPos[0];
	}
	
	public int getEndPosY() {
		return endPos[1];
	}
	
	public void setEndPosition(int x, int y) {
		endPos[0] = x;
		endPos[1] = y;
		hasEnded = true;
		if(mouseMode.contains("Zoom") && startPos[1]> endPos[1])
			mouseMode += " (Zoomed Out)";
		else if(mouseMode.contains("Zoom") && startPos[1]< endPos[1])
			mouseMode += " (Zoomed In)";
		setMouseMode(mouseMode);
	}
	
	public String getMouseMode() {
		return mouseMode;
	}
	
	public void setMouseMode(String mouseMode){
		this.mouseMode = mouseMode;
	}

	public void clearState() {
		this.hasEnded = false;
		this.startPos = new int[2];
		this.endPos = new int[2];
		this.trackPos.clear();
		this.mouseMode = "";
	}
}
