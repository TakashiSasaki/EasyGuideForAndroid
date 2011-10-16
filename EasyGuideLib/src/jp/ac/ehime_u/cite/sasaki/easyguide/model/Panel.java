package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

import android.graphics.Bitmap;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Panel {
	private static final String panelImageName = "panel.png";
	private File panelDirectory;
	private DirectoryName panelDirectoryName;
	private DirectoryImage panelDirectoryImage;
	private PanelText panelText;
	private PanelImage panelImage;
	private PanelMovie panelMovie;
	private PanelHtml panelHtml;

	/**
	 * @param panel_directory
	 */
	public Panel(File panel_directory) {
		this.panelDirectory = panel_directory;
		this.panelDirectoryName = new DirectoryName(panel_directory.getName());
		this.panelDirectoryImage = new DirectoryImage(panel_directory,
				panelImageName);
		try {
			this.panelText = new PanelText(panel_directory);
		} catch (PanelException e) {
		}
		try {
			this.panelImage = new PanelImage(panel_directory);
		} catch (PanelException e) {
		}
		try {
			this.panelMovie = new PanelMovie(panel_directory);
		} catch (PanelException e) {
		}
		try {
			this.panelHtml = new PanelHtml(panel_directory);
		} catch (PanelException e) {
		}
	}// a constructor

	@SuppressWarnings("javadoc")
	public File getPanelDirectory() {
		return panelDirectory;
	}

	@SuppressWarnings("javadoc")
	public String getPanelName() {
		return this.panelDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public int getPanelNumber() {
		return this.panelDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getPanelThumbnail() {
		return this.panelDirectoryImage.getThumbnail();
	}

}// Panel

