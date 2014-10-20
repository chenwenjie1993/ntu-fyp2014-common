package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.awt.event.*;

public class Main {

	private static org.biojava.bio.structure.Structure structure = null;
	private static javax.swing.JFrame frame;
	public static ToolPanel toolPanel;
	private static JmolDisplay jmolPanel;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO - implement Main.main
		throw new UnsupportedOperationException();
	}

	private static void initDisplay() {
		// TODO - implement Main.initDisplay
		throw new UnsupportedOperationException();
	}

	/**
	 * public void setStructure(Structure s) {
	 * System.out.println("Statring here?");
	 * if ( jmolPanel == null ){
	 * System.err.println("please install Jmol first");
	 * return;
	 * }
	 * 
	 * setTitle(s.getPDBCode());
	 * 
	 * // actually this is very simple
	 * // just convert the structure to a PDB file
	 * 
	 * String pdb = s.toPDB();
	 * //System.out.println(s.isNmr());
	 * 
	 * //System.out.println(pdb);
	 * // Jmol could also read the file directly from your file system
	 * //viewer.openFile("/Path/To/PDB/1tim.pdb");
	 * 
	 * //System.out.println(pdb);
	 * jmolPanel.openStringInline(pdb);
	 * 
	 * // send the PDB file to Jmol.
	 * // there are also other ways to interact with Jmol, e.g make it directly
	 * // access the biojava structure object, but they require more
	 * // code. See the SPICE code repository for how to do this.
	 * }
	 * 
	 * public void setTitle(String title) {
	 * frame.setTitle(title);
	 * frame.repaint();
	 * }
	 */
	public org.jmol.api.JmolViewer getViewer() {
		// TODO - implement Main.getViewer
		throw new UnsupportedOperationException();
	}

	public Main() {
		// TODO - implement Main.Main
		throw new UnsupportedOperationException();
	}


	static class ApplicationCloser extends WindowAdapter {

		/**
		 * 
		 * @param e
		 */
		public void windowClosing(java.awt.event.WindowEvent e) {
			// TODO - implement ApplicationCloser.windowClosing
			throw new UnsupportedOperationException();
		}

		ApplicationCloser() {
			// TODO - implement ApplicationCloser.ApplicationCloser
			throw new UnsupportedOperationException();
		}

	}


	class Window {

		Window() {
			// TODO - implement Window.Window
			throw new UnsupportedOperationException();
		}

		/**
		 * 
		 * @param we
		 */
		public void windowClosing(java.awt.event.WindowEvent we) {
			// TODO - implement Window.windowClosing
			throw new UnsupportedOperationException();
		}

	}


	class window {

		window() {
			// TODO - implement window.window
			throw new UnsupportedOperationException();
		}

		/**
		 * 
		 * @param we
		 */
		public void windowClosing(java.awt.event.WindowEvent we) {
			// TODO - implement window.windowClosing
			throw new UnsupportedOperationException();
		}

	}

}