package com.gmail.takashi316.easyguide.player;

import java.util.ArrayList;

import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class BreadcrumbFragment extends Fragment {
	private HorizontalScrollView horizontalScrollViewBreadcrumb;
	private LinearLayout layoutBreadcrumb;
	private Context context;
	private Class<? extends Activity> activityClass;

	@Override
	public void onAttach(Activity activity) {
		this.context = activity.getApplicationContext();
		this.activityClass = activity.getClass();
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater.inflate(R.layout.breadcrumb_fragment, container);
		this.horizontalScrollViewBreadcrumb = (HorizontalScrollView) getView()
				.findViewById(R.id.scrollViewBreadcrumb);
		this.layoutBreadcrumb = (LinearLayout) getView().findViewById(
				R.id.layoutBreadcrumb);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void hide() {
		this.horizontalScrollViewBreadcrumb.setVisibility(View.GONE);
	}

	public void show() {
		this.horizontalScrollViewBreadcrumb.setVisibility(View.VISIBLE);
	}

	public void update(ContentUnit content_unit) {
		if (content_unit.getAncestors().size() == 0) {
			this.hide();
		} else if (content_unit.getAncestors().size() > 0) {
			this.showParents(content_unit.getAncestors(), content_unit);
			this.show();
		}// if content unit has parents

	}

	public void showParents(ArrayList<ContentUnit> ancestors,
			final ContentUnit current_content_unit) {

		this.layoutBreadcrumb.removeAllViews();
		// final UnifiedActivity unified_activity = this;

		for (int i = ancestors.size() - 1; i >= 0; --i) {
			Button b = new Button(context);
			b.setText(ancestors.get(i).getName());
			b.setTextSize(30);
			b.setTextColor(Color.BLACK);
			b.setMinWidth(30);
			final ContentUnit content_unit = ancestors.get(i);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, activityClass);
					intent.putExtra("contentPath",
							content_unit.getContentPath());
					// unified_activity.setContentUnit(content_unit);
					// video_fragment.stopPlayback();
					// video_fragment.hide();
					// unified_activity.onResume();
					startActivity(intent);
				}// onClick
			});// onClickListener
			this.layoutBreadcrumb.addView(b);
		}// for

		Button b = new Button(context);
		b.setText(current_content_unit.getName());
		b.setTextSize(30);
		b.setTextColor(Color.BLACK);
		b.setMinWidth(30);
		b.setBackgroundColor(Color.CYAN);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, activityClass);
				intent.putExtra("contentPath",
						current_content_unit.getContentPath());
				// unified_activity.setContentUnit(current_content_unit);
				// video_fragment.stopPlayback();
				// video_fragment.hide();
				// unified_activity.onResume();
				startActivity(intent);
			}// onClick
		});// onClickListener
		this.layoutBreadcrumb.addView(b);

		this.horizontalScrollViewBreadcrumb.smoothScrollTo(
				this.layoutBreadcrumb.getWidth() + 1000, 0);
		this.horizontalScrollViewBreadcrumb.smoothScrollBy(2000, 0);
	}// showParents
}
