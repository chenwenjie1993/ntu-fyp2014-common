package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import org.jmol.api.JmolSelectionListener;

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
		jmolPanel.getMediator().setSelectedAtoms(values);
	}

}