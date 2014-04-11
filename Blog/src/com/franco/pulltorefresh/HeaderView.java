package com.franco.pulltorefresh;

import com.franco.blog.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HeaderView extends RelativeLayout {

	private TextView mTextView = null;

	private ImageView mImageView = null;

	private Context mContext = null;

	private RotateAnimation mFlipAnimation;

	private int mTextViewHeight = 100;

	public HeaderView(Context context) {
		this(context, null);
	}

	public HeaderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setMinimumHeight(mTextViewHeight);
		initViewLayout();
	}

	private void initViewLayout() {

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, mTextViewHeight);
		mTextView = new TextView(mContext, null, 0);
		this.addView(mTextView, lp);
		mTextView.setText(R.string.pull_to_refresh);
		mTextView.setGravity(Gravity.CENTER);

		lp = new RelativeLayout.LayoutParams(60, 60);
		lp.addRule(ALIGN_PARENT_LEFT);
		lp.setMargins(30, 20, 0, 20);
		mImageView = new ImageView(mContext);
		this.addView(mImageView, lp);
		mImageView.setImageResource(R.drawable.android);
		mImageView.setVisibility(View.GONE);

		mFlipAnimation = new RotateAnimation(0, 359,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(1000);
		mFlipAnimation.setRepeatCount(-1);
		mFlipAnimation.setFillAfter(true);

	}

	public void reset() {
		mImageView.setVisibility(View.GONE);
		mImageView.clearAnimation();
		mImageView.setVisibility(View.GONE);
		mTextView.setText(R.string.pull_to_refresh);
	}

	public int getRefreshHeight() {
		return mTextViewHeight;
	}

	public void setRefreshText(int resId) {
		mTextView.setText(resId);
	}

	public void startRefresh() {
		mImageView.setVisibility(View.VISIBLE);
		mImageView.startAnimation(mFlipAnimation);
	}

}
