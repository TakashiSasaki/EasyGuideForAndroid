package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Panel {
	private File panelDirectory;
	private DirectoryName panelDirectoryName;
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

}// Panel

