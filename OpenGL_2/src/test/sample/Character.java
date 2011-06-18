package test.sample;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.app.AlertDialog;

public class Character {
	private String mFileData;
	private X x;

    public void loadFile(String fileName, Activity activity)
    {
        try {    // 同じディレクトリにあるファイルリストを取得
    		InputStream fileInputStream = activity.getAssets().open(fileName);
    		byte[] readBytes = new byte[fileInputStream.available()];
    		fileInputStream.read(readBytes);
    		mFileData = new String(readBytes);

	   }catch(Exception e){
	    	showDialog(activity,"エラー","読み込み失敗しました ");
	    }
    }
	public int parseNum(String name, Activity activity)
	{
        loadFile(name, activity);
        int end = mFileData.indexOf("\n", 0)-1;
        return Integer.parseInt(mFileData.substring(0,end));
	}
    private static void showDialog(final Activity activity,String title,String text) {
        AlertDialog.Builder ad=new AlertDialog.Builder(activity);
        ad.setTitle(title);
        ad.setMessage(text);
        ad.create();
        ad.show();
    }

    public void parseX() {
		int begin = mFileData.indexOf("Frame 材質root");
		x = new X();
		x = x.CreateInstance(mFileData.substring(begin, mFileData.length()));
		for(int i = 0; i < x.mInstanceChildren.size(); ++i) {
			x.mInstanceChildren.get(i).loadTPose(mFileData);
		}
		for(int i = 0; i < x.mInstanceChildren.size(); ++i) {
			x.mInstanceChildren.get(i).setBuffer();
		}

//		x.mInstanceChildren.get(0).loadTPose(mFileData);
//		x.mInstanceChildren.get(0).setBuffer();

    }

    public void draw(GL10 gl) {
		for(int i = 0; i < x.mInstanceChildren.size(); ++i) {
			x.mInstanceChildren.get(i).draw(gl);
		}

//    	x.mInstanceChildren.get(0).draw(gl);
    }
    public void showData(final Activity activity) {
        AlertDialog.Builder ad=new AlertDialog.Builder(activity);
        ad.setTitle("X_file_test");
        ad.setMessage(""+x.mInstanceChildren.get(0).vertexArray[0]);
        ad.create();
        ad.show();

    }
}
