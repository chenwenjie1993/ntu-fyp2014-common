package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import org.jmol.api.*;

public class MyJmolStatusListener implements JmolStatusListener {

	private boolean verbose;
	private boolean _verbose;
	public JmolDisplay jmolPanel;

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * 
	 * @param jmolPanel
	 */
	public MyJmolStatusListener(JmolDisplay jmolPanel) {
		// TODO - implement MyJmolStatusListener.MyJmolStatusListener
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param callbackType
	 */
	public boolean notifyEnabled(org.jmol.c.CBK callbackType) {
		// TODO - implement MyJmolStatusListener.notifyEnabled
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param callbackType
	 * @param data
	 */
	public void notifyCallback(org.jmol.c.CBK callbackType, java.lang.Object[] data) {
		// TODO - implement MyJmolStatusListener.notifyCallback
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param fullPathName
	 * @param fileName
	 * @param modelName
	 * @param errorMsg
	 */
	private void notifyFileLoaded(String fullPathName, String fileName, String modelName, String errorMsg) {
		// TODO - implement MyJmolStatusListener.notifyFileLoaded
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param callbackType
	 * @param callbackFunction
	 */
	public void setCallbackFunction(String callbackType, String callbackFunction) {
		// TODO - implement MyJmolStatusListener.setCallbackFunction
		throw new UnsupportedOperationException();
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