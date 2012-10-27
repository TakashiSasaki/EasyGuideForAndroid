package jp.ac.ehime_u.cite.sasaki.easyguide.ui;

import java.util.ArrayList;

import jp.ac.ehime_u.cite.sasaki.easyguide.R;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.BitmapLoader;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.TextLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class TocAdapter extends BaseAdapter {

	public class TocItem {
		Bitmap thumbnail;
		String domain;
		String description;

		public TocItem(Bitmap thumbnail, String domain, String description) {
			this.thumbnail = thumbnail;
			this.domain = domain;
			this.description = description;
		}

	}// TocItem

	// Organizations organizations = Organizations.getInstance();

	ArrayList<TocItem> tocItems = new ArrayList<TocItem>();

	LayoutInflater layoutInflater;

	/**
	 * the constructor of TocAdapter
	 * 
	 * @param context
	 * @throws Exception
	 */
	public TocAdapter(Context context, ArrayList<ContentUnit> content_units)
			throws Exception {
		super();

		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (ContentUnit content_unit : content_units) {
			BitmapLoader bitmap_loader = new BitmapLoader(context);
			bitmap_loader.loadBitmapFromFile(content_unit.getImageFile());
			Bitmap thumbnail = bitmap_loader.getBitmap();
			TextLoader text_loader = new TextLoader();
			text_loader.loadTextFromFile(content_unit.getTextFile());
			String description = text_loader.getText();
			String domain = content_unit.getName();
			TocItem toc_item = new TocItem(thumbnail, domain, description);
			this.tocItems.add(toc_item);
		}
	}// the constructor

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			// View can be recycled. If the view is not generated yet, it should
			// be inflated here.
			view = this.layoutInflater.inflate(R.layout.tocitem, null);
		}
		try {
			TocItem toc_item = this.tocItems.get(position);
			ImageView imageViewTocItem = (ImageView) view
					.findViewById(R.id.imageViewTocItem);
			imageViewTocItem.setImageBitmap(toc_item.thumbnail);
		} catch (IndexOutOfBoundsException e) {
			Log.e(this.getClass().getSimpleName(),
					"no item in tocArrayList at position" + position);
			return null;
		}// try
		return view;
	}// getView

	@Override
	public int getCount() {
		return this.tocItems.size();
	}

	@Override
	public Object getItem(int position) {
		return this.tocItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}// TocAdapter
