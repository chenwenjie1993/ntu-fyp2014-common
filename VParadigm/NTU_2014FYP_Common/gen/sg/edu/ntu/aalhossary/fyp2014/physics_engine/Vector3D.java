package sg.edu.ntu.aalhossary.fyp2014.physics_engine;
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

	public void invert() {
		x=-x; y=-y; z=-z;
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
		double magInv = 1/getMagnitude();
		return new Vector3D(x*magInv, y*magInv, z*magInv);
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
		return String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z);
	}
}