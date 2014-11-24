package com.ihgoo.autobanner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * 可自动滚动的Banner
 * 
 * @author <a href="http://www.xunhou.me" target="_blank">Kelvin</a>
 *
 */
public class AutoBannerViewPager extends ViewPager {

	private final int BANNER_WHAT = 0;

	private MyHandler handler;

	private boolean isRunning = false;

	private boolean stopScrollWhenTouch = false;

	private boolean isStoped;

	private final int LEFT = 0;

	private final int RIGHT = 1;

	private final int START_WHAT = 0;

	private final int STOP_WHAT = 1;

	private int DEFAULT_ROLL_SPEED = 1500;

	private int method = LEFT;

	private boolean smoothScroll = true;

	public AutoBannerViewPager(Context context) {
		super(context);
		init();
	}

	public AutoBannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		handler = new MyHandler();
	}

	public void startScroll() {
		handler.removeCallbacksAndMessages(null);
		handler.sendEmptyMessageDelayed(START_WHAT, DEFAULT_ROLL_SPEED);
	}

	public void stopScroll() {
		handler.removeCallbacksAndMessages(null);
		handler.sendEmptyMessageDelayed(STOP_WHAT, DEFAULT_ROLL_SPEED);
	}

	/**
	 * 设置滚动的方向
	 * 
	 * @param method
	 *            从左往右 从右往左
	 */
	public void setMethod(int method) {
		this.method = method;
	}

	/**
	 * 设置平滑滚动，从第一个滚动到其他位置 平滑滚动
	 * 
	 * @param smoothScroll
	 *            true为平滑滚动
	 */
	public void setSmoothScroll(boolean smoothScroll) {
		this.smoothScroll = smoothScroll;
	}

	public void scroll() {
		PagerAdapter adapter = getAdapter();
		int currentItem = getCurrentItem();
		int totalItem;
		if (adapter == null || (totalItem = adapter.getCount()) <= 1) {
			return;
		}

		int nextItemPostion = (method == LEFT) ? --currentItem : ++currentItem;
		if (nextItemPostion < 0) {
			setCurrentItem(totalItem - 1, smoothScroll);
		} else if (currentItem == totalItem) {
			setCurrentItem(0, smoothScroll);
		} else {
			setCurrentItem(nextItemPostion);
		}
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case START_WHAT:
				scroll();
				isRunning = true;
				handler.sendEmptyMessageDelayed(START_WHAT, DEFAULT_ROLL_SPEED);

				break;
			case STOP_WHAT:
				handler.removeCallbacksAndMessages(null);
				isRunning = false;

				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		int action = ev.getAction();

		if (stopScrollWhenTouch) {
			if (isRunning && MotionEvent.ACTION_DOWN == action) {
				isStoped = true;
				stopScroll();
			} else if (isRunning && MotionEvent.ACTION_UP == action) {
				isStoped = false;
				startScroll();
			}
		}

		return super.dispatchTouchEvent(ev);
	}

}
