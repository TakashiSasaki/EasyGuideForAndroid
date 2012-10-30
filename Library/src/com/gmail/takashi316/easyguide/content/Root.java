package com.gmail.takashi316.easyguide.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;

/**
 * represents the root directory of EasyGuide contents.
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 */
public class Root extends ContentUnit {
	private static final String _EASYGUIDE = "EASYGUIDE";
	private static Root singleton;

	private static File _getEasyGuideRoot() {
		File external_storage_directory = Environment
				.getExternalStorageDirectory();
		File easy_guide_root = new File(external_storage_directory, _EASYGUIDE);
		if (!easy_guide_root.isDirectory()) {
			easy_guide_root.mkdir();
		}
		if (!easy_guide_root.canWrite()) {
			easy_guide_root.setWritable(true);
		}
		return easy_guide_root;
	}// _getEasyGuideRoot

	public static Root getTheRoot() throws FileNotFoundException {
		if (singleton != null)
			return singleton;
		singleton = new Root();
		return singleton;
	}

	private Root() throws FileNotFoundException {
		super(_getEasyGuideRoot(), null, 1);
		_checkDomainName();
	}// an constructor

	private void _checkDomainName() {
		final Pattern DOMAIN_PATTERN = Pattern
				.compile("^[a-zA-Z0-9_\\-][a-zA-Z0-9_\\-.]+[a-zA-Z0-9_\\-]$");
		// "^.{1,254}$)(^(?:(?!\\d+\\.)[a-zA-Z0-9_\\-]{1,63}\\.?)+(?:[a-zA-Z]{2,})$"
		for (ContentUnit child : this.getChildren()) {
			Matcher m = DOMAIN_PATTERN.matcher(child.getName());
			if (!m.find()) {
				throw new RuntimeException("invalid domain name "
						+ child.getName());
			}// if
		}// for
	}// _checkDomainName
}// Root
