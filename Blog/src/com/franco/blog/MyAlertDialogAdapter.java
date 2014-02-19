package com.franco.blog;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;

/**
 * Created by franco on 13-7-30.
 */
public class MyAlertDialogAdapter extends SimpleCursorAdapter {

	private int layout;
	private LayoutInflater mInflater;
	private MainActivity instance;

	@SuppressWarnings("deprecation")
	public MyAlertDialogAdapter(Context context, int layout, Cursor cursor,
			String[] from, int[] to) {
		super(context, layout, cursor, from, to);

		this.layout = layout;
		mInflater = LayoutInflater.from(context);
		instance = MainActivity.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(layout, null);
			holder.del = (ImageButton) convertView.findViewById(R.id.del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final long _id = getItemId(position);
		holder.del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.i("franco", "_id = " + _id);
				instance.myDBHelper.delDraft(_id);
				instance.updateUI();
			}
		});
		return super.getView(position, convertView, parent);
	}

	class ViewHolder {
		ImageButton del = null;
	}

	ViewHolder holder;

}
