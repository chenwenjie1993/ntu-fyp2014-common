package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.*;

import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.ToolPanelHandler;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.DisplayType;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.UserActionType;

public class ToolPanel extends JPanel implements ActionListener  {

	protected JmolDisplay jmolPanel;
	protected JTextArea textArea;
	protected JTextField textField;
	protected ButtonGroup pickingGroup;
	protected ButtonGroup draggingGroup;
	protected JButton setPicking;
	protected JButton setDragging;
	protected static final String newline = "\n";
	
	/**
	 * 
	 * @param jmolPanel
	 */
	public ToolPanel(JmolDisplay jmolPanel) {
		this.jmolPanel = jmolPanel;
		JTabbedPane tabbedPane = new JTabbedPane();
		//ToolPanelActionListener toolPanelListener = new ToolPanelActionListener(jmolPanel, UserActionType.DISPLAYTYPE);
		/**
		 * Panel 1 - Style and Model Details
		 */
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
        styleList.setSelectedItem(DisplayType.BALLNSTICK);
        panel1.add(styleList, gbc);
        styleList.addActionListener(this);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,0,0);
        JLabel modelDetail = new JLabel("Model Details");
        panel1.add(modelDetail, gbc);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.ipady = 30;      //make this component tall
        textArea = new JTextArea();
        textArea.setEditable(false);
        panel1.add(textArea, gbc);
        
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        /**
		 * Panel 2 - Editing of Model
		 */
        /*JComponent panel2 = new JPanel();
        tabbedPane.addTab("Editing", panel2);
        layout = new GridBagLayout();
        panel2.setLayout(layout);        
        gbc = new GridBagConstraints();
        
        JPanel setPickingPanel = new JPanel();
        setPickingPanel.setLayout(layout);
        setPickingPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Set Picking"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(1,1,1,1);
        JButton selectionHaloButton = new JButton("Selection Halos ON");
        selectionHaloButton.addActionListener(this);
        setPickingPanel.add(selectionHaloButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        JButton selectAllButton = new JButton("Select All");
        selectAllButton.addActionListener(this);
        setPickingPanel.add(selectAllButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        JButton selectNoneButton = new JButton("Select None");
        selectNoneButton.addActionListener(this);
        setPickingPanel.add(selectNoneButton, gbc);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        setPickingPanel.add(sep, gbc);
        
        JRadioButton pickAtomButton = new JRadioButton("Atom");
        JRadioButton pickGroupButton = new JRadioButton("Group");
        JRadioButton pickChainButton = new JRadioButton("Chain");
        pickAtomButton.setSelected(true);
        pickAtomButton.setActionCommand("Atom");
        pickGroupButton.setActionCommand("Group");
        pickChainButton.setActionCommand("Chain");
        pickingGroup = new ButtonGroup();
        pickingGroup.add(pickAtomButton);
        pickingGroup.add(pickGroupButton);
        pickingGroup.add(pickChainButton);
        pickAtomButton.addActionListener(this);
        pickGroupButton.addActionListener(this);
        pickChainButton.addActionListener(this);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        setPickingPanel.add(pickAtomButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        setPickingPanel.add(pickGroupButton, gbc);
        gbc.gridx = 2;
        gbc.gridy = 4;
        setPickingPanel.add(pickChainButton, gbc);
        
        setPicking = new JButton("Set Picking ON");
        setPicking.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        setPickingPanel.add(setPicking, gbc);
        
        JPanel setModifySelPanel = new JPanel();
        setModifySelPanel.setLayout(layout);
        setModifySelPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Modify Selected"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JButton deleteSelButton = new JButton("Delete");
        deleteSelButton.addActionListener(this);
        setModifySelPanel.add(deleteSelButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        JButton cutSelButton = new JButton("Cut");
        cutSelButton.addActionListener(this);
        setModifySelPanel.add(cutSelButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton pasteSelButton = new JButton("Paste");
        pasteSelButton.addActionListener(this);
        setModifySelPanel.add(pasteSelButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton copySelButton = new JButton("Copy");
        copySelButton.addActionListener(this);
        setModifySelPanel.add(copySelButton, gbc);
        
        JPanel setMinPanel = new JPanel();
        setMinPanel.setLayout(layout);
        setMinPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Modify Selected"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
        
        JRadioButton dragAtomButton = new JRadioButton("Drag Atom");
        JRadioButton dragAtomMinButton = new JRadioButton("Drag Atom (and Minimize)");
        JRadioButton dragSelAtomButton = new JRadioButton("Drag Selected Atoms");
        JRadioButton dragSelMinButton = new JRadioButton("Drag Selected Atoms (and Minimize)");
        dragAtomButton.setSelected(true);
        dragAtomButton.setActionCommand("Drag Atom");
        dragAtomMinButton.setActionCommand("Drag Atom (and Minimize)");
        dragSelAtomButton.setActionCommand("Drag Selected Atoms");
        dragSelMinButton.setActionCommand("Drag Selected Atoms (and Minimize)");
        draggingGroup = new ButtonGroup();
        draggingGroup.add(dragAtomButton);
        draggingGroup.add(dragAtomMinButton);
        draggingGroup.add(dragSelAtomButton);
        draggingGroup.add(dragSelMinButton);
        dragAtomButton.addActionListener(this);
        dragAtomMinButton.addActionListener(this);
        dragSelAtomButton.addActionListener(this);
        dragSelMinButton.addActionListener(this);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        setMinPanel.add(dragAtomButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        setMinPanel.add(dragAtomMinButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        setMinPanel.add(dragSelAtomButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        setMinPanel.add(dragSelMinButton, gbc);
        
        setDragging = new JButton("Drag Atom(s) ON");
        setDragging.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        setMinPanel.add(setDragging, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel2.add(setPickingPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel2.add(setModifySelPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel2.add(setMinPanel, gbc);*/
        
