package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class HtmlFragment extends Fragment {
	private LinearLayout layoutHtml;
	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater.inflate(R.layout.html_fragment, container);
		this.layoutHtml = (LinearLayout) getView()
				.findViewById(R.id.layoutHtml);
		this.webView = (WebView) getView().findViewById(R.id.webView);
		return super.onCreateView(inflater, container, savedInstanceState);
	}// onCreateView

	public void update(ContentUnit content_unit) {
		if (content_unit.hasHtml()) {
			this.webView.loadUrl("file://" + content_unit.getDirectory()
					+ "/index.html");
			this.layoutHtml.setVisibility(View.VISIBLE);
		} else {
			this.layoutHtml.setVisibility(View.GONE);
		}// if contentUnit.has HTML
	}// update

}// HtmlFragment

