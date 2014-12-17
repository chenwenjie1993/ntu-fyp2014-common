package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Vector3D {
	public double x;
	public double y;
	public double z;

	public Vector3D() {
		x = y = z = 0;
	}

	public Vector3D (Vector3D v){
		this.x = v.x; this.y = v.y; this.z = v.z;
	}
	
	public Vector3D(double x, double y, double z) {
		this.x = x; this.y = y; this.z = z;
	}
	
	@Override
	public boolean equals(Object vector) {
	    if (vector == null) 
	        return false;
	    
	    if (getClass() != vector.getClass()) 
	        return false;
	    
	    final Vector3D other = (Vector3D) vector;
	    if(this.x != other.x || this.y != other.y || this.z != other.z)
	    	return false;
	    
	    return true;
	}

	public void invert() {
		x=-x; y=-y; z=-z;
	}
	
	public Vector3D getNegativeVector(){
		return new Vector3D(-x,-y,-z);
	}

	public void clear() {
		x = y = z = 0;
	}

	public double getMagnitude() {
		return Math.sqrt(x*x + y *y + z*z);
	}

	public double getSquaredMagnitude() {
		return x*x + y *y + z*z;
	}

	public void normalize() {
		double temp = getMagnitude();
		if(temp > 0)
			this.scale(1/temp);
	}

	public Vector3D getUnitVector() {
		Vector3D temp = this;
		temp.normalize();
		return temp;
	}

	public void add(Vector3D vector) {
		x += vector.x;
		y += vector.y;
		z += vector.z;
	}

	public void subtract(Vector3D vector) {
		x -= vector.x;
		y -= vector.y;
		z -= vector.z;
	}

	public void addScaledVector(Vector3D vector, double scale) {
		x += scale * vector.x;
		y += scale * vector.y;
		z += scale * vector.z;
	}

	public void scale(double scale) {
		x *= scale;
		y *= scale;
		z *= scale;
	}

	public double getScalarProduct(Vector3D vector) {
		return x*vector.x + y*vector.y + z*vector.z;
	}

	public Vector3D getCrossProduct(Vector3D vector) {
		double new_x = y*vector.z - z*vector.y;
		double new_y = z*vector.x - x*vector.z;
		double new_z = x*vector.y - y*vector.x;
		return new Vector3D(new_x,new_y,new_z);
	}

	public Vector3D getComponentProduct(Vector3D vector) {
		return new Vector3D(x*vector.x, y*vector.y, z*vector.z);
	}
	
	public String print(){
		
		String x_str = convertMetric(x);
		String y_str = convertMetric(y);
		String z_str = convertMetric(z);
		return x_str + ", " + y_str + ", " + z_str;
		//return String.valueOf(formatter.format(x)) + ", " + String.valueOf(formatter.format(y)) + ", " + String.valueOf(formatter.format(z));
	}
	
	public String convertMetric(double i){
		int exponent = (int)(Math.log10(i));
		NumberFormat formatter = new DecimalFormat("0.00");
		
		if (exponent <-9)
			return formatter.format(i*Math.pow(10, 12)) + "pm";
		
		else if (exponent < -6)
			return formatter.format(i*Math.pow(10, 9)) + "nm";
	
		else if (exponent < -3)
			return formatter.format(i*Math.pow(10, 6)) + " μm";
	
		else if (exponent < 0)
			return formatter.format(i*Math.pow(10, 3)) + "mm";
		
		return i + "m";
		
	}
}