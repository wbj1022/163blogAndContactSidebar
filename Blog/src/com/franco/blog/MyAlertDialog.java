package com.franco.blog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

public class MyAlertDialog extends Dialog {

	public MyAlertDialog(Context context, int theme) {
		super(context, theme);
		//setTitle(null);
		//setIcon(0);
	}

	public static class Builder {
		private Context context;
		private View view;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setView(View view) {
			this.view = view;
			return this;
		}

		public MyAlertDialog create() {

			final MyAlertDialog dialog = new MyAlertDialog(context,
					R.style.MyAlertDialogStyle);
			if (view != null) {
				dialog.setContentView(view);
			} else {

			}
			return dialog;
		}
	}

}
