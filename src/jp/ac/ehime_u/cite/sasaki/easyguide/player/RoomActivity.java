package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Equipment;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Floor;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class RoomActivity extends Activity {
	Room room;
	int organizationIndex;
	int facilityIndex;
	int buildingIndex;
	int floorIndex;
	int roomIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room);

		SelectRoom();
		SetImageView();
		SetSpinnerEquipments();
	}// onCreate

	private void SetSpinnerEquipments() {
		ArrayAdapter<Equipment> equipment_array_adapter = new ArrayAdapter<Equipment>(
				this, android.R.layout.simple_spinner_dropdown_item);
		equipment_array_adapter.add(Equipment.getEmptyEquipment());
		for (Equipment e : this.room) {
			equipment_array_adapter.add(e);
		}
		Spinner s = (Spinner) findViewById(R.id.spinnerEquipments);
		s.setAdapter(equipment_array_adapter);
		OnItemSelectedListener l = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Equipment e = (Equipment) arg0.getItemAtPosition(arg2);
				if (e.isEmpty())
					return;
				InvokeEquipmentActivity(e.getEquipmentIndex());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		};
		s.setOnItemSelectedListener(l);
	}// SetSpinnerFloors

	private void SetImageView() {
		ImageView i = (ImageView) findViewById(R.id.imageViewRoom);
		i.setImageBitmap(this.room.getRoomImage());
	}

	private void SelectRoom() {
		Intent intent = this.getIntent();
		this.organizationIndex = intent.getIntExtra("organizationIndex", 0);
		this.facilityIndex = intent.getIntExtra("facilityIndex", 0);
		this.buildingIndex = intent.getIntExtra("buildingIndex", 0);
		this.floorIndex = intent.getIntExtra("floorIndex", 0);
		this.roomIndex = intent.getIntExtra("roomIndex", 0);

		Organizations organizations = Organizations.GetTheOrganizations();
		Organization organization = organizations
				.GetOrganizationByIndex(this.organizationIndex);
		Facility facility = organization.getFacilityByIndex(this.facilityIndex);
		Building building = facility.getBuildingByIndex(this.buildingIndex);
		Floor floor = building.getFloorByIndex(this.floorIndex);
		this.room = floor.getRoomByIndex(this.roomIndex);
	}

	private void InvokeEquipmentActivity(int equipmentIndex) {
		Intent intent = new Intent();
		intent.setClass(this, EquipmentActivity.class);
		intent.putExtra("organizationIndex", this.organizationIndex);
		intent.putExtra("facilityIndex", this.facilityIndex);
		intent.putExtra("buildingIndex", this.buildingIndex);
		intent.putExtra("floorIndex", this.floorIndex);
		intent.putExtra("roomIndex", this.roomIndex);
		intent.putExtra("equipmentIndex", equipmentIndex);
		startActivity(intent);
	}

}
