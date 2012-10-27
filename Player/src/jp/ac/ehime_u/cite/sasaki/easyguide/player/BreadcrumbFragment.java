package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import java.util.ArrayList;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
import android.content.Context;
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

	public void showParents(ArrayList<ContentUnit> ancestors,
			final ContentUnit current_content_unit, Context context,
			final UnifiedActivity unified_activity,
			final VideoFragment video_fragment) {

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
					unified_activity.setContentUnit(content_unit);
					video_fragment.stopPlayback();
					video_fragment.hide();
					unified_activity.onResume();
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
				unified_activity.setContentUnit(current_content_unit);
				video_fragment.stopPlayback();
				video_fragment.hide();
				unified_activity.onResume();
			}// onClick
		});// onClickListener
		this.layoutBreadcrumb.addView(b);

		this.horizontalScrollViewBreadcrumb.smoothScrollTo(
				this.layoutBreadcrumb.getWidth() + 1000, 0);
		this.horizontalScrollViewBreadcrumb.smoothScrollBy(2000, 0);
	}// showParents
}
