package jp.ac.ehime_u.cite.sasaki.easyguide.model;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DirectoryName {
	private String name;
	private int number;
	private int x, y;

	@SuppressWarnings({ "javadoc", "serial" })
	static public class Exception extends RuntimeException {
		public Exception(String message_) {
			super(message_);
		}
	}

	/**
	 * @param directory_name
	 */
	public DirectoryName(String directory_name) {
		String[] parts = directory_name.split("[ ]+");
		this.number = Integer.parseInt(parts[0]);
		this.name = parts[1];
		this.x = Integer.parseInt(parts[2]);
		this.y = Integer.parseInt(parts[3]);
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

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
}
