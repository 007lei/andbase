package com.andbase.blog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * Copyright (c) 2011 All rights reserved 
 * ���ƣ�WebActivity 
 * ��������վWap
 * @author zhaoqp
 * @date 2011-11-8
 * @version 
 */
public class WebActivity extends AbActivity {
	
	//������
	private WebView mWebView = null;
	private ProgressBar mProgressBar = null;
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //���ò���
        setAbContentView(R.layout.web);
        
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.blog_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.setVisibility(View.GONE);
		
        mWebView = (WebView)findViewById(R.id.webView);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        //����֧��JavaScript�ű�
		WebSettings webSettings = mWebView.getSettings();  
		webSettings.setJavaScriptEnabled(true);
		//���ÿ��Է����ļ�
		webSettings.setAllowFileAccess(true);
		//���ÿ���֧������
		webSettings.setSupportZoom(true);
		//����Ĭ�����ŷ�ʽ�ߴ���far
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		//���ó������Ź���
		webSettings .setBuiltInZoomControls(true);
		webSettings.setDefaultFontSize(20);
		
        //����assetsĿ¼�µ��ļ�
        String url = "http://www.418log.org/m";
        mWebView.loadUrl(url);
        
		
        // ����WebViewClient
		mWebView.setWebViewClient(new WebViewClient() {
			// url����
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// ʹ���Լ���WebView�������ӦUrl�����¼���������ʹ��Ĭ�������������ҳ��
				view.loadUrl(url);
				// ��Ӧ��ɷ���true
				return true;
				//return super.shouldOverrideUrlLoading(view, url);
			}

			// ҳ�濪ʼ����
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			// ҳ��������
			@Override
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			// WebView���ص�������Դurl
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

		});

		// ����WebChromeClient
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			// ����javascript�е�alert
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				return super.onJsAlert(view, url, message, result);
			};

			@Override
			// ����javascript�е�confirm
			public boolean onJsConfirm(WebView view, String url,
					String message, final JsResult result) {
				return super.onJsConfirm(view, url, message, result);
			};

			@Override
			// ����javascript�е�prompt
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, final JsPromptResult result) {
				return super.onJsPrompt(view, url, message, defaultValue, result);
			};
			
			//������ҳ���صĽ�����
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				mProgressBar.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}
			
			//���ó����Title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
		
        mAbTitleBar.getLogoView().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				back();
			}
		});

	}
    
    /**
	 * ���ط��ؼ�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// �Ƿ񴥷�����Ϊback��
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return true;
			// �������back��������Ӧ
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private void back(){
		if(mWebView.canGoBack()){
			mWebView.goBack();
		}else{
			finish();
		}
	}
}
