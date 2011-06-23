package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FloorListItem {
	private Bitmap image;
	private String title;
	private String description;

	public FloorListItem(Context context) {
		super();
		image = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.sample_floor_icon);
		title = "Floor title";
		description = "Floor description";
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
