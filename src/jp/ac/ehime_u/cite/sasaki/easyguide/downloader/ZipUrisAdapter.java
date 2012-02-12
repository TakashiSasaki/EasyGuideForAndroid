package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import java.util.ArrayList;
import java.util.Date;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ZipUrisAdapter extends ArrayAdapter<Source> {
	private LayoutInflater layoutInflater;

	public ZipUrisAdapter(Context context, int layout_id,
			ArrayList<Source> array_list) {
		super(context, layout_id, array_list);
		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}// a constructor

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = this.layoutInflater.inflate(R.layout.listviewrow, null);
			//view.setBackgroundColor(Color.DKGRAY);
		}// if
		Source zip_url = this.getItem(position);
		TextView text_view_domain = (TextView) view
				.findViewById(R.id.textViewDomain);
		text_view_domain.setText(zip_url.GetDomainByString());

		TextView text_view_downloaded_file = (TextView) view
				.findViewById(R.id.textViewDownloadedFile);
		text_view_downloaded_file
				.setText(zip_url.getDownloadedFile().getPath());

		TextView text_view_downloaded_time = (TextView) view
				.findViewById(R.id.textViewDownloadedTime);
		text_view_downloaded_time.setText(zip_url.getDownloadedDate()
				.toString());

		TextView text_view_url = (TextView) view.findViewById(R.id.textViewUrl);
		text_view_url.setText(zip_url.getUri().toString());

		TextView text_view_last_modified = (TextView) view
				.findViewById(R.id.textViewLastModified);
		Date last_modified = zip_url.getLastModifiedNonBlocking();
		if (last_modified != null) {
			text_view_last_modified.setText(last_modified.toString());
		}// if
		return view;
	}// getView
}// ZipUrisAdapter
