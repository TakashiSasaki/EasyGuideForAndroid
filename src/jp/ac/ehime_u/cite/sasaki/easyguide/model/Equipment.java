package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.graphics.Bitmap;
import android.util.Log;
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
		Log.v(this.getClass().getSimpleName(), "Constructing equipment "
				+ equipment_directory.getAbsolutePath());
		this.equipmentDirectory = equipment_directory;
		this.equipmentDirectoryName = new DirectoryName(
				equipment_directory.getName());
		this.equipmentDirectoryImage = new DirectoryImage(equipment_directory,
				equipmentImageName);
		for (File panel_directory : this.equipmentDirectory.listFiles()) {
			if (panel_directory.isDirectory())
				this.add(new Panel(panel_directory));
		}// for
		this.Sort();
	}// a constructor

	@SuppressWarnings("javadoc")
	public void Sort() {
		Collections.sort(this, new Comparator<Panel>() {

			@Override
			public int compare(Panel arg0, Panel arg1) {
				return arg0.getPanelNumber() - arg1.getPanelNumber();
			}// compare
		});// sort
	}// Sort

	@SuppressWarnings("javadoc")
	public File getEquipmentDirectory() {
		return equipmentDirectory;
	}

	@SuppressWarnings("javadoc")
	public String getEquipmentName() {
		return equipmentDirectoryName.getName();
	}

	@SuppressWarnings("javadoc")
	public int getEquipmentNumber() {
		return equipmentDirectoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public int getEquipmentX() {
		return equipmentDirectoryName.getX();
	}

	@SuppressWarnings("javadoc")
	public int getEquipmentY() {
		return equipmentDirectoryName.getY();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getEquipmentImage() {
		return equipmentDirectoryImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getEquipmentThumbnail() {
		return equipmentDirectoryImage.getThumbnail();
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

	@Override
	@Deprecated
	public Panel get(int index) {
		throw new Error("Equipment#get is deprecated.");
	}// get
}// Equipment
