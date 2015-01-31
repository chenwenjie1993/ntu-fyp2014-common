package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.JmolDisplay;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.DisplayType;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.UserActionType;

public class ToolPanelHandler{

	public static void handleJComboBox(ActionEvent event, JmolDisplay jmolPanel) {
		String script = null;
		@SuppressWarnings("unchecked")
		JComboBox<DisplayType> source = (JComboBox<DisplayType>) event.getSource();
		Object value = source.getSelectedItem();
		if ( value == DisplayType.LINE){
			script = "hide null; select all; spacefill off; cartoon off; backbone off;" + 
					"wireframe 0.2;";
		}
		else if ( value == DisplayType.SPACE){
			script = "hide null; select all; spacefill on; wireframe off; backbone off;" + 
					"cartoon off;";
		}
		else if ( value == DisplayType.STICK){
			script = "hide null; select all; spacefill off; wireframe on; backbone on;" + 
					"cartoon off;";
		}
		else if ( value == DisplayType.SPHERE){
			script = "hide null; select all; spacefill +1.2; wireframe on; backbone off;" + 
					"cartoon off;";
		}
		else if ( value == DisplayType.CARTOON){
			script = "hide null; select all; spacefill off; wireframe off; backbone off;" + 
					"cartoon on;" +
					"color cartoon group";
		}
		else if ( value == DisplayType.BALLNSTICK){
			script = "hide null; select all; color cpk; wireframe only; wireframe 0.15; spacefill 23%; cartoon off;";
		}
		jmolPanel.executeCmd(script);
	}

	public static void handlePickingFunction(JButton source, JmolDisplay jmolPanel, ButtonGroup group) {
		if(source.getText().compareTo("Set Picking ON")==0){
			source.setText("Set Picking OFF");
			switch(group.getSelection().getActionCommand()){
				case "Atom": jmolPanel.executeCmd("set picking select atom");
								return;
				case "Group": jmolPanel.executeCmd("set picking select group");
								return;
				case "Chain": jmolPanel.executeCmd("set picking select chain");
								return;
			}			
		}
		else if(source.getText().compareTo("Set Picking OFF")==0){
			source.setText("Set Picking ON");
			jmolPanel.executeCmd("set picking off");
		}
	}

	public static void handleRadioPick(JButton setPicking, JmolDisplay jmolPanel, ButtonGroup group) {
		setPicking.setText("Set Picking OFF");
		
		switch(group.getSelection().getActionCommand()){
		
		case "Atom": jmolPanel.executeCmd("set picking select atom");
						return;
		case "Group": jmolPanel.executeCmd("set picking select group");
						return;
		case "Chain": jmolPanel.executeCmd("set picking select chain");
						return;
		}	
	}

	public static void handleSelectionHalo(JButton source, JmolDisplay jmolPanel) {
		if(source.getText().compareTo("Selection Halos ON")==0){
			source.setText("Selection Halos OFF");
			jmolPanel.executeCmd("selectionHalos ON");
		}
		else if(source.getText().compareTo("Selection Halos OFF")==0){
			source.setText("Selection Halos ON");
			jmolPanel.executeCmd("selectionHalos OFF");
		}
		else if(source.getText().compareTo("Select All")==0){
			jmolPanel.executeCmd("select all");
		}
		else if(source.getText().compareTo("Select None")==0){
			jmolPanel.executeCmd("select remove all");
		}
	}

	public static void handleModifySelected(JButton source, JmolDisplay jmolPanel) {
		String action = source.getText().toLowerCase();
		switch(action){
			case "delete": EvaluateUserAction.findSelectedAtom(jmolPanel.getMediator(), EvaluateUserAction.DELETE);
							return;
			case "copy": EvaluateUserAction.findSelectedAtom(jmolPanel.getMediator(), EvaluateUserAction.COPY);
							return;
			case "paste": EvaluateUserAction.findSelectedAtom(jmolPanel.getMediator(), EvaluateUserAction.PASTE);
							return;
			case "cut": EvaluateUserAction.findSelectedAtom(jmolPanel.getMediator(), EvaluateUserAction.CUT);
							return;
		}
		
	}

	public static void handleDragging(JButton source, JmolDisplay jmolPanel, ButtonGroup draggingGroup) {
		if(source.getText().compareTo("Drag Atom(s) ON")==0){
			source.setText("Drag Atom(s) OFF");
			handleRadioDrag(source, jmolPanel, draggingGroup);
		}
		else if(source.getText().compareTo("Drag Atom(s) OFF")==0){
			source.setText("Drag Atom(s) ON");
			jmolPanel.executeCmd("set picking off");
		}
		
	}

	public static void handleRadioDrag(JButton source, JmolDisplay jmolPanel, ButtonGroup draggingGroup) {
		source.setText("Drag Atom(s) OFF");
		switch(draggingGroup.getSelection().getActionCommand()){
		case "Drag Atom": 
			jmolPanel.executeCmd("set picking DRAGATOM");
			return;
		case "Drag Atom (and Minimize)":
			jmolPanel.executeCmd("set picking DRAGMINIMIZE");
			return;
		case "Drag Selected Atoms": 
			jmolPanel.executeCmd("set picking DRAGSELECTED");
			return;
		case "Drag Selected Atoms (and Minimize)":
			jmolPanel.executeCmd("");
			return;
	}
	}
	
	
}