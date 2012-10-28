package jp.ac.ehime_u.cite.sasaki.easyguide.download;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class Asset {
	String pathInAsset;
	static AssetManager assetManager;
	static Context context;

	Asset(String path_in_asset) {
		pathInAsset = path_in_asset;
	}

	void setContext(Context context) {
		if (Asset.context == null) {
			Asset.context = context;
			return;
		}
		assert (Asset.context.equals(context));
	}

	void preareAssetManager() {
		if (assetManager != null)
			return;
		assetManager = context.getResources().getAssets();
	}

	BufferedInputStream getBufferedInputStream() throws IOException {
		InputStream input_stream = Asset.assetManager.open(pathInAsset);
		return new BufferedInputStream(input_stream);
	}
}
