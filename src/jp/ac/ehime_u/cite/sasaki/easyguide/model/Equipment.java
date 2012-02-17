package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.graphics.Bitmap;
import android.content.res.Resources.NotFoundException;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
@SuppressWarnings("serial")
public class Equipment extends ArrayList<Panel> {

	private static final String equipmentImageName = "equipment.png";
	private File equipmentDirectory;
	private DirectoryName equipmentDirectoryName;
	private DirectoryImage equipmentDirectoryImage;

	/**
	 * a constructor
	 * 
	 * @param equipment_directory
	 */
	public Equipment(File equipment_directory) {
		super();
		Log.v(new Throwable(),
				"Constructing equipment "
						+ equipment_directory.getAbsolutePath());
		this.equipmentDirectory = equipment_directory;
		this.equipmentDirectoryName = new DirectoryName(
				equipment_directory.getName());
		this.equipmentDirectoryImage = new DirectoryImage(equipment_directory);
		this.EnumeratePanels();
	}// a constructor

	private Equipment() {
		super();
	}

	public String toString() {
		if (this.isEmpty())
			return "";
		return this.equipmentDirectoryName.getName();
	}// toString

	@SuppressWarnings("javadoc")
	public void EnumeratePanels() {
		for (File panel_directory : this.equipmentDirectory.listFiles()) {
			if (panel_directory.isDirectory())
				this.add(new Panel(panel_directory));
		}// for
		this.SortByPanelNumber();
	}// EnumeratePanels

	@SuppressWarnings("javadoc")
	public void SortByPanelNumber() {
		Collections.sort(this, new Comparator<Panel>() {

			@Override
			public int compare(Panel arg0, Panel arg1) {
				return arg0.getPanelNumber() - arg1.getPanelNumber();
			}// compare
		});// sort
	}// SortByPanelNumber

	@SuppressWarnings("javadoc")
	public File getEquipmentDirectory() {
		return this.equipmentDirectory;
	}

	@SuppressWarnings("javadoc")
	public String getEquipmentName() {
		return this.equipmentDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public DirectoryName getEquipmentDirectoryName() {
		return this.equipmentDirectoryName;
	}

	@SuppressWarnings("javadoc")
	public int getEquipmentIndex() {
		return this.equipmentDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public int getEquipmentX() {
		return this.equipmentDirectoryName.getX();
	}

	@SuppressWarnings("javadoc")
	public int getEquipmentY() {
		return this.equipmentDirectoryName.getY();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getEquipmentImage() {
		return this.equipmentDirectoryImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getEquipmentThumbnail() {
		return this.equipmentDirectoryImage.getThumbnail();
	}

	/**
	 * @param panel_name
	 * @return Panel object
	 */
	public Panel GetPanel(String panel_name) {
		for (Panel panel : this) {
			if (panel.getPanelName().equals(panel_name)) {
				return panel;
			}
		}// for
		throw new NotFoundException("Can't find " + panel_name + " in "
				+ this.getEquipmentName());
	}// GetPanel

	/**
	 * @param panel_number
	 * @return Panel object
	 */
	public Panel GetPanel(int panel_number) {
		for (Panel panel : this) {
			if (panel.getPanelNumber() == panel_number) {
				return panel;
			}
		}
		throw new NotFoundException("Can't find " + panel_number + " in "
				+ this.getEquipmentName());
	}// GetPanel

	public static Equipment getEmptyEquipment() {
		return new Equipment();
	}

	// @Override
	// @Deprecated
	// public Panel get(int index) {
	// throw new Error("Equipment#get is deprecated.");
	// }// get
}// Equipment
