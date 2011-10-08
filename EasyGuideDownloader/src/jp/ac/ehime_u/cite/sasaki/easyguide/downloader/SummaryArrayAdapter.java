package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import android.content.Context;
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
	private int resourceId;

	/**
	 * @param context
	 * @param resource_id
	 */
	public SummaryArrayAdapter(Context context, int resource_id) {
		super(context, resource_id);
		this.resourceId = resource_id;
	}// a constructor

	private class SummaryViews {
		private TextView textViewSummaryTitle;

		private TextView textViewSummaryX;
		private TextView textViewSummaryY;
		private ImageView imageViewSummary;

		public SummaryViews(View view) {
			this.textViewSummaryTitle = (TextView) view
					.findViewById(R.id.textViewSummaryTitle);
			this.textViewSummaryX = (TextView) view
					.findViewById(R.id.textViewSummaryX);
			this.textViewSummaryY = (TextView) view
					.findViewById(R.id.textViewSummaryY);
			this.imageViewSummary = (ImageView) view
					.findViewById(R.id.imageViewSummary);
		}

		public void SetViews(Summary summary) {
			textViewSummaryTitle.setText(summary.getTitle());
			textViewSummaryX.setText(summary.getX());
			textViewSummaryY.setText(summary.getY());
			imageViewSummary.setImageBitmap(summary.getImage());
		}// SetViews
	}// SummaryViews

	@Override
	public View getView(int position, View convert_view, ViewGroup parent) {
		SummaryViews summary_views;
		if (convert_view == null) {
			LayoutInflater layout_inflater = LayoutInflater.from(this
					.getContext());
			convert_view = layout_inflater.inflate(R.layout.summary, null);
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
