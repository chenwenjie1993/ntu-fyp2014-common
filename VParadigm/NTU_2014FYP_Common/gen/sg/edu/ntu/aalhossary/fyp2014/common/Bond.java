package sg.edu.ntu.aalhossary.fyp2014.common;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.BondType;

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
		if(isCarbon(a) || isCarbon(b)){
			bondType = BondType.COVALENT;
		}
		if((isNitrogen(a) && isCarbon(b)) ||  (isNitrogen(b) && isCarbon(a))){
			bondType = BondType.AMIDE;
		}
		else
			bondType = BondType.COVALENT;
	}

	private boolean isCarbon(Particle a) {
		if (a.getName().equals("C"))
			return true;
		return false;
	}

	private boolean isNitrogen(Particle a) {
		if (a.getName().equals("N"))
			return true;
		return false;
	}
	public String getBondType(){
		return bondType.toString();
	}

}