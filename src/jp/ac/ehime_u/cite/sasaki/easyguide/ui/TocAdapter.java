package jp.ac.ehime_u.cite.sasaki.easyguide.ui;

import java.util.ArrayList;
import jp.ac.ehime_u.cite.sasaki.easyguide.R;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Equipment;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Floor;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Panel;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TocAdapter extends BaseAdapter {

	Organizations organizations = Organizations.getInstance();

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
			this.tocArrayList.add(organization.getTocItem());
			organization.EnumerateFacilities();
			for (Facility facility : organization) {
				this.tocArrayList.add(facility.getTocItem());
				facility.EnumerateBuildings();
				for (Building building : facility) {
					this.tocArrayList.add(building.getTocItem());
					building.EnumerateFloors();
					for (Floor floor : building) {
						this.tocArrayList.add(floor.getTocItem());
						floor.EnumerateRooms();
						for (Room room : floor) {
							this.tocArrayList.add(room.getTocItem());
							room.EnumerateEquipments();
							for (Equipment equipment : room) {
								this.tocArrayList.add(equipment.getTocItem());
								equipment.EnumeratePanels();
								for (Panel panel : equipment) {
									this.tocArrayList.add(panel.getTocItem());
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
