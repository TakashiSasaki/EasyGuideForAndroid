package jp.ac.ehime_u.cite.sasaki.easyguide.download;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * ZipFilesInAssets represents ZIP files in assets folder. Once zip files are
 * enumerated, it is represented as instances of ZipUrl.
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
@SuppressWarnings("serial")
public class Assets extends ArrayList<Asset> {
	// private static final String zipAssetDirectory = "zip";
	// private AssetManager assetManager;
	static Context context;
	static AssetManager assetManager;
	String pathInAssets;
	Pattern pattern;

	public void setContext(Context context) {
		if (context == null) {
			assert (Asset.context != null);
			return;
		}
		if (Assets.context == null) {
			Assets.context = context;
			return;
		}
		assert (Assets.context.equals(context));
	}// setContext

	void prepareAssetManager() {
		assetManager = context.getResources().getAssets();
	}

	public Assets(String path_in_asset, String regex) throws IOException {
		setContext(context);
		prepareAssetManager();
		// this.clear();
		pattern = Pattern.compile(regex /* "^[^.][a-zA-Z_0-9.-]+[^.].zip$" */);
		String[] asset_paths;

		asset_paths = assetManager.list("");

		for (String asset_path : asset_paths) {
			Matcher matcher = pattern.matcher(asset_path);
			if (!matcher.find())
				continue;
			// URI uri;
			// try {
			// uri = new URI("file", null, "assets", -1, "/" + asset_path,
			// null, null);
			// } catch (URISyntaxException e) {
			// e.printStackTrace();
			// continue;
			// }// try
			this.add(new Asset(pathInAssets + "/" + asset_path));
		}// for
	}// constructor

}// Assets
