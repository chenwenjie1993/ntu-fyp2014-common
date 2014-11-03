package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

public class Matrix3 {
	private double [] data = new double [9];
	
	public Matrix3(){
		for(int i=0; i<9; i++) data[i]=0;
	}
	
	public Matrix3(double a, double b, double c, double d, double e, double f, double g, double h, double i){
		data[0] = a;	data[1] = b;	data[2] = c;
		data[3] = d;	data[4] = e;	data[5] = f;
		data[6] = g;	data[7] = h;	data[8] = i;
		
	}
	
	// v1, v2 and v3 are columns of the matrix
	public Matrix3(Vector3D v1, Vector3D v2, Vector3D v3){
		data[0] = v1.x;	data[1]=v2.x;	data[2]=v3.x;
		data[3] = v1.y; data[4]=v2.y;	data[5]=v3.y;
		data[6] = v1.z; data[7]=v2.z;	data[8]=v3.z;
	}
	
	// only for Junit Test
	public double [] getData(){
		return data;
	}
	
	@Override
	public boolean equals(Object matrix3) {
		double epsilon = 1e-6;
		
	    if (matrix3 == null) 
	        return false;
	    
	    if (getClass() != matrix3.getClass()) 
	        return false;
	    
	    final Matrix3 other = (Matrix3) matrix3;
	    for(int i=0; i<9; i++) {
	    	if(Math.abs(this.data[i]-other.data[i]) > epsilon)
	    		return false;
	    }
	    return true;
	}
	
	public Matrix3 multiplyAndReturn(Matrix3 m){
		Matrix3 temp = new Matrix3();
		temp = this;
		temp.multiply(m);
		return temp;
	}
	
	public void multiply (Matrix3 m){
		Matrix3 temp = new Matrix3();
		temp.data[0] = data[0]*m.data[0] + data[1]*m.data[3] + data[2]*m.data[6];
		temp.data[1] = data[0]*m.data[1] + data[1]*m.data[4] + data[2]*m.data[7];
		temp.data[2] = data[0]*m.data[2] + data[1]*m.data[5] + data[2]*m.data[8];

		temp.data[3] = data[3]*m.data[0] + data[4]*m.data[3] + data[5]*m.data[6];
		temp.data[4] = data[3]*m.data[1] + data[4]*m.data[4] + data[5]*m.data[7];
		temp.data[5] = data[3]*m.data[2] + data[4]*m.data[5] + data[5]*m.data[8];
	
		temp.data[6] = data[6]*m.data[0] + data[7]*m.data[3] + data[8]*m.data[6];
		temp.data[7] = data[6]*m.data[1] + data[7]*m.data[4] + data[8]*m.data[7];
		temp.data[8] = data[6]*m.data[2] + data[7]*m.data[5] + data[8]*m.data[8];
		
		for(int i=0; i<9; i++)
			data[i] =  temp.data[i];
	}
	
	public Matrix3 scaleAndReturn(double scale){
		Matrix3 m = new Matrix3();
		m = this;
		m.scale(scale);
		return m;
	}
	
	public void scale(double scale){
		data[0]*=scale; data[1]*= scale; data[2]*=scale;
		data[3]*=scale; data[4]*= scale; data[5]*=scale;
		data[6]*=scale; data[7]*= scale; data[8]*=scale;
	}
	

	
	public void add (Matrix3 m){
		data[0] += m.data[0];	data[1] += m.data[1];	data[2] += m.data[2];
		data[3] += m.data[3];	data[4] += m.data[4];	data[5] += m.data[5];
		data[6] += m.data[6];	data[7] += m.data[7];	data[8] += m.data[8];
	}
	
	public void setDiagonal(double a, double b, double c){
		setInertiaTensorCoeffs(a,b,c,0,0,0);
	}
	
	public void setInertiaTensorCoeffs(double x, double y, double z, double xy, double xz, double yz){
		data[0] = x;
        data[1] = data[3] = -xy;
        data[2] = data[6] = -xz;
        data[4] = y;
        data[5] = data[7] = -yz;
        data[8] = z;
	}

	// Transform the given vector by this matrix
	public Vector3D transform(Vector3D v) {
		return new Vector3D ( 	v.x*data[0] + v.y*data[1] + v.z*data[2],
								v.x*data[3] + v.y*data[4] + v.z*data[5],
								v.x*data[6] + v.y*data[7] + v.z*data[8] );
		
	}
	
	// Transform the given matrix by this matrix
	public Matrix3 transform (Matrix3 matrix) {
		return new Matrix3 (
				data[0]*matrix.data[0] + data[1]*matrix.data[3] + data[2]*matrix.data[6],
				data[0]*matrix.data[1] + data[1]*matrix.data[4] + data[2]*matrix.data[7],
				data[0]*matrix.data[2] + data[1]*matrix.data[5] + data[2]*matrix.data[8],
				data[3]*matrix.data[0] + data[4]*matrix.data[3] + data[5]*matrix.data[6],
				data[3]*matrix.data[1] + data[4]*matrix.data[4] + data[5]*matrix.data[7],
				data[3]*matrix.data[2] + data[4]*matrix.data[5] + data[5]*matrix.data[8],
				data[6]*matrix.data[0] + data[7]*matrix.data[3] + data[8]*matrix.data[6],
				data[6]*matrix.data[1] + data[7]*matrix.data[4] + data[8]*matrix.data[7],
				data[6]*matrix.data[2] + data[7]*matrix.data[5] + data[8]*matrix.data[8]
		);
	}
	
