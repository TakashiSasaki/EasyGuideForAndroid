package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

public class DebugActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug);
		//File storage_directory = Environment.getExternalStorageDirectory();
		//File data_directory = Environment.getDataDirectory();
	}
}
