package jp.ac.ehime_u.cite.sasaki.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
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
import android.widget.LinearLayout;

public class ButtonsFragment extends Fragment {
	HorizontalScrollView horizontalScrollViewButtons;
	LinearLayout layoutButtons;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater.inflate(R.id.buttonsFragment, container);
		this.horizontalScrollViewButtons = (HorizontalScrollView) getView()
				.findViewById(R.id.horizontalScrollViewButtons);
		this.layoutButtons = (LinearLayout) getView().findViewById(
				R.id.layoutButtons);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void showButtons(final UnifiedActivity unified_activity,
			ContentUnit current_content_unit) {
		this.layoutButtons.removeAllViews();
		// final UnifiedActivity ua = this;

		for (final ContentUnit cu : current_content_unit.getChildren()) {
			Button b = new Button(unified_activity);
			b.setText(cu.getName());
			b.setTextSize(30);
			// b.setBackgroundColor(Color.YELLOW);
			b.setTextColor(Color.BLACK);
			b.setMinWidth(30);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					unified_activity.setContentUnit(cu);
					// ua.onResume();
				}
			});
			this.layoutButtons.addView(b);

		}// for
		final ContentUnit parent_cu = current_content_unit.getParent();
		if (parent_cu != null) {
			Button b = new Button(unified_activity);
			b.setText("もどる");
			b.setTextSize(30);
			b.setBackgroundColor(Color.GREEN);
			// b.setTextColor(Color.WHITE);
			b.setGravity(Gravity.RIGHT);
			b.setMinWidth(30);
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					unified_activity.videoView.stopPlayback();
					unified_activity.layoutVideo.setVisibility(View.GONE);
					unified_activity.setContentUnit(parent_cu);
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
