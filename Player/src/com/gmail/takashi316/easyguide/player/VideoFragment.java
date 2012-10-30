package com.gmail.takashi316.easyguide.player;

import jp.ac.ehime_u.cite.sasaki.easyguide.content.ContentUnit;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater.inflate(R.layout.video_fragment, container);
		this.videoView = (VideoView) getView().findViewById(R.id.videoView1);
		this.layoutVideo = (LinearLayout) getView().findViewById(
				R.id.layoutVideo);
		return super.onCreateView(inflater, container, savedInstanceState);
	}// onCreateView

	public void stopPlayback() {
		videoView.stopPlayback();
	}// stopPlayback

	public void hide() {
		layoutVideo.setVisibility(View.GONE);
	} // hide

	public void update(ContentUnit content_unit, Context context) {
		if (content_unit.hasMovie()) {
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
