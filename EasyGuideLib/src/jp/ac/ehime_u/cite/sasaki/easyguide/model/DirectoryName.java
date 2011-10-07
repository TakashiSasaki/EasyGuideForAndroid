package jp.ac.ehime_u.cite.sasaki.easyguide.model;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DirectoryName {
	private String name = "unknown";
	private int number = -1;
	private int x = -1, y = -1;

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
		if (parts.length == 1) {
			this.name = parts[0];
			return;
		}// if
		if (parts.length == 2) {
			try {
				this.number = Integer.parseInt(parts[0]);
			} catch (NumberFormatException e) {
				throw new Exception("Invalid directory name " + directory_name
						+ ", " + e.getMessage());
			}// try
			this.name = parts[1];
			return;
		}// if
		if (parts.length == 4) {
			this.number = Integer.parseInt(parts[0]);
			this.name = parts[1];
			try {
				this.x = Integer.parseInt(parts[2]);
				this.y = Integer.parseInt(parts[3]);
				return;
			} catch (NumberFormatException e) {
				throw new Exception("Invalid directory name " + directory_name
						+ ", " + e.getMessage());
			}// try
		}// if
		throw new Exception("Invalid directory name " + directory_name);
	}// a constructor

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
