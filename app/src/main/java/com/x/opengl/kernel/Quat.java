package com.x.opengl.kernel;

/**
 * 四元数
 * @date   2013-08-08 11:10:10
 */
public class Quat {

	public static final String TAG = "Quat";

	public float W = 1;
	public float X = 0;
	public float Y = 0;
	public float Z = 0;
	
	public Quat() {
		
	}
	
	public Quat(float w, float x, float y, float z) {
		W = w;
		X = x;
		Y = y;
		Z = z;
	}
	
	public Quat normalize() {
		Quat quat = normalise(this);
		W = quat.W;
		X = quat.X;
		Y = quat.Y;
		Z = quat.Z;
		return this;
	}
	
	public void setAngleAxis(AngleAxis angleAxis) {
		Quat quat = Quat.fromAngleAxis(angleAxis);
		W = quat.W;
		X = quat.X;
		Y = quat.Y;
		Z = quat.Z;
	}
	
	public AngleAxis toAngleAxis() {
		return toAngleAxis(this);
	}
	
	public Vector3 toEular() {
		return toEular(this);
	}
	
	/**
	 * 获取横滚角
	 * @return
	 */
	public float getRoll() {
		return getRoll(this, true);
	}
	
	/**
	 * 获取横滚四元数
	 * @return
	 */
	public Quat getRollQuat() {
		float rollRadian     = getRoll();
		float halfRollRadian = rollRadian / 2.0f;
		Quat  quat           = new Quat();
		
		quat.W = (float) Math.cos(halfRollRadian);
		quat.X = 0;
		quat.Y = 0;
		quat.Z = (float) Math.sin(halfRollRadian);
		
		return quat;
	}
	
	/**
	 * 旋转横滚角
	 * @param radian
	 */
	public void rotateRoll(float radian) {
		Quat quat = rotateRoll(this, radian);
		
		this.W = quat.W;
		this.X = quat.X;
		this.Y = quat.Y;
		this.Z = quat.Z;
	}
	
	/**
	 * 获取俯仰角
	 * @return
	 */
	public float getPitch() {
		return getPitch(this, true);
	}
	
	/**
	 * 获取俯仰四元数
	 * @return
	 */
	public Quat getPitchQuat() {
		float pitchRadian     = getPitch();
		float halfPitchRadian = pitchRadian / 2.0f;
		Quat  quat            = new Quat();
		
		quat.W = (float) Math.cos(halfPitchRadian);
		quat.X = (float) Math.sin(halfPitchRadian);
		quat.Y = 0;
		quat.Z = 0;
		
		return quat;
	}
	
	/**
	 * 旋转俯仰角
	 * @param radian
	 */
	public void rotatePitch(float radian) {
		Quat quat = rotatePitch(this, radian);
		
		this.W = quat.W;
		this.X = quat.X;
		this.Y = quat.Y;
		this.Z = quat.Z;
	}
	
	/**
	 * 获取偏航角
	 * @return
	 */
	public float getYaw() {
		return getYaw(this, true);
	}
	
	/**
	 * 获取偏航四元数
	 * @return
	 */
	public Quat getYawQuat() {
		float yawRadian     = getYaw();
		float halfYawRadian = yawRadian / 2.0f;
		Quat  quat          = new Quat();
		
		quat.W = (float) Math.cos(halfYawRadian);
		quat.X = 0;
		quat.Y = (float) Math.sin(halfYawRadian);
		quat.Z = 0;
		
		return quat;
	}
	
	/**
	 * 旋转偏航角
	 * @param radian
	 */
	public void rotateYaw(float radian) {
		Quat quat = rotateYaw(this, radian);
		
		this.W = quat.W;
		this.X = quat.X;
		this.Y = quat.Y;
		this.Z = quat.Z;
	}
	
	public Vector3 getAxes() {
		return getAxes(this, true);
	}
	
	@Override
	public String toString() {
		return "(" + W + ", " + X + ", " + Y + ", " + Z + ")";
	}

	public static Vector3 toEular(Quat quat) {
		return toEular(quat.W, quat.X, quat.Y, quat.Z);
	}
	
	public static Vector3 toEular(float quatW, float quatX, float quatY, float quatZ) {
		Vector3 eularVector = new Vector3();
		
		eularVector.X = (float) Math.asin(2 * (quatW * quatX - quatY * quatZ));
		eularVector.Y = (float) Math.atan2(2 * (quatW * quatY + quatX * quatZ), 1 - 2 * (Math.pow(quatX, 2) + Math.pow(quatY, 2)));
		eularVector.Z = (float) Math.atan2(2 * (quatW * quatZ + quatX * quatY), 1 - 2 * (Math.pow(quatZ, 2) + Math.pow(quatX, 2)));
		
		return eularVector;
	}
	
	public static Quat toQuat(Vector3 vector) {
		return toQuat(vector.X, vector.Y, vector.Z);
	}
	
	public static Quat toQuat(float eularX, float eularY, float eularZ) {
		Quat quatVector = new Quat();
		double halfX = eularX / 2.0;
		double halfY = eularY / 2.0;
		double halfZ = eularZ / 2.0;
		
		quatVector.W = (float) (Math.cos(halfX) * Math.cos(halfY) * Math.cos(halfZ) + Math.sin(halfX) * Math.sin(halfY) * Math.sin(halfZ)); 
		quatVector.X = (float) (Math.sin(halfX) * Math.cos(halfY) * Math.cos(halfZ) - Math.cos(halfX) * Math.sin(halfY) * Math.sin(halfZ));
		quatVector.Y = (float) (Math.cos(halfX) * Math.sin(halfY) * Math.cos(halfZ) + Math.sin(halfX) * Math.cos(halfY) * Math.sin(halfZ));
		quatVector.Z = (float) (Math.cos(halfX) * Math.cos(halfY) * Math.sin(halfZ) - Math.sin(halfX) * Math.sin(halfY) * Math.cos(halfZ));
		
		// XYZ规格化
		return Quat.normalise(quatVector);
	}
	
	/**
	 * 球面差值
	 * @param fromQuat
	 * @param toQuat
	 * @param intervalRadian 插值百分比 0~1.0
	 * @return
	 */
	public static Quat Slerp(Quat fromQuat, Quat toQuat, float intervalRadian) {
		float cosFactor = Quat.dot(fromQuat, toQuat);
		Quat  wareQuat  = null;
		
		if (cosFactor < 0.0f) {
			cosFactor = -cosFactor;
			wareQuat = new Quat(-toQuat.W, -toQuat.X, -toQuat.Y, -toQuat.Z);
		}else{
			wareQuat = toQuat;
		}
		
		if (Math.abs(cosFactor) < 1.0f) {
			float sinFactor = (float) Math.sqrt(1 - Math.pow(cosFactor, 2));
			float angle = (float) Math.atan2(sinFactor, cosFactor);
			float invSinFactor = 1.0f / sinFactor;
			return Quat.plus(
					Quat.product(fromQuat, (float)(Math.sin((1 - intervalRadian) * angle) * invSinFactor)),
					Quat.product(wareQuat, (float)(Math.sin(intervalRadian * angle) * invSinFactor)));
		}else{
			return Quat.plus(
					Quat.product(fromQuat, 1 - intervalRadian),
					Quat.product(wareQuat, intervalRadian))
					.normalize();
		}
	}
	
	/**
	 * 四元数的逆
	 * @param quat
	 * @return
	 */
	public static Quat inverse(Quat quat) {
        float norm = Quat.norm(quat);
        if (norm > 0.0) {
        	float invNorm = 1.0f / norm;
            return new Quat(quat.W * invNorm, -quat.X * invNorm, -quat.Y * invNorm, -quat.Z * invNorm);
        }else{
            return null;
        }
    }
	
	/**
	 * 规格化
	 * @param quat
	 * @return
	 */
	public static Quat normalise(Quat quat) {
		float norm = Quat.norm(quat);
		float factor = (float) (1.0f / Math.sqrt(norm));
		return Quat.product(quat, factor);
	}
	
	/**
	 * 四元数的范数
	 * @param quat
	 * @return
	 */
	public static float norm(Quat quat) {
		return quat.W * quat.W + quat.X * quat.X + quat.Y * quat.Y + quat.Z * quat.Z;
	}
	
	public static Quat rotatePitch(Quat quat, float radian) {
		Quat   relativeRotate = new Quat();
		double halfRadian     = radian/2.0;
		
		relativeRotate.W = (float) Math.cos(halfRadian);
		relativeRotate.X = (float) Math.sin(halfRadian);
		relativeRotate.Y = 0;
		relativeRotate.Z = 0;
		
		return Quat.product(relativeRotate, quat).normalize();
	}
	
	public static float getPitch(Quat quat, boolean reprojectAxis) {
		if (reprojectAxis) {
			float tx  = 2.0f * quat.X;
			float tz  = 2.0f * quat.Z;
			float twx = tx * quat.W;
			float txx = tx * quat.X;
			float tyz = tz * quat.Y;
			float tzz = tz * quat.Z;
			return (float) Math.atan2(tyz + twx, 1 - txx - tzz);
			
		}else{
			return (float) Math.atan2(
					2.0f * (quat.Y * quat.Z + quat.W * quat.X),
					quat.W * quat.W - quat.X * quat.X - quat.Y * quat.Y + quat.Z * quat.Z);
		}
	}
	
	public static Quat rotateYaw(Quat quat, float radian) {
		Quat   relativeRotate = new Quat();
		double halfRadian     = radian/2.0;
		
		relativeRotate.W = (float) Math.cos(halfRadian);
		relativeRotate.X = 0;
		relativeRotate.Y = (float) Math.sin(halfRadian);
		relativeRotate.Z = 0;
		
		return Quat.product(relativeRotate, quat).normalize();
	}
	
	public static float getYaw(Quat quat, boolean reprojectAxis) {
		if (reprojectAxis) {
			float tx  = 2.0f * quat.X;
			float ty  = 2.0f * quat.Y;
			float tz  = 2.0f * quat.Z;
			float twy = ty * quat.W;
			float txx = tx * quat.X;
			float txz = tz * quat.X;
			float tyy = ty * quat.Y;
			
			return (float) Math.atan2(txz + twy, 1 - txx - tyy);
		}else{
			return (float) Math.asin(-2.0f * (quat.X * quat.Z - quat.W * quat.Y));
		}
	}
	
	public static Quat rotateRoll(Quat quat, float radian) {
		Quat   relativeRotate = new Quat();
		double halfRadian     = radian / 2.0;
		
		relativeRotate.W = (float) Math.cos(halfRadian);
		relativeRotate.X = 0;
		relativeRotate.Y = 0;
		relativeRotate.Z = (float) Math.sin(halfRadian);
		
		return Quat.product(relativeRotate, quat).normalize();
	}
	
	public static float getRoll(Quat quat, boolean reprojectAxis) {
		if (reprojectAxis) {
			float ty  = 2.0f * quat.Y;
			float tz  = 2.0f * quat.Z;
			float twz = tz * quat.W;
			float txy = ty * quat.X;
			float tyy = ty * quat.Y;
			float tzz = tz * quat.Z;
			return (float) Math.atan2(txy + twz, 1 - tyy - tzz);
		}else{
			return (float) Math.atan2(
					2 * (quat.X * quat.Y + quat.W * quat.Z),
					quat.W * quat.W + quat.X * quat.X - quat.Y * quat.Y - quat.Z * quat.Z);
		}
	}
	
	public static Vector3 getAxes(Quat quat, boolean reprojectAxis) {
		if (reprojectAxis) {
			float tx  = 2.0f * quat.X;
			float ty  = 2.0f * quat.Y;
			float tz  = 2.0f * quat.Z;
			float twx = tx * quat.W;
			float twy = ty * quat.W;
			float twz = tz * quat.W;
			float txx = tx * quat.X;
			float txy = ty * quat.X;
			float txz = tz * quat.X;
			float tyy = ty * quat.Y;
			float tyz = tz * quat.Y;
			float tzz = tz * quat.Z;
			
			return new Vector3(
					(float) Math.atan2(tyz + twx, 1 - txx - tzz), 
					(float) Math.atan2(txz + twy, 1 - txx - tyy), 
					(float) Math.atan2(txy + twz, 1 - tyy - tzz));
		}else{
			return new Vector3(
					(float) Math.atan2(
							2.0f * (quat.Y * quat.Z + quat.W * quat.X),
							quat.W * quat.W - quat.X * quat.X - quat.Y * quat.Y + quat.Z * quat.Z),
					(float) Math.asin(-2.0f * (quat.X * quat.Z - quat.W * quat.Y)),
					(float) Math.atan2(
							2 * (quat.X * quat.Y + quat.W * quat.Z),
							quat.W * quat.W + quat.X * quat.X - quat.Y * quat.Y - quat.Z * quat.Z));
		}
	}
	
	public static Quat setAxes(Vector3 axes) {
		Quat      quat   = new Quat();
		float[][] matrix = new float[3][3];
		float     trace  = 0;
		float     root   = 0;
		
		for (int i = 0; i < 3; i++){
			matrix[0][i] = axes.X;
			matrix[1][i] = axes.Y;
			matrix[2][i] = axes.Z;
        }
		
		trace = matrix[0][0] + matrix[1][1] + matrix[2][2];
		if (trace > 0.0f){
			root = (float) Math.sqrt(trace + 1.0f);
			quat.W = 0.5f * root;
			root = 0.5f / root;
			quat.X = (matrix[2][1] - matrix[1][2]) * root;
			quat.Y = (matrix[0][2] - matrix[2][0]) * root;
			quat.Z = (matrix[1][0] - matrix[0][1]) * root;
		}else{
			int next[] = {1, 2, 0};
            int i = 0;
            
            if (matrix[1][1] > matrix[0][0]){
                i = 1;
            }
            
            if (matrix[2][2] > matrix[i][i]){
                i = 2;
            }
            
            int j = next[i];
            int k = next[j];

            root = (float) Math.sqrt(matrix[i][i] - matrix[j][j] - matrix[k][k] + 1.0f);
            
            float quatAxes[] = {quat.X, quat.Y, quat.Z};
            quatAxes[i] = 0.5f * root;
            root = 0.5f / root;
            quat.W = (matrix[k][j] - matrix[j][k]) * root;
            quatAxes[j] = (matrix[j][i] + matrix[i][j]) * root;
            quatAxes[k] = (matrix[k][i] + matrix[i][k]) * root;
            
            quat.X = quatAxes[0];
            quat.Y = quatAxes[1];
            quat.Z = quatAxes[2];
		}
		
		return quat;
	}
	
	/**
	 * 转换为轴-角表达式
	 * @param quat
	 * @return
	 */
	public static AngleAxis toAngleAxis(Quat quat) {
		AngleAxis angleAxis = new AngleAxis();
		float mag = quat.X * quat.X + quat.Y * quat.Y + quat.Z * quat.Z;
		if (mag > 0.0f) {
			float invMag = (float) (1.0 / Math.sqrt(mag));
			angleAxis.Angle = (float) (2.0 * Math.acos(quat.W));
			angleAxis.xAxis = quat.X * invMag;
			angleAxis.yAxis = quat.Y * invMag;
			angleAxis.zAxis = quat.Z * invMag;

		}else{
			angleAxis.Angle = 0;
			angleAxis.xAxis = 0;
			angleAxis.yAxis = 0;
			angleAxis.zAxis = 1;
		}
		
		return angleAxis;
	}
	
	/**
	 * 轴-角表达式转换为四元数
	 * @param angleAxis
	 * @return
	 */
	public static Quat fromAngleAxis(AngleAxis angleAxis) {
		Quat  quat         = new Quat();
		float halfAngle    = angleAxis.Angle * 0.5f;
		float sinHalfAngle = (float) Math.sin(halfAngle);
		
		quat.W = (float) Math.cos(halfAngle);
//		quat.X = (float) (sinHalfAngle * Math.cos(angleAxis.xAxis));
//		quat.Y = (float) (sinHalfAngle * Math.cos(angleAxis.yAxis));
//		quat.Z = (float) (sinHalfAngle * Math.cos(angleAxis.zAxis));
		quat.X = (float) (sinHalfAngle * angleAxis.xAxis);
		quat.Y = (float) (sinHalfAngle * angleAxis.yAxis);
		quat.Z = (float) (sinHalfAngle * angleAxis.zAxis);
				
		return quat;
	}
	
	public static float dot(Quat quatA, Quat quatB) {
		return quatA.W * quatB.W + quatA.X * quatB.X + quatA.Y * quatB.Y + quatA.Z * quatB.Z; 
	}
	
	public static Quat plus(Quat quatA, Quat quatB) {
		return new Quat(quatA.W + quatB.W, quatA.X + quatB.X, quatA.Y + quatB.Y, quatA.Z + quatB.Z);
	}
	
	public static Quat product(Quat quat, float factor) {
		return new Quat(quat.W * factor, quat.X * factor, quat.Y * factor, quat.Z * factor);
	}
	
	public static Vector3 product(Quat quat, Vector3 vector) {
		Vector3 u1Vector   = null;
		Vector3 u2Vector   = null;
		Vector3 quatVector = new Vector3(quat.X, quat.Y, quat.Z);
		
		u1Vector = Vector3.crossProduct(quatVector, vector);
		u2Vector = Vector3.crossProduct(quatVector, u1Vector);
		u1Vector = Vector3.product(u1Vector, 2 * quat.W);
		u2Vector = Vector3.product(u2Vector, 2);
		
		return Vector3.addition(vector, Vector3.addition(u1Vector, u2Vector));
	}
	
	public static Quat product(Quat quatA, Quat quatB) {
		return new Quat(
				quatA.W * quatB.W - quatA.X * quatB.X - quatA.Y * quatB.Y - quatA.Z * quatB.Z,
				quatA.W * quatB.X + quatA.X * quatB.W + quatA.Y * quatB.Z - quatA.Z * quatB.Y,
				quatA.W * quatB.Y + quatA.Y * quatB.W + quatA.Z * quatB.X - quatA.X * quatB.Z,
				quatA.W * quatB.Z + quatA.Z * quatB.W + quatA.X * quatB.Y - quatA.Y * quatB.X);
	}

}
