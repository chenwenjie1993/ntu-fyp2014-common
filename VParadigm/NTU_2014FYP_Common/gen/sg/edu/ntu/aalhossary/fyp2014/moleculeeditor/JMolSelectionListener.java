package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import org.jmol.api.JmolSelectionListener;

public class JMolSelectionListener implements JmolSelectionListener {

	public String selectionList;
	public java.lang.String _selectionList;

	public JMolSelectionListener() {
	}

	/**
	 * 
	 * @param values
	 */
	public void selectionChanged(org.jmol.java.BS values) {
		JmolDisplay.getSelected(values);
	}

}