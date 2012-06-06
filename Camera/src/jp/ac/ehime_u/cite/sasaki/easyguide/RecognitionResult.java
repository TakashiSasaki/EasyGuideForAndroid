package jp.ac.ehime_u.cite.sasaki.easyguide;

/**
 * RecognitionResult holds results of object recognition.
 * 
 */
public class RecognitionResult {
	final public int organizationNumber;
	final public int facilityNumber;
	final public int buildingNumber;
	final public int floorNumber;
	final public int roomNumber;
	final public int equipmentNumber;

	public RecognitionResult(int organization_number, int facility_number,
			int building_number, int floor_number, int room_number,
			int equipment_number) {
		this.organizationNumber = organization_number;
		this.facilityNumber = facility_number;
		this.buildingNumber = building_number;
		this.roomNumber = room_number;
		this.equipmentNumber = equipment_number;
		this.floorNumber = floor_number;
	}

	public RecognitionResult(int equipment_number) {
		this.organizationNumber = this.facilityNumber = this.buildingNumber = this.floorNumber = this.roomNumber = 0;
		this.equipmentNumber = equipment_number;
	}
}
