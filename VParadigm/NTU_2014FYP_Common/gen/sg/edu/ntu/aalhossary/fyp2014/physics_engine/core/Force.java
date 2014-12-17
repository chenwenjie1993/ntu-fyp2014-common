package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import sg.edu.ntu.aalhossary.fyp2014.common.Vector3D;

public class Force extends Vector3D {

	public Force(){
		super();
	}
	
	public Force (Vector3D v){
		super(v);
	}
	
	public Force (double x, double y, double z){
		super(x,y,z);
	}
	
	@Override
	public boolean equals(Object force) {
	    if (force == null) 
	        return false;
	    
	    if (getClass() != force.getClass()) 
	        return false;
	    
	    final Force other = (Force) force;
	    if(this.x != other.x || this.y != other.y || this.z != other.z)
	    	return false;
	    
	    return true;
	}
}