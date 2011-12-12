package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
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
@SuppressWarnings("serial")
public class Domain extends ArrayList<Organization> {
	private File domainDirectory;

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
			throw new RuntimeException("Can't make domain directory for "
					+ domain_name.toLowerCase());
		}
		assert (domain_directory.isDirectory());
		this.domainDirectory = domain_directory;

	}// a constructor of class Domain

	/**
	 * a constructor
	 * 
	 * @param domain_directory
	 * @throws FileNotFoundException
	 */
	public Domain(File domain_directory) throws FileNotFoundException {
		// if (!IsValidFqdn(domain_directory.getName())) {
		// throw new Exception(domain_directory.getName()
		// + " is invalid for FQDN.");
		// }
		this.domainDirectory = domain_directory;
		if (!domain_directory.exists()) {
			throw new FileNotFoundException(domain_directory.getPath());
		}
	}// a constructor of class Domain

	public void EnumerateOrganizations() {
		for (File file : this.domainDirectory.listFiles()) {
			Log.v(this.getClass().getSimpleName(),
					"unzipped directory " + file.getAbsolutePath()
							+ " was found.");
			if (file.isDirectory()) {
				Log.v(this.getClass().getSimpleName(), file.getName()
						+ " was found as unzipped directory.");
				this.add(new Organization(file));
			}// if
		}// for
	}// EnumerateOrganizations

	static private void RemoveDirectory(File directory) {
		assert (directory.isDirectory());
		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				f.delete();
			} else {
				RemoveDirectory(f);
			}
		}// for
		directory.delete();
	}// RemoveDirectory

	public void RemoveAllOrganizations() {
		for (File f : this.domainDirectory.listFiles()) {
			if (f.isDirectory()) {
				RemoveDirectory(f);
			}
		}// for
		this.clear();
	}// RemoveAllFiles

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
					hostAddress = null;
				}
			}// run
		});
		thread.start();
		try {
			thread.join(10000);
			if (hostAddress == null) {
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
	}// getDomainDirectory

}// Domain
