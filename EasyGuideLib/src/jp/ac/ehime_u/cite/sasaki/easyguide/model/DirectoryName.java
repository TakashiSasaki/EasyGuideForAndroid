package jp.ac.ehime_u.cite.sasaki.easyguide.model;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DirectoryName {
	private String name;
	private int number;

	/**
	 * @param directory_name
	 */
	public DirectoryName(String directory_name) {
		String[] parts = directory_name.split("[ ]+");
		this.number = Integer.parseInt(parts[0]);
		this.name = parts[1];
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

}
