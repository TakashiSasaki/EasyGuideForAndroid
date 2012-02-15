package jp.ac.ehime_u.cite.sasaki.easyguide.download;

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
 * ZipFilesInAssets represents ZIP files in assets folder. Once zip files are
 * enumerated, it is represented as instances of ZipUrl.
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
@SuppressWarnings("serial")
public class Asset extends ArrayList<Source> {
	// private static final String zipAssetDirectory = "zip";
	private static Asset theZipFilesInAssets;
	private AssetManager assetManager;

	/**
	 * The constructor
	 * 
	 * @param context_
	 */
	public Asset(Context context_) {
		this.assetManager = context_.getResources().getAssets();
	}// an constructor

	public void EnumerateSources() {
		this.clear();
		Pattern domain_pattern = Pattern.compile("^[^.][a-zA-Z_0-9.-]+[^.]$");
		String[] domain_list;
		try {
			domain_list = this.assetManager.list("");
		} catch (IOException e2) {
			e2.printStackTrace();
			return;
		}// try
		for (String domain_string : domain_list) {
			Matcher domain_matcher = domain_pattern.matcher(domain_string);
			if (!domain_matcher.find())
				continue;
			String[] zip_list;
			try {
				zip_list = this.assetManager.list(domain_string);
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}// try
			if (zip_list == null)
				return;

			Pattern zip_pattern = Pattern.compile(".+\\.zip$");
			for (String zip_string : zip_list) {
				// we need to exclude files and directories implicitly placed on
				// assets
				Matcher zip_matcher = zip_pattern.matcher(zip_string);
				if (!zip_matcher.find())
					continue;
				URI uri;
				try {
					uri = new URI("file", null, "assets", -1, "/"
							+ domain_string + "/" + zip_string, null, null);
				} catch (URISyntaxException e) {
					e.printStackTrace();
					continue;
				}// try
				try {
					this.add(new Source(new Domain(domain_string), uri));
				} catch (MalformedURLException e) {
					e.printStackTrace();
					continue;
				} catch (URISyntaxException e) {
					e.printStackTrace();
					continue;
				}// try
			}// for
		}// for
	}// ScanAssets
	
	public static Asset GetTheInstance(Context context_) {
		if (theZipFilesInAssets == null) {
			theZipFilesInAssets = new Asset(context_);
		}
		return theZipFilesInAssets;
	}// GetTheZipFilesInAssets

}// ZippedAssets
