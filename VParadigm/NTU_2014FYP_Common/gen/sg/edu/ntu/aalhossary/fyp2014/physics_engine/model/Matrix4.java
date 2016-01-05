package sg.edu.ntu.aalhossary.fyp2014.physics_engine.model;

/**
 * @author waiyan
 * Maths Module for 3x4 Matrix Calculations
 */
public class Matrix4 {
	private double [] data = new double [12];
	
	/**
	 * Construct a 3x4 identity matrix
	 */
	public Matrix4(){
		data [0] = data [5] = data [10] = 1;
		data [1] = data [2] = data [3] = 0;
		data [4] = data [6] = data [7] = 0;
		data [8] = data [9] = data [11] = 0;
	}

	public Matrix4(double data[]){
		for(int i=0; i<12; i++)
			this.data[i] = data[i];
	}
	
	/**
     * Returns a matrix which is this matrix multiplied by the given other matrix.
     */
	public Matrix4 multiply (Matrix4 m) {
		Matrix4 temp = new Matrix4();
		temp.data[0] = (m.data[0]*data[0]) + (m.data[4]*data[1]) + (m.data[8]*data[2]);
        temp.data[4] = (m.data[0]*data[4]) + (m.data[4]*data[5]) + (m.data[8]*data[6]);
        temp.data[8] = (m.data[0]*data[8]) + (m.data[4]*data[9]) + (m.data[8]*data[10]);

        temp.data[1] = (m.data[1]*data[0]) + (m.data[5]*data[1]) + (m.data[9]*data[2]);
        temp.data[5] = (m.data[1]*data[4]) + (m.data[5]*data[5]) + (m.data[9]*data[6]);
        temp.data[9] = (m.data[1]*data[8]) + (m.data[5]*data[9]) + (m.data[9]*data[10]);

        temp.data[2] = (m.data[2]*data[0]) + (m.data[6]*data[1]) + (m.data[10]*data[2]);
        temp.data[6] = (m.data[2]*data[4]) + (m.data[6]*data[5]) + (m.data[10]*data[6]);
        temp.data[10] = (m.data[2]*data[8]) + (m.data[6]*data[9]) + (m.data[10]*data[10]);

        temp.data[3] = (m.data[3]*data[0]) + (m.data[7]*data[1]) + (m.data[11]*data[2]) + data[3];
        temp.data[7] = (m.data[3]*data[4]) + (m.data[7]*data[5]) + (m.data[11]*data[6]) + data[7];
        temp.data[11] = (m.data[3]*data[8]) + (m.data[7]*data[9]) + (m.data[11]*data[10]) + data[11];
		return temp;
	}
	
	/**
     * Transform the given vector by this matrix.
     */
	public Vector3D transform(Vector3D v) {
		return new Vector3D ( 	v.x*data[0] + v.y*data[1] + v.z*data[2] + data[3],
								v.x*data[4] + v.y*data[5] + v.z*data[6] + data[7],
								v.x*data[8] + v.y*data[9] + v.z*data[10] + data[11]);
	}

	public void setDiagonal (double a, double b, double c){
		data[0] = a;
		data[5] = b;
		data [10] = c;
	}

	public void setOrientationAndPos(Quaternion q, Vector3D pos) {
		data[0] = 1 - (2*q.j*q.j + 2*q.k*q.k);
        data[1] = 2*q.i*q.j + 2*q.k*q.r;
        data[2] = 2*q.i*q.k - 2*q.j*q.r;
        data[3] = pos.x;

        data[4] = 2*q.i*q.j - 2*q.k*q.r;
        data[5] = 1 - (2*q.i*q.i  + 2*q.k*q.k);
        data[6] = 2*q.j*q.k + 2*q.i*q.r;
        data[7] = pos.y;

        data[8] = 2*q.i*q.k + 2*q.j*q.r;
        data[9] = 2*q.j*q.k - 2*q.i*q.r;
        data[10] = 1 - (2*q.i*q.i  + 2*q.j*q.j);
        data[11] = pos.z;
	}
	
	/**
	 * If the matrix is not a scale and shear free transform matrix, then this functioN will not give correct results. 
	 * @param vector
	 * @return
	 */
	public Vector3D transformInverse(Vector3D vector) {
		Vector3D tmp = vector;
        tmp.x -= data[3];
        tmp.y -= data[7];
        tmp.z -= data[11];
        
        return new Vector3D(tmp.x * data[0] + tmp.y * data[4] + tmp.z * data[8],
        					tmp.x * data[1] + tmp.y * data[5] + tmp.z * data[9],
	        				tmp.x * data[2] + tmp.y * data[6] + tmp.z * data[10]
        );
	}

	public Vector3D transformDirection(Vector3D vector) {
		return new Vector3D ( 	vector.x*data[0] + vector.y*data[1] + vector.z*data[2],
								vector.x*data[4] + vector.y*data[5] + vector.z*data[6],
								vector.x*data[8] + vector.y*data[9] + vector.z*data[10]
							);
	}
	
	/**
	 * If the matrix is not a scale and shear free transform matrix, then this functioN will not give correct results.
	 * @param vector
	 * @return
	 */
	public Vector3D transformInverseDirection(Vector3D vector) {
		return new Vector3D ( 	vector.x*data[0] + vector.y*data[4] + vector.z*data[8],
								vector.x*data[1] + vector.y*data[5] + vector.z*data[9],
								vector.x*data[2] + vector.y*data[6] + vector.z*data[10]
							);
	}
	
	/**
     * Gets a vector representing one axis (i.e. one column) in the matrix.
     * @param i=3 corresponds to the position of the transform matrix.
     */
	public Vector3D getAxisVector(int i){
		return new Vector3D(data[i], data[i+4], data[i+8]);
	}
	
	public void print(){
		System.out.println(data[0] + ", " + data [1] + ", " + data[2] + ", " + data[3]);
		System.out.println(data[4] + ", " + data [5] + ", " + data[6] + ", " + data[7]);
		System.out.println(data[8] + ", " + data [9] + ", " + data[10] + ", " + data[11]);
	}
}