/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ab.view.pullview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Scroller;

import com.ab.view.listener.AbOnListViewListener;

// TODO: Auto-generated Javadoc
/**
 * The Class AbPullGridView.
 */
public class AbPullGridView extends AbBaseGridView implements OnScrollListener,OnTouchListener {

	
	/** The m last y. */
	private float mLastY = -1; 
	
	/** The m scroller. */
	private Scroller mScroller;
	
	/** ͷ��ˢ��View. */
	private AbListViewHeader mHeaderView;
	
	/** The m footer view. */
	private AbListViewFooter mFooterView;
	
	/** ͷ��View�ĸ߶�. */
	private int mHeaderViewHeight; 
	
	/** The m enable pull refresh. */
	private boolean mEnablePullRefresh = true;
	
	/** The m enable pull load. */
	private boolean mEnablePullLoad = true;
	
	/** The m pull refreshing. */
	private boolean mPullRefreshing = false;
	
	/** The m pull loading. */
	private boolean mPullLoading;
	
	/** The m ab on refresh listener. */
	private AbOnListViewListener  mListViewListener = null;
 
	/** The m scroll back. */
	private int mScrollBack;
	
	/** The Constant SCROLLBACK_HEADER. */
	private final static int SCROLLBACK_HEADER = 0;
	
	/** The Constant SCROLL_DURATION. */
	private final static int SCROLL_DURATION = 200;
	
	/** The Constant OFFSET_RADIO. */
	private final static float OFFSET_RADIO = 1.8f;
	
	/** The m grid view. */
	private GridView mGridView = null;
	
	private AbLoadingView loadingView = null;
	
	/** �������. */
	private BaseAdapter mAdapter = null;
	
	/** ����Ƿ�ɹ���. */
	private boolean childScrollState = false;
	
	/**
	 * Instantiates a new ab pull view.
	 *
	 * @param context the context
	 */
	public AbPullGridView(Context context) {
		super(context);
		initWithContext(context);
	}

	/**
	 * Instantiates a new ab pull view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbPullGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	/**
	 * Inits the with context.
	 *
	 * @param context the context
	 */
	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		
		// init header view
		mHeaderView = new AbListViewHeader(context);
		
		// init header height
		mHeaderViewHeight = mHeaderView.getHeaderHeight();
		mHeaderView.setGravity(Gravity.BOTTOM);
		addHeaderView(mHeaderView);
		
		mGridView = this.getGridView();
		mGridView.setCacheColorHint(context.getResources().getColor(android.R.color.transparent));
		//Ĭ��ֵ �Զ�����Ե�Activity���޸�
		mGridView.setColumnWidth(100);
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setHorizontalSpacing(5);
		mGridView.setNumColumns(GridView.AUTO_FIT);
		mGridView.setPadding(5, 5, 5, 5);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setVerticalSpacing(5);
		//mGridView.setBackgroundColor(Color.GRAY);
		mGridView.setOnScrollListener(this);
		mGridView.setOnTouchListener(this);
		
		//����ʱ��View
		//loadingView = new AbLoadingView(context);
		
		/*LinearLayout.LayoutParams layoutParamsFW1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		layoutParamsFW1.gravity = Gravity.CENTER;
		layoutParamsFW1.weight = 1;
		addFooterView(mHeaderView);*/
		
		// init footer view
		mFooterView = new AbListViewFooter(context);
		
