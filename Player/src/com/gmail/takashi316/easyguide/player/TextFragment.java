package com.gmail.takashi316.easyguide.player;

import com.gmail.takashi316.easyguide.content.ContentUnit;
import com.gmail.takashi316.easyguide.content.TextLoader;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextFragment extends Fragment {
	private TextView textViewContent;
	private LinearLayout layoutText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater.inflate(R.layout.text_fragment, container);
		this.layoutText = (LinearLayout) getView()
				.findViewById(R.id.layoutText);
		this.textViewContent = (TextView) getView().findViewById(
				R.id.textViewContent);

		return super.onCreateView(inflater, container, savedInstanceState);
	}// onCreateView

	public void update(ContentUnit content_unit) {
		if (content_unit.hasText()) {
			try {
				TextLoader text_loader = new TextLoader();
				text_loader.loadTextFromFile(content_unit.getTextFile());
				this.textViewContent.setText(text_loader.getText());
				this.textViewContent.setTextSize(35);
				this.textViewContent.setBackgroundColor(Color.WHITE);
				this.textViewContent.setTextColor(Color.BLACK);
				this.layoutText.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				this.layoutText.setVisibility(View.GONE);
			}// try
		} else {
			this.layoutText.setVisibility(View.GONE);
		}// if contentUnit has a text
	}// update
}// TextFragment

