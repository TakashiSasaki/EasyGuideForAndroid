package com.gmail.takashi316.easyguide.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.takashi316.easyguide.content.ContentUnit;
import com.gmail.takashi316.easyguide.content.Glossary;
import com.gmail.takashi316.easyguide.content.TextLoader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
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
		View v = inflater.inflate(R.layout.text_fragment, container);
		this.layoutText = (LinearLayout) v.findViewById(R.id.layoutText);
		this.textViewContent = (TextView) v.findViewById(R.id.textViewContent);

		// return super.onCreateView(inflater, container, savedInstanceState);
		return v;
	}// onCreateView

	public void update(ContentUnit content_unit, Glossary glossary) {
		if (!content_unit.hasText()) {
			this.layoutText.setVisibility(View.GONE);
		}
		try {
			TextLoader text_loader = new TextLoader();
			text_loader.loadTextFromFile(content_unit.getTextFile());
			this.textViewContent.setText(text_loader.getText(),
					TextView.BufferType.SPANNABLE);
			this.textViewContent.setTextSize(35);
			this.textViewContent.setBackgroundColor(Color.WHITE);
			this.textViewContent.setTextColor(Color.BLACK);
			this.updateClickableSpan(glossary);
			this.layoutText.setVisibility(View.VISIBLE);
		} catch (IndexOutOfBoundsException e) {
			this.layoutText.setVisibility(View.GONE);
		} catch (IOException e) {
			this.layoutText.setVisibility(View.GONE);
		}
		// try
	}// update

	private void updateClickableSpan(final Glossary glossary) {
		Spannable spannable = (Spannable) this.textViewContent.getText();
		for (final String string : glossary.keySet()) {
			Log.v(this.getClass().getSimpleName(), string);
			Pattern pattern = Pattern.compile(string);
			Matcher matcher = pattern.matcher(spannable);
			while (matcher.find()) {
				Log.v(this.getClass().getSimpleName(), "" + matcher.start(0));
				spannable.setSpan(new ClickableSpan() {

					@Override
					public void onClick(View widget) {
						Intent intent = new Intent(getActivity(),
								UnifiedActivity.class);
						intent.putExtra("contentPath", glossary.get(string)
								.getContentPath());
						startActivity(intent);
					}
				}, matcher.start(0), matcher.end(0),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}// while
		}// for
		this.textViewContent
				.setMovementMethod(LinkMovementMethod.getInstance());
	}// updateClickableSpan
}// TextFragment
