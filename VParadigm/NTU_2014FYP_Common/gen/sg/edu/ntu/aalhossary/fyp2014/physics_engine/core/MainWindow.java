package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener;

import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.JmolDisplay;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.MoleculeEditor;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.UpdateRegistry;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JPanel coeOfResPanel;
	private JSlider coeOfResSlider;
	private JLabel coeOfResLbl;
	private JTextField commandTextField;
	
	private UpdateRegistry mediator;
	private List<Model> modelList;
	
	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// add JMol Panel
		JmolDisplay jmolPanel = new JmolDisplay();
		contentPane.add(jmolPanel);
		
		// add mediator
		mediator = new UpdateRegistry(jmolPanel.getViewer(), modelList);
		jmolPanel.setMediator(mediator);
		
		// add slider and label for coefficient of restitution
		addCoeOfResPanel();
		
		// add text field for user input
		commandTextField = new JTextField();
		contentPane.add(commandTextField, BorderLayout.SOUTH);
		commandTextField.requestFocusInWindow();
		
		// add action listeners
		addActionListeners();
		this.setVisible(true);
	}
	
	public UpdateRegistry getMediator(){
		return mediator;
	}
	
	private void addCoeOfResPanel (){
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.EAST);
		
		coeOfResPanel = new JPanel();
		coeOfResPanel.setLayout(new BoxLayout(coeOfResPanel, BoxLayout.Y_AXIS));
		
		panel.add(coeOfResPanel);
		
		JPanel panel1 = new JPanel();
		JLabel label1 = new JLabel("Coefficient of Restitution: ");
		panel1.add(label1);
		
		coeOfResLbl = new JLabel(String.valueOf(World.COEFFICENT_OF_RESTITUTION));
		panel1.add(coeOfResLbl);
		
		coeOfResSlider = new JSlider(0,100);
		coeOfResSlider.setValue((int)(World.COEFFICENT_OF_RESTITUTION*100));
		
		coeOfResPanel.add(panel1);
		coeOfResPanel.add(coeOfResSlider);
	}
	
	private void addActionListeners() {
		DecimalFormat formatter = new DecimalFormat("0.00");
		
		coeOfResSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
                double currentVal = ((JSlider)event.getSource()).getValue()/100.0;
                coeOfResLbl.setText(formatter.format(currentVal));
                World.COEFFICENT_OF_RESTITUTION = currentVal;
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
					World.setCommand(textField.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
		});
	}
}
