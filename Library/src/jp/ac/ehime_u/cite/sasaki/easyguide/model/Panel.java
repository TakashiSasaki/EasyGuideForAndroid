package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.io.FileNotFoundException;

import sun.security.action.OpenFileInputStreamAction;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.Classifier;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Panel extends ItemBase {
	// private static final String panelImageName = "panel.png";
	// private File panelDirectory;
	// private DirectoryName panelDirectoryName;
	// private DirectoryImage panelDirectoryImage;
	private Classifier classifier;

	/**
	 * @param panel_directory
	 */
	public Panel(File panel_directory) {
		super(panel_directory);
		// this.panelDirectory = panel_directory;
		// this.panelDirectoryName = new
		// DirectoryName(panel_directory.getName());
		// this.panelDirectoryImage = new DirectoryImage(panel_directory);

		this.classifier = new Classifier(panel_directory);
	}// a constructor

	// @SuppressWarnings("javadoc")
	// public File getPanelDirectory() {
	// return panelDirectory;
	// }

	// @SuppressWarnings("javadoc")
	// public DirectoryName getPanelDirectoryName() {
	// return this.panelDirectoryName;
	// }

	// @SuppressWarnings("javadoc")
	// public String getPanelTitle() {
	// return this.panelDirectoryName.getName();
	// }

	// @SuppressWarnings("javadoc")
	// public int getPanelIndex() {
	// return this.panelDirectoryName.getNumber();
	// }

	// @SuppressWarnings("javadoc")
	// public Bitmap getPanelThumbnail() {
	// return this.panelDirectoryImage.getThumbnail();
	// }

	public boolean hasVideo() {
		if (this.classifier.getMovieFiles().size() > 0) {
			return true;
		} else
			return false;
	}

	public String getVideoPath() {
		if (this.classifier.getMovieFiles().size() > 0) {
			return this.classifier.getMovieFiles().get(0).getAbsolutePath();
		} else
			return null;
	}

	public boolean hasImage() {
		if (this.classifier.getImageFiles().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasHtml() {
		if (this.classifier.getHtmlFiles().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasText() {
		if (this.classifier.getTextFiles().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public File getTextFile() throws FileNotFoundException {
		if (this.classifier.getTextFiles().size() == 0)
			throw new FileNotFoundException("No text file in "
					+ this.getTitle());
		return this.classifier.getTextFiles().get(0);
	}// getTextFile
}// Panel
