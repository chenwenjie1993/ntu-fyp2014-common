package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTextField;

import org.biojava.bio.structure.Structure;
import org.jmol.api.JmolViewer;

public class Main {

	private static Structure structure = null;
	private static JFrame frame;
	public static ToolPanel toolPanel;
	private static JmolDisplay jmolPanel;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		initDisplay();
	}

	private static void initDisplay() {
		frame = new JFrame();
		JMenuBar menu = MenuCreator.initMenu(frame);
        frame.setJMenuBar(menu);
        frame.addWindowListener(new WindowAdapter() {
        	  public void windowClosing(WindowEvent we) {
        		    System.exit(0);
        		  }
        		});
        Container contentPane = frame.getContentPane();
        jmolPanel = new JmolDisplay();
        jmolPanel.setPreferredSize(new Dimension(500,500));
        
        Box vBox = Box.createVerticalBox();
        vBox.add(jmolPanel);
        
        JTextField field = new JTextField();
		field.setMaximumSize(new Dimension(Short.MAX_VALUE,30));
		field.setText("enter RASMOL like command...");
		RasmolFieldListener rasmolcmd = new RasmolFieldListener(jmolPanel, field);
		field.addActionListener(rasmolcmd);
		vBox.add(field);
		
		Box hBox = Box.createHorizontalBox();
		hBox.add(vBox);
		
		toolPanel = new ToolPanel(jmolPanel);
		toolPanel.setMaximumSize(new Dimension(250,Short.MAX_VALUE));
		hBox.add(toolPanel);
 
        contentPane.add(hBox);
        frame.pack();
        frame.setVisible(true);
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
	
	public JmolViewer getViewer() {
		return jmolPanel.getViewer();
	}


	static class ApplicationCloser extends WindowAdapter {

		/**
		 * 
		 * @param e
		 */
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}

		ApplicationCloser() {
			// TODO - implement ApplicationCloser.ApplicationCloser
			throw new UnsupportedOperationException();
		}

	}
}