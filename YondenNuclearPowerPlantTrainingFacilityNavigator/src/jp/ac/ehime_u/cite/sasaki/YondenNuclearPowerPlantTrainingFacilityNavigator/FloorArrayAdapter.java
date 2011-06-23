package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FloorArrayAdapter extends ArrayAdapter<FloorContent> {
	private List<FloorContent> floorListItems;
	private LayoutInflater layoutInflater;

	// コンストラクタ
	// 特に深い意味は無い気がする・・
	public FloorArrayAdapter(Context context, int resourceId,
			List<FloorContent> items) {
		super(context, resourceId, items);
		this.floorListItems = items;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// 1行分の処理
	// とにかく何をするにしてもこのgetViewというメソッドが1行操作するごとに呼ばれるので
	// 追加処理とかもここに押し込む
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// 操作対象のViewを見る
		// 完全に新規に作る場合はnullがわたってくる
		// それにしてもViewを引数にとっているのにgetViewとは・・・なんだか変な話だ
		//View view = convertView;
		if (view == null) {
			// 1行分layoutからViewの塊を生成
			view = layoutInflater.inflate(R.layout.floor_list_item, null);
		}
		// itemsからデータ
		// vから画面にくっついているViewを取り出して値をマッピングする
		FloorContent floor_list_item = (FloorContent) floorListItems
				.get(position);
		final FloorContent f_floor_list_item = floor_list_item;

		TextView text_view_floor_title = (TextView) view
				.findViewById(R.id.textViewFloorTitle);
		text_view_floor_title.setText(floor_list_item.getTitle());

		TextView text_view_floor_description = (TextView) view
				.findViewById(R.id.textViewFloorDescription);
		text_view_floor_description.setText(floor_list_item.getDescription());

		ImageView image_view_floor_icon = (ImageView) view
				.findViewById(R.id.imageViewFloorIcon);
		image_view_floor_icon.setImageBitmap(floor_list_item.getImage());

		Button button_go_to_floor = (Button) view
				.findViewById(R.id.buttonGoToFloor);

		button_go_to_floor.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// final宣言したローカル変数でlistenr生成時の値でパラメータを固定化する
				// クロージャモドキ実装
				// doHoge(f_list_item);
			}
		});
		return view;
	}
}