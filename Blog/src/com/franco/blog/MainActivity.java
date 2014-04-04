package com.franco.blog;

import com.franco.util.ScreensSupport;

import android.database.Cursor;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static SharedPreferences sp;
	private final static String TAG = "franco";
	private final static int NEW_DIARY = 0;
	private final static int DIARY_LIST = 1;
	private final static int DRAFTS = 2;
	private final static int APPLICATIONS = 3;

	private Button newDiary;
	private Button diaryList;
	private Button draft;
	private Button applications;
	private EditText id;
	private EditText usrnameET;
	private EditText passwordET;
	private EditText titleET;
	private EditText contentET;
	private String blogId;
	private String blogUsrname;
	private String blogPassword;
	private String diaryTitle;
	private String diaryContent;
	private MyAlertDialog mAlertDialog;
	public DBHelper myDBHelper;
	private Cursor cursor;
	public MyAlertDialogAdapter draftsAdapter;
	public TextView draftsListTitle;
	public Button clearAll;
	private static MainActivity mInstance;
	String result = "";

	Handler mHandler = new Handler();

	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			updateUI();
			showDialog(result);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "getDensity = " + ScreensSupport.getDensity(this));
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		Log.v(TAG, "DENSITY_DEFAULT = " + DisplayMetrics.DENSITY_DEFAULT);
		mInstance = this;
		findView();
		setDefaultValues();
		setListener();
	}

	public static MainActivity getInstance() {
		return mInstance;
	}

	private void findView() {
		id = (EditText) findViewById(R.id.id);
		usrnameET = (EditText) findViewById(R.id.usrname);
		passwordET = (EditText) findViewById(R.id.password);
		newDiary = (Button) findViewById(R.id.new_diary);
		diaryList = (Button) findViewById(R.id.diary_list);
		draft = (Button) findViewById(R.id.draft_list);
		applications = (Button) findViewById(R.id.applications);
	}

	private void setDefaultValues() {
		sp = getSharedPreferences("blog", 0);
		blogId = sp.getString("id", "");
		blogUsrname = sp.getString("usrname", "");
		blogPassword = sp.getString("password", "");
		id.setText(blogId);
		usrnameET.setText(blogUsrname);
		passwordET.setText(blogPassword);
		if (myDBHelper == null) {
			myDBHelper = new DBHelper(MainActivity.this, DBHelper.TB_NAME,
					null, 1);
		}
		cursor = myDBHelper.getWritableDatabase().query(DBHelper.TB_NAME, null,
				null, null, null, null, DBHelper.ID + " ASC");
		int num = 0;
		if (cursor != null) {
			num = cursor.getCount();
		}
		draft.setText(getString(R.string.drafts) + "(" + num + ")");
	}

	private void setListener() {
		newDiary.setOnClickListener(buttonListener);
		newDiary.setId(NEW_DIARY);
		diaryList.setOnClickListener(buttonListener);
		diaryList.setId(DIARY_LIST);
		draft.setOnClickListener(buttonListener);
		draft.setId(DRAFTS);
		applications.setOnClickListener(buttonListener);
		applications.setId(APPLICATIONS);
	}

	public OnClickListener buttonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {

			case NEW_DIARY:
				Log.v(TAG, "NEW_DIARY");
				showDiaryDialog(-1, "", "");
				break;

			case DIARY_LIST:
				Log.v(TAG, "DIARY_LIST");
				/*
				 * new Thread(new Runnable() { public void run() { String outS =
				 * MetaWeblogMethod.getPostString(
				 * MetaWeblogMethod.methodGetCategories, blogId, blogUsrname,
				 * blogPassword); String re = MetaWeblogMethod.sendPost(outS);
				 * Log.v("franco", "re = " + re); } }).start();
				 */
				startActivity(new Intent(MainActivity.this, com.franco.sidebar.ContactListActivity.class));
				break;

			case DRAFTS:
				Log.v(TAG, "DRAFTS");
				LayoutInflater inflate3 = LayoutInflater
						.from(MainActivity.this);
				View draftView = inflate3.inflate(R.layout.draft_list, null);
				MyAlertDialog.Builder builder3 = new MyAlertDialog.Builder(
						MainActivity.this).setView(draftView);
				mAlertDialog = builder3.create();
				mAlertDialog.show();

				ListView draftList = (ListView) draftView
						.findViewById(android.R.id.list);
				draftsListTitle = (TextView) draftView.findViewById(R.id.title);
				clearAll = (Button) draftView.findViewById(R.id.clear_all);
				if (myDBHelper == null) {
					myDBHelper = new DBHelper(MainActivity.this,
							DBHelper.TB_NAME, null, 1);
				}
				cursor = myDBHelper.getWritableDatabase().query(
						DBHelper.TB_NAME, null, null, null, null, null,
						DBHelper.ID + " ASC");

				if (cursor == null) {
					return;
				}
				int num = cursor.getCount();

				draftsListTitle.setText(getString(R.string.draft_num, num));

				draftsAdapter = new MyAlertDialogAdapter(MainActivity.this,
						R.layout.list_item, cursor,
						new String[] { DBHelper.TITLE },
						new int[] { R.id.draft_item_text });

				draftList.setAdapter(draftsAdapter);

				draftList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> adapterView,
									View view, int i, long l) {

								Log.i("franco", "i = " + i);
								Log.i("franco", "l = " + l);
								String sql = "select * from Drafts where _ID = "
										+ l;
								cursor = myDBHelper.getWritableDatabase()
										.rawQuery(sql, null);
								cursor.moveToFirst();

								String title = cursor.getString(1);
								String content = cursor.getString(2);
								showDiaryDialog(2, title, content);
							}
						});

				if (num != 0) {
					clearAll.setEnabled(true);
					clearAll.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							myDBHelper.delAllDrafts();
							updateUI();
						}
					});
				}

				break;
				
			case APPLICATIONS:
				startActivity(new Intent(MainActivity.this, AppListActivity.class));
				break;

			default:
				Log.e(TAG, "unknown button id");
			}
		}
	};

	private void showDiaryDialog(final long _id, String title, String content) {

		LayoutInflater inflate1 = LayoutInflater.from(MainActivity.this);
		View view = inflate1.inflate(R.layout.new_diary, null);
		MyAlertDialog.Builder builder1 = new MyAlertDialog.Builder(
				MainActivity.this).setView(view);
		mAlertDialog = builder1.create();
		mAlertDialog.show();
		if (_id == -1 || _id == 2) {
			mAlertDialog.setCancelable(false);
		}
		titleET = (EditText) view.findViewById(R.id.diary_title);
		contentET = (EditText) view.findViewById(R.id.diary_content);
		Button publish = (Button) view.findViewById(R.id.publish);
		Button save = (Button) view.findViewById(R.id.save);

		titleET.setText(title);
		contentET.setText(content);

		publish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (contentET == null || titleET == null) {
					return;
				}
				diaryTitle = titleET.getText().toString();
				diaryContent = contentET.getText().toString();
				blogId = id.getText().toString();
				blogUsrname = usrnameET.getText().toString();
				blogPassword = passwordET.getText().toString();
				if ("".equals(blogId)) {
					showDialog("未输入ID！");
				} else if ("".equals(blogUsrname)) {
					showDialog("未输入帐号！");
				} else if ("".equals(blogPassword)) {
					showDialog("未输入密码！");
				} else if ("".equals(diaryTitle)) {
					showDialog("未输入日志标题！");
				} else if ("".equals(diaryContent)) {
					showDialog("未输入日志内容！");
				} else {
					Thread t = new Thread() {
						public void run() {
							String outS = MetaWeblogMethod.getPostString(
									MetaWeblogMethod.methodNewPost, blogId,
									blogUsrname, blogPassword, diaryTitle,
									diaryContent);
							result = MetaWeblogMethod.sendPost(outS);
							Log.v("franco", "re = " + result);
							if (result.startsWith("Success!") && _id != -1) {
								myDBHelper.delDraft(_id);
								mAlertDialog.dismiss();
							} else {

							}
							mHandler.post(mRunnable);

						}
					};
					t.start();
					t = null;
					/*
					 * new Thread(new Runnable() { public void run() { String
					 * outS = MetaWeblogMethod.getPostString(
					 * MetaWeblogMethod.methodNewPost, blogId, blogUsrname,
					 * blogPassword, diaryTitle, diaryContent); String re =
					 * MetaWeblogMethod.sendPost(outS); Log.v("franco", "re = "
					 * + re); if(re.startsWith("Success!") && _id != -1) {
					 * myDBHelper.delDraft(_id); updateUI(); } //showDialog(re);
					 * } }).start();
					 */
				}
			}
		});

		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (contentET == null || titleET == null) {
					return;
				}
				mAlertDialog.dismiss();
				String blog_title = titleET.getText().toString();
				String blog_content = contentET.getText().toString();
				if (myDBHelper == null) {
					myDBHelper = new DBHelper(MainActivity.this,
							DBHelper.TB_NAME, null, 1);
				}
				if (blog_title == null || blog_content == null) {
					return;
				}
				if ("".equals(blog_title) && "".equals(blog_content)) {
					Toast.makeText(MainActivity.this,
							getString(R.string.toast_no_title_content),
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (_id == -1) {
					myDBHelper.addDraft(blog_title, blog_content);
				} else {
					myDBHelper.updateDraft(blog_title, blog_content, _id);
				}
				cursor = myDBHelper.getWritableDatabase().query(
						DBHelper.TB_NAME, null, null, null, null, null,
						DBHelper.ID + " ASC");

				if (cursor != null) {
					draft.setText(getString(R.string.drafts) + "("
							+ cursor.getCount() + ")");
					if (draftsAdapter != null) {
						draftsAdapter.changeCursor(cursor);
					}
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		blogId = id.getText().toString();
		blogUsrname = usrnameET.getText().toString();
		blogPassword = passwordET.getText().toString();
		sp.edit().putString("id", blogId).putString("usrname", blogUsrname)
				.putString("password", blogPassword).commit();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (myDBHelper != null) {
			myDBHelper.close();
		}
		if (cursor != null) {
			cursor.close();
		}
	}

	private void showDialog(String message) {
		Log.v("franco", "message = " + message);
		new AlertDialog.Builder(this).setTitle(message)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
	}

	public void updateUI() {

		cursor = myDBHelper.getWritableDatabase().query(DBHelper.TB_NAME, null,
				null, null, null, null, DBHelper.ID + " ASC");

		if (cursor == null) {
			return;
		}

		int num = cursor.getCount();
		if (draftsListTitle != null) {
			draftsListTitle.setText(getString(R.string.draft_num, num));
		}

		if (draft != null) {
			draft.setText(getString(R.string.drafts) + "(" + num + ")");
		}

		if (num == 0 && clearAll != null) {
			clearAll.setEnabled(false);
		}

		if (draftsAdapter != null) {
			draftsAdapter.changeCursor(cursor);
		}

	}
}
