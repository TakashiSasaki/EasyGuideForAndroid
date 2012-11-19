package com.gmail.takashi316.easyguide.player;

import com.gmail.takashi316.easyguide.content.ContentUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoFragment extends Fragment {
	VideoView videoView;
	LinearLayout layoutVideo;
	Context context;
	// Class<? extends Activity> activityClass;
	ContentUnit contentUnit;

	@Override
	public void onAttach(Activity activity) {
		this.context = activity.getApplicationContext();
		// this.activityClass = activity.getClass();
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.video_fragment, container);
		this.videoView = (VideoView) v.findViewById(R.id.videoView1);
		this.layoutVideo = (LinearLayout) v.findViewById(R.id.layoutVideo);
		return v;
	}// onCreateView

	public void stopPlayback() {
		videoView.stopPlayback();
	}// stopPlayback

	public void hide() {
		layoutVideo.setVisibility(View.GONE);
	} // hide

	public void update(ContentUnit content_unit) {
		if (this.contentUnit != null) {
			if (this.contentUnit.getParent() == null
					&& content_unit.getParent() != null) {
				this.stopPlayback();
			} else if (this.contentUnit.getAncestors() != null
					&& content_unit.getParent() == null) {
				this.stopPlayback();
			} else if (this.contentUnit.getParent() == null
					&& content_unit.getParent() == null) {
				// do nothing
			} else if (this.contentUnit.getParent().equals(
					content_unit.getParent())) {
				// do nothing
			} else {
				this.stopPlayback();
			}
		}
		this.contentUnit = content_unit;
		if (this.contentUnit.hasMovie()) {
			// this.layoutVideo.bringToFront();
			this.layoutVideo.setBackgroundColor(Color.WHITE);
			if (!this.videoView.isPlaying()) {
				this.videoView.setVideoPath(content_unit.getMovieFile()
						.getAbsolutePath());
				this.videoView.setMediaController(new MediaController(context));
				this.videoView.start();
			}
			this.layoutVideo.setVisibility(View.VISIBLE);
		} else {
			this.layoutVideo.setVisibility(View.GONE);
		}// if contentunit has a movie

	}
}// VideoFragment
