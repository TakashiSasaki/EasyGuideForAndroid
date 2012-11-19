package com.gmail.takashi316.easyguide.ui;

import java.util.ArrayList;

import com.gmail.takashi316.easyguide.content.BitmapLoader;
import com.gmail.takashi316.easyguide.content.ContentUnit;
import com.gmail.takashi316.easyguide.content.TextLoader;
import com.gmail.takashi316.easyguide.lib.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TocAdapter extends BaseAdapter {

	public class TocItem {
		Bitmap thumbnail;
		String name;
		String description;
		public String absolutePath;

		public TocItem(Context context, ContentUnit content_unit) {
			BitmapLoader bitmap_loader = new BitmapLoader(context);
			try {
				bitmap_loader.loadBitmapFromFile(content_unit.getImageFile());
				bitmap_loader.resizeBitmap(128, 128);
				this.thumbnail = bitmap_loader.getBitmap();
			} catch (Exception e) {
				bitmap_loader.loadDefaultBitmap();
				bitmap_loader.resizeBitmap(128, 128);
				this.thumbnail = bitmap_loader.getBitmap();
			}
			this.name = content_unit.getName();
			try {
				TextLoader text_loader = new TextLoader();
				text_loader.loadTextFromFile(content_unit.getTextFile());
				this.description = text_loader.getText();
			} catch (Exception e) {
				this.description = "この項目の説明はありません";
			}
			this.absolutePath = content_unit.getDirectory().getAbsolutePath();
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
			TocItem toc_item = new TocItem(context, content_unit);
			this.tocItems.add(toc_item);
		}// for
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
			TextView textViewTocItemName = (TextView) view
					.findViewById(R.id.textViewTocItemName);
			textViewTocItemName.setText(toc_item.name);
			TextView textViewTocItemDescription = (TextView) view
					.findViewById(R.id.textViewTocItemDescription);
			textViewTocItemDescription.setText(toc_item.description);
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
