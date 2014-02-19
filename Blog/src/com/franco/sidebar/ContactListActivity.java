package com.franco.sidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.franco.blog.R;
import com.franco.hanzi2pinyin.ChineseToPinyinHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListActivity extends Activity {

	private ListView contactListView = null;
	private ListAdapter adapter = null;
	private AlphabetIndexer mAlphabetIndexer;
	private int columnDisplayName;
	private Cursor cursor;
	private TextView toastView;
	List<ContactName> contactNameList = new ArrayList<ContactName>();
	private static Set<Character> charSet = new HashSet<Character>();
	private String[] mSectionstr;
	private int[] mSection;
	private int[] mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlistview);
		init();
	}

	private void init() {
		
		mAlphabetIndexer = (AlphabetIndexer) findViewById(R.id.side_bar);
		contactListView = (ListView) findViewById(R.id.contact_list);
		toastView = (TextView) findViewById(R.id.toast_string);

		cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, // must  contain  "_id" if not null
				null, null, "sort_key_alt");
		columnDisplayName = cursor.getColumnIndex("display_name");
		String[] chars = getResources().getStringArray(R.array.vailable_charset);
		for (String ch : chars) {
			charSet.add(ch.charAt(0));
        }
		
		sortContactName();

		adapter = new MyAdapter(this, contactNameList);
		contactListView.setAdapter(adapter);
		
		setSectionsAndPositions();
		mAlphabetIndexer.setToastView(toastView);
		mAlphabetIndexer.setContactListView(contactListView);
		mAlphabetIndexer.setSections(mSectionstr);
		mAlphabetIndexer.setSection(mSection);
		mAlphabetIndexer.setPosition(mPosition);
		if(cursor.getCount() > 6) {
			mAlphabetIndexer.setVisibility(View.VISIBLE);
		} else {
			mAlphabetIndexer.setVisibility(View.GONE);
		}
		
	}

	@SuppressLint("DefaultLocale")
	private void sortContactName() {
		
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ContactName contactName = new ContactName();
			String displayName = cursor.getString(columnDisplayName);
			char ch = hanzi2pinyin(displayName).toUpperCase().charAt(0);
			if(ch >= 'A' && ch <= 'Z') {
				contactName.firstChar = ch;
			} else {
				contactName.firstChar = '#';
			}
			contactName.displayName = displayName;
			contactNameList.add(contactName);
			Log.v("franco", "---->" + contactName.displayName + "---->" + contactName.firstChar);
		}
		
		Collections.sort(contactNameList, new CompareObject());
//		for(ContactName contactame : contactNameList) {
//			Log.v("franco", "***" + contactame.firstChar + "***" + contactame.displayName);
//		}

	}
	
	private void setSectionsAndPositions() {
		if(contactNameList == null) {
			return;
		}
		int len = contactNameList.size();
		mSectionstr = new String[len];
		mPosition = new int[len];
		mSection = new int[len];
		
		int section = 0, position = 0;
		char preChar = ' ';
		for(ContactName contactName : contactNameList) {
			if(contactName.firstChar != preChar) {
				mSectionstr[section] = contactName.firstChar + "";
				mSection[section] = position;
				mPosition[position] = section++;
			} else {
				mPosition[position] = section;
			}
			position++;
		}
	}
	
	public static String hanzi2pinyin(String hanzi) {
        if (!TextUtils.isEmpty(hanzi)) {
            ChineseToPinyinHelper.setLegalCharactSet(charSet);
            String[] retvalues = ChineseToPinyinHelper
                    .translateMulti(hanzi);
            return retvalues[0];
        }
        return "";
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Log.v("franco", getPinYin("吴斌杰"));
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

}
