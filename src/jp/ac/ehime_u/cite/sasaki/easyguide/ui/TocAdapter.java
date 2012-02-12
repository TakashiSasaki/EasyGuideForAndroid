package jp.ac.ehime_u.cite.sasaki.easyguide.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TocAdapter extends ArrayAdapter<TocArrayItem> {

	Organizations organizations = Organizations.GetTheOrganizations();

	class TocItem {
		public TocItem(LayerType layer_type, DirectoryName directory_name,
				File path, Bitmap thumbnail) {
			this.layerType = layer_type;
			this.directoryName = directory_name;
			this.path = path;
			this.thumbnail = thumbnail;
		}

		LayerType layerType;
		DirectoryName directoryName;
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
			return this.directoryName.getRawName();
		}

		public String getPath() {
			return this.path.getPath();
		}

		public String getX() {
			return "" + this.directoryName.getX();
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
	 * @param resource
	 * @param textViewResourceId
	 */
	public TocAdapter(Context context, int resource) {
		super(context, resource);

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
					organization.getOrganizationDirectoryName(), organization
							.getOrganizationDirectory(), organization
							.getOrganizationThumbnail()));
			organization.EnumerateFacilities();
			for (Facility facility : organization) {
				this.tocArrayList.add(new TocItem(LayerType.FACILITY_TYPE,
						facility.getFacilityDirectoryName(), facility
								.getFacilityDirectory(), facility
								.getFacilityThumbnail()));
				facility.EnumerateBuildings();
				for (Building building : facility) {
					this.tocArrayList.add(new TocItem(LayerType.BUILDING_TYPE,
							building.getBuildingDirectoryName(), building
									.getBuildingDirectory(), building
									.getBuildingThumbnail()));
					building.EnumerateFloors();
					for (Floor floor : building) {
						this.tocArrayList.add(new TocItem(LayerType.FLOOR_TYPE,
								floor.getFloorDirectoryName(), floor
										.getFloorDirectory(), floor
										.getFloorThumbnail()));
						floor.EnumerateRooms();
						for (Room room : floor) {
							this.tocArrayList.add(new TocItem(
									LayerType.ROOM_TYPE, room
											.getRoomDirectoryName(), room
											.getRoomDirectory(), room
											.getRoomThumbnail()));
							room.EnumerateEquipments();
							for (Equipment equipment : room) {
								this.tocArrayList.add(new TocItem(
										LayerType.EQUIPMENT_TYPE, equipment
												.getEquipmentDirectoryName(),
										equipment.getEquipmentDirectory(),
										equipment.getEquipmentThumbnail()));
								equipment.EnumeratePanels();
								for (Panel panel : equipment) {
									this.tocArrayList.add(new TocItem(
											LayerType.PANEL_TYPE, panel
													.getPanelDirectoryName(),
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
			((TextView) (view.findViewById(R.id.textViewDirectoryName)))
					.setText(toc_item.directoryName.getRawName());
			((TextView) (view.findViewById(R.id.textViewLevelName)))
					.setText(getLayerTypeName(toc_item.layerType));
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
		TwitterStatus item = (TwitterStatus) items.get(position);
		if (item != null) {
			TextView screenName = (TextView) view.findViewById(R.id.toptext);
			screenName.setTypeface(Typeface.DEFAULT_BOLD);

			// スクリーンネームをビューにセット
			TextView text = (TextView) view.findViewById(R.id.bottomtext);
			if (screenName != null) {
				screenName.setText(item.getScreenName());
			}

			// テキストをビューにセット
			if (text != null) {
				text.setText(item.getText());
			}
		}
		return view;
	}// getView
}// TocAdapter