		addFooterView(mFooterView);
		mFooterView.hide();
		//Ĭ���Ǵ�ˢ�������
		setPullRefreshEnable(true);
		setPullLoadEnable(true);
		
	}

	/**
	 * �򿪻��߹ر�����ˢ�¹���.
	 *
	 * @param enable ���ر��
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) {
			mHeaderView.setVisibility(View.INVISIBLE);
		} else {
			mHeaderView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * �򿪻��߹رռ��ظ��๦��.
	 *
	 * @param enable ���ر��
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.setState(AbListViewFooter.STATE_READY);
			//load more����¼�.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	
	/**
	 * Start load more.
	 */
	private void startLoadMore() {
		mFooterView.show();
		mPullLoading = true;
		mFooterView.setState(AbListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			//��ʼ��������
			mListViewListener.onLoadMore();
		}
	}
	
	/**
	 * stop load more, reset footer view.
	 *
	 * @param more the more
	 */
	public void stopLoadMore(boolean more) {
		mFooterView.hide();
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(AbListViewFooter.STATE_READY);
		}
		//�ж���û�и���������
		if(more){
			mFooterView.setState(AbListViewFooter.STATE_READY);
		}else{
			mFooterView.setState(AbListViewFooter.STATE_NO);
		}
	}

	/**
	 * ����header�ĸ߶�.
	 *
	 * @param �����ĸ߶�
	 */
	private void updateHeaderHeight(float delta) {
		int newHeight = (int) delta + mHeaderView.getVisiableHeight();
		mHeaderView.setVisiableHeight(newHeight);
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
				mHeaderView.setState(AbListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(AbListViewHeader.STATE_NORMAL);
			}
		}
	}

	/**
	 * ����״̬����Header��λ��.
	 */
	private void resetHeaderHeight() {
		//��ǰ�������ĸ߶�
		int height = mHeaderView.getVisiableHeight();
		if (height < mHeaderViewHeight || !mPullRefreshing) {
			//�����  ����
			mScrollBack = SCROLLBACK_HEADER;
			mScroller.startScroll(0, height, 0, -1*height, SCROLL_DURATION);
		}else if(height > mHeaderViewHeight || !mPullRefreshing){
			//������  ���ص�mHeaderViewHeight
			mScrollBack = SCROLLBACK_HEADER;
			mScroller.startScroll(0, height, 0, -(height-mHeaderViewHeight), SCROLL_DURATION);
		}
		
		//�����¼�
		childScrollState = true;
		
		invalidate();
	}
	
	/**
	 * �������¼�
	 * @see android.widget.ScrollView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		//GridView�����¼����Լ�������
		if(childScrollState){
			return false;
		}
		
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		
			case MotionEvent.ACTION_DOWN:
				//Log.i("TAG", "--ACTION_DOWN--");
				mLastY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				//Log.i("TAG", "--ACTION_MOVE--");
				
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				if (mEnablePullRefresh && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
					//�������¸߶�
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					
				}
				break;
			case MotionEvent.ACTION_UP:
				//Log.i("TAG", "--ACTION_UP--");
				mLastY = -1;
				//��Ҫˢ�µ�����
				if (mEnablePullRefresh && mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(AbListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						//ˢ��
						mListViewListener.onRefresh();
					}
				}
				if(mEnablePullRefresh){
					//Log.i("TAG", "--����--");
					//����
					resetHeaderHeight();
				}
				
				break;
			default:
				break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	/**
	 * ����������ListView�ļ�����.
	 *
	 * @param listViewListener the new ab on list view listener
	 */
	public void setAbOnListViewListener(AbOnListViewListener listViewListener) {
		mListViewListener = listViewListener;
	}
	
	/**
	 * ���������������б��������.
	 * @param adapter the new adapter
	 */
	public void setAdapter(BaseAdapter adapter) {
		mAdapter = adapter;
		mGridView.setAdapter(mAdapter);
	}
	

	@Override
	public boolean onTouch(View arg0, MotionEvent ev) {
		return onTouchEvent(ev);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
	   
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		switch (scrollState) {
	    // ������ʱ
	    case OnScrollListener.SCROLL_STATE_IDLE:
	    	Log.i("TAG", "SCROLL_STATE_IDLE");
	    	if(view.getFirstVisiblePosition() == 0){
	    		Log.i("TAG", "����������");
	    		childScrollState = false;
	    		
	    	}else if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
	    		Log.i("TAG", "�������ײ�");
	    		startLoadMore();
	    	}else{
	    		mFooterView.hide();
	    		if(!mPullRefreshing){
	    			childScrollState = true;
	    		}
	    	}
	        break;
	    case OnScrollListener.SCROLL_STATE_FLING:
	    	Log.i("TAG", "SCROLL_STATE_FLING");
	    	break;
	    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
	         break;
        } 
	
	}
	
	
	

	
}
