package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Floor;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class FloorActivity extends Activity {
	Floor floor;
	int organizationIndex;
	int facilityIndex;
	int buildingIndex;
	int floorIndex;
	Spinner spinner;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.floor);
		this.spinner = (Spinner) findViewById(R.id.spinnerRooms);

		SelectFloor();
		SetImageView();
		SetSpinnerRooms();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(new Throwable(), "onStart called");
		this.spinner.setSelection(0);
	}

	private void SetSpinnerRooms() {
		ArrayAdapter<Room> room_array_adapter = new ArrayAdapter<Room>(this,
				android.R.layout.simple_spinner_dropdown_item);
		room_array_adapter.add(Room.getEmptyRoom());
		for (Room r : this.floor) {
			room_array_adapter.add(r);
		}
		this.spinner.setAdapter(room_array_adapter);
		OnItemSelectedListener oisl = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Room r = (Room) arg0.getItemAtPosition(arg2);
				if (r.isEmpty())
					return;
				InvokeRoomActivity(r.getRoomIndex());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		};
		this.spinner.setOnItemSelectedListener(oisl);
	}// SetSpinnerFloors

	private void InvokeRoomActivity(int roomIndex) {
		Intent intent = new Intent();
		intent.setClass(this, RoomActivity.class);
		intent.putExtra("organizationIndex", this.organizationIndex);
		intent.putExtra("facilityIndex", this.facilityIndex);
		intent.putExtra("buildingIndex", this.buildingIndex);
		intent.putExtra("floorIndex", this.floorIndex);
		intent.putExtra("roomIndex", roomIndex);
		startActivity(intent);
	}

	private void SetImageView() {
		ImageView i = (ImageView) findViewById(R.id.imageViewFloor);
		i.setImageBitmap(this.floor.getFloorImage());
	}

	private void SelectFloor() {
		Intent intent = this.getIntent();
		this.organizationIndex = intent.getIntExtra("organizationIndex", 0);
		this.facilityIndex = intent.getIntExtra("facilityIndex", 0);
		this.buildingIndex = intent.getIntExtra("buildingIndex", 0);
		this.floorIndex = intent.getIntExtra("floorIndex", 0);

		Organizations organizations = Organizations.GetTheOrganizations();
		Organization organization = organizations
				.GetOrganizationByIndex(this.organizationIndex);
		Facility facility = organization.getFacilityByIndex(this.facilityIndex);
		Building building = facility.getBuildingByIndex(this.buildingIndex);
		this.floor = building.getFloorByIndex(this.floorIndex);
	}

}
