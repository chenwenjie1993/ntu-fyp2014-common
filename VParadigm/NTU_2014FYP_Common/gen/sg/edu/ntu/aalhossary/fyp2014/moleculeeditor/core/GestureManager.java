package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

public class GestureManager {

	public static void setMouseState(int x, int y, int mode, MouseState mouseState) {
		switch(mode){
		// left-click, shift-left-click
		case 4368:
		case 4369: 	mouseState.setStartPosition(x,y);
					return;
		case 8464: 	mouseState.setMouseMode("Rotate Model");
					mouseState.setCurrentPosition(x,y);
					return;
		case 8465:	
		case 8456:	mouseState.setMouseMode("Zoom Model");
					mouseState.setCurrentPosition(x,y);
					return;
		case 8721: 	mouseState.setMouseMode("Translate Model");
					mouseState.setCurrentPosition(x,y);
					return;
		case 16640: 
		case 16896: mouseState.setEndPosition(x,y);
					return;		
		default: return;
		}
	}
}
