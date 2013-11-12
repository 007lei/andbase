package com.andbase.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class TitleTransparentActivity extends AbActivity {
	
	private MyApplication application;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//������super֮ǰ���ò����ܹ���ʾ���Ǳ�����
    	//this.getIntent().putExtra(AbConstant.TITLE_TRANSPARENT_FLAG,AbConstant.TITLE_TRANSPARENT);
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.base_detail);
        application = (MyApplication)abApplication;
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText("͸��������");
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        mAbTitleBar.setTitleTextBackgroundResource(R.drawable.drop_down_title_btn);
	    
        mAbTitleBar.getTitleTextButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showToast("���˱���Ŷ");
				
			}
		});
        
        mAbTitleBar.getLogoView().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        TextView mText = (TextView)this.findViewById(R.id.mText);
        mText.setText(R.string.title_transparent_desc);
    }    
}


