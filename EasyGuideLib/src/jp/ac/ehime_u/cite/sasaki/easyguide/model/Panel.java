package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Panel {
	private File panelDirectory;

	/**
	 * @param panel_directory
	 */
	public Panel(File panel_directory) {
		this.panelDirectory = panel_directory;
	}// a constructor

	/**
	 * @return the panelDirectory
	 */
	public File getPanelDirectory() {
		return panelDirectory;
	}
}// Panel

