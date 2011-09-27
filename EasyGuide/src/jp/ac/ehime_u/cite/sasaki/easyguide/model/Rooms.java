package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;

public class Rooms {

	public ArrayList<Room> rooms = new ArrayList<Room>();

	public Rooms(File floor_directory) {
		File[] room_directories = floor_directory.listFiles();
		for (int i = 0; i < room_directories.length; ++i) {
			File room_directory = room_directories[i];
			if (!room_directory.isDirectory()) {
				continue;
			}
			this.rooms.add(new Room(room_directory));
		}
	}

}