        add(tabbedPane);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	
	}

	protected javax.swing.JComponent makeTextPanel(java.lang.String text) {
		// TODO - implement ToolPanel.makeTextPanel
		throw new UnsupportedOperationException();
	}

	public void setModelText(List<Model> modelList) {
		String modelDetail  = "";
		for(int i=0;i<modelList.size();i++){
			modelDetail += "Model Name: " + modelList.get(i).getModelName() + newline;
			modelDetail += "# of Chains: " + modelList.get(i).getMolecules().get(0).getChains().size() + newline;
			modelDetail += "# of Atoms: " + modelList.get(i).getAtomHash().size() + newline + newline;
		}
		textArea.setText(modelDetail);
		textArea.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() instanceof JComboBox){
			ToolPanelHandler.handleJComboBox(event,jmolPanel);	
		}
		else if(event.getSource() instanceof JButton){
			JButton source = (JButton) event.getSource();
			if(source.getText().contains("Picking")){
				ToolPanelHandler.handlePickingFunction(source, jmolPanel, pickingGroup);
			}
			else if(source.getText().contains("Select")){
				ToolPanelHandler.handleSelectionHalo(source, jmolPanel);
			}
			else if(source.getText().contains("Delete")|| source.getText().contains("Cut") ||
					source.getText().contains("Copy")|| source.getText().contains("Paste")){
				ToolPanelHandler.handleModifySelected(source,jmolPanel);
			}
			if(source.getText().contains("Drag")){
				ToolPanelHandler.handleDragging(source, jmolPanel, draggingGroup);
			}
		}
		else if(event.getSource() instanceof JRadioButton){
			JRadioButton source = (JRadioButton) event.getSource();
			if(source.getActionCommand().contains("Drag")){
				ToolPanelHandler.handleRadioDrag(setDragging, jmolPanel, draggingGroup);
			}
			else{
				ToolPanelHandler.handleRadioPick(setPicking, jmolPanel, pickingGroup);
			}
		}
	}

}