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
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
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

import javax.swing.border.EtchedBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;



public class View extends JFrame{
	EventListener e;
	
	private JPanel contentPane;
	private JPanel coeOfResPanel;
	private JSlider coeOfResSlider;
	private JLabel coeOfResLbl;
	private JPanel simSpdPanel;
	private JSlider simSpdSlider;
	private JLabel simSpdLbl;
	private JCheckBox lennardJonesCheckBox;
	private JCheckBox electrostaticCheckBox;
	
	private JPanel simLvlPanel;
	private JPanel forcesPanel;
	private JRadioButton atomicRadioButton;
	private JRadioButton molecularRadioButton;
	private JTextField commandTextField;
	
	private UpdateRegistry mediator;
	private JLabel lblSimulationMedium;
	private JPanel panel;
	private JButton pauseButton;
	private JButton restartButton;
	private JCheckBox partialMolCheckBox;
	
	
	/**
	 * Create the main frame.
	 */
	public View(EventListener e, Map<String, Object> config) {
		super((String) config.get("name"));
		
		this.e = e;
		
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
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.setBorder(new EmptyBorder(0,5,0,5));
		contentPane.add(inputPanel, BorderLayout.EAST);
		
		createSimLvlPanel();
		inputPanel.add(simLvlPanel);
		
		
		JPanel padding = new JPanel();
		padding.setSize(20, 20);
		inputPanel.add(padding);
		
		// add slider and label for coefficient of restitution
		createCoeOfResPanel();
		inputPanel.add(coeOfResPanel);
		JPanel padding2 = new JPanel();
		padding2.setSize(20, 20);
		inputPanel.add(padding2);
		
		createSimSpdPanel();
		inputPanel.add(simSpdPanel);
		JPanel padding4 = new JPanel();
		padding4.setSize(20, 20);
		inputPanel.add(padding4);
		
		
		createForcesPanel();
		inputPanel.add(forcesPanel);
		JPanel padding3 = new JPanel();
		padding3.setSize(20, 20);
		inputPanel.add(padding3);
		
		panel = new JPanel();
		inputPanel.add(panel);
		
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
		panel.add(pauseButton);
		
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
		panel.add(restartButton);
		
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
	private void createCoeOfResPanel (){
		
		coeOfResPanel = new JPanel();
		coeOfResPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		coeOfResPanel.setLayout(new BoxLayout(coeOfResPanel, BoxLayout.Y_AXIS));	
		
		JPanel panel1 = new JPanel();
	//	panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		
		JLabel label1 = new JLabel("Coefficient of Restitution: ");
		panel1.add(label1);
		
//		coeOfResLbl = new JLabel(String.valueOf(World.COEFFICENT_OF_RESTITUTION));
		coeOfResLbl = new JLabel("coe");
		coeOfResLbl.setAlignmentY(Component.TOP_ALIGNMENT);
		panel1.add(coeOfResLbl);
		
		lblSimulationMedium = new JLabel("Simulation Medium");
		lblSimulationMedium.setAlignmentX(Component.CENTER_ALIGNMENT);
		coeOfResPanel.add(lblSimulationMedium);
		
		coeOfResSlider = new JSlider(0,100);
		coeOfResSlider.setSnapToTicks(true);
		coeOfResSlider.setPaintTicks(true);
		coeOfResSlider.setPaintLabels(true);
		coeOfResPanel.add(coeOfResSlider);
//		coeOfResSlider.setValue((int)(World.COEFFICENT_OF_RESTITUTION*100));
		coeOfResSlider.setValue(100);
		
		coeOfResPanel.add(panel1);
		
	}
	
	private void createSimSpdPanel(){
		simSpdPanel = new JPanel();
		simSpdPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		simSpdPanel.setLayout(new BoxLayout(simSpdPanel, BoxLayout.Y_AXIS));	
		
		JPanel panel1 = new JPanel();
	//	panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		
		JLabel label1 = new JLabel("Frame Duration (attosecond): ");
		panel1.add(label1);
		
//		simSpdLbl = new JLabel(String.valueOf(World.frameTime_as));
		simSpdLbl = new JLabel("100");
		simSpdLbl.setAlignmentY(Component.TOP_ALIGNMENT);
		panel1.add(simSpdLbl);
		
		JLabel l = new JLabel("Frame Duration");
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		simSpdPanel.add(l);
		
		simSpdSlider = new JSlider(0,100);
		simSpdSlider.setSnapToTicks(true);
		simSpdSlider.setPaintTicks(true);
		simSpdSlider.setPaintLabels(true);
		simSpdPanel.add(simSpdSlider);
//		simSpdSlider.setValue((int)(World.frameTime_as));
		simSpdPanel.add(panel1);
	}
	
	/**
	 * Create the panel for setting the simulation level (atomic or molecular)
	 */
	private void createSimLvlPanel (){
		
		simLvlPanel = new JPanel();
		simLvlPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		simLvlPanel.setLayout(new BoxLayout(simLvlPanel, BoxLayout.Y_AXIS));
		
		JLabel label1 = new JLabel("Simulation Level");
		label1.setAlignmentX(Component.CENTER_ALIGNMENT);
		simLvlPanel.add(label1);
		
		ButtonGroup bg = new ButtonGroup();
		
		atomicRadioButton = new JRadioButton("atomic");
	    molecularRadioButton = new JRadioButton("molecular");
	    bg.add(atomicRadioButton);
	    bg.add(molecularRadioButton);
	    partialMolCheckBox = new JCheckBox("Partial Molecular");
	    
//	    if(World.simulationLvlAtomic == true) {
//	    	atomicRadioButton.setSelected(true);
//	    	partialMolCheckBox.setEnabled(false);
//	    	partialMolCheckBox.setSelected(false);
//	    }
//	    else {
//	    	molecularRadioButton.setSelected(true);
//	    	partialMolCheckBox.setEnabled(true);
//	    	partialMolCheckBox.setSelected(World.simulationLvlPartial);
//	    }
	    
	    JPanel panel2 = new JPanel();
	    panel2.add(atomicRadioButton);
	    panel2.add(molecularRadioButton);
		simLvlPanel.add(panel2);	
		JPanel panel1 = new JPanel();
		panel1.add(partialMolCheckBox);
		simLvlPanel.add(panel1);
	}
	
	private void createForcesPanel(){
		forcesPanel = new JPanel();
		forcesPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
	//	forcesPanel.setLayout(new BoxLayout(forcesPanel, BoxLayout.Y_AXIS));
		forcesPanel.setLayout(new BorderLayout());
		
		ButtonGroup bg = new ButtonGroup();
		lennardJonesCheckBox = new JCheckBox("Lennard-Jones Force");
		lennardJonesCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
	    electrostaticCheckBox = new JCheckBox("Electrostatic Force");
	    electrostaticCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
	 
//	    if(World.electricForceActive)
//	    	electrostaticCheckBox.setSelected(true);
//	    if(World.LJForceActive)
//	    	lennardJonesCheckBox.setSelected(true);
	    
	    JPanel panel2 = new JPanel();
	    panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
	    JLabel label1 = new JLabel("Forces");
	    label1.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    panel2.add(label1);
	    panel2.add(electrostaticCheckBox);
	    panel2.add(lennardJonesCheckBox);
		forcesPanel.add(panel2, BorderLayout.CENTER);	
	}

	/**
	 * Add ActionListeners for UI inputs
	 */
	private void addActionListeners() {
		DecimalFormat formatter = new DecimalFormat("0.00");
		
		coeOfResSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
                double currentVal = ((JSlider)event.getSource()).getValue()/100.0;
                coeOfResLbl.setText(formatter.format(currentVal));
//                World.COEFFICENT_OF_RESTITUTION = currentVal;
//                World.resetActiveParticlesVelocities();
            }
        });
		
		simSpdSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
                int currentVal = ((JSlider)event.getSource()).getValue();
                simSpdLbl.setText(String.valueOf(currentVal));
//                World.frameTime_as = currentVal;
//                World.resetActiveParticlesVelocities();
            }
        });
		
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
		
		atomicRadioButton.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	JRadioButton button = (JRadioButton) e.getSource();
		        if (button.getText().equals("atomic")){
//		        	World.simulationLvlAtomic = true;
		        	electrostaticCheckBox.setEnabled(true);
//		        	electrostaticCheckBox.setSelected(World.electricForceActive);
		        	
//		        	World.frameTime_as = 50;
		        	simSpdLbl.setText("50");
		        	simSpdSlider.setValue(50);
		        	partialMolCheckBox.setEnabled(false);
			    	partialMolCheckBox.setSelected(false);
//			    	World.simulationStatus = "changed";
		        }
		    }
		});
		
		molecularRadioButton.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	JRadioButton button = (JRadioButton) e.getSource();
		        if (button.getText().equals("molecular")){
//		        	World.simulationLvlAtomic = false;
		    
		        	partialMolCheckBox.setEnabled(true);
//			    	partialMolCheckBox.setSelected(World.simulationLvlPartial);
//		        	World.simulationStatus = "changed";
		        }
		    }
		});
		
		lennardJonesCheckBox.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	JCheckBox checkBox = (JCheckBox) e.getSource();
		        if (checkBox.getText().equals("Lennard-Jones Force")){
//		        	if(checkBox.isSelected())
//		        		World.LJForceActive = true;
//		        	else
//		        		World.LJForceActive = false;
		        }
//		        World.resetActiveParticlesVelocities();
		    }
		});
		
		electrostaticCheckBox.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	JCheckBox checkBox = (JCheckBox) e.getSource();
		        if (checkBox.getText().equals("Electrostatic Force")){
//		        	if(checkBox.isSelected())
//		        		World.electricForceActive = true;
//		        	else
//		        		World.electricForceActive = false;
		        }
//		        World.resetActiveParticlesVelocities();
		     //   World.simulationStatus = "restart";
		    }
		});
		
		partialMolCheckBox.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	JCheckBox checkBox = (JCheckBox) e.getSource();
		        if (checkBox.getText().equals("Partial Molecular")){
//		        	if(checkBox.isSelected())
//		        		World.simulationLvlPartial = true;
//		        	else
//		        		World.simulationLvlPartial = false;
		        }
//		        World.resetActiveParticlesVelocities();
//		        World.simulationStatus = "changed";
		    }
		});
		
	}
		
}

