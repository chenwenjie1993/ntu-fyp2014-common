package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.util.logging.Level;

import org.jmol.api.JmolSelectionListener;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.JmolDisplay;

public class JMolSelectionListener implements JmolSelectionListener {

	public String selectionList;
	public JmolDisplay jmolPanel;

	public JMolSelectionListener(JmolDisplay jmolPanel) {
		this.jmolPanel = jmolPanel;
	}

	/**
	 * 
	 * @param values
	 */
	public void selectionChanged(org.jmol.java.BS values) {
		System.out.println(values);
		System.out.println(jmolPanel.getMediator());
		jmolPanel.getMediator().setSelectedAtoms(values);
	}

}