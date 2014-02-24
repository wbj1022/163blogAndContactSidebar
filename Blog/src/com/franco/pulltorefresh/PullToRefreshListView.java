package com.franco.pulltorefresh;

import com.franco.blog.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class PullToRefreshListView extends ListView implements OnScrollListener {

	private RelativeLayout mRefreshView;
	private RelativeLayout mHeadView;
	private TextView headViewText;
	private ImageView mRefreshViewImage;
	private LayoutInflater mInflater;
	private OnScrollListener mOnScrollListener;
	private int mCurrentScrollState;
	private int mHeadViewHeight;
	private int mRefreshViewHeight;
	private int mRefreshState;
	private boolean mBounceHack;

	private RotateAnimation mFlipAnimation;
	private RotateAnimation mReverseFlipAnimation;

	private static int REFRESHICON = R.drawable.android;
	private static int REFRESH_STATE_IDLE = 0;
	private static int PULL_TO_REFRESH = 1;
	private static int RELEASE_TO_REFRESF = 2;
	private static int REFRESHING = 3;

	private OnRefreshListener mOnRefreshListener;

	public interface OnRefreshListener {

		public void onRefresh();
	}

	public PullToRefreshListView(Context context) {
		super(context);

	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {

		mFlipAnimation = new RotateAnimation(0, 359,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(1000);
		mFlipAnimation.setRepeatCount(-1);
		mFlipAnimation.setFillAfter(true);

		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRefreshView = (RelativeLayout) mInflater.inflate(
				R.layout.pull_to_refresh, null);
		mHeadView = (RelativeLayout) mRefreshView
				.findViewById(R.id.pull_to_refresh);
		headViewText = (TextView) mRefreshView
				.findViewById(R.id.pull_to_refresh_head_view);
		mRefreshViewImage = (ImageView) mRefreshView
				.findViewById(R.id.pull_to_refresh_image);
		mRefreshViewImage.setMinimumHeight(50);
		mRefreshState = REFRESH_STATE_IDLE;

		addHeaderView(mRefreshView);

		measureView(mRefreshView);
		mHeadViewHeight = headViewText.getMeasuredHeight();
		mRefreshViewHeight = mRefreshView.getMeasuredHeight();
		super.setOnScrollListener(this);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		setSelection(1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mBounceHack = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (getFirstVisiblePosition() == 0) {
				if (mRefreshView.getBottom() >= mHeadViewHeight) {
					if (mRefreshState != REFRESHING) {
						onRefresh();
					}
				} else {
					//if(mRefreshState !=)
					resetHeader();
					setSelection(1);
				}
			}
			break;
		}

		return super.onTouchEvent(event);
	}

	private void onRefresh() {
		mRefreshState = REFRESHING;
		mRefreshViewImage.setVisibility(View.VISIBLE);
		mRefreshViewImage.clearAnimation();
		mRefreshViewImage.setAnimation(mFlipAnimation);
		mFlipAnimation.startNow();
		headViewText.setText(R.string.loading);

		RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) mHeadView
				.getLayoutParams();
		p.height = mHeadViewHeight;
		mHeadView.setLayoutParams(p);

		setSelection(0);
		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh();
		}
	}

	private void resetHeader() {
		if (mRefreshState != REFRESH_STATE_IDLE) {
			mRefreshState = REFRESH_STATE_IDLE;
			headViewText.setText(R.string.pull_to_refresh);
			mRefreshViewImage.setImageResource(REFRESHICON);
			mRefreshViewImage.clearAnimation();
			mRefreshViewImage.setVisibility(View.GONE);

			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) mHeadView
					.getLayoutParams();
			p.height = mRefreshViewHeight;
			mHeadView.setLayoutParams(p);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& mRefreshState != REFRESHING) {
			if (firstVisibleItem == 0) {
				if (mRefreshView.getBottom() >= mHeadViewHeight) {
					mRefreshState = RELEASE_TO_REFRESF;
					headViewText.setText(R.string.release_to_refresh);
				} else {
					mRefreshState = PULL_TO_REFRESH;
					headViewText.setText(R.string.pull_to_refresh);
				}
			} else {
				mRefreshViewImage.setVisibility(View.GONE); // 隐藏刷新图片
				resetHeader(); // 初始化，头部
			}
		} else if (mCurrentScrollState == SCROLL_STATE_FLING // 如果是自己滚动状态
				&& firstVisibleItem == 0 && mRefreshState != REFRESHING) {
			setSelection(1);
			mBounceHack = true; // 状态为回弹
		}
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		mCurrentScrollState = scrollState;
		if (mCurrentScrollState == SCROLL_STATE_IDLE) { // 如果滚动停顿
			mBounceHack = false;
		}

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	// 测量视图的高度
	private void measureView(View child) {
		// 获取头部视图属性
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;

		if (lpHeight > 0) { // 如果视图的高度大于0
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);

	}

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
	}

	public void onRefreshComplete() {
		resetHeader();
		setSelection(1);
	}

	public void onRefreshComplete(CharSequence lastUpdated) {
		onRefreshComplete();
	}

}
