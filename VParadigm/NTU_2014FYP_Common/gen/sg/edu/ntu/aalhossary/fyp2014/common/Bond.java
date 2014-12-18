package sg.edu.ntu.aalhossary.fyp2014.common;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.AtomType;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.BondType;

/**
 * @author Xiu Ting
 *
 */
public class Bond implements Interaction {

	BondType bondType = null;
	//float bondAngle = 0.0f;
	Particle a,b;

	public Bond(Particle a, Particle b) {
		this.a = a;
		this.b = b;
		setBondType(a,b);
	}

	@Override
	public void setBondType(Particle a, Particle b) {
		// check if either particle is C.
		if((isNitrogen(a) && isCarbon(b)) ||  (isNitrogen(b) && isCarbon(a))){
			bondType = BondType.AMIDE;
		}
		else if(isCarbon(a) || isCarbon(b)){
			bondType = BondType.COVALENT;
		}
		else
			bondType = BondType.COVALENT;
	}

	private boolean isCarbon(Particle p) {
		if(AtomType.C.getAtomName().compareTo(p.getName())==0)
			return true;
		return false;
	}

	private boolean isNitrogen(Particle p) {
		if(AtomType.N.getAtomName().compareTo(p.getName())==0)
			return true;
		return false;
	}
	public String getBondType(){
		return bondType.toString();
	}

}