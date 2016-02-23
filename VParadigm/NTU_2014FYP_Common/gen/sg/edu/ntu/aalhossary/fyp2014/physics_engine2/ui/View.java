package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
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



public class View extends JFrame{
	EventListener e;
	
	private JPanel contentPane;
	private JPanel forceFieldPanel;
//	private JSlider coeOfResSlider;
	private JLabel coeOfResLbl;
	private JPanel simulationPanel;
	private JSlider simSpdSlider;
	private JLabel simSpdLbl;
	private JCheckBox lennardJonesCheckBox;
	private JCheckBox electrostaticCheckBox;
	private JCheckBox bondCheckBox;
	private JCheckBox angleCheckBox;
	private JCheckBox properDihedralCheckBox;
	private JCheckBox improperDihedralCheckBox;
	
	private JPanel simLvlPanel;
	private JPanel forcesPanel;
	private JRadioButton atomicRadioButton;
	private JRadioButton molecularRadioButton;
	private JTextField commandTextField;
	
	private UpdateRegistry mediator;
	private JLabel lblSimulationMedium;
	private JPanel controlPanel;
	private JButton pauseButton;
	private JButton stopButton;
	private JButton restartButton;
	private JCheckBox partialMolCheckBox;
	
	private Map<String, Object> config;
	
	
	/**
	 * Create the main frame.
	 */
	public View(EventListener e, Map<String, Object> config) {
		super((String) config.get("name"));
		
		this.e = e;
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
		
		Viewer viewer = jmolPanel.getViewer();
		mediator = new UpdateRegistry(viewer);
		viewer.setPercentVdwAtom(20);
		viewer.evalString("load " + (String) config.get("dir") + "conf.gro");
		viewer.evalString("wireframe only;wireframe reset;spacefill reset");
		viewer.evalString("set mouseDragFactor 1.0");
        jmolPanel.setMediator(mediator);
		
		// add RHS Panel for user input
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3, 1, 10, 10));
		inputPanel.setBorder(new EmptyBorder(0,5,0,5));
		contentPane.add(inputPanel, BorderLayout.EAST);
		
//		createForceFieldPanel();
//		inputPanel.add(simLvlPanel);
		
		
//		JPanel padding = new JPanel();
//		padding.setSize(20, 20);
//		inputPanel.add(padding);
		
		// add slider and label for coefficient of restitution
//		createCoeOfResPanel();
//		inputPanel.add(forceFieldPanel);
//		JPanel padding2 = new JPanel();
//		padding2.setSize(20, 20);
//		inputPanel.add(padding2);
		
		createSimulationPanel();
		simulationPanel.setAlignmentX(LEFT_ALIGNMENT);
		inputPanel.add(simulationPanel);
//		JPanel padding4 = new JPanel();
//		padding4.setSize(20, 20);
//		inputPanel.add(padding4);
		
		
		createForcesPanel();
		forcesPanel.setAlignmentX(LEFT_ALIGNMENT);
//		forcesPanel.
		inputPanel.add(forcesPanel);
//		JPanel padding3 = new JPanel();
//		padding3.setSize(20, 20);
//		inputPanel.add(padding3);
		
		controlPanel = new JPanel();
		controlPanel.setAlignmentX(LEFT_ALIGNMENT);
		inputPanel.add(controlPanel);
		
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pauseButton.getText().equals("Pause")){
					pauseButton.setText("Resume");
//					World.simulationStatus = "paused";
			}
				else{
					pauseButton.setText("Pause");
//					World.simulationStatus = "running";
//					World.countDownLatch.countDown();
				}
			}
		});
		controlPanel.add(pauseButton);
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stopButton.getText().equals("Stop")){
					pauseButton.setText("Start");
			}
				else{
					stopButton.setText("Stop");

				}
			}
		});
		controlPanel.add(stopButton);
		
		restartButton = new JButton("Restart");
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				if(World.simulationStatus.equals("paused")){
//					World.countDownLatch.countDown();
//					pauseButton.setText("Pause");
//				}
//				World.simulationStatus = "restart";
			}
		});
		controlPanel.add(restartButton);
		
		// add text field for user input
		commandTextField = new JTextField();
		contentPane.add(commandTextField, BorderLayout.SOUTH);
		commandTextField.requestFocusInWindow();
		
		// add action listeners
		addActionListeners();
		this.setVisible(true);
	}
	
	/**
	 * Return display interface from JMol
	 * @return mediator
	 */
	public UpdateRegistry getMediator(){
		return mediator;
	}
	
	/**
	 * Create the panel for setting the coefficient of resolution
	 */
//	private void createCoeOfResPanel (){
//		
//		forceFieldPanel = new JPanel();
//		forceFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
//		forceFieldPanel.setLayout(new BoxLayout(forceFieldPanel, BoxLayout.Y_AXIS));	
//		
//		JPanel panel1 = new JPanel();
//	//	panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
//		
//		JLabel label1 = new JLabel("Coefficient of Restitution: ");
//		panel1.add(label1);
//		
////		coeOfResLbl = new JLabel(String.valueOf(World.COEFFICENT_OF_RESTITUTION));
//		coeOfResLbl = new JLabel("coe");
//		coeOfResLbl.setAlignmentY(Component.TOP_ALIGNMENT);
//		panel1.add(coeOfResLbl);
//		
//		lblSimulationMedium = new JLabel("Simulation Medium");
//		lblSimulationMedium.setAlignmentX(Component.CENTER_ALIGNMENT);
//		forceFieldPanel.add(lblSimulationMedium);
//		
////		coeOfResSlider = new JSlider(0,100);
////		coeOfResSlider.setSnapToTicks(true);
////		coeOfResSlider.setPaintTicks(true);
////		coeOfResSlider.setPaintLabels(true);
////		forceFieldPanel.add(coeOfResSlider);
////		coeOfResSlider.setValue((int)(World.COEFFICENT_OF_RESTITUTION*100));
////		coeOfResSlider.setValue(100);
//		
//		forceFieldPanel.add(panel1);
//		
//	}
	
	private void createSimulationPanel(){

        simulationPanel = new JPanel(new GridLayout(3, 2));
        simulationPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

        JLabel lbTimeDelta = new JLabel("Time Delta(ns): ", SwingConstants.RIGHT);
        JLabel lbMolecule = new JLabel("Molecule: ", SwingConstants.RIGHT);
        JLabel lbForceField = new JLabel("Force Field: ", SwingConstants.RIGHT);
        
        
        JTextField tfTimeDelta = new JTextField(config.get("timeDelta").toString());
        JTextField tfMolecule = new JTextField();

        String[] options = { "Amber03" };
        JComboBox<String> cbForceField = new JComboBox<String>(options);
        cbForceField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        
        simulationPanel.add(lbTimeDelta);
        simulationPanel.add(tfTimeDelta);
        
        simulationPanel.add(lbMolecule);
        simulationPanel.add(tfMolecule);
        
        simulationPanel.add(lbForceField);
        simulationPanel.add(cbForceField);

		
//		simulationPanel = new JPanel();
		
//		String[] labels = {"Time Delta: ", "Molecule Path: ", "Email: ", "Address: "};
//		int numPairs = labels.length;
//
//		//Create and populate the panel.
//		JPanel p = new JPanel(new SpringLayout());
//		for (int i = 0; i < numPairs; i++) {
//		    JLabel l = new JLabel(labels[i], JLabel.TRAILING);
//		    p.add(l);
//		    JTextField textField = new JTextField(10);
//		    l.setLabelFor(textField);
//		    p.add(textField);
//		}
//		
//		String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
//
//		//Create the combo box, select item at index 4.
//		//Indices start at 0, so 4 specifies the pig.
//		JComboBox petList = new JComboBox(petStrings);
//		petList.setSelectedIndex(0);
////		petList.addActionListener(this);
//		JLabel lbForceField = new JLabel("Force Field: ", JLabel.TRAILING);
//		p.add(lbForceField);
////		lbForceField.setLabelFor(petList);
//		p.add(petList);
		
//		simulationPanel = p;

		//Lay out the panel.
//		SpringUtilities.makeCompactGrid(p,
//		                                numPairs, 2, //rows, cols
//		                                6, 6,        //initX, initY
//		                                6, 6);       //xPad, yPad
		
		
//		simulationPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
//		simulationPanel.setLayout(new BoxLayout(simulationPanel, BoxLayout.Y_AXIS));	
//		
//		JPanel panel1 = new JPanel();
//	//	panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
//		
//		JLabel label1 = new JLabel("Simulation Settings ");
//		panel1.add(label1);
//		
////		simSpdLbl = new JLabel(String.valueOf(World.frameTime_as));
////		simSpdLbl = new JLabel("0.002");
////		simSpdLbl.setAlignmentY(Component.TOP_ALIGNMENT);
////		panel1.add(simSpdLbl);
//		
//		JLabel l = new JLabel("Time Delta");
//		l.setAlignmentX(Component.LEFT_ALIGNMENT);
//		simulationPanel.add(l);
//		
//		JTextField tfTimeDelta = new JTextField("0.0002");
//		
//		
////		simSpdSlider = new JSlider(0,100);
////		simSpdSlider.setSnapToTicks(true);
////		simSpdSlider.setPaintTicks(true);
////		simSpdSlider.setPaintLabels(true);
//		simulationPanel.add(tfTimeDelta);
////		simSpdSlider.setValue((int)(World.frameTime_as));
//		simulationPanel.add(panel1);
	}
	

