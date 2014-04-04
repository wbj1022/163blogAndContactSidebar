package com.franco.blog;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class AppListActivity extends Activity {
	
	private static final int[] picID = { R.drawable.ic_app_1,
		R.drawable.ic_app_2, R.drawable.ic_app_3, R.drawable.ic_app_4,
		R.drawable.ic_app_5, R.drawable.ic_app_6, R.drawable.ic_app_7 };
	
	private static final int[] appName = { R.string.app_name_1,
		R.string.app_name_2, R.string.app_name_3, R.string.app_name_4,
		R.string.app_name_5, R.string.app_name_6, R.string.app_name_7 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.app_gridview);
		GridView gridview = (GridView) findViewById(R.id.gridview);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 7; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", picID[i]);
			map.put("text", getString(appName[i]));
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem,
				R.layout.app_gridview_item, new String[] { "image", "text" },
				new int[] { R.id.item_image, R.id.item_text });
		gridview.setAdapter(saImageItems);
	}

}