package com.blogspot.minmin_work.OpenGL_2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import test.sample.R;
import test.sample.R.drawable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Yoshiki IZUMI
 * @see "http://minmin-work.blogspot.com/"
 */
public class Scene implements View.OnTouchListener{

	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
	private FloatBuffer textureBuffer;


	private int mTextureNumber;
	private int mWidth;
	private int mHeight;

	private Activity mActivity;
	private Character mCharacter;

	public Scene(Activity activity, Display display) {
		mActivity = activity;
		mWidth = display.getWidth();
		mHeight = display.getHeight();

		mCharacter = new Character();
		mCharacter.loadFile("man.x", activity);
		mCharacter.parseX();
		mCharacter.showData(activity);
		float x = 0.0f;
		float y = 0.0f;
		float z = 0.0f;
		float sizeX = 10.0f;
		float sizeY = 10.0f;
		float sizeZ = 0.0f;
		float[] vertex = {
				//左側の三角形
				x + sizeX, y + sizeY, z + sizeZ,//右下
				x - sizeX, y - sizeY, z + sizeZ,//左上
				x - sizeX, y + sizeY, z + sizeZ,//左下
				//右側の三角形
				x + sizeX, y + sizeY, z + sizeZ,//右下
				x + sizeX, y - sizeY, z + sizeZ,//右上
				x - sizeX, y - sizeY, z + sizeZ,//左上
		};
		ByteBuffer vb = ByteBuffer.allocateDirect(6 * 3 * 4);
		vb.order(ByteOrder.nativeOrder());	//ビッグエンディアンかリトルエンディアンにあわせてくれる
		vertexBuffer = vb.asFloatBuffer();
		vertexBuffer.put(vertex);
		vertexBuffer.position(0);

		float[] color = {
				//左側の三角形（青）
				0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f,
				//右側の三角形（緑）
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f
		};
		ByteBuffer vbc = ByteBuffer.allocateDirect(6 * 4 * 4);
		vbc.order(ByteOrder.nativeOrder());	//ビッグエンディアンかリトルエンディアンにあわせてくれる
		colorBuffer = vbc.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);


		float[] texture = {
				1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,	//左側の三角形に貼る
				1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f	//右側の三角形に貼る
		};
		ByteBuffer tb = ByteBuffer.allocateDirect(2 * 3 * 2 * 4);
		tb.order(ByteOrder.nativeOrder());	//ビッグエンディアンかリトルエンディアンにあわせてくれる
		textureBuffer = tb.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

	}


    public void setTexture(GL10 gl)
    {
    	Bitmap bitmap;
    	try {
    		bitmap = BitmapFactory.decodeStream(
    				mActivity.getResources().openRawResource(
    						R.drawable.image));
    	} finally {
	    	try {
	    		mActivity.getResources().openRawResource(
						R.drawable.image).close();
	    	} catch(IOException e) {}
    	}
    	int[] textureNumber = new int[1];
    	gl.glGenTextures(1, textureNumber, 0);
    	mTextureNumber = textureNumber[0];
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureNumber);
    	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

    	gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT );
    	gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT );
    	gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
    	gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );

    }

	public void draw(GL10 gl) {
		gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		//ビューポート行列(ウィンドウへの出力領域)設定
		gl.glViewport(0,0,mWidth,mHeight);

		//射影行列設定
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		//視界(視野角、画面のアスペクト、比近視点、遠視点)の設定
		float aspect = (float)mWidth / (float)mHeight;
		GLU.gluPerspective(gl, 45.0f, aspect, 0.1f, 3000.0f);

	    GLU.gluLookAt(
	             gl,
     			0.0f, 400.0f, 1000.0f, 	//位置(視点)
    			0.0f, 400.0f, 0.0f, 	//見つめているところ
	            0.0f, 1.0f, 0.0f	//水平
	    );
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
//		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		int triangleNum = 2;
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, triangleNum * 3);

		mCharacter.draw(gl);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
