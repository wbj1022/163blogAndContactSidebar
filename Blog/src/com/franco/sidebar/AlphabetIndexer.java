package com.franco.sidebar;

import com.franco.pulltorefresh.PullToRefreshListView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class AlphabetIndexer extends View implements SectionIndexer, OnTouchListener{

	private final String[] ABC_STRING = { "#", "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };
	
	private TextView toastView;
	private PullToRefreshListView contactListView;
	private final int length = ABC_STRING.length;
	private Paint paint;
	private FontMetricsInt mFmi;
	private int mLabelHeight;
	private float perStringheight;
	private String[] mSectionstr;
	private int[] mSection;
	private int[] mPosition;
	
	public AlphabetIndexer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		paint = new Paint();
		paint.setColor(Color.parseColor("#6E511E"));
		paint.setTextSize(30);
		paint.setAntiAlias(true);
		mFmi = paint.getFontMetricsInt();
    	mLabelHeight = mFmi.bottom - mFmi.top;
    	super.setOnTouchListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		perStringheight = canvas.getHeight() * 1.0f / length;
		float width = canvas.getWidth();
		
		for(int i=0; i < length; i++ ) {
			float x = (width - paint.measureText(ABC_STRING[i])) / 2.0f;
			float y = (i + 0.5f) * perStringheight + mLabelHeight / 4.0f;
			canvas.drawText(ABC_STRING[i], x, y, paint);
		}
	}
	
	@Override
	public int getPositionForSection(int section) {
		if(section < 0 || mPosition == null) {
			return -2;
		}
		return mPosition[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		if(position < 0 || mSection == null) {
			return -1;
		}
		return mSection[position];
	}
	
	private int getSectionForIndex(int index) {
		if(mSectionstr == null) {
			return -1;
		}
		String str = ABC_STRING[index];
		for(int i = 0; i < mSectionstr.length; i++) {
			if(str.equals(mSectionstr[i])) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int index = getCurrentSection(event.getY());
		int section = getSectionForIndex(index);
		int position = getPositionForSection(section);
		boolean hasHead = contactListView.getHasHeadBoolean();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setBackgroundColor(Color.parseColor("#604F4F4F"));
			toastView.setVisibility(View.VISIBLE);
			toastView.setText(ABC_STRING[index]);
			if(hasHead) {
				position++;
			}
			contactListView.setSelection(position);
			break;
		case MotionEvent.ACTION_MOVE:
			setBackgroundColor(Color.parseColor("#604F4F4F"));
			toastView.setText(ABC_STRING[index]);
			if(hasHead) {
				position++;
			}
			contactListView.setSelection(position);
			break;
		default:
			toastView.setVisibility(View.GONE);
			setBackgroundColor(Color.parseColor("#00000000"));
		}
		return true;
	}
	
	public void setToastView(TextView toastView) {
		this.toastView = toastView;
	}
	
	public void setSections(String[] mSectionstr) {
		this.mSectionstr =  mSectionstr;
	}
	
	public void setSection(int[] mSection) {
		this.mSection =  mSection;
	}
	
	public void setPosition(int[] mPosition) {
		this.mPosition = mPosition;
	}
	
	public void setContactListView(PullToRefreshListView contactListView) {
		this.contactListView = contactListView;
	}
	
	private int getCurrentSection(float h) {
		int index = (int) (h / perStringheight);
		if(index >= length) {
			index = length - 1;
		}
		if(index < 0) {
			index = 0;
		}
		return index;
	}
	
	

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

}
