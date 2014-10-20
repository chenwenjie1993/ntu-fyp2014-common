package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import org.jmol.api.*;
import org.jmol.c.CBK;

public class MyJmolStatusListener implements JmolStatusListener {

	private boolean verbose;
	private boolean _verbose;
	public JmolDisplay jmolPanel;
	
	/**
	 * 
	 * @param jmolPanel
	 */
	public MyJmolStatusListener(JmolDisplay jmolPanel) {
		this.jmolPanel = jmolPanel;
	}
	
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * 
	 * @param callbackType
	 */
	public boolean notifyEnabled(CBK callbackType) {
		switch (callbackType) {
	    case ANIMFRAME: return false;
	    case ECHO: return false;
	    case LOADSTRUCT: return true;
	    case MEASURE: return false;
	    case MESSAGE: return false;
	    case PICK: return false;
	    case SCRIPT: return true;
	    case CLICK: return false;
	    case ERROR: return false;
	    case HOVER: return false;
	    case MINIMIZATION: return false;
	    case RESIZE: return false;
	    case SYNC: return false;
	      // applet only (but you could change this for your listener)
	    }
	    return false;
	}

	/**
	 * 
	 * @param callbackType
	 * @param data
	 */
	public void notifyCallback(org.jmol.c.CBK callbackType, java.lang.Object[] data) {
		System.out.println(callbackType + " " + data[0] + " " + data[1] + " " + data[2]);
		
		switch (callbackType) {
    	case LOADSTRUCT:	
    		if(data[1]==null || data[1].toString().contains("file[]"))// no data send
    			return;
    		else{
    			notifyFileLoaded((String) data[1], (String) data[2], (String) data[3], (String) data[4]);
    		}
    		return;
    	case SCRIPT: return;
    	default: return;
    }
	}

	/**
	 * 
	 * @param fullPathName
	 * @param fileName
	 * @param modelName
	 * @param errorMsg
	 */
	private void notifyFileLoaded(String fullPathName, String fileName, String modelName, String errorMsg) {
		if (errorMsg != null) {
			return;
		}
		jmolPanel.notifyNewFileOpen(fullPathName, modelName, fileName);
	}

	/**
	 * 
	 * @param callbackType
	 * @param callbackFunction
	 */
	public void setCallbackFunction(String callbackType, String callbackFunction) {
		System.out.println(callbackType);
		System.out.println(callbackFunction);
	}

	/**
	 * 
	 * @param aAStrEval
	 */
	public String eval(String aAStrEval) {
		// TODO - implement MyJmolStatusListener.eval
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aAFunctionName
	 * @param aAX
	 * @param aAY
	 */
	public float[][] functionXY(java.lang.String aAFunctionName, int aAX, int aAY) {
		// TODO - implement MyJmolStatusListener.functionXY
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aArg0
	 * @param aArg1
	 * @param aArg2
	 * @param aArg3
	 */
	public float[][][] functionXYZ(java.lang.String aArg0, int aArg1, int aArg2, int aArg3) {
		// TODO - implement MyJmolStatusListener.functionXYZ
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aAFileName
	 * @param aAType
	 * @param aAText_or_bytes
	 * @param aAQuality
	 */
	public java.lang.String createImage(java.lang.String aAFileName, java.lang.String aAType, java.lang.Object aAText_or_bytes, int aAQuality) {
		// TODO - implement MyJmolStatusListener.createImage
		throw new UnsupportedOperationException();
	}

	public java.util.Map getRegistryInfo() {
		// TODO - implement MyJmolStatusListener.getRegistryInfo
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aAUrl
	 */
	public void showUrl(java.lang.String aAUrl) {
		// TODO - implement MyJmolStatusListener.showUrl
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aAData
	 */
	public javajs.awt.Dimension resizeInnerPanel(java.lang.String aAData) {
		// TODO - implement MyJmolStatusListener.resizeInnerPanel
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aAType
	 */
	public java.util.Map getJSpecViewProperty(java.lang.String aAType) {
		// TODO - implement MyJmolStatusListener.getJSpecViewProperty
		throw new UnsupportedOperationException();
	}

}