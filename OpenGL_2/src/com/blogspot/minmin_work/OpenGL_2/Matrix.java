package com.blogspot.minmin_work.OpenGL_2;

/**
 * @author Yoshiki IZUMI
 * @see "http://minmin-work.blogspot.com/"
 */
public class Matrix {
	/**
	 * 
	 */
	public float[] m = new float[16];

	/**
	 * 
	 */
	public Matrix() {
	}

	/*
	 * public float m1 = 1.0f, m2 = 0.0f, m3 = 0.0f, m4 = 0.0f, m5 = 0.0f, m6 =
	 * 1.0f, m7 = 0.0f, m8 = 0.0f, m9 = 0.0f, m10= 0.0f, m11= 1.0f, m12= 0.0f,
	 * m13= 0.0f, m14= 0.0f, m15= 0.0f, m16= 1.0f;
	 * 
	 * public Matrix() { m1 = 1.0f; m2 = 0.0f; m3 = 0.0f; m4 = 0.0f; m5 = 0.0f;
	 * m6 = 1.0f; m7 = 0.0f; m8 = 0.0f; m9 = 0.0f; m10= 0.0f; m11= 1.0f; m12=
	 * 0.0f; m13= 0.0f; m14= 0.0f; m15= 0.0f; m16= 1.0f;
	 * 
	 * }
	 */
	/*
	 * public Vector3 multiplication(Vector3 v) { Vector3 ret = new Vector3();
	 * ret.x = m1 * v.x + m2 * v.y + m3 * v.z + m4; ret.y = m5 * v.x + m6 * v.y
	 * + m7 * v.z + m8; ret.z = m9 * v.x + m10* v.y + m11* v.z + m12; return
	 * ret; } public Vector3 multiplication(float x, float y ,float z) { Vector3
	 * ret = new Vector3(); ret.x = m1 * x + m2 * y + m3 * z + m4; ret.y = m5 *
	 * x + m6 * y + m7 * z + m8; ret.z = m9 * x + m10* y + m11* z + m12; return
	 * ret; }
	 * 
	 * public void quaternionToMatrix(float x, float y, float z, float w) { m1 =
	 * 1.0f - 2.0f * (y * y + z * z); m5 = 2.0f * (x * y + z * w); m9 = 2.0f *
	 * (x * z - y * w); m13 = 0.0f;
	 * 
	 * m2 = 2.0f * (x * y - z * w); m6 = 1.0f - 2.0f * (z * z + x * x); m10 =
	 * 2.0f * (y * z + x * w); m14 = 0.0f;
	 * 
	 * m3 = 2.0f * (x * z + y * w); m7= 2.0f * (y * z - x * w); m11= 1.0f - 2.0f
	 * * (y * y + x * x); m15= 0.0f;
	 * 
	 * m4= 0.0f; m8= 0.0f; m12= 0.0f; m16= 1.0f; }
	 * 
	 * public void translation(float x, float y, float z) { m4 = x; m8 = y; m12
	 * = z; }
	 * 
	 * public void addTranslation(float x, float y, float z) { m4 += x; m8 += y;
	 * m12 += z; }
	 * 
	 * public void ratationY(float angle) { double temp = angle * 3.14159265358
	 * / 180.0f; m1 = (float)Math.cos(temp); m2 = 0.0f; m3 =
	 * -(float)Math.sin(temp);// m4 = 0.0f; m5 = 0.0f; m6 = 1.0f; m7 = 0.0f; //
	 * m8 = 0.0f; m9 = (float)Math.sin(temp); m10= 0.0f; m11=
	 * (float)Math.cos(temp); // m12= 0.0f; m13= 0.0f; m14= 0.0f; m15= 0.0f;
	 * m16= 1.0f; }
	 */
}
