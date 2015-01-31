package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.DataManager;

public class MenuPanel implements ActionListener{

	private JmolDisplay jmolPanel;
	private static final String openPDB = "Open PDB file";
	private static final String lysGly = "Lysine and Glysine";
	private static final String haemoglobin = "Haemoglobin";
	private static final String exitProg = "Exit Program";

	public MenuPanel(JmolDisplay jmolPanel) {
		this.jmolPanel = jmolPanel;
	}

	/**
	 * 
	 * @param frame
	 */
	public javax.swing.JMenuBar initMenu(javax.swing.JFrame frame) {
		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		file.getAccessibleContext().setAccessibleDescription("File Menu");
		
		JMenuItem openPDB = getOpenPDBMenuItem();
		file.add(openPDB);
		JMenu defaultModel = new JMenu("Default Model");
		JMenuItem lysineModel = getDefaultModel(lysGly);
		defaultModel.add(lysineModel);
		JMenuItem haemoglobinModel = getDefaultModel(haemoglobin);
		defaultModel.add(haemoglobinModel);
		file.add(defaultModel);
		JMenuItem exitApp = getExitMenuItem();		
		file.add(exitApp);
		menu.add(file);
		
		return menu;
	}

	private javax.swing.JMenuItem getOpenPDBMenuItem() {
		JMenuItem openFile = new JMenuItem(openPDB);
		//openFile.setMnemonic(KeyEvent.VK_O);
		openFile.addActionListener(this);
		return openFile;
	}
	
	private javax.swing.JMenuItem getDefaultModel(String modelName){
		JMenuItem model = new JMenuItem(modelName);
		model.addActionListener(this);
		return model;
	}

	private javax.swing.JMenuItem getExitMenuItem() {
		JMenuItem exit =new JMenuItem(exitProg);
		exit.addActionListener(this);
		return exit;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(openPDB)){
			System.out.println("Event: " +  e.getActionCommand());
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(new FileNameExtensionFilter("PDB Files", "pdb"));
			int returnVal = fc.showOpenDialog(jmolPanel);
			 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                DataManager.readFile(file.getAbsolutePath(), jmolPanel.getMediator());
            } else {
            	System.out.println("Open command cancelled by user.\n");
            }
		}
		else if(e.getActionCommand().equals(lysGly)){
			String dirName = "res/res/lys.pdb";
			File file = new File(dirName);
			DataManager.readFile(file.getAbsolutePath(), jmolPanel.getMediator());
		}
		else if(e.getActionCommand().equals(haemoglobin)){
			String dirName = "res/res/4hhb.pdb";
			File file = new File(dirName);
			DataManager.readFile(file.getAbsolutePath(), jmolPanel.getMediator());
		}
		else if(e.getActionCommand().equals(exitProg)){
			System.exit(0);
		}
	}
}