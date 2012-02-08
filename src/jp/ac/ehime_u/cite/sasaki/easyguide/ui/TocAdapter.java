package jp.ac.ehime_u.cite.sasaki.easyguide.ui;

import jp.ac.ehime_u.cite.sasaki.easyguide.model.Facility;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organization;
import jp.ac.ehime_u.cite.sasaki.easyguide.model.Organizations;
import android.content.Context;
import android.widget.ArrayAdapter;

public class TocAdapter extends ArrayAdapter<TocArrayItem> {

	Organizations organizations = Organizations.GetTheOrganizations();

	public TocAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource);
		for (Organization organization : this.organizations){
			organization.e
			for(Facility facility : organization){
			}
		}
	}// TocAdapter
}
