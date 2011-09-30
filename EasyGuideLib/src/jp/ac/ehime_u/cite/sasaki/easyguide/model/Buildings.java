package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

public class Buildings {
	public ArrayList<Building> buildings = new ArrayList<Building>();

	public Buildings(File buildings_directory) {
		File[] building_directories = buildings_directory.listFiles();
		for (int i = 0; i < building_directories.length; ++i) {
			File building_directory = building_directories[i];
			if (!building_directory.isDirectory()) {
				continue;
			}
			this.buildings.add(new Building(building_directory));
		}
	}
}
