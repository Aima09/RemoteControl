package com.yf.remotecontrolclient.activity.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * liteplayer by loader
 * @author qibin
 */
public class ScrollRelativeLayout extends RelativeLayout {
	private Scroller mScroller;
	private int mIndicatorHeight;
	
	public ScrollRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		View indicator = getChildAt(0);
		mIndicatorHeight = indicator.getMeasuredHeight();
		
		//L.l("indicator height", mIndicatorHeight);
	}
	
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()) {
			scrollTo(0, mScroller.getCurrY());
			postInvalidate();
		}
	}
	
	/**
	 * 隐藏indicator
	 */
	public void hideIndicator() {
		if(!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
		
		mScroller.startScroll(0, 0, 0, mIndicatorHeight, 500);
	}
	
	public void showIndicator() {
		//回到顶部
		scrollTo(0, 0);
		//  使用postInvalidate则比较简单，不需要handler，直接在线程中调用postInvalidate即可。
		//invalidate()的调用是把之前的旧的view从主UI线程队列中pop掉
		postInvalidate();
	}
}
