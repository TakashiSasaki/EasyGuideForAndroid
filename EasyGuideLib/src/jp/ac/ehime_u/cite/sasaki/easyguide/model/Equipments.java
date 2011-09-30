package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

public class Equipments {

	public ArrayList<Equipment> equipments = new ArrayList<Equipment>();

	public Equipments(File equipments_directory) {
		File[] equipment_directories = equipments_directory.listFiles();
		for (int i = 0; i < equipment_directories.length; ++i) {
			File equipment_directory = equipment_directories[i];
			if (!equipment_directory.isDirectory()) {
				continue;
			}
			this.equipments.add(new Equipment(equipment_directory));
		}
	}

}
