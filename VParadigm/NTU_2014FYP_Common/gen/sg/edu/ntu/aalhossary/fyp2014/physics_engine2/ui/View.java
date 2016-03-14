package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jmol.viewer.Viewer;

import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.JmolDisplay;
//import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry;
//import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.World;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.border.EtchedBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;


public class View extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EventListener listener;
	
	private JPanel contentPane;
	private JPanel simulationPanel;
	private JPanel forcesPanel;

	private JTextField commandTextField;
	
	private UpdateRegistry mediator;
	private JPanel controlPanel;
	private JButton pauseButton;
	private JButton stopButton;
	private JButton restartButton;
	
	private Map<String, Object> config;
	private Viewer viewer;
	
	// Fields in simulation panel
	private JComboBox cbMolecule;
	private JTextField tfTimeDelta;
	private JComboBox cbForceField;
	
	// Fields in force panel
	private ArrayList<JComponent> ffParamComponents = new ArrayList<JComponent>();
	
	/**
	 * Create the main frame.
	 */
	public View(EventListener listener, Map<String, Object> config) {
		super((String) config.get("name"));
		
		this.listener = listener;
		this.config = config;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// add JMol Panel
		JmolDisplay jmolPanel = new JmolDisplay();
		jmolPanel.setBorder(new EmptyBorder(5,0,5,0));
		contentPane.add(jmolPanel);
		
		// add mediator
		
		viewer = jmolPanel.getViewer();
		viewer.evalString("set debug OFF;");
		mediator = new UpdateRegistry(viewer);
		viewer.setPercentVdwAtom(20);
		viewer.evalString("");
		viewer.evalString("load " + (String) config.get("dir") + "/" + (String) config.get("name") + "/" + (String) config.get("name") + ".gro");
		viewer.evalString("wireframe only;wireframe reset;spacefill reset;");
//		viewer.evalString("set mouseDragFactor 1.0");
        jmolPanel.setMediator(mediator);
		
		// add RHS Panel for user input
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new EmptyBorder(0,5,0,5));
		contentPane.add(inputPanel, BorderLayout.EAST);
		
		createSimulationPanel();

		inputPanel.setLayout(new BorderLayout(0, 1));
		inputPanel.add(simulationPanel, BorderLayout.NORTH);
		
		createForcesPanel();
		inputPanel.add(forcesPanel, BorderLayout.CENTER);

		controlPanel = new JPanel();
		controlPanel.setAlignmentX(LEFT_ALIGNMENT);
		inputPanel.add(controlPanel, BorderLayout.SOUTH);
		
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listener.getStatus() > 0){
					pauseButton.setText("Resume");
					partialEnableSimulationPanel();
					enableForcePanel();
					listener.onPause();
				}
				else {
					pauseButton.setText("Pause");
					saveParams();
					disableForcePanel();
					disableSimulationPanel();
					listener.onResume();
				}
			}
		});
		controlPanel.add(pauseButton);
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stopButton.getText().equals("Stop")){
					stopButton.setText("Start");
					pauseButton.setEnabled(false);
					enableConfig();
					listener.onStop();
				}
				else{
					stopButton.setText("Stop");
					pauseButton.setText("Pause");
					pauseButton.setEnabled(true);
					saveParams();
					listener.onRestart();
					System.out.println("restart done");
				}
			}
		});
		controlPanel.add(stopButton);
		
		restartButton = new JButton("Restart");
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopButton.setText("Stop");
				pauseButton.setText("Pause");
				pauseButton.setEnabled(true);
				saveParams();
				listener.onRestart();
			}
		});
		controlPanel.add(restartButton);
		
		// add text field for user input
