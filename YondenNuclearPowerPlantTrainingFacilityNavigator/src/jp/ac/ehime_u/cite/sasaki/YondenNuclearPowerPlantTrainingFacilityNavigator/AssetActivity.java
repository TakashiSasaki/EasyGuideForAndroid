package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AssetActivity extends Activity {
	public static final String ORGANIZATIONS_DIRECTORY_NAME = "EASYGUIDE";
	public File assetsDirectory;
	public File organizationsDirectoryInAssets;
	public File organizationsDirectoryInExternalStorage;
	public File organizationsDirectoryInRemovableMedia;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.assets);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		AssetManager a = this.getAssets();
		try {
			String[] assets = a.list("/");
			for (int i = 0; i < assets.length; ++i) {
				adapter.add(assets[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ListView assets_list_view = (ListView) findViewById(R.id.listViewAssets);
		assets_list_view.setAdapter(adapter);
	}

}
