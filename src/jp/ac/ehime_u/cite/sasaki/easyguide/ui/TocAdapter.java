package jp.ac.ehime_u.cite.sasaki.easyguide.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.SetterOnlyReflection;

import jp.ac.ehime_u.cite.sasaki.easyguide.R;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.DirectoryName;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Equipment;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Floor;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.LayerType;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Panel;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import jp.ac.ehime_u.cite.sasaki.easyguide.ui.TocAdapter.TocItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TocAdapter extends BaseAdapter {

	Organizations organizations = Organizations.GetTheOrganizations();

	class TocItem {
		public TocItem(LayerType layer_type, int index, String title, int x,
				int y, File path, Bitmap thumbnail) {
			this.layerType = layer_type;
			this.title = title;
			this.index = index;
			this.path = path;
			this.x = x;
			this.y = y;
			this.thumbnail = thumbnail;
		}

		LayerType layerType;
		String title;
		int index, x, y;
		File path;
		Bitmap thumbnail;

		public String getLayerTypeName() {
			switch (this.layerType) {
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

	ArrayList<TocItem> tocArrayList = new ArrayList<TocItem>();

	LayoutInflater layoutInflater;

	/**
	 * the constructor of TocAdapter
	 * 
	 * @param context
	 */
	public TocAdapter(Context context) {
		super();

		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		rebuildArray();

	}// the constructor

	void rebuildArray() {
		this.tocArrayList.clear();
		this.tocArrayList = null;
		this.tocArrayList = new ArrayList<TocItem>();
		for (Organization organization : this.organizations) {
			this.tocArrayList.add(new TocItem(LayerType.ORGANIZATION_TYPE,
					organization.getOrganizationDirectoryName().getNumber(),
					organization.getOrganizationDirectoryName().getName(),
					organization.getOrganizationDirectoryName().getX(),
					organization.getOrganizationDirectoryName().getY(),
					organization.getOrganizationDirectory(), organization
							.getOrganizationThumbnail()));
			organization.EnumerateFacilities();
			for (Facility facility : organization) {
				this.tocArrayList.add(new TocItem(LayerType.FACILITY_TYPE,
						facility.getFacilityDirectoryName().getNumber(),
						facility.getFacilityDirectoryName().getName(), facility
								.getFacilityDirectoryName().getX(), facility
								.getFacilityDirectoryName().getY(), facility
								.getFacilityDirectory(), facility
								.getFacilityThumbnail()));
				facility.EnumerateBuildings();
				for (Building building : facility) {
					this.tocArrayList.add(new TocItem(LayerType.BUILDING_TYPE,
							building.getBuildingDirectoryName().getNumber(),
							building.getBuildingDirectoryName().getName(),
							building.getBuildingDirectoryName().getX(),
							building.getBuildingDirectoryName().getY(),
							building.getBuildingDirectory(), building
									.getBuildingThumbnail()));
					building.EnumerateFloors();
					for (Floor floor : building) {
						this.tocArrayList.add(new TocItem(LayerType.FLOOR_TYPE,
								floor.getFloorDirectoryName().getNumber(),
								floor.getFloorDirectoryName().getName(), floor
										.getFloorDirectoryName().getX(), floor
										.getFloorDirectoryName().getY(), floor
										.getFloorDirectory(), floor
										.getFloorThumbnail()));
						floor.EnumerateRooms();
						for (Room room : floor) {
							this.tocArrayList.add(new TocItem(
									LayerType.ROOM_TYPE, room.getRoomIndex(),
									room.toString(), room.getRoomX(), room
											.getRoomY(), room
											.getRoomDirectory(), room
											.getRoomThumbnail()));
							room.EnumerateEquipments();
							for (Equipment equipment : room) {
								this.tocArrayList.add(new TocItem(
										LayerType.EQUIPMENT_TYPE, equipment
												.getEquipmentIndex(), equipment
												.getEquipmentTitle(), equipment
												.getEquipmentX(), equipment
												.getEquipmentY(), equipment
												.getEquipmentDirectory(),
										equipment.getEquipmentThumbnail()));
								equipment.EnumeratePanels();
								for (Panel panel : equipment) {
									this.tocArrayList.add(new TocItem(
											LayerType.PANEL_TYPE, panel
													.getPanelIndex(), panel
													.getPanelTitle(), -1, -1,
											panel.getPanelDirectory(), panel
													.getPanelThumbnail()));
								}// for
							}// for
						}// for
					}// for
				}// for
			}// for
		}// for
	}// rebuildArray

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			// View can be recycled. If the view is not generated yet, it should
			// be inflated here.
			view = this.layoutInflater.inflate(R.layout.tocitem, null);
		}
		try {
			TocItem toc_item = this.tocArrayList.get(position);
		((TextView) (view.findViewById(R.id.textViewLevelName)))
					.setText(toc_item.getLayerTypeName());
			((TextView) (view.findViewById(R.id.textViewTitle)))
					.setText(toc_item.getTitle());
			((TextView) (view.findViewById(R.id.textViewNumber)))
					.setText(toc_item.index);
			((TextView) (view.findViewById(R.id.textViewPath)))
					.setText(toc_item.getPath());
			((TextView) (view.findViewById(R.id.textViewX))).setText(toc_item
					.getX());
			((TextView) (view.findViewById(R.id.textViewY))).setText(toc_item
					.getY());
		} catch (IndexOutOfBoundsException e) {
			Log.e(this.getClass().getSimpleName(),
					"no item in tocArrayList at position" + position);
			return null;
		}// try
		return view;
	}// getView

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}// TocAdapter
