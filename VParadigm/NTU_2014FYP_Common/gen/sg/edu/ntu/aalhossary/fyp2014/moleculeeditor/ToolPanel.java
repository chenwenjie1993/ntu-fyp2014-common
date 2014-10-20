package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class ToolPanel extends JPanel {

	static javax.swing.JList list;
	static javax.swing.DefaultListModel listModel = new DefaultListModel();
	static JmolDisplay jmolPanel;

	/**
	 * 
	 * @param jmolPanel
	 */
	public ToolPanel(JmolDisplay jmolPanel) {
		this.jmolPanel = jmolPanel;
		JTabbedPane tabbedPane = new JTabbedPane();
        
		JComponent panel1 = new JPanel();
        tabbedPane.addTab("Style", panel1);
        GridBagLayout layout = new GridBagLayout();
        panel1.setLayout(layout);        
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel moleculeStyle = new JLabel("Molecule Style");
        panel1.add(moleculeStyle, gbc);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JComboBox<DisplayType> styleList = new JComboBox<DisplayType>(DisplayType.values());
        styleList.setSelectedItem(DisplayType.BALLSTICK);
        panel1.add(styleList, gbc);
        styleList.addActionListener(new UserActionListener(jmolPanel, UserActionType.DISPLAYTYPE));
        
        JLabel modelDetail = new JLabel("Model Details");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,0,0);
        panel1.add(modelDetail, gbc);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.ipady = 30;      //make this component tall
        list = new JList(listModel); //data has type Object[]
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        panel1.add(list, gbc);
        
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
         
        JComponent panel2 = new JPanel();
        tabbedPane.addTab("Tab 2", panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        add(tabbedPane);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	
	}

	protected static void makeModelList() {
		Object [] molecules =  jmolPanel.model.getModelDetailList();
		listModel.removeAllElements();
		for(int i=0;i<molecules.length;i++){
			listModel.addElement(molecules[i]);
		}
	}

	/**
	 * 
	 * @param text
	 */
	protected javax.swing.JComponent makeTextPanel(java.lang.String text) {
		// TODO - implement ToolPanel.makeTextPanel
		throw new UnsupportedOperationException();
	}

}