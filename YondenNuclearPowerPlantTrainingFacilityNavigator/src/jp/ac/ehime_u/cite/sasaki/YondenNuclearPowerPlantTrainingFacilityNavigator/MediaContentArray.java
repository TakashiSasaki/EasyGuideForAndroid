package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.util.ArrayList;

public class MediaContentArray extends ArrayList<MediaContent> {
	//SDカードからコンテンツを読み込む
	MediaContentArray(String facility_name, String media_root) {
		
	}

	//デバッグ時に使う
	MediaContentArray(int n_media_content) {
		super();
		for (int i = 0; i < n_media_content; ++i) {
			this.add(new MediaContent());
		}
	}
}
