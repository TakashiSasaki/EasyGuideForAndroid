package test.sample;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class X {
	private String	child;
	private String	templateName;
	private String	frameName;
	private String	FrameTransformMatrix;
	private X		mParent;
	public ArrayList<X> mInstanceChildren;
	public Matrix transformMatrix;
	public float[]	vertexArray;
	private short[]	indexArray;
	private float[] textureCoordArray;
	private int		vertexNumber;
	private int		indexNumber;
	private int		textureCoordNumber;

	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer textureCoordBuffer;
	public X() {
		child = "";
		mInstanceChildren = new ArrayList<X>();
		transformMatrix = new Matrix();
	}

	private void loadFrameTransformMatrix(String str)
	{
		int valueBegin = 0;
		int valueEnd = 0;
		for(int i = 0; i < 15; ++i) {
			valueEnd = str.indexOf(",", valueBegin);
			transformMatrix.m[i] = Float.parseFloat(str.substring(valueBegin, valueEnd));
			valueBegin = valueEnd + 1;
			if(i % 4 == 3) {
			}
		}
		valueEnd = str.indexOf(";", valueBegin);
		transformMatrix.m[15] = Float.parseFloat(str.substring(valueBegin, valueEnd));
	}

	public X CreateInstance(String s) {
		int pos =0;
		pos = s.indexOf("Frame");
		int backetEnd = s.indexOf("{");
		templateName	= s.substring(pos + 6, backetEnd - 1);
		frameName		= s.substring(pos + 8, backetEnd - 1);

		X instance = new X();

		int beginTransformMatrix = s.indexOf("FrameTransformMatrix", pos + 1);
		if(beginTransformMatrix >= 0) {
			int end = s.indexOf("}", beginTransformMatrix);
			FrameTransformMatrix = s.substring(beginTransformMatrix + 22, end);
			instance.loadFrameTransformMatrix(FrameTransformMatrix);
			pos = end + 1;
		}

		instance.setTemplateName(templateName);
		instance.setFrameName(frameName);
		//System.out.println(frameName);
		while ((pos = parseInstanceBody(s, pos)) >= 0)
		{
			X childInstance = CreateInstance(child);
			childInstance.mParent = instance;
			instance.mInstanceChildren.add(childInstance);
		}

		return instance;
	}

	private int parseInstanceBody(String s, int pos) {
		int ct = 0;
		int n = 0;

		pos = s.indexOf("Frame", pos + 1);
		if(pos == -1) {
			return -1;
		}

		do
		{
			int open, close;
			if(n == 0) {
				open = s.indexOf("{", pos);
				close = s.indexOf("}", pos);
			} else {
				open = s.indexOf("{", n);
				close = s.indexOf("}", n);
			}
			if(open < close) {
				if(open > 0) {
					++ct;
					n = open + 1;
				} else {
					--ct;
					n = close + 1;
				}
			} else {
				if(close > 0) {
					--ct;
					n = close + 1;
				}
			}

			if (ct == -1 || (open == -1 && close == -1))
			{
				return -1;
			}
		} while ( ct > 0 );

		child = s.substring(pos, n);						// {}の中身を渡す
		pos = n;
		return pos;
	}

	public void loadTPose(String s) {

		int begin = s.indexOf("Mesh " + frameName);
		begin = s.indexOf("{", begin) + 6;
		int end = s.indexOf(";", begin);
		vertexNumber = Integer.parseInt(s.substring(begin, end));
		vertexArray = new float[vertexNumber * 3];

		for(int i = 0; i < vertexNumber; ++i) {
			for(int j = 0; j < 3; ++j) {
				begin = end + 1;
				end = s.indexOf(";", begin);
				vertexArray[i * 3 + j] = Float.parseFloat(s.substring(begin, end));
			}
			++end;
		}

		begin = end + 6;
		end = s.indexOf(";", begin);
		indexNumber = Integer.parseInt(s.substring(begin, end));
		indexArray = new short[indexNumber * 3];

		for(int i = 0; i < indexNumber; ++i) {
			begin = s.indexOf(";", end + 1) + 1;
			end = s.indexOf(",", begin);
			indexArray[i * 3 + 0] = Short.parseShort(s.substring(begin, end));

			begin = end + 1;
			end = s.indexOf(",", begin);
			indexArray[i * 3 + 1] = Short.parseShort(s.substring(begin, end));

			begin = end + 1;
			end = s.indexOf(";", begin);
			indexArray[i * 3 + 2] = Short.parseShort(s.substring(begin, end));
		}

		begin = s.indexOf("MeshTextureCoords", begin) + 25;
		end = s.indexOf(";", begin);
		textureCoordNumber = Integer.parseInt(s.substring(begin, end));
		textureCoordArray = new float[textureCoordNumber * 2];
		for(int i = 0; i < textureCoordNumber; ++i) {
			for(int j = 0; j < 2; ++j) {
				begin = end + 1;
				end = s.indexOf(";", begin);
				textureCoordArray[i * 2 + j] = Float.parseFloat(s.substring(begin, end));
			}
			++end;
		}


		for(int i = 0; i < mInstanceChildren.size(); ++i) {
			mInstanceChildren.get(i).loadTPose(s);
		}
	}

	public void setBuffer() {
		ByteBuffer vb = ByteBuffer.allocateDirect(vertexNumber * 3 * 4);
		vb.order(ByteOrder.nativeOrder());	//ビッグエンディアンかリトルエンディアンにあわせてくれる
		vertexBuffer = vb.asFloatBuffer();
		vertexBuffer.put(vertexArray);
		vertexBuffer.position(0);

		ByteBuffer ib = ByteBuffer.allocateDirect(indexNumber * 3 * 2);
		ib.order(ByteOrder.nativeOrder());	//ビッグエンディアンかリトルエンディアンにあわせてくれる
		indexBuffer = ib.asShortBuffer();
		indexBuffer.put(indexArray);
		indexBuffer.position(0);

		ByteBuffer tb = ByteBuffer.allocateDirect(textureCoordNumber * 2 * 4);
		tb.order(ByteOrder.nativeOrder());	//ビッグエンディアンかリトルエンディアンにあわせてくれる
		textureCoordBuffer = tb.asFloatBuffer();
		textureCoordBuffer.put(textureCoordArray);
		textureCoordBuffer.position(0);

		for(int i = 0; i < mInstanceChildren.size(); ++i) {
			mInstanceChildren.get(i).setBuffer();
		}

	}
	public void draw(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoordBuffer);

		gl.glDrawElements(GL10.GL_TRIANGLES, indexNumber * 3, gl.GL_UNSIGNED_SHORT, indexBuffer);

		for(int i = 0; i < mInstanceChildren.size(); ++i) {
			mInstanceChildren.get(i).draw(gl);
		}

	}
	public void setTemplateName(String s) {		templateName = s;	}
	public void setFrameName(String s) {		frameName = s;		}
	public String getFrameName() {		return frameName;	}
}