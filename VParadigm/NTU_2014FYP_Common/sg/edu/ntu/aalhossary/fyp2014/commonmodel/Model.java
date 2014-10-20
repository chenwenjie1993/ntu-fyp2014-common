package sg.edu.ntu.aalhossary.fyp2014.commonmodel;

import java.util.*;

public class Model {

	/**
	 * protected Structure structure;
	 */
	protected String modelName = null;
	protected org.biojava.bio.structure.Structure structure;
	Collection<Molecule> molecules;

	public Collection<Molecule> getMolecules() {
		return this.molecules;
	}

	public Model() {
		// TODO - implement Model.Model
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param struc
	 */
	public void setMolecule(org.biojava.bio.structure.Structure struc) {
		// TODO - implement Model.setMolecule
		throw new UnsupportedOperationException();
	}

	public java.lang.String getModelName() {
		// TODO - implement Model.getModelName
		throw new UnsupportedOperationException();
	}

	public Object[] getModelDetailList() {
		// TODO - implement Model.getModelDetailList
		throw new UnsupportedOperationException();
	}

}