package com.franco.pulltorefresh;

import com.franco.blog.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class PullToRefreshListView extends ListView implements OnScrollListener {

	private OnScrollListener mOnScrollListener;
	private int mCurrentScrollState;
	private int firstItem = 0;

	private HeaderView mHeaderView = null;

	private Context mContext = null;
	
	private int yDown;
	private int yMove;
	
	private int state;
	private int STATE_IDLE = 0;
	private int STATE_PREPARED = 1;
	private int STATE_REFRESHING = 2;
	private int STATE_FLING = 3;
	
	public boolean hasHead = false;

	private OnRefreshListener mOnRefreshListener;

	public interface OnRefreshListener {
		public void onRefresh();
	}
	
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
	}

	public void onRefreshComplete() {
		state = STATE_IDLE;
		setSelection(hasHead == true ? 1 : 0);
	}

	public void onRefreshComplete(CharSequence lastUpdated) {
		onRefreshComplete();
	}

	public PullToRefreshListView(Context context) {
		this(context, null);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	private void init() {
		mHeaderView = new HeaderView(mContext);
		addHeaderView(mHeaderView);
		hasHead = true;
		super.setOnScrollListener(this);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		setSelection(hasHead == true ? 1 : 0);
	}

	public boolean getHasHeadBoolean() {
		return hasHead;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			yDown = (int) event.getY();
			break;
			
		case MotionEvent.ACTION_UP:
			if (getFirstVisiblePosition() == 0) {
				if(state == STATE_PREPARED) {
					setSelection(0);
					mHeaderView.setRefreshText(R.string.loading);
					mHeaderView.startRefresh();
					state = STATE_REFRESHING;
					mOnRefreshListener.onRefresh();
				}
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			yMove = (int) event.getY();
			if(firstItem == 0) {
				if ((yMove - yDown) >= mHeaderView.getRefreshHeight()) {
					mHeaderView.setRefreshText(R.string.release_to_refresh);
					state = STATE_PREPARED;
				} else {
					mHeaderView.reset();
					state = STATE_IDLE;
				}
				
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		firstItem = firstVisibleItem;
		
		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
			if(getHeaderViewsCount() == 0) {
				addHeaderView(mHeaderView);
				hasHead = true;
			}
		} else if (mCurrentScrollState == SCROLL_STATE_FLING) {//如果是自己滚动状态
			if(firstItem == 0) {
				if(getHeaderViewsCount() > 0) {
					removeHeaderView(mHeaderView);
					hasHead = false;
				}
				state = STATE_FLING;
			}
		}
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		mCurrentScrollState = scrollState;
		if (mCurrentScrollState == SCROLL_STATE_IDLE) {//如果滚动停顿
			if(state != STATE_REFRESHING) {
				state = STATE_IDLE;
				if(firstItem == 0) {
					setSelection(hasHead == true ? 1 : 0);
				}
			}
		}

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

}
