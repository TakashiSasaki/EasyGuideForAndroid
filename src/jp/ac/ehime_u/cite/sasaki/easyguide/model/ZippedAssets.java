package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
@SuppressWarnings("serial")
public class ZippedAssets extends ArrayList<ZipUrl> {
	// private static final String zipAssetDirectory = "zip";

	private AssetManager assetManager;

	public ZippedAssets(Context context_) {
		final Domain assets_domain = new Domain("assets");
		this.assetManager = context_.getResources().getAssets();
		String[] list;
		try {
			list = this.assetManager.list("");
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}// try
		if (list == null)
			return;
		Pattern pattern = Pattern.compile(".+\\.zip$");
		for (String s : list) {
			// we need to exclude files and directories implicitly placed on
			// assets
			Matcher matcher = pattern.matcher(s);
			if (matcher.find()) {
				URI uri;
				try {
					uri = new URI("file", null, "assets", -1, "/" + s, null,
							null);
				} catch (URISyntaxException e) {
					e.printStackTrace();
					continue;
				}// try
				try {
					this.add(new ZipUrl(assets_domain, uri));
				} catch (MalformedURLException e) {
					e.printStackTrace();
					continue;
				} catch (URISyntaxException e) {
					e.printStackTrace();
					continue;
				}// try
			}// if
		}// for
	}// an constructor
}// ZippedAssets
