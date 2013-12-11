package com.gmail.takashi316.easyguide.player;

import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ButtonsFragment extends Fragment {
	HorizontalScrollView horizontalScrollViewButtons;
	LinearLayout layoutButtons;
	Context context;
	Class<? extends Activity> activityClass;

	@Override
	public void onAttach(Activity activity) {
		this.context = activity.getApplicationContext();
		activityClass = activity.getClass();
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.buttons_fragment, container);
		this.horizontalScrollViewButtons = (HorizontalScrollView) v
				.findViewById(R.id.horizontalScrollViewButtons);
		this.layoutButtons = (LinearLayout) v.findViewById(R.id.layoutButtons);

		// return super.onCreateView(inflater, container, savedInstanceState);
		return v;
	}

	public void update(ContentUnit content_unit) {
		if (content_unit.getChildren().size() == 0) {
			// this.layoutButtons.setVisibility(View.GONE);
		} else if (content_unit.getChildren().size() > 0) {
			this.showButtons(content_unit);
			this.show();
		}// if content unit has children
	}

	public void showButtons(ContentUnit current_content_unit) {
		this.layoutButtons.removeAllViews();
		// final UnifiedActivity ua = this;

		/*
		 * ImageButton image_button = new ImageButton(context);
		 * image_button.setImageResource(android.R.drawable.btn_radio);
		 * image_button.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { UnifiedActivity
		 * unified_activity = (UnifiedActivity) getActivity();
		 * unified_activity.toggleWifiLayout(); } });
		 * this.layoutButtons.addView(image_button);
		 */

		for (final ContentUnit cu : current_content_unit.getChildren()) {
			Button b = new Button(context);
			b.setBackgroundResource(R.drawable.button_child);
			b.setText(cu.getName());
			b.setTextSize(30);
			// b.setBackgroundColor(Color.YELLOW);
			b.setTextColor(Color.BLACK);
			b.setMinWidth(30);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, activityClass);
					intent.putIntegerArrayListExtra("contentPath",
							cu.getContentPath());
					startActivity(intent);
					// unified_activity.setContentUnit(cu);
					// ua.onResume();
				}
			});
			this.layoutButtons.addView(b);

		}// for
		final ContentUnit parent_cu = current_content_unit.getParent();
		if (parent_cu != null) {
			Button b = new Button(context);
			b.setBackgroundResource(R.drawable.button_parent);
			b.setText("もどる");
			b.setTextSize(30);
			// b.setBackgroundColor(Color.GREEN);
			b.setTextColor(Color.BLACK);
			b.setMinWidth(40);
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, activityClass);
					intent.putExtra("contentPath", parent_cu.getContentPath());
					startActivity(intent);
					// video_fragment.stopPlayback();
					// video_fragment.hide();
					// unified_activity.setContentUnit(parent_cu);
					// ua.onResume();
				}// onClick
			});
			this.layoutButtons.addView(b);
		}// if
		this.layoutButtons.setMinimumWidth(this.horizontalScrollViewButtons
				.getWidth());
	}// showButtons

	public void show() {
		layoutButtons.setVisibility(View.VISIBLE);
	}

	public void hide() {
		layoutButtons.setVisibility(View.GONE);
	}

}// ButtonsFragment
