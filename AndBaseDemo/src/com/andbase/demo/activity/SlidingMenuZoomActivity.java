package com.andbase.demo.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ab.activity.AbActivity;
import com.ab.view.slidingmenu.SlidingMenu;
import com.ab.view.slidingmenu.SlidingMenu.CanvasTransformer;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;

public class SlidingMenuZoomActivity extends AbActivity {

	private SlidingMenu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.sliding_menu_content);
		
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.sliding_menu_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.button_selector_menu);
		
        //����ͼ��Fragment���
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new Fragment1())
		.commit();

		//SlidingMenu������
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		
		//menu��ͼ��Fragment���
		menu.setMenu(R.layout.sliding_menu_menu);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new SampleListFragment())
		.commit();
		
		//��������
		menu.setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				//������Ĭ�ϵĺڱ����滻��
				canvas.drawColor(SlidingMenuZoomActivity.this.getResources().getColor(R.color.gray_white));
				float scale = (float) (percentOpen*0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
			}
		});
		
		mAbTitleBar.getLogoView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (menu.isMenuShowing()) {
 					menu.showContent();
 				} else {
 					menu.showMenu();
 				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}

}