//	private void createForceFieldPanel (){
//		
//		simLvlPanel = new JPanel();
//		simLvlPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
//		simLvlPanel.setLayout(new BoxLayout(simLvlPanel, BoxLayout.Y_AXIS));
//		
//		JLabel label1 = new JLabel("Simulation Level");
//		label1.setAlignmentX(Component.CENTER_ALIGNMENT);
//		simLvlPanel.add(label1);
//		
//		ButtonGroup bg = new ButtonGroup();
//		
//		atomicRadioButton = new JRadioButton("atomic");
//	    molecularRadioButton = new JRadioButton("molecular");
//	    bg.add(atomicRadioButton);
//	    bg.add(molecularRadioButton);
//	    partialMolCheckBox = new JCheckBox("Partial Molecular");
//	    
////	    if(World.simulationLvlAtomic == true) {
////	    	atomicRadioButton.setSelected(true);
////	    	partialMolCheckBox.setEnabled(false);
////	    	partialMolCheckBox.setSelected(false);
////	    }
////	    else {
////	    	molecularRadioButton.setSelected(true);
////	    	partialMolCheckBox.setEnabled(true);
////	    	partialMolCheckBox.setSelected(World.simulationLvlPartial);
////	    }
//	    
//	    JPanel panel2 = new JPanel();
//	    panel2.add(atomicRadioButton);
//	    panel2.add(molecularRadioButton);
//		simLvlPanel.add(panel2);	
//		JPanel panel1 = new JPanel();
//		panel1.add(partialMolCheckBox);
//		simLvlPanel.add(panel1);
//	}
	
	private void createForcesPanel(){
		forcesPanel = new JPanel();
		forcesPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
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
			System.out.println("add");
		}
		
//		lennardJonesCheckBox = new JCheckBox("Lennard-Jones Force");
//		lennardJonesCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
//	    electrostaticCheckBox = new JCheckBox("Electrostatic Force");
//	    electrostaticCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
//	    
//	    bondCheckBox = new JCheckBox("Bond Stretching Force");
//	    bondCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
//	    angleCheckBox = new JCheckBox("Bond Angle Rotation Force");
//	    angleCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
//	    properDihedralCheckBox = new JCheckBox("Bond Proper Dihedral Force");
//	    properDihedralCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
//	    improperDihedralCheckBox = new JCheckBox("Bond Improper Dihedral Force");
//	    improperDihedralCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
	 
//	    if(World.electricForceActive)
//	    	electrostaticCheckBox.setSelected(true);
//	    if(World.LJForceActive)
//	    	lennardJonesCheckBox.setSelected(true);
	    
//	    JPanel panel2 = new JPanel();
//	    panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
	    JLabel label1 = new JLabel("Forces");
	    label1.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
//	    panel2.add(label1);
//	    panel2.add(electrostaticCheckBox);
//	    panel2.add(lennardJonesCheckBox);
//	    panel2.add(bondCheckBox);
//	    panel2.add(angleCheckBox);
//	    panel2.add(properDihedralCheckBox);
//	    panel2.add(improperDihedralCheckBox);
//		forcesPanel.add(panel2, BorderLayout.CENTER);	
	}

	/**
	 * Add ActionListeners for UI inputs
	 */
	private void addActionListeners() {
		DecimalFormat formatter = new DecimalFormat("0.00");
		
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
		
		commandTextField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	if(e.getKeyCode() != 10)	// enter key
            		return;
            	
            	JTextField textField = (JTextField) e.getSource();
                String text = textField.getText();
                commandTextField.setText(text.toUpperCase()); 
                try {
//					World.setCommand(textField.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
		});
		
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
		
	}
		
}