//		commandTextField = new JTextField();
//		contentPane.add(commandTextField, BorderLayout.SOUTH);
//		commandTextField.requestFocusInWindow();
		
		this.setVisible(true);
	}
	
	/**
	 * Return display interface from JMol
	 * @return mediator
	 */
	public UpdateRegistry getMediator(){
		return mediator;
	}
	
	public void reload() {
		System.out.println("Reloading view...");
		reloadViewer();
		reloadParams();
	}
	
	private void reloadViewer() {
		viewer.evalString("load " + (String) config.get("dir") + (String) config.get("name") + "/" + (String) config.get("name") + ".gro");
	}
	
	private void reloadParams() {
//		forcesPanel = null;
	}
	
	private void createSimulationPanel(){

        simulationPanel = new JPanel();
        simulationPanel.setBorder(BorderFactory.createTitledBorder("Simulation Parameters"));
//        simulationPanel.setToolTipText("sample text");
//        simulationPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        GridBagLayout gbl_simulationPanel = new GridBagLayout();
        gbl_simulationPanel.columnWeights = new double[]{0.0, 1.0};
        gbl_simulationPanel.rowWeights = new double[]{0.0, 0.0, 0.0};
        simulationPanel.setLayout(gbl_simulationPanel);
                
        JLabel lbTimeDelta = new JLabel("Time Delta(ns): ", SwingConstants.RIGHT);
        
        GridBagConstraints gbc_lbTimeDelta = new GridBagConstraints();
        gbc_lbTimeDelta.fill = GridBagConstraints.HORIZONTAL;
        gbc_lbTimeDelta.insets = new Insets(0, 0, 5, 5);
        gbc_lbTimeDelta.gridx = 0;
        gbc_lbTimeDelta.gridy = 0;
        simulationPanel.add(lbTimeDelta, gbc_lbTimeDelta);
                
        tfTimeDelta = new JTextField(config.get("timeDelta").toString());
        GridBagConstraints gbc_tfTimeDelta = new GridBagConstraints();
        gbc_tfTimeDelta.fill = GridBagConstraints.BOTH;
        gbc_tfTimeDelta.insets = new Insets(0, 0, 5, 0);
        gbc_tfTimeDelta.gridx = 1;
        gbc_tfTimeDelta.gridy = 0;
        simulationPanel.add(tfTimeDelta, gbc_tfTimeDelta);
        JLabel lbMolecule = new JLabel("Molecule: ", SwingConstants.RIGHT);
        
        GridBagConstraints gbc_lbMolecule = new GridBagConstraints();
        gbc_lbMolecule.anchor = GridBagConstraints.EAST;
        gbc_lbMolecule.insets = new Insets(0, 0, 5, 5);
        gbc_lbMolecule.fill = GridBagConstraints.VERTICAL;
        gbc_lbMolecule.gridx = 0;
        gbc_lbMolecule.gridy = 1;
        simulationPanel.add(lbMolecule, gbc_lbMolecule);
		
		cbMolecule = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		
		String[] molecules = getMoleculeList(); 
		
		cbMolecule.setModel(new DefaultComboBoxModel(molecules));
		
		String name = (String) config.get("name");
		System.out.println(name);
		for (int i=0; i<molecules.length; i++) {
			System.out.println(molecules[i]);
			if (name.equals(molecules[i])) {
				cbMolecule.setSelectedIndex(i);
				break;
			}
		}
		
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		simulationPanel.add(cbMolecule, gbc_comboBox);
		JLabel lbForceField = new JLabel("Force Field: ", SwingConstants.RIGHT);
		
		GridBagConstraints gbc_lbForceField = new GridBagConstraints();
		gbc_lbForceField.fill = GridBagConstraints.BOTH;
		gbc_lbForceField.insets = new Insets(0, 0, 0, 5);
		gbc_lbForceField.gridx = 0;
		gbc_lbForceField.gridy = 2;
		simulationPanel.add(lbForceField, gbc_lbForceField);
		        
        cbForceField = new JComboBox();
        cbForceField.setModel(new DefaultComboBoxModel(new String[] {"Amber03"}));
        cbForceField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            }
        });
        GridBagConstraints gbc_cbForceField = new GridBagConstraints();
        gbc_cbForceField.fill = GridBagConstraints.HORIZONTAL;
        gbc_cbForceField.gridx = 1;
        gbc_cbForceField.gridy = 2;
        simulationPanel.add(cbForceField, gbc_cbForceField);
        
        disableSimulationPanel();
	}
	
	protected void disableSimulationPanel() {
		tfTimeDelta.setEnabled(false);
		cbMolecule.setEnabled(false);
		cbForceField.setEnabled(false);
	}
	
	protected void partialEnableSimulationPanel() {
		tfTimeDelta.setEnabled(true);
//		cbMolecule.setEnabled(false);
//		cbForceField.setEnabled(false);
	}
	
	protected void enableSimulationPanel() {
		tfTimeDelta.setEnabled(true);
		cbMolecule.setEnabled(true);
		cbForceField.setEnabled(true);
	}
	
	private void createForcesPanel(){
		forcesPanel = new JPanel();
		forcesPanel.setBorder(BorderFactory.createTitledBorder("Force Field Parameters"));
		forcesPanel.setLayout(new BoxLayout(forcesPanel, BoxLayout.Y_AXIS));
		
		Map<String, Object> params = (Map<String, Object>) config.get("ffParams");
		
		for (Map.Entry<String, Object> e: params.entrySet()) {
			if (e.getValue() instanceof Boolean) {
				JCheckBox cb = new JCheckBox(e.getKey());
				cb.setAlignmentX(LEFT_ALIGNMENT);
				if ((Boolean) e.getValue()) {
					cb.setSelected(true);
				}
				cb.addActionListener(new ActionListener(){
				    public void actionPerformed(ActionEvent e) {
				    	JCheckBox cb = (JCheckBox) e.getSource();
				    	config.put(cb.getText(), cb.isSelected());
				    }
				});
				forcesPanel.add(cb);
				ffParamComponents.add(cb);
			}
		}

	    JLabel label1 = new JLabel("Forces");
	    label1.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    disableForcePanel();
	}
	
	protected void disableForcePanel() {
		for (JComponent c: ffParamComponents) {
			c.setEnabled(false);
		}
	}
	
	protected void enableForcePanel() {
		for (JComponent c: ffParamComponents) {
			c.setEnabled(true);
		}
	}
	
	protected void saveParams() {
		// fields in simulation panel
		config.put("name", (String) cbMolecule.getSelectedItem());
		config.put("timeDelta", Double.parseDouble(tfTimeDelta.getText()));
		config.put("forceField", (String) cbForceField.getSelectedItem());
		// fields in force panel
		Map<String, Object> params = (Map<String, Object>) config.get("ffParams");
		
		for (JComponent c: ffParamComponents) {
			if (c instanceof JCheckBox) {
				JCheckBox cb = (JCheckBox) c;
				String key = cb.getText();
				Boolean value = cb.isSelected();
				params.put(key, value);
			}
		}
		System.out.println(params.toString());
	}
	
	protected void disableConfig() {
		disableSimulationPanel();
		disableForcePanel();
	}
	
	protected void enableConfig() {
		enableSimulationPanel();
		enableForcePanel();
	}
	
	protected String[] getMoleculeList() {
		File f = null;
	    String[] paths = {};
	    List<String> molecules = new ArrayList<String>();
	            
	    try{      
	    	f = new File("res/amber03_test/");
            paths = f.list();
	    	for(String path:paths) {
	    		if (!path.equals(".DS_Store")) {
	    			molecules.add(path);
	    		}
	    	}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return molecules.toArray(new String[molecules.size()]);
	}
		
}

