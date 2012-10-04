package jp.ac.ehime_u.cite.sasaki.easyguide.download;

import android.content.Context;
import android.util.Pair;
import android.widget.ArrayAdapter;

public class AssetsArrayAdapter extends ArrayAdapter<String> {

	
	
	public AssetsArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		Assets assets = new Assets(context);
		for (Pair<String, String> pair : assets) {
			this.add(pair.first);
		}
	}// a constructor

}// AssetsArrayAdapter
