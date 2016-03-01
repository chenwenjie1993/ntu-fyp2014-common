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
//	private JSlider coeOfResSlider;
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
	private JComboBox cbMolecule;
	
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
		viewer.evalString("load " + (String) config.get("dir") + "/" + (String) config.get("name") + "/conf.gro");
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
				if (listener.getStatus().equals("Running")){
					pauseButton.setText("Resume");
					listener.onPause();
			}
				else{
					pauseButton.setText("Pause");
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
					listener.onStop();
				}
				else{
					stopButton.setText("Stop");
					listener.onRestart();
					System.out.println("restart done");
				}
			}
		});
		controlPanel.add(stopButton);
		
		restartButton = new JButton("Restart");
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.onRestart();
			}
		});
		controlPanel.add(restartButton);
		
		// add text field for user input
		commandTextField = new JTextField();
		contentPane.add(commandTextField, BorderLayout.SOUTH);
		commandTextField.requestFocusInWindow();
		
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
		viewer.evalString("load " + (String) config.get("dir") + "conf.gro");
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
                
        JTextField tfTimeDelta = new JTextField(config.get("timeDelta").toString());
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
		        
        JComboBox cbForceField = new JComboBox();
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
	}
	
	private void createForcesPanel(){
		forcesPanel = new JPanel();
		forcesPanel.setBorder(BorderFactory.createTitledBorder("Force Field Parameters"));
//		forcesPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		forcesPanel.setLayout(new BoxLayout(forcesPanel, BoxLayout.Y_AXIS));
		
		Map<String, Object> params = (Map<String, Object>) config.get("ffParams");
		
		for (Map.Entry<String, Object> e: params.entrySet()) {
			if (e.getValue() instanceof Boolean) {
				JCheckBox cb = new JCheckBox(e.getKey());
				cb.setAlignmentX(LEFT_ALIGNMENT);
				cb.addActionListener(new ActionListener(){
				    public void actionPerformed(ActionEvent e) {
				    	JCheckBox cb = (JCheckBox) e.getSource();
				    	config.put(cb.getText(), cb.isSelected());
				    }
				});
				forcesPanel.add(cb);
			}
		}

	    JLabel label1 = new JLabel("Forces");
	    label1.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	}

	/**
	 * Add ActionListeners for UI inputs
	 */
//	private void addActionListeners() {
//		DecimalFormat formatter = new DecimalFormat("0.00");
		
//		coeOfResSlider.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent event) {
//                double currentVal = ((JSlider)event.getSource()).getValue()/100.0;
//                coeOfResLbl.setText(formatter.format(currentVal));
////                World.COEFFICENT_OF_RESTITUTION = currentVal;
////                World.resetActiveParticlesVelocities();
//            }
//        });
		
//		simSpdSlider.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent event) {
//                int currentVal = ((JSlider)event.getSource()).getValue();
//                simSpdLbl.setText(String.valueOf(currentVal));
////                World.frameTime_as = currentVal;
////                World.resetActiveParticlesVelocities();
//            }
//        });
		
//		commandTextField.addKeyListener(new KeyAdapter() {
//            public void keyReleased(KeyEvent e) {
//            	if(e.getKeyCode() != 10)	// enter key
//            		return;
//            	
//            	JTextField textField = (JTextField) e.getSource();
//                String command = textField.getText();
////                commandTextField.setText(text.toUpperCase()); 
//                try {
//                	getMediator().getViewer().evalString(command);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//            }
//		});
		
//		atomicRadioButton.addActionListener(new ActionListener(){
//		    public void actionPerformed(ActionEvent e) {
//		    	JRadioButton button = (JRadioButton) e.getSource();
//		        if (button.getText().equals("atomic")){
////		        	World.simulationLvlAtomic = true;
//		        	electrostaticCheckBox.setEnabled(true);
////		        	electrostaticCheckBox.setSelected(World.electricForceActive);
//		        	
////		        	World.frameTime_as = 50;
//		        	simSpdLbl.setText("50");
//		        	simSpdSlider.setValue(50);
//		        	partialMolCheckBox.setEnabled(false);
//			    	partialMolCheckBox.setSelected(false);
////			    	World.simulationStatus = "changed";
//		        }
//		    }
//		});
		
//		molecularRadioButton.addActionListener(new ActionListener(){
//		    public void actionPerformed(ActionEvent e) {
//		    	JRadioButton button = (JRadioButton) e.getSource();
//		        if (button.getText().equals("molecular")){
////		        	World.simulationLvlAtomic = false;
//		    
//		        	partialMolCheckBox.setEnabled(true);
////			    	partialMolCheckBox.setSelected(World.simulationLvlPartial);
////		        	World.simulationStatus = "changed";
//		        }
//		    }
//		});
		
//		lennardJonesCheckBox.addActionListener(new ActionListener(){
//		    public void actionPerformed(ActionEvent e) {
//		    	JCheckBox checkBox = (JCheckBox) e.getSource();
//		        if (checkBox.getText().equals("Lennard-Jones Force")){
////		        	if(checkBox.isSelected())
////		        		World.LJForceActive = true;
////		        	else
////		        		World.LJForceActive = false;
//		        }
////		        World.resetActiveParticlesVelocities();
//		    }
//		});
		
//		electrostaticCheckBox.addActionListener(new ActionListener(){
//		    public void actionPerformed(ActionEvent e) {
//		    	JCheckBox checkBox = (JCheckBox) e.getSource();
//		        if (checkBox.getText().equals("Electrostatic Force")){
////		        	if(checkBox.isSelected())
////		        		World.electricForceActive = true;
////		        	else
////		        		World.electricForceActive = false;
//		        }
////		        World.resetActiveParticlesVelocities();
//		     //   World.simulationStatus = "restart";
//		    }
//		});
		
//		partialMolCheckBox.addActionListener(new ActionListener(){
//		    public void actionPerformed(ActionEvent e) {
//		    	JCheckBox checkBox = (JCheckBox) e.getSource();
//		        if (checkBox.getText().equals("Partial Molecular")){
////		        	if(checkBox.isSelected())
////		        		World.simulationLvlPartial = true;
////		        	else
////		        		World.simulationLvlPartial = false;
//		        }
////		        World.resetActiveParticlesVelocities();
////		        World.simulationStatus = "changed";
//		    }
//		});
		
//	}
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

