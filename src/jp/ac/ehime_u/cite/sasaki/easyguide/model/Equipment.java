package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Equipment extends ItemBase<Equipment, Panel> {

	// private static final String equipmentImageName = "equipment.png";
	// private File equipmentDirectory;
	// private DirectoryName equipmentDirectoryName;
	// private DirectoryImage equipmentDirectoryImage;
	// private Collection<Panel> panels;

	static private Equipment dummyEquipment = new Equipment();

	static public Equipment getDummy() {
		return dummyEquipment;
	}

	/**
	 * a constructor
	 * 
	 * @param equipment_directory
	 */
	public Equipment(File equipment_directory) {
		super(equipment_directory);
		Log.v(new Throwable(),
				"Constructing equipment "
						+ equipment_directory.getAbsolutePath());
		// this.directory = equipment_directory;
		// this.directoryName = new
		// DirectoryName(equipment_directory.getName());
		// this.directoryImage = new DirectoryImage(equipment_directory);
		this.EnumeratePanels();
	}// a constructor

	private Equipment() {
		super();
	}

	public Panel getPanel(String title) throws ItemNotFoundException {
		return this.getByTitle(title);
	}

	// public String toString() {
	// if (this.isEmpty())
	// return "";
	// return this.equipmentDirectoryName.getName();
	// }// toString

	@SuppressWarnings("javadoc")
	public void EnumeratePanels() {
		for (File panel_directory : this.listFiles()) {
			if (panel_directory.isDirectory())
				this.add(new Panel(panel_directory));
		}// for
		this.sortByIndex();
	}// EnumeratePanels

	// @SuppressWarnings("javadoc")
	// public void SortByPanelNumber() {
	// Collections.sort(this.items, new Comparator<Panel>() {
	//
	// @Override
	// public int compare(Panel arg0, Panel arg1) {
	// return arg0.getPanelIndex() - arg1.getPanelIndex();
	// }// compare
	// });// sort
	// }// SortByPanelNumber

	// @SuppressWarnings("javadoc")
	// public File getEquipmentDirectory() {
	// return this.directory;
	// }

	// @SuppressWarnings("javadoc")
	// public String getEquipmentTitle() {
	// return this.getTitle();
	// }

	// @SuppressWarnings("javadoc")
	// public DirectoryName getEquipmentDirectoryName() {
	// return this.equipmentDirectoryName;
	// }

	// /**
	// * @param panel_name
	// * @return Panel object
	// */
	// public Panel getPanelByTitle(String panel_name) {
	// for (Panel panel : this) {
	// if (panel.getPanelTitle().equals(panel_name)) {
	// return panel;
	// }
	// }// for
	// throw new NotFoundException("Can't find " + panel_name + " in "
	// + this.getEquipmentTitle());
	// }// GetPanel

	// /**
	// * @param panel_number
	// * @return Panel object
	// */
	// public Panel getPanelByIndex(int panel_number) {
	// for (Panel panel : this) {
	// if (panel.getPanelIndex() == panel_number) {
	// return panel;
	// }
	// }
	// throw new NotFoundException("Can't find " + panel_number + " in "
	// + this.getEquipmentTitle());
	// }// GetPanel

	// public static Equipment getEmptyEquipment() {
	// return new Equipment();
	// }

}// Equipment
