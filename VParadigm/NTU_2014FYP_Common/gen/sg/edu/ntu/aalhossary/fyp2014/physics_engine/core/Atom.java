package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.common.Atom{

	public Atom(){
		super();
		double radius = 1;
		this.boundingPrimitive = new BoundingSphere(radius, this.position);
	}
	
	public Atom(double radius){
		super();
		this.boundingPrimitive = new BoundingSphere(radius, this.position);
	}
}
