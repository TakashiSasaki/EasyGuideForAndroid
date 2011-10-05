package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.IOException;
import java.io.InputStream;
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
	private static final String zipAssetDirectory = "zip";
	
	static class Exception extends RuntimeException {
		public Exception(String message_) {
			super(message_);
		}
	}

	private static ZippedAssets zippedAssets;
	private AssetManager assetManager;

	private ZippedAssets(Context context_) {
		assetManager = context_.getResources().getAssets();
		try {
			String[] list = assetManager.list("zip");
			if (list != null) {
				for (int i = 0; i < list.length; ++i) {
					this.add(new URL("file://assets/" + list[i]));
				}
			}
		} catch (IOException e) {
			Log.v(this.getClass().getSimpleName(), e.getMessage());
		}
	}

	/**
	 * @param slash_and_zip_file_name 
	 * @return InputStream for the file in assets
	 */
	public InputStream GetInputStream(String slash_and_zip_file_name) {
		String asset_path = zipAssetDirectory + slash_and_zip_file_name;
		try {
			return this.assetManager.open(asset_path);
		} catch (IOException e) {
			throw new Exception("Can't get " + asset_path
					+ " in assets. " + e.getMessage());
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
}