	// Transform the given vector by the transpose of this matrix.
    public Vector3D transformTranspose(Vector3D vector){
        return new Vector3D(
            vector.x * data[0] + vector.y * data[3] + vector.z * data[6],
            vector.x * data[1] + vector.y * data[4] + vector.z * data[7],
            vector.x * data[2] + vector.y * data[5] + vector.z * data[8]
        );
    }
    
    /**
     * Gets a vector representing one row in the matrix.
     */
    public Vector3D getRowVector(int i){
        return new Vector3D (data[i*3], data[i*3+1], data[i*3+2]);
    }

    /**
     * Gets a vector representing one axis (i.e. one column) in the matrix.
     */
    public Vector3D getAxisVector(int i){
        return new Vector3D (data[i], data[i+3], data[i+6]);
    }
    
    /**
     * Sets the matrix to be the inverse of the given matrix.
     */
	public void setInverse(Matrix3 matrix) {
		
		double t4 = matrix.data[0]*matrix.data[4];
		double t6 = matrix.data[0]*matrix.data[5];
		double t8 = matrix.data[1]*matrix.data[3];
		double t10 = matrix.data[2]*matrix.data[3];
		double t12 = matrix.data[1]*matrix.data[6];
		double t14 = matrix.data[2]*matrix.data[6];
		
		double t16 = (t4*matrix.data[8] - t6*matrix.data[7] - t8*matrix.data[8]+
                t10*matrix.data[7] + t12*matrix.data[5] - t14*matrix.data[4]);

	    // Make sure the determinant is non-zero.
	    if (t16 == 0) return;
	    double t17 = 1/t16;
	
	    double [] temp = new double [9];
	    
	    temp[0] = (matrix.data[4]*matrix.data[8]-matrix.data[5]*matrix.data[7])*t17;
	    temp[1] = -(matrix.data[1]*matrix.data[8]-matrix.data[2]*matrix.data[7])*t17;
	    temp[2] = (matrix.data[1]*matrix.data[5]-matrix.data[2]*matrix.data[4])*t17;
	    temp[3] = -(matrix.data[3]*matrix.data[8]-matrix.data[5]*matrix.data[6])*t17;
	    temp[4] = (matrix.data[0]*matrix.data[8]-t14)*t17;
	    temp[5] = -(t6-t10)*t17;
	    temp[6] = (matrix.data[3]*matrix.data[7]-matrix.data[4]*matrix.data[6])*t17;
	    temp[7] = -(matrix.data[0]*matrix.data[7]-t12)*t17;
	    temp[8] = (t4-t8)*t17;
	    
	    for(int i=0; i<9; i++) 
	    	data[i] = temp[i];
	}

	public Matrix3 inverse() {
		Matrix3 m = new Matrix3();
		m.setInverse(this);
		return m;
	}

	public void invert(){
		setInverse(this);
	}
	
	public void setTranspose(Matrix3 m) {
        data[0] = m.data[0];
        data[1] = m.data[3];
        data[2] = m.data[6];
        data[3] = m.data[1];
        data[4] = m.data[4];
        data[5] = m.data[7];
        data[6] = m.data[2];
        data[7] = m.data[5];
        data[8] = m.data[8];      
	}

	public Matrix3 transpose() {
		Matrix3 m = new Matrix3();
		m.setTranspose(this);
		return m;
	}

	/** Sets this matrix to be the rotation matrix corresponding to the given quaternion.*/
	public void setOrientation(Quaternion q) {
		data[0] = 1 - (2*q.j*q.j + 2*q.k*q.k);
        data[1] = 2*q.i*q.j + 2*q.k*q.r;
        data[2] = 2*q.i*q.k - 2*q.j*q.r;
        data[3] = 2*q.i*q.j - 2*q.k*q.r;
        data[4] = 1 - (2*q.i*q.i  + 2*q.k*q.k);
        data[5] = 2*q.j*q.k + 2*q.i*q.r;
        data[6] = 2*q.i*q.k + 2*q.j*q.r;
        data[7] = 2*q.j*q.k - 2*q.i*q.r;
        data[8] = 1 - (2*q.i*q.i  + 2*q.j*q.j);
	}
	
	public void print(){
		System.out.println(data[0] + ", " + data [1] + ", " + data[2]);
		System.out.println(data[3] + ", " + data [4] + ", " + data[5]);
		System.out.println(data[6] + ", " + data [7] + ", " + data[8]);
	}
}