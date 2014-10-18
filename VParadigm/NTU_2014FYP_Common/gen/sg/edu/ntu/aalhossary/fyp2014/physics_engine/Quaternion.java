package sg.edu.ntu.aalhossary.fyp2014.physics_engine;
// A Quaternion represents the orientation 
// r is the angle, i,j,k are axis
public class Quaternion {
	public double r;
	public double i;
	public double j;
	public double k;

	public Quaternion(){
		r = i = j = k = 0;
	}
	
	public Quaternion(double r, double i, double j, double k){
		this.r = r; this.i = i; this.j = j; this.k = k;
	} 

	public void normalize() {
		double mag = r*r + i*i + j*j + k*+k;
		if(mag < World.machineEpsilon){
			r = 1;
			return;
		}
		mag = 1/Math.sqrt(mag);
		r*=mag; i*=mag; j*=mag; k*=mag;
	}

	public void multiply(Quaternion mul) {
		Quaternion q = new Quaternion(r,i,j,k);
		r = q.r * mul.r - q.i * mul.i - q.j * mul.j - q.k * mul.k;
		i = q.r * mul.i + q.i * mul.r + q.j * mul.k - q.k * mul.j;
		j = q.r * mul.j + q.j * mul.r + q.k * mul.i - q.i * mul.k;
		k = q.r * mul.k + q.k * mul.r + q.i * mul.j - q.j * mul.i;
	}

	public void rotateByVector(Vector3D vector) {
		Quaternion q = new Quaternion(0, vector.x, vector.y, vector.z);
		multiply(q);
	}

	public void addScaledVector(Vector3D vector, double scale) {
		Quaternion q  = new Quaternion(0, vector.x*scale, vector.y*scale, vector.z*scale);
		q.multiply(this);
		r += q.r/2;
		i += q.i/2;
		j += q.j/2;
		k += q.k/2;
	}
}