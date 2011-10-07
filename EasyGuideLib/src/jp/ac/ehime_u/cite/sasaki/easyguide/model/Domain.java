package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * Each organization belongs to one FQDN. class Domain manipulates resources
 * related to FQDN. *
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Domain {
	private ArrayList<UnzippedDirectory> unzippedDirectories;
	private UnzippedDirectory newestUnzippedDirectory;
	private File domainDirectory;

	@SuppressWarnings({ "javadoc", "serial" })
	public class Exception extends RuntimeException {
		public Exception(String message_) {
			super(message_);
		}
	}

	/**
	 * 
	 * @param root_
	 * @param domain_name
	 * @throws UnknownHostException
	 */
	public Domain(String domain_name) {
		// if (!IsResolvable(url_.getHost())) {
		// throw new Exception(url_.getHost() + " is not resolvable.");
		// }
		File domain_directory = new File(Root.GetTheRoot().getRootDirectory(),
				domain_name.toLowerCase());
		if (!domain_directory.exists()) {
			domain_directory.mkdirs();
			assert (domain_directory.exists());
		}
		if (domain_directory.isFile()) {
			throw new Exception("Can't make domain directory for "
					+ domain_name.toLowerCase());
		}
		assert (domain_directory.isDirectory());
		this.domainDirectory = domain_directory;
		EnumerateUnzippedDirectories();
	}

	/**
	 * Constructor of class Domain.
	 * 
	 * @param domain_directory
	 */
	public Domain(File domain_directory) {
		// TODO:
		//if (!IsValidFqdn(domain_directory.getName())) {
		//	throw new Exception(domain_directory.getName()
		//			+ " is invalid for FQDN.");
		//}
		if (!domain_directory.exists()) {
			throw new Exception(domain_directory.getPath() + " does not exist.");
		}
		this.domainDirectory = domain_directory;
		EnumerateUnzippedDirectories();
	}

	/**
	 * enumerate all unzipped directories under the domain directory. It also
	 * finds the latest directory and save into this.newestUnzippedDirectory.
	 */
	public void EnumerateUnzippedDirectories() {
		long newest_time = 0l;
		this.newestUnzippedDirectory = null;
		this.unzippedDirectories = new ArrayList<UnzippedDirectory>();
		File[] files = this.domainDirectory.listFiles();
		Log.v(this.getClass().getSimpleName(),
				"Enumerating unzipped directories on " + this.domainDirectory);
		if (files == null) {
			Log.v(this.getClass().getSimpleName(), "No unzipped directory in "
					+ this.domainDirectory);
			return;
		}
		Log.v(this.getClass().getSimpleName(), files.length
				+ " unzipped directory/directories found.");
		for (int i = 0; i < files.length; ++i) {
			try {
				UnzippedDirectory unzipped_directory = new UnzippedDirectory(
						files[i]);
				long last_modified = files[i].lastModified();
				this.unzippedDirectories.add(unzipped_directory);
				if (last_modified >= newest_time) {
					this.newestUnzippedDirectory = unzipped_directory;
					newest_time = last_modified;
				}
			} catch (UnzippedDirectory.Exception e) {
				continue;
			}
		}
	}

	/**
	 * 
	 */
	public void RemoveAllFiles() {
		for (UnzippedDirectory unzipped_directory : unzippedDirectories) {
			unzipped_directory.RemoveAllFiles();
		}
	}

	private String hostAddress;

	private boolean IsResolvable(final String host_) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				InetAddress inet_address;
				try {
					inet_address = InetAddress.getByName(host_);
					hostAddress = inet_address.getHostAddress();
				} catch (UnknownHostException e) {
				}
			}
		});
		thread.start();
		try {
			thread.join(10000);
			if (hostAddress == null || hostAddress.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} catch (InterruptedException e) {
			return false;
		}
	}

	private boolean IsValidFqdn(String host_) {
		Pattern pattern = Pattern
				.compile("^.{1,254}$)(^(?:(?!\\d+\\.)[a-zA-Z0-9_\\-]{1,63}\\.?)+(?:[a-zA-Z]{2,})$");
		// regex string comes from
		// http://regexlib.com/Search.aspx?k=fqdn&c=-1&m=-1&ps=20
		// by Scott Mulcahy
		Matcher matcher = pattern.matcher(host_);
		if (matcher.find()) {
			return true;
		} else {
			return true;
		}
	}

	/**
	 * @return the domainDirectory
	 */
	public File getDomainDirectory() {
		return domainDirectory;
	}

	/**
	 * @return the newestUnzippedDirectory
	 */
	public UnzippedDirectory getNewestUnzippedDirectory() {
		return newestUnzippedDirectory;
	}
}
