package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class ZippedAssets {
	private static ZippedAssets zippedAssets;
	private String[] zipFiles;

	private ZippedAssets(Context context_) {
		AssetManager asset_manager = context_.getResources().getAssets();
		try {
			zipFiles = asset_manager.list("zip");
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

	/**
	 * @return the zipFiles
	 */
	public String[] getZipFiles() {
		return zipFiles;
	}
}
