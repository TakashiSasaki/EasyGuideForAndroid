package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Equipment;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Floor;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class RoomActivity extends ClickableActivity<Equipment> {
	Room room;
	int organizationIndex;
	int facilityIndex;
	int buildingIndex;
	int floorIndex;
	int roomIndex;
	Button buttonShowCurrentContentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.room);

		SelectRoom();
		try {
			setImageView(this.room);
		} catch (Exception e1) {
			Log.v(new Throwable(), "Can't set image of " + this.room.getTitle());
		}
		for (Equipment e : this.room) {
			addStarPoint(new Point(e.getX(), e.getY()));
		}
		// SetImageView();
		// SetSpinnerEquipments();
		ArrayAdapter<Equipment> equipment_array_adapter = new ArrayAdapter<Equipment>(
				this, android.R.layout.simple_spinner_dropdown_item);
		equipment_array_adapter.add(Equipment.getDummy());
		for (Equipment e : this.room) {
			equipment_array_adapter.add(e);
		}
		setSpinnerArrayAdapter(equipment_array_adapter);

//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//		alertDialogBuilder.setTitle("content index");
//		alertDialogBuilder.setMessage("organizationIndex="
//				+ RoomActivity.this.organizationIndex + ", facilityIndex="
//				+ RoomActivity.this.facilityIndex + ", buildingIndex="
//				+ RoomActivity.this.buildingIndex + ", floorIndex="
//				+ RoomActivity.this.floorIndex + ", roomIndex="
//				+ RoomActivity.this.roomIndex);
//		AlertDialog alertDialog = alertDialogBuilder.create();
//		alertDialog.show();

	}// onCreate

	// private void SetSpinnerEquipments() {
	// ArrayAdapter<Equipment> equipment_array_adapter = new
	// ArrayAdapter<Equipment>(
	// this, android.R.layout.simple_spinner_dropdown_item);
	// equipment_array_adapter.add(Equipment.getDummy());
	// for (Equipment e : this.room) {
	// equipment_array_adapter.add(e);
	// }
	// Spinner s = (Spinner) findViewById(R.id.spinnerEquipments);
	// s.setAdapter(equipment_array_adapter);
	// OnItemSelectedListener l = new OnItemSelectedListener() {
	//
	// @Override
	// public void onItemSelected(AdapterView<?> arg0, View arg1,
	// int arg2, long arg3) {
	// Equipment e = (Equipment) arg0.getItemAtPosition(arg2);
	// if (e.isEmpty())
	// return;
	// InvokeActivity(e.getIndex());
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	// };
	// s.setOnItemSelectedListener(l);
	// }// SetSpinnerFloors

	// private void SetImageView() {
	// ImageView i = (ImageView) findViewById(R.id.imageViewRoom);
	// i.setImageBitmap(this.room.getImage());
	// }

	private void SelectRoom() {
		Intent intent = this.getIntent();
		this.organizationIndex = intent.getIntExtra("organizationIndex", 0);
		this.facilityIndex = intent.getIntExtra("facilityIndex", 0);
		this.buildingIndex = intent.getIntExtra("buildingIndex", 0);
		this.floorIndex = intent.getIntExtra("floorIndex", 0);
		this.roomIndex = intent.getIntExtra("roomIndex", 0);

		try {
			Organizations organizations = Organizations.getInstance();
			Organization organization;
			organization = organizations
					.getOrganization(this.organizationIndex);
			Facility facility = organization.getFacility(this.facilityIndex);
			Building building = facility.getBuilding(this.buildingIndex);
			Floor floor = building.getFloor(this.floorIndex);
			this.room = floor.getRoom(this.roomIndex);
		} catch (ItemNotFoundException e) {
			this.room = Room.getDummy();
		}
	}

	@Override
	protected void InvokeActivity(Equipment selected_item) {
		Intent intent = new Intent();
		intent.setClass(this, EquipmentActivity.class);
		intent.putExtra("organizationIndex", this.organizationIndex);
		intent.putExtra("facilityIndex", this.facilityIndex);
		intent.putExtra("buildingIndex", this.buildingIndex);
		intent.putExtra("floorIndex", this.floorIndex);
		intent.putExtra("roomIndex", this.roomIndex);
		intent.putExtra("equipmentIndex", selected_item.getIndex());
		startActivity(intent);
	}

	@Override
	protected void onStarTouched(Point point) {
		try {
			Equipment eq = this.room.getNearest(point);
			InvokeActivity(eq);
		} catch (ItemNotFoundException e) {
			Log.v(new Throwable(), "No equipment near " + point.toString()
					+ " in room " + this.room.getTitle());
		}// try
	}// onStarTouched

}// RoomActivity
