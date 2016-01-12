package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

/**
 * @author waiyan
 * A quaternion represents the orientation
 * θ is the angle, x,y,z are axis
 */
public class Quaternion {
	public double r;		// cos θ/2
	public double i;		// x sin θ/2
	public double j;		// y sin θ/2
	public double k;		// z sin θ/2

	public Quaternion(){
		r = 1; 
		i = j = k = 0;
	}
	
	public Quaternion (Vector3D axis, double degAngle){
		double half_theta = degAngle * Math.PI / 360; // theta = angle * PI/180;
		double sin_half_theta = Math.sin(half_theta);
		r = Math.cos(half_theta);
		i = axis.x * sin_half_theta;
		j = axis.y * sin_half_theta;
		k = axis.z * sin_half_theta;
	}
	
	public Vector3D getVector(){
		Vector3D temp= new Vector3D();
		double sin_half_theta = Math.sqrt(1-r*r);
		temp.x = i/sin_half_theta;
		temp.y = j/sin_half_theta;
		temp.z = k/sin_half_theta;
		return temp;
	}
	
	public Quaternion(double r, double i, double j, double k){
		this.r = r; this.i = i; this.j = j; this.k = k;
	} 
	
	public void setQuaternion (Quaternion q){
		r=q.r; i=q.i; j=q.j; k=q.k;
	}
	
	/**
	 * Normalize the quaternion
	 */
	public void normalize() {
		double mag = r*r + i*i + j*j + k*k;
		if(mag < Init.machineEpsilon){
			r = 1;
			return;
		}
		mag = 1/Math.sqrt(mag);
		r*=mag; 
		i*=mag; j*=mag; k*=mag;
	}

	/**
	 * Multiplies a quaternion by another quaternion
	 * @param mul
	 */
	public void multiply(Quaternion mul) {
		Quaternion temp = new Quaternion();
		temp.r = r * mul.r - i * mul.i - j * mul.j - k * mul.k;
		temp.i = r * mul.i + i * mul.r + j * mul.k - k * mul.j;
		temp.j = r * mul.j + j * mul.r + k * mul.i - i * mul.k;
		temp.k = r * mul.k + k * mul.r + i * mul.j - j * mul.i;
		setQuaternion(temp);
	
	}

	/**
	 * Rotate the quaternion by a given vector
	 * @param vector
	 */
	public void rotateByVector(Vector3D vector) {
		Quaternion q = new Quaternion(0, vector.x, vector.y, vector.z);
		this.multiply(q);
	}

	/**
	 * Add a scaled vector to this quaternion
	 * @param vector
	 * @param scale
	 */
	public void addScaledVector(Vector3D vector, double scale) {
		Quaternion q  = new Quaternion(0, vector.x*scale, vector.y*scale, vector.z*scale);
		q.multiply(this);
		r += q.r/2;
		i += q.i/2;
		j += q.j/2;
		k += q.k/2;
	}
	
	/**
	 * Transform this quaternion to a 3x3 Matrix
	 * @return
	 */
	public Matrix3 toMatrix3 (){
		double data [] = new double[9];
		data[0] = 1 - (2*j*j + 2*k*k);
        data[1] = 2*i*j + 2*k*r;
        data[2] = 2*i*k - 2*j*r;
        data[3] = 2*i*j - 2*k*r;
        data[4] = 1 - (2*i*i  + 2*k*k);
        data[5] = 2*j*k + 2*i*r;
        data[6] = 2*i*k + 2*j*r;
        data[7] = 2*j*k - 2*i*r;
        data[8] = 1 - (2*i*i  + 2*j*j);
        return new Matrix3(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
	}

	
	/**
	 * Transform this quaternion to a 3x4 Matrix
	 * @param pos
	 * @return
	 */
	public Matrix4 toMatrix4 (Vector3D pos){
		double data[] = new double [12];
		data[0] = 1 - (2*j*j + 2*k*k);
        data[1] = 2*i*j + 2*k*r;
        data[2] = 2*i*k - 2*j*r;
        data[3] = pos.x;
        data[4] = 2*i*j - 2*k*r;
        data[5] = 1 - (2*i*i  + 2*k*k);
        data[6] = 2*j*k + 2*i*r;
        data[7] = pos.y;
        data[8] = 2*i*k + 2*j*r;
        data[9] = 2*j*k - 2*i*r;
        data[10] = 1 - (2*i*i  + 2*j*j);
        data[11] = pos.z;
		return new Matrix4(data);
	}
	
	public String print (){
		String str = "";
		str += "r = " + r + "\n" ;
		str += "i = " + i + "\n" ;
		str += "j = " + j + "\n" ;
		str += "k = " + k + "\n" ;
		return str;
	}
}