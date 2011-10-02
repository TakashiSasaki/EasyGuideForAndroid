package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * @param url_
	 * @throws UnknownHostException
	 */
	public Domain(Root root_, URL url_) throws UnknownHostException {
		if (!IsResolvable(url_.getHost())) {
			throw new Exception(url_.getHost() + " is not resolvable.");
		}
		File domain_directory = new File(root_.getRootDirectory(), url_
				.getHost().toLowerCase());
		if (!domain_directory.exists()) {
			domain_directory.mkdir();
		}
		if (domain_directory.isFile()) {
			throw new Exception("Can't make domain directory for "
					+ url_.toString());
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
		if (!IsValidFqdn(domain_directory.getName())) {
			throw new Exception(domain_directory.getName()
					+ " is invalid for FQDN.");
		}
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

	private boolean IsResolvable(String host_) {
		InetAddress inet_address;
		try {
			inet_address = InetAddress.getByName(host_);
		} catch (UnknownHostException e) {
			return false;
		}
		String host_address = inet_address.getHostAddress();
		if (host_address == null || host_address.isEmpty()) {
			return false;
		} else {
			return true;
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
			return false;
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
