package com.gmail.takashi316.easyguide.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Assert;

import com.gmail.takashi316.easyguide.content.ContentUnit;
import com.gmail.takashi316.easyguide.content.Root;
import com.gmail.takashi316.easyguide.ui.TocAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 施設一覧を表示して選択させるアクティビティ
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
public class TocActivity extends Activity {
	/** Called when the activity is first created. */

	// private ArrayList<Building> buildingList;
	// private static Building chosenBuilding;

	Root root;
	AssetManager assetManager;
	TextView textViewDocumentationDomain;
	TextView textViewExistingDocumentationDomainDirectory;
	String documentationDomain;
	TextView textViewDocumentationDirectoryCount;
	int documentationDirectoryCount = 0;
	TextView textViewDocumentationFileCount;
	int documentationFileCount = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.toc);
		this.textViewDocumentationDomain = 
				(TextView)findViewById(R.id.textViewDocumentationDomain);
		this.textViewExistingDocumentationDomainDirectory =
				(TextView)findViewById(R.id.textViewExistingDocumentationDomainDirectory);
		this.textViewDocumentationDirectoryCount = 
				(TextView)findViewById(R.id.textViewDocumentationDirectoryCount);
		this.textViewDocumentationFileCount = 
				(TextView)findViewById(R.id.textViewDocumentationFileCount);
		
		this.assetManager = this.getResources().getAssets();

		try {
			String[] asset_directories = this.assetManager.list("");
			assert (asset_directories.length==1);
			documentationDomain = asset_directories[0];
			textViewDocumentationDomain.setText(documentationDomain);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			root = new Root();
		} catch (FileNotFoundException e) {
			new AlertDialog.Builder(this).setTitle(
					"SDカード、本体メモリ、外部メモリにEASYGUIDEというフォルダが見つかりません。").show();
			finish();
		}// try
		
		try {
			installDocumentation();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// android.R.layout.simple_list_item_1);
		// this.buildingList = new ArrayList<Building>();
		// for (Organization organization : Organizations.getInstance()) {
		// Log.v(this.getClass().getSimpleName(), "Enumerating organizations");
		// for (Facility facility : organization) {
		// Log.v(this.getClass().getSimpleName(), "Enumerating facilities");
		// for (Building building : facility) {
		// Log.v(this.getClass().getSimpleName(),
		// "Enumerating buildings. Building "
		// + building.getTitle() + " found.");
		// assert (building.getTitle() != null);
		// adapter.add(building.getTitle());
		// this.buildingList.add(building);
		// }//for
		// }//for
		// }//for
		ListView building_list_view = (ListView) findViewById(R.id.tocListView);
		// Log.v(this.getClass().getSimpleName(), "Totally " +
		// adapter.getCount()
		// + " buildings found.");
		Assert.assertNotNull(building_list_view);

		building_list_view
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ListView list_view = (ListView) parent;
						TocAdapter.TocItem toc_item = (TocAdapter.TocItem) list_view
								.getItemAtPosition(position);
						InvokeAbsolutePath(toc_item.absolutePath);
					}// onItemClick
				}// OnItemClickListener
				);

		try {
			ArrayList<ContentUnit> children = root.getChildren();
			TocAdapter toc_adapter = new TocAdapter(this, children);
			building_list_view.setAdapter(toc_adapter);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// InvokeAbsolutePath("/mnt/sdcard/EASYGUIDE/www.yonden.co.jp/01 四国電力/");
	}// onCreate

	private void InvokeAbsolutePath(String absolute_path) {
		Intent intent = new Intent(TocActivity.this, UnifiedActivity.class);
		intent.putExtra("root", absolute_path);
		startActivity(intent);
	}// InvokeAbsolutePath

	// private void InvokeMapActivity(Building building_) {
	// OpeningActivity.chosenBuilding = building_;
	// Intent intent = new Intent(getApplicationContext(),
	// BuildingActivity.class);
	// intent.putExtra("jp.ac.ehime_u.cite.sasaki.easyguide.model.Building",
	// building_.getTitle());
	// startActivity(intent);
	// }// InvokeMapActivity
	
	void installDocumentation() throws IOException{
		for(ContentUnit content_unit : root.getChildren()){
			if(content_unit.getName().equals(this.documentationDomain)){
				this.textViewExistingDocumentationDomainDirectory.setText(content_unit.getDirectory().getAbsolutePath());
				break;
			}//if
		}//for
		deleteDirectory(new File(root.getDirectory(), this.documentationDomain));
		assert(!new File(root.getDirectory(), this.documentationDomain).exists());
		copyAsset("", documentationDomain);
	}//installDocumentation
	
	void copyAsset(final String asset_path, final String asset_name) throws IOException{
		if(isAssetPathDirectory(asset_path+"/"+asset_name)){
			File directory = new File(root.getDirectory(), asset_path + "/"+asset_name);
			directory.mkdir();
			documentationDirectoryCount+=1;
			textViewDocumentationDirectoryCount.setText(""+documentationDirectoryCount);
		}
	}//copyAsset
	
	static void deleteDirectory(File directory){
		for(File child : directory.listFiles()){
			if(child.isFile()) child.delete();
		}
		for(File child : directory.listFiles()){
			if(child.isDirectory()) deleteDirectory(child);
		}
	}// deleteDirectory
	
	boolean isAssetPathDirectory(final String asset_path) throws IOException{
		//this code is from http://d.hatena.ne.jp/h_mori/20121002/1349134592
	       boolean isDirectory = false;
	          try {
	               if (assetManager.list(asset_path).length > 0){ //子が含まれる場合はディレクトリ
	                    isDirectory = true;
	               } else {
	                    // オープン可能かチェック
	                    assetManager.open(asset_path);
	               }
	          } catch (FileNotFoundException fnfe) {
	               isDirectory = true;
	          }
	          return isDirectory;
	}// isAssetPathDirectory
	
}// OpeningActivity
