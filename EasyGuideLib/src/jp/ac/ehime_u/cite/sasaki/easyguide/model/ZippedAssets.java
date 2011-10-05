package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
@SuppressWarnings("serial")
public class ZippedAssets extends ArrayList<URL> {
	private static ZippedAssets zippedAssets;

	private ZippedAssets(Context context_) {
		AssetManager asset_manager = context_.getResources().getAssets();
		try {
			String[] list = asset_manager.list("zip");
			if (list != null) {
				for (int i = 0; i < list.length; ++i) {
					this.add(new URL("file://assets/zip/" + list[i]));
				}
			}
		} catch (IOException e) {
			Log.v(this.getClass().getSimpleName(), e.getMessage());
		}
	}

	/**
	 * @param context_
	 * @return singleton object of ZippedAssets
	 */
	public static ZippedAssets GetTheZippedAssets(Context context_) {
		if (zippedAssets == null) {
			zippedAssets = new ZippedAssets(context_);
		}
		return zippedAssets;
	}
	
	public void Unzip(String asset_path, File destination_directory){
		
	}
}
