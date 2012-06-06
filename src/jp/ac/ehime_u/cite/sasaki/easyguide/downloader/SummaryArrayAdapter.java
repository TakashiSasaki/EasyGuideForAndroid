package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Building;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.DirectoryImage;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.DirectoryImageException;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Equipment;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Floor;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Panel;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Room;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class SummaryArrayAdapter extends ArrayAdapter<Summary> {

	/**
	 * @param context
	 * @param resource_id
	 * @throws DirectoryImageException
	 */
	public SummaryArrayAdapter(Context context, int resource_id)
			throws DirectoryImageException {
		super(context, resource_id);
		DirectoryImage.SetDefaultImage(context, R.drawable.unknown);
		for (Organization organization : Organizations.getInstance()) {
			this.add(new Summary(organization));
			for (Facility facility : organization) {
				this.add(new Summary(facility));
				for (Building building : facility) {
					this.add(new Summary(building));
					for (Floor floor : building) {
						this.add(new Summary(floor));
						for (Room room : floor) {
							this.add(new Summary(room));
							for (Equipment equipment : room) {
								this.add(new Summary(equipment));
								for (Panel panel : equipment) {
									this.add(new Summary(panel));
								}// for panel
							}// for equipment
						}// for room
					}// for floor
				}// for building
			}// for facility
		}// for organization
		Log.v(this.getClass().getSimpleName(), this.getCount()
				+ " item(s) found.");
	}// a constructor

	private class SummaryViews {
		private TextView textViewSummaryTitle;
		private TextView textViewSummaryX;
		private TextView textViewSummaryY;
		private ImageView imageViewSummary;
		private TextView textViewSummaryPath;

		public SummaryViews(View view) {
			// TODO: the constructor was emptied in order to eliminate errors. 
//			this.textViewSummaryTitle = (TextView) view
//					.findViewById(R.id.textViewSummaryTitle);
//			this.textViewSummaryX = (TextView) view
//					.findViewById(R.id.textViewSummaryX);
//			this.textViewSummaryY = (TextView) view
//					.findViewById(R.id.textViewSummaryY);
//			this.imageViewSummary = (ImageView) view
//					.findViewById(R.id.imageViewSummary);
//			this.textViewSummaryPath = (TextView) view
//					.findViewById(R.id.textViewSummaryPath);
		}

		public void SetViews(Summary summary) {
			this.textViewSummaryTitle.setText(summary.getTitle());
			this.textViewSummaryX.setText("x=" + summary.getX());
			this.textViewSummaryY.setText("y=" + summary.getY());
			this.imageViewSummary.setImageBitmap(summary.getImage());
			this.textViewSummaryPath.setText(summary.getPath());
		}// SetViews
	}// SummaryViews

	@Override
	public View getView(int position, View convert_view, ViewGroup parent) {
		SummaryViews summary_views;
		if (convert_view == null) {
			LayoutInflater layout_inflater = LayoutInflater.from(this
					.getContext());
			convert_view = layout_inflater.inflate(R.layout.listviewrow, null);
			summary_views = new SummaryViews(convert_view);
			convert_view.setTag(summary_views);
		} else {
			summary_views = (SummaryViews) convert_view.getTag();
		}// if
		Summary summary = getItem(position);
		summary_views.SetViews(summary);
		return convert_view;
	}// getView

}// SummaryArrayAdapter
