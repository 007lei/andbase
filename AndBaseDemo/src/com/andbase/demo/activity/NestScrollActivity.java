package com.andbase.demo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * ���ƣ�NestScrollActivity
 * ����������Ƕ��
 * @author zhaoqp
 * @date 2011-12-13
 * @version
 */
public class NestScrollActivity extends AbActivity {
	
	private MyApplication application;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.nest_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.nest_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button listPager  = (Button)this.findViewById(R.id.listPager);
        Button slidingMenuPager  = (Button)this.findViewById(R.id.slidingMenuPager);
        Button scrollList  = (Button)this.findViewById(R.id.scrollList);
        Button scrollPager  = (Button)this.findViewById(R.id.scrollPager);
        
        
        listPager.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NestScrollActivity.this,ListNestViewPagerActivity.class); 
 				startActivity(intent);
			}
		});
        
        slidingMenuPager.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NestScrollActivity.this,SlidingMenuNestViewPagerActivity.class); 
 				startActivity(intent);
			}
		});
        
        
        scrollList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog("��ʾ", "�����ListVie����AbInnerListView�Ϳ�����", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
			}
		});
        
        scrollPager.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                 showDialog("��ʾ", "�����ScrollView����AbOuterScrollView�Ϳ�����", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
			}
		});
        
      } 
    
}