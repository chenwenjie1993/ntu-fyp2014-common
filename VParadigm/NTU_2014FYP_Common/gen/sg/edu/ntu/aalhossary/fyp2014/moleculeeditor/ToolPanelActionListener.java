package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.awt.event.*;

import javax.swing.JComboBox;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.DisplayType;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.UserActionType;

public class ToolPanelActionListener implements ActionListener {

	public UserActionType userActionType;
	private JmolDisplay jmolPanel;
	public UserActionType _userAction;

	/**
	 * 
	 * @param jmolPanel
	 * @param actionType
	 */
	public ToolPanelActionListener(JmolDisplay jmolPanel, UserActionType actionType) {
		this.jmolPanel = jmolPanel;
		this.userActionType = actionType;
	}

	/**
	 * 
	 * @param event
	 */
	public void actionPerformed(java.awt.event.ActionEvent event) {
		if(userActionType == UserActionType.DISPLAYTYPE){
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
			else if ( value == DisplayType.BALLSTICK){
				script = "hide null; select all; color cpk; wireframe only; wireframe 0.15; spacefill 23%; cartoon off;";
			}
			jmolPanel.executeCmd(script);
		}
		
		/*Object mysource = event.getSource();
		
		JComboBox source = (JComboBox) event.getSource();
		String value = source.getSelectedItem().toString();

		String selectLigand = "select ligand;wireframe 0.16;spacefill 0.5; color cpk ;";

		if ( value.equals("Cartoon")){
			String script = "hide null; select all;  spacefill off; wireframe off; backbone off;" +
					" cartoon on; " +
					" select ligand; wireframe 0.16;spacefill 0.5; color cpk; " +
					" select *.FE; spacefill 0.7; color cpk ; " +
					" select *.CU; spacefill 0.7; color cpk ; " +
					" select *.ZN; spacefill 0.7; color cpk ; " +
					" select all; ";
			jmolPanel.executeCmd(script);
		} else if (value.equals("Backbone")){
			String script = "hide null; select all; spacefill off; wireframe off; backbone 0.4;" +
					" cartoon off; " +
					" select ligand; wireframe 0.16;spacefill 0.5; color cpk; " +
					" select *.FE; spacefill 0.7; color cpk ; " +
					" select *.CU; spacefill 0.7; color cpk ; " +
					" select *.ZN; spacefill 0.7; color cpk ; " +
					" select all; ";
			jmolPanel.executeCmd(script);
		} else if (value.equals("CPK")){
			String script = "hide null; select all; spacefill off; wireframe off; backbone off;" +
					" cartoon off; cpk on;" +
					" select ligand; wireframe 0.16;spacefill 0.5; color cpk; " +
					" select *.FE; spacefill 0.7; color cpk ; " +
					" select *.CU; spacefill 0.7; color cpk ; " +
					" select *.ZN; spacefill 0.7; color cpk ; " +
					" select all; ";
			jmolPanel.executeCmd(script);

		} else if (value.equals("Ligands")){
			jmolPanel.executeCmd("restrict ligand; cartoon off; wireframe on;  display selected;");			
		} else if (value.equals("Ligands and Pocket")){
			jmolPanel.executeCmd(" select within (6.0,ligand); cartoon off; wireframe on; backbone off; display selected; ");
		} else if ( value.equals("Ball and Stick")){
			String script = "hide null; restrict not water;  wireframe 0.2; spacefill 25%;" +
					" cartoon off; backbone off; " +
					" select ligand; wireframe 0.16; spacefill 0.5; color cpk; " +
					" select *.FE; spacefill 0.7; color cpk ; " +
					" select *.CU; spacefill 0.7; color cpk ; " +
					" select *.ZN; spacefill 0.7; color cpk ; " +
					" select all; ";
			jmolPanel.executeCmd(script);
		} else if ( value.equals("By Chain")){
			String script = "hide null; select all;set defaultColors Jmol; color_by_chain(\"cartoon\"); color_by_chain(\"\"); " + selectLigand + "; select all; ";
			jmolPanel.executeCmd(script);
		} else if ( value.equals("Rainbow")) {
			jmolPanel.executeCmd("hide null; select all; set defaultColors Jmol; color group; color cartoon group; " + selectLigand + "; select all; " );
		} else if ( value.equals("Secondary Structure")){
			jmolPanel.executeCmd("hide null; select all; set defaultColors Jmol; color structure; color cartoon structure;" + selectLigand + "; select all; " );

		} else if ( value.equals("By Element")){
			jmolPanel.executeCmd("hide null; select all; set defaultColors Jmol; color cpk; color cartoon cpk; " + selectLigand + "; select all; "); 
		} else if ( value.equals("By Amino Acid")){
			jmolPanel.executeCmd("hide null; select all; set defaultColors Jmol; color amino; color cartoon amino; " + selectLigand + "; select all; " );
		} else if ( value.equals("Hydrophobicity") ){
			jmolPanel.executeCmd("hide null; set defaultColors Jmol; select hydrophobic; color red; color cartoon red; select not hydrophobic ; color blue ; color cartoon blue; "+ selectLigand+"; select all; ");
		} else if ( value.equals("Suggest Domains")){
		} else if ( value.equals("Show SCOP Domains")){
		}
*/
	}

}