package jp.ac.ehime_u.cite.sasaki.easyguide.ui;

import java.io.File;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.ItemType;
import android.graphics.Bitmap;

public class TocItem {
	public TocItem(ItemType item_type, int index, String title, int x, int y,
			File path, Bitmap thumbnail) {
		this.itemType = item_type;
		this.title = title;
		this.index = index;
		this.path = path;
		this.x = x;
		this.y = y;
		this.thumbnail = thumbnail;
	}

	ItemType itemType;
	String title;
	int index, x, y;
	File path;
	Bitmap thumbnail;

	public String getLayerTypeName() {
		switch (this.itemType) {
		case ORGANIZATION_TYPE:
			return "organization";
		case BUILDING_TYPE:
			return "building";
		case EQUIPMENT_TYPE:
			return "equipment";
		case FACILITY_TYPE:
			return "facility";
		case FLOOR_TYPE:
			return "floor";
		case PANEL_TYPE:
			return "panel";
		case ROOM_TYPE:
			return "room";
		default:
			return "unknown";
		}// switch
	}

	public String getTitle() {
		return this.title;
	}

	public String getPath() {
		return this.path.getPath();
	}

	public String getX() {
		return "" + this.x;
	}

	public String getY() {
		return "" + this.y;
	}

	public String getIndex() {
		return "" + this.index;
	}

	public Bitmap getThumbnail() {
		return this.thumbnail;
	}
}// TocItem
