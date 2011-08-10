package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.io.File;
import java.util.ArrayList;

public class Floors {

	public ArrayList<Floor> floors;

	public Floors(File floors_directory) {
		this.floors = new ArrayList<Floor>();
		File[] floor_directories = floors_directory.listFiles();
		for (int i = 0; i < floor_directories.length; ++i) {
			File floor_directory = floor_directories[i];
			if (!floor_directory.isDirectory()) {
				continue;
			}
			floors.add(new Floor(floor_directory));
		}
	}

}
