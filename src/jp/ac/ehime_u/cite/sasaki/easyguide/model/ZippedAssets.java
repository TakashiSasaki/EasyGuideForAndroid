package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
@SuppressWarnings("serial")
public class ZippedAssets extends ArrayList<ZipUrl> {
	// private static final String zipAssetDirectory = "zip";

	private AssetManager assetManager;

	private ZippedAssets(Context context_) {
		final Domain assets_domain = new Domain("assets");
		assetManager = context_.getResources().getAssets();
		try {
			String[] list = assetManager.list("");
			if (list != null) {
				for (String s : list) {
					this.add(new ZipUrl(assets_domain, new URL("file://assets/"
							+ s)));
				}// for
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}// try
	}// an constructor

}// ZippedAssets
