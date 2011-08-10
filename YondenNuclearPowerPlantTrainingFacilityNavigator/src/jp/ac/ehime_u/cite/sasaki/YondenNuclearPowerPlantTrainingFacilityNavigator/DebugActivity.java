package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

public class DebugActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug);
		File storage_directory = Environment.getExternalStorageDirectory();
		TextView textview_debug = (TextView) findViewById(R.id.textViewDebug);
		textview_debug.append("\n" + "Storage Directory = "
				+ storage_directory.getPath() + "\n");
		File data_directory = Environment.getDataDirectory();
		textview_debug.append("Data Directory = " + data_directory.getPath()
				+ "\n");
		
		File[] files = storage_directory.listFiles();
		for(int i = 0; i<files.length; ++i) {
			textview_debug.append(files[i].getPath() + ", ");
			textview_debug.append(files[i].getName() + "\n");
		}
	}
}
