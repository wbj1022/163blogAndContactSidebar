package com.franco.pulltorefresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {
	
	private Paint mPaint;
	private String mText;
	
	public TextProgressBar(Context context) {
		this(context, null);
	}

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Rect rect = new Rect();
        mPaint.getTextBounds(mText, 0, this.mText.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();  
        int y = (getHeight() / 2) - rect.centerY();  
        canvas.drawText(mText, x, y, mPaint);
	}

	public void setText(String text) {
		mText = text;
	}
	
	
}
