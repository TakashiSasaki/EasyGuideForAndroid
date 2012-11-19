package com.gmail.takashi316.easyguide.player;

import com.gmail.takashi316.easyguide.content.ContentUnit;

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
	private ViewGroup container;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.container = container;
		View v = inflater.inflate(R.layout.html_fragment, container);
		this.layoutHtml = (LinearLayout) v
				.findViewById(R.id.layoutHtml);
		this.webView = (WebView) v.findViewById(R.id.webView);
		//return super.onCreateView(inflater, container, savedInstanceState);
		return v;
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

