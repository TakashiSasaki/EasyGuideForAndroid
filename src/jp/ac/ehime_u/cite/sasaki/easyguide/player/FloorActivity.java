package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Floor;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class FloorActivity extends ClickableActivity<Room> {
	Floor floor;
	int organizationIndex;
	int facilityIndex;
	int buildingIndex;
	int floorIndex;

	// Spinner spinner;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.floor);
		// this.spinner = (Spinner) findViewById(R.id.spinnerRooms);

		SelectFloor();
		setImageView(this.floor);
		for (Room r : this.floor) {
			addStarPoint(new Point(r.getX(), r.getY()));
		}
		
		ArrayAdapter<Room> room_array_adapter = new ArrayAdapter<Room>(this,
				android.R.layout.simple_spinner_dropdown_item);
		room_array_adapter.add(Room.getDummy());
		for (Room r : this.floor) {
			room_array_adapter.add(r);
		}
		setSpinnerArrayAdapter(room_array_adapter);
		
		//SetSpinnerRooms();
	}

	// @Override
	// protected void onStart() {
	// super.onStart();
	// Log.v(new Throwable(), "onStart called");
	// this.spinner.setSelection(0);
	// }

//	private void SetSpinnerRooms() {
//		ArrayAdapter<Room> room_array_adapter = new ArrayAdapter<Room>(this,
//				android.R.layout.simple_spinner_dropdown_item);
//		room_array_adapter.add(Room.getDummy());
//		for (Room r : this.floor) {
//			room_array_adapter.add(r);
//		}
//		this.spinner.setAdapter(room_array_adapter);
//		OnItemSelectedListener oisl = new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				Room r = (Room) arg0.getItemAtPosition(arg2);
//				if (r.isEmpty())
//					return;
//				InvokeRoomActivity(r.getIndex());
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		};
//		this.spinner.setOnItemSelectedListener(oisl);
//	}// SetSpinnerFloors

//	private void InvokeRoomActivity(int roomIndex) {
//	}
//
//	private void SetImageView() {
//		ImageView i = (ImageView) findViewById(R.id.imageViewFloor);
//		i.setImageBitmap(this.floor.getImage());
//	}

	private void SelectFloor() {
		Intent intent = this.getIntent();
		this.organizationIndex = intent.getIntExtra("organizationIndex", 0);
		this.facilityIndex = intent.getIntExtra("facilityIndex", 0);
		this.buildingIndex = intent.getIntExtra("buildingIndex", 0);
		this.floorIndex = intent.getIntExtra("floorIndex", 0);

		try {
			Organizations organizations = Organizations.getInstance();
			Organization organization;
			organization = organizations
					.getOrganization(this.organizationIndex);
			Facility facility = organization.getFacility(this.facilityIndex);
			Building building = facility.getBuilding(this.buildingIndex);
			this.floor = building.getFloor(this.floorIndex);
		} catch (ItemNotFoundException e) {
			this.floor = Floor.getDummy();
		}// try
	}// SelectFloor

	@Override
	protected void InvokeActivity(Room r) {
		Intent intent = new Intent();
		intent.setClass(this, RoomActivity.class);
		intent.putExtra("organizationIndex", this.organizationIndex);
		intent.putExtra("facilityIndex", this.facilityIndex);
		intent.putExtra("buildingIndex", this.buildingIndex);
		intent.putExtra("floorIndex", this.floorIndex);
		intent.putExtra("roomIndex", r.getIndex());
		startActivity(intent);
	}

	@Override
	protected void onStarTouched(Point point) {
		try {
			Room r = this.floor.getNearest(point);
			InvokeActivity(r);
		} catch (ItemNotFoundException e) {
			Log.v(new Throwable(), "No facility in this organization "
					+ this.floor.getTitle());
		}// try
	}

}// FloorActivity
