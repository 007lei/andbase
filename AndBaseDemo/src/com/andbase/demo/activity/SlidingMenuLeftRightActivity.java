package com.andbase.demo.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ab.view.slidingmenu.SlidingFragmentActivity;
import com.ab.view.slidingmenu.SlidingMenu;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;

public class SlidingMenuLeftRightActivity extends SlidingFragmentActivity {

	private SlidingMenu menu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//ʹ��ϵͳ������λ�ã�Ϊ�˷����Զ���ı�����
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.sliding_menu_content);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);  
		
		//�Զ���ı�����
		AbTitleBar mAbTitleBar = new AbTitleBar(this);
		mAbTitleBar.setTitleText(R.string.sliding_menu_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.button_selector_menu);
		
		//�ӵ�ϵͳ������λ����
		LinearLayout titleBarLinearLayout = (LinearLayout)this.findViewById(R.id.titleBar);
		LinearLayout.LayoutParams layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		titleBarLinearLayout.addView(mAbTitleBar,layoutParamsFF);
		
		
	    //������setContentView()֮ǰ����
	    setBehindContentView(R.layout.sliding_menu_menu);
		
	    //SlidingMenu������
 		menu = getSlidingMenu();
 		menu.setMode(SlidingMenu.LEFT_RIGHT);
 		menu.setShadowWidthRes(R.dimen.shadow_width);
 		menu.setShadowDrawable(R.drawable.shadow);
 		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
 		menu.setFadeDegree(0.35f);
 		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
 		//�����õĲ���
 		//menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
 		//menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
 		
 		//menu.setBehindScrollScale(0.2f);
 		
 		//menu.setBehindWidth((int) (0.75 * menu.getWidth()));
 		//menu.requestLayout();
 		
 		//menu.setFadeEnabled(true);
 		
 		
 		//����ͼ��Fragment���
	    setContentView(R.layout.sliding_menu_content);
        getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new Fragment1())
		.commit();
 		
 		//menu1��ͼ��Fragment���
		menu.setMenu(R.layout.sliding_menu_menu);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new Fragment2())
		.commit();
 		
 		//menu2��ͼ��Fragment���
		menu.setSecondaryMenu(R.layout.sliding_menu_menu2);
		menu.setSecondaryShadowDrawable(R.drawable.shadow_right);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame_two, new Fragment1())
		.commit();
        
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
