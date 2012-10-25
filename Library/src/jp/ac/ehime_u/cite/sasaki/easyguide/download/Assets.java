package jp.ac.ehime_u.cite.sasaki.easyguide.download;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Pair;

/**
 * ZipFilesInAssets represents ZIP files in assets folder. Once zip files are
 * enumerated, it is represented as instances of ZipUrl.
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
@SuppressWarnings("serial")
public class Assets extends ArrayList<Pair<String, String>> {
	// private static final String zipAssetDirectory = "zip";
	// private AssetManager assetManager;

	/**
	 * The constructor
	 * 
	 * @param context_
	 */
	public Assets(Context context_) {
		AssetManager asset_manager = context_.getResources().getAssets();
		this.clear();
		Pattern valid_asset_pattern = Pattern
				.compile("^[^.][a-zA-Z_0-9.-]+[^.].zip$");
		String[] asset_paths;

		try {
			// trying to list assets on the top
			asset_paths = asset_manager.list("");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}// try

		for (String asset_path : asset_paths) {
			Matcher valid_asset_matcher = valid_asset_pattern
					.matcher(asset_path);
			if (!valid_asset_matcher.find())
				continue;
			URI uri;
			try {
				uri = new URI("file", null, "assets", -1, "/" + asset_path,
						null, null);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				continue;
			}// try
			this.add(new Pair<String, String>(asset_path, uri.toString()));
		}// for
	}// constructor

}// Assets
