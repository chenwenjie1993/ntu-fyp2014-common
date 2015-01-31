package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.JmolDisplay;

public class RasmolFieldListener extends KeyAdapter implements ActionListener, MouseListener {

	javax.swing.JTextField textfield;
	java.util.List<String> history;
	int historyPosition;
	JmolDisplay jmolPanel;

	/**
	 * 
	 * @param panel
	 * @param field
	 */
	public RasmolFieldListener(JmolDisplay panel, javax.swing.JTextField field) {
		textfield = field;
		jmolPanel = panel;
		
		history = new ArrayList<String>();
		historyPosition = -2; // -2 = history = empty;
	}

	/**
	 * 
	 * @param e
	 */
	public void mouseClicked(java.awt.event.MouseEvent e) {
		String cmd = textfield.getText();
		if ( cmd.equals("enter RASMOL like command...")){
			textfield.setText("");
			textfield.repaint();
		}
	}

	/**
	 * 
	 * @param e
	 */
	public void mousePressed(java.awt.event.MouseEvent e) {
		// TODO - implement RasmolFieldListener.mousePressed
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param e
	 */
	public void mouseReleased(java.awt.event.MouseEvent e) {
		// TODO - implement RasmolFieldListener.mouseReleased
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param e
	 */
	public void mouseEntered(java.awt.event.MouseEvent e) {
		// TODO - implement RasmolFieldListener.mouseEntered
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param e
	 */
	public void mouseExited(java.awt.event.MouseEvent e) {
		// TODO - implement RasmolFieldListener.mouseExited
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param e
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		String cmd = textfield.getText();
		if(cmd.contains("load ")){
			String filePath = cmd.substring(5);
			File file = new File(filePath);
			DataManager.readFile(file.getAbsolutePath(), jmolPanel.getMediator());
		}
		else
			jmolPanel.executeCmd(cmd);
		textfield.setText("");

		// now comes history part:

		// no need for history:
		if ( cmd.equals("")) return;

		// check last command in history
		// if equivalent, don't add,
		// otherwise add               
		if (history.size()>0){
			String txt=(String)history.get(history.size()-1);
			if (! txt.equals(cmd)) {
				history.add(cmd);  
			}
		} else {             
			// the first time always add
			history.add(cmd);
		}
		historyPosition=history.size();
	}

	/**
	 * 
	 * @param e
	 */
	public void keyReleased(java.awt.event.KeyEvent e) {
		int code = e.getKeyCode();
		//String s = e.getKeyText(code);
		//System.out.println(s);
		if (( code == KeyEvent.VK_UP ) || 
				( code == KeyEvent.VK_KP_UP)) {
			// go one back in history;
			if ( historyPosition > 0){
				historyPosition= historyPosition-1;              
			} 
		} else if (( code == KeyEvent.VK_DOWN ) || 
				( code == KeyEvent.VK_KP_DOWN)) {            
			if ( historyPosition < (history.size()-1) ){
				historyPosition++;                
			} else {
				// clear command if at beginning of history
				textfield.setText("");
				historyPosition=history.size();
				return;
			}
		} else if ( code == KeyEvent.VK_PAGE_UP) {
			if ( historyPosition > 0) {
				historyPosition = 0;
			}
		} else if ( code == KeyEvent.VK_PAGE_DOWN) {
			if ( historyPosition >= 0) {
				historyPosition = history.size()-1;
			}
		} else {
			// some other key has been pressed, do nothing
			return;
		}

		if ( historyPosition >= 0) {
			String txt = (String)history.get(historyPosition);
			textfield.setText(txt);
		}

	}

}