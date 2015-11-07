package sg.edu.ntu.aalhossary.fyp2014.physics_engine.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author waiyan
 * Vector class for Physics Engine
 */
public class Vector3D {
	public double x;
	public double y;
	public double z;
	public int metric;
	
	public Vector3D() {
		x = y = z = metric = 0;
	}

	public Vector3D (Vector3D v){
		this.x = v.x; this.y = v.y; this.z = v.z; this.metric = v.metric;
	}
	
	public Vector3D(double x, double y, double z) {
		this.x = x; this.y = y; this.z = z; this.metric = 0;
	}
	
	public Vector3D(double x, double y, double z, int metric) {
		this.x = x; this.y = y; this.z = z; this.metric = metric;
	}
	
	/**
	 * Overridden equals method for comparing two Vector3D instances
	 */
	@Override
	public boolean equals(Object vector) {
	    if (vector == null) 
	        return false;
	    
	    if (getClass() != vector.getClass()) 
	        return false;
	    
	    final Vector3D other = (Vector3D) vector;
	    if(this.x != other.x || this.y != other.y || this.z != other.z || this.metric != other.metric)
	    	return false;
	    
	    return true;
	}

	/**
	 * Invert this vector to its negative component
	 */
	public void invert() {
		x=-x; y=-y; z=-z;
	}
	
	/**
	 * Return a negative vector of this vector
	 * @return vector
	 */
	public Vector3D getNegativeVector(){
		return new Vector3D(-x,-y,-z,metric);
	}

	/**
	 * Reset the vector to zero
	 */
	public void clear() {
		x = y = z = 0;
	}
	
	/**
	 * Round off the decimal points of the double
	 */
	public void round(){
		double roundOffVal = 1e7;
		if(x==0 || y==0 || z==0)
			return;
		x = (double)Math.round(x * roundOffVal) / roundOffVal;
		y = (double)Math.round(y * roundOffVal) / roundOffVal;
		z = (double)Math.round(z * roundOffVal) / roundOffVal;
	}

	/**
	 * Get the magnitude of this vector
	 * @return magnitude
	 */
	public double getMagnitude() {
		return Math.sqrt(x*x + y *y + z*z);
	}

	/**
	 * Get the squared magnitude of this vector
	 * @return squared magnitude
	 */
	public double getSquaredMagnitude() {
		return x*x + y *y + z*z;
	}

	/**
	 * Normalize this vector
	 */
	public void normalize() {
		double temp = getMagnitude();
		if(temp > 0)
			this.scale(1/temp);
	}

	/**
	 * Get the unit vector of this vector
	 * @return unti vector
	 */
	public Vector3D getUnitVector() {
		Vector3D temp = this;
		temp.normalize();
		return temp;
	}

	/**
	 * Add the given vector to this vector
	 * @param vector
	 */
	public void add(Vector3D vector) {
		double metricDiff = this.metric - vector.metric;
		if(metricDiff == 0){
			x += vector.x;
			y += vector.y;
			z += vector.z;
		}
		else if (metricDiff >0) { // 1ms - 500us
			double scale = Math.pow(10, metricDiff);
			x = x * scale + vector.x;
			y = y * scale + vector.y;
			z = z * scale + vector.z;
			metric = vector.metric;
		}
		else{	//1us - 1ms
			double scale = Math.pow(10, -metricDiff);
			x += vector.x * scale;
			y += vector.y * scale;
			z += vector.z * scale;
		}
	}
	
	public Vector3D addAndReturn(Vector3D vector) {
		Vector3D temp = new Vector3D();
		temp.add(this);
		temp.add(vector);
		return temp;
	}
	
	public Vector3D subtractAndReturn(Vector3D vector) {
		Vector3D temp = new Vector3D();
		temp.add(this);
		temp.subtract(vector);
		return temp;
	}

	/**
	 * Subtract the given vector from this vector
	 * @param vector
	 */
	public void subtract(Vector3D vector) {
		add(vector.getNegativeVector());
	}

	/**
	 * Add a scaled vector to this vector
	 * @param vector
	 * @param scale
	 */
	public void addScaledVector(Vector3D vector, double scale) {
		vector.scale(scale);
		add(vector);
	}

	/**
	 * Scale this vector
	 * @param scale
	 */
	public void scale(double scale) {
		x *= scale;
		y *= scale;
		z *= scale;
	}

	/**
	 * Return the scalar product of this vector and the given vector
	 * @param vector
	 * @return scalar product
	 */
	public double getScalarProduct(Vector3D vector) {
		double metricDiff = vector.metric - this.metric;
		if(metricDiff == 0)
			return x*vector.x + y*vector.y + z*vector.z;
		else {
			double scale = Math.pow(10, metricDiff);
			return x*scale*vector.x + y*scale*vector.y + z*scale*vector.z;
		}
	}

	/**
	 * Return the cross product (vector) of this vector and the given vector
	 * @param vector
	 * @return cross product
	 */
	public Vector3D getCrossProduct(Vector3D vector) {
		double scale = Math.pow(10, vector.metric - this.metric);
		double new_x = (y*vector.z - z*vector.y)*scale;
		double new_y = (z*vector.x - x*vector.z)*scale;
		double new_z = (x*vector.y - y*vector.x)*scale;
		return new Vector3D(new_x,new_y,new_z,metric);
	}

	/**
	 * Return the component product (vector) of this vector and the given vector
	 * @param vector
	 * @return
	 */
	public Vector3D getComponentProduct(Vector3D vector) {
		double scale = Math.pow(10, vector.metric - this.metric);
		return new Vector3D(x*vector.x*scale, y*vector.y*scale, z*vector.z*scale, metric);
	}
	
	public Vector3D rotatebyMatrix (Matrix3 matrix){
		Vector3D vector = new Vector3D();
		double [] data = matrix.getData();
		vector.x = data[0] * x + data[1] * y + data[2] * z;
		vector.y = data[3] * x + data[4] * y + data[5] * z;
		vector.z = data[6] * x + data[7] * y + data[8] * z;
		return vector;
	}
	
	/**
	 * Return the formatted values of x, y, z values of the vector
	 * @return String
	 */
	public String print(){
		double scale = Math.pow(10, metric);
		String x_str = convertMetric(x*scale);
		String y_str = convertMetric(y*scale);
		String z_str = convertMetric(z*scale);
		return x_str + ", " + y_str + ", " + z_str;
		//return String.valueOf(formatter.format(x)) + ", " + String.valueOf(formatter.format(y)) + ", " + String.valueOf(formatter.format(z));
	}
	
	/**
	 * Convert the given double to a String with SI prefix
	 * @param value
	 * @return String
	 */
	public String convertMetric(double value){
		int exponent = (int)(Math.log10(value));
		NumberFormat formatter = new DecimalFormat("0.000");
		
		if (exponent <-9)
			return formatter.format(value*Math.pow(10, 12)) + "pm";
				
		else if (exponent < -6)
			return formatter.format(value*Math.pow(10, 9)) + "nm";
	
		else if (exponent < -3)
			return formatter.format(value*Math.pow(10, 6)) + " μm";
	
		else if (exponent < 0)
			return formatter.format(value*Math.pow(10, 3)) + "mm";
		
		return value + "m";
		
	}
}