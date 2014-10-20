package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuCreator {

	public MenuCreator() {
		// TODO - implement MenuCreator.MenuCreator
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param frame
	 */
	public static javax.swing.JMenuBar initMenu(javax.swing.JFrame frame) {
		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		file.getAccessibleContext().setAccessibleDescription("File Menu");
		
		JMenuItem openPDB = getOpenPDBMenuItem();
		file.add(openPDB);
		JMenuItem openMol2 = getOpenMol2MenuItem();
		file.add(openMol2);
		JMenuItem openMmcif = getOpenMmcifMenuItem();
		file.add(openMmcif);
		JMenuItem closeF = getCloseMenuItem();
		file.add(closeF);
		JMenuItem exitApp = getExitMenuItem();		
		file.add(exitApp);
		menu.add(file);
		
		JMenu edit = new JMenu("Edit");
		menu.add(edit);
		
		JMenu view = new JMenu("View");
		menu.add(view);
		
		return menu;
	}

	private static javax.swing.JMenuItem getOpenPDBMenuItem() {
		JMenuItem openFile = null;
		openFile =new JMenuItem("Open PDB file");
		openFile.setMnemonic(KeyEvent.VK_O);
		//openFile.addActionListener(new MyOpenPdbFileListener());
		return openFile;
	}

	private static javax.swing.JMenuItem getOpenMol2MenuItem() {
		JMenuItem openFile = null;
		openFile =new JMenuItem("Open Mol2 file");
		return openFile;
	}

	private static javax.swing.JMenuItem getOpenMmcifMenuItem() {
		JMenuItem openFile = null;
		openFile =new JMenuItem("Open mmcif file");
		return openFile;
	}

	private static javax.swing.JMenuItem getCloseMenuItem() {
		JMenuItem openFile = null;
		openFile =new JMenuItem("Open mmcif file");
		return openFile;
	}

	private static javax.swing.JMenuItem getExitMenuItem() {
		JMenuItem closeFile = null;
		closeFile =new JMenuItem("Close file");
		return closeFile;
	}

}