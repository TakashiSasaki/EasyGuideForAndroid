package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Equipment;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Floor;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Panel;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import android.graphics.Bitmap;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class Summary {

	private String title;
	private int order;
	private int x;
	private int y;
	private Bitmap image;
	private String path;

	/**
	 * a constructor
	 * 
	 * @param organization
	 */
	public Summary(Organization organization) {
		this.title = organization.getOrganizationDomain();
		this.order = -1;
		this.x = -1;
		this.y = -1;
		this.image = organization.getOrganizationThumbnail();
		this.path = organization.getOrganizationDirectory().getAbsolutePath();
	}// a constructor

	/**
	 * a constructor
	 * 
	 * @param facility
	 */
	public Summary(Facility facility) {
		this.title = facility.getFacilityName();
		this.order = facility.getFacilityIndex();
		this.x = facility.getFacilityX();
		this.y = facility.getFacilityY();
		this.image = facility.getFacilityThumbnail();
		this.path = facility.getFacilityDirectory().getAbsolutePath();
	}// a constructor

	/**
	 * a constructor
	 * 
	 * @param building
	 */
	public Summary(Building building) {
		this.title = building.getBuildingName();
		this.order = building.getBuildingIndex();
		this.x = building.getBuildingX();
		this.y = building.getBuildingY();
		this.image = building.getBuildingThumbnail();
		this.path = building.getBuildingDirectory().getAbsolutePath();
	}// a constructor

	/**
	 * a constructor
	 * 
	 * @param floor
	 */
	public Summary(Floor floor) {
		this.title = floor.getFloorName();
		this.order = floor.getFloorNumber();
		this.x = floor.getFloorX();
		this.y = floor.getFloorY();
		this.image = floor.getFloorThumbnail();
		this.path = floor.getFloorDirectory().getAbsolutePath();
	}// a constructor

	/**
	 * a constructor
	 * 
	 * @param room
	 */
	public Summary(Room room) {
		this.title = room.getRoomName();
		this.order = room.getRoomNumber();
		this.x = room.getRoomX();
		this.y = room.getRoomY();
		this.image = room.getRoomThumbnail();
		this.path = room.getRoomDirectory().getAbsolutePath();
	}// a constructor

	/**
	 * a constructor
	 * 
	 * @param equipment
	 */
	public Summary(Equipment equipment) {
		this.title = equipment.getEquipmentName();
		this.order = equipment.getEquipmentNumber();
		this.x = equipment.getEquipmentX();
		this.y = equipment.getEquipmentY();
		this.image = equipment.getEquipmentThumbnail();
		this.path = equipment.getEquipmentDirectory().getAbsolutePath();
	}// a constructor

	/**
	 * a constructor
	 * 
	 * @param panel
	 */
	public Summary(Panel panel) {
		this.title = panel.getPanelName();
		this.order = panel.getPanelNumber();
		this.x = -1;
		this.y = -1;
		this.image = panel.getPanelThumbnail();
		this.path = panel.getPanelDirectory().getAbsolutePath();
	}// a constructor

	@SuppressWarnings("javadoc")
	public String getTitle() {
		return this.title;
	}

	@SuppressWarnings("javadoc")
	public int getX() {
		return this.x;
	}

	@SuppressWarnings("javadoc")
	public int getY() {
		return this.y;
	}

	@SuppressWarnings("javadoc")
	public Bitmap getImage() {
		return this.image;
	}

	@SuppressWarnings("javadoc")
	public int getOrder() {
		return this.order;
	}

	@SuppressWarnings("javadoc")
	public String getPath() {
		return this.path;
	}
}// Summary