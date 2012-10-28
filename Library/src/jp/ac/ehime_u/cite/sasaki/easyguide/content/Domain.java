package jp.ac.ehime_u.cite.sasaki.easyguide.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Each organization belongs to one FQDN. class Domain manipulates resources
 * related to FQDN. *
 * 
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Domain extends ContentUnit {
	// private File _domainDirectory;
	private static final Pattern _ZIP_FILE_PATTERN = Pattern
			.compile("^[0-9]+\\.[zZ][iI][pP]$");
	private String hostAddress;

	/**
	 * class Domain represents domain directory. This constructor makes
	 * corresponding directory if not exists.
	 * 
	 * @param domain_name
	 * @throws FileNotFoundException
	 */
	public Domain(Root root, String domain_name, int sibling_index)
			throws FileNotFoundException {
		super(new File(root.getDirectory(), domain_name.toLowerCase()), root,
				sibling_index);
		updateHostAddress();
	}// a constructor of class Domain

	public void mkdir() {
		if (!getDirectory().exists()) {
			getDirectory().mkdirs();
			assert (getDirectory().exists());
		}
	}// mkdir

	public void rmdir() {
		_rmdir(getDirectory());
	}// rmdir

	static private void _rmdir(File directory) {
		if (directory == null)
			return;
		assert (directory.isDirectory());
		for (File f : directory.listFiles()) {
			if (f.isFile()) {
				f.delete();
			} else {
				_rmdir(f);
			}// if
		}// for
		directory.delete();
	}// _rmdir

	/**
	 * Removes all files with zip extension under the domain directory. Domain
	 * directory holds ZIP files and one or more organization directoryies.
	 */
	public void removeZipFiles() {
		FilenameFilter file_name_filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".zip");
			}// accept
		};// FileNameFilter
		for (File f : this.getDirectory().listFiles(file_name_filter)) {
			f.delete();
		}// for
	}// removeZipFiles

	public void updateHostAddress() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				InetAddress inet_address;
				try {
					inet_address = InetAddress.getByName(getName());
					Domain.this.setHostAddress(inet_address.getHostAddress());
				} catch (UnknownHostException e) {
					Domain.this.setHostAddress(null);
				}
			}// run
		});
		thread.start();
	}// updateHostAddress

	private boolean isFqdn() {
		Pattern pattern = Pattern
				.compile("^.{1,254}$)(^(?:(?!\\d+\\.)[a-zA-Z0-9_\\-]{1,63}\\.?)+(?:[a-zA-Z]{2,})$");
		// regex string comes from
		// http://regexlib.com/Search.aspx?k=fqdn&c=-1&m=-1&ps=20
		// by Scott Mulcahy
		Matcher matcher = pattern.matcher(getName());
		if (matcher.find()) {
			return true;
		} else {
			return true;
		}
	}// isFqdn

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

}// Domain
