package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
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
@SuppressWarnings("serial")
public class Domain extends ArrayList<UnzippedDirectory> {
	// private UnzippedDirectory newestUnzippedDirectory;
	private File domainDirectory;

	@SuppressWarnings({ "javadoc" })
	public class Exception extends RuntimeException {
		public Exception(String message_) {
			super(message_);
		}
	}

	/**
	 * a constructor
	 * 
	 * @param domain_name
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
		ScanUnzippedDirectories();
	}

	/**
	 * a constructor
	 * 
	 * @param domain_directory
	 */
	public Domain(File domain_directory) {
		// if (!IsValidFqdn(domain_directory.getName())) {
		// throw new Exception(domain_directory.getName()
		// + " is invalid for FQDN.");
		// }
		if (!domain_directory.exists()) {
			throw new Exception(domain_directory.getPath() + " does not exist.");
		}
		this.domainDirectory = domain_directory;
		ScanUnzippedDirectories();
	}

	/**
	 * enumerate all unzipped directories under the domain directory. It also
	 * finds the latest directory and save into this.newestUnzippedDirectory.
	 */
	public void ScanUnzippedDirectories() {
		Log.v(this.getClass().getSimpleName(),
				"scanning unzipped directories on " + this.domainDirectory);
		for (File file : this.domainDirectory.listFiles()) {
			Log.v(this.getClass().getSimpleName(),
					"unzipped directory " + file.getAbsolutePath()
							+ " was found.");
			this.add(new UnzippedDirectory(file));
		}// for
	}// ScanUnzippedDirectories

	/**
	 * @return last unzipped directory in this domain directory
	 */
	public UnzippedDirectory GetLastUnzippedDirectory() {
		if (this.size() == 0) {
			throw new Exception("No unzipped directory in "
					+ this.domainDirectory);
		}
		Date temp_date = new Date(0l);
		UnzippedDirectory temp_unzipped_directory = null;

		for (UnzippedDirectory unzipped_directory : this) {
			if (unzipped_directory.getModfiedDateTime().after(temp_date)) {
				temp_date = unzipped_directory.getModfiedDateTime();
				temp_unzipped_directory = unzipped_directory;
			}// if
		}// for
		return temp_unzipped_directory;
	}

	/**
	 * 
	 */
	public void RemoveAllFiles() {
		for (UnzippedDirectory unzipped_directory : this) {
			unzipped_directory.RemoveAllFiles();
		}
	}

	private String hostAddress;

	@SuppressWarnings("unused")
	@Deprecated
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
	}// IsResolvable

	@SuppressWarnings("unused")
	@Deprecated
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

}
