package com.jiaogui.androidexam.activity;


import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class FeedBackActivity extends Activity{
	private static final String TAG = "FeedBackActivity";
	
	//ui
	private Button backButton;
	private WebView shareWebView; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.feedback);
		MobclickAgent.onEvent(this, "s06_4");
		
		try{
			init();
			onclick();
			showAboutInfo();
		}catch(Exception e){
			Log.e(TAG, e.getMessage(),e);
		}
		
	}
	private void init()throws Exception{
		backButton = (Button)findViewById(R.id.exam_back);
		shareWebView = (WebView)findViewById(R.id.web_share);
	}
	private void onclick(){
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(FeedBackActivity.this, MoreActivity.class);
				startActivity(intent);
			}
		});
	}
	private void showAboutInfo()throws Exception{
		shareWebView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
			    return true;
			}
		});
		shareWebView.loadUrl("http://www./fankui/androidjiaogui.html");
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent intent = new Intent();
		intent.setClass(FeedBackActivity.this, MoreActivity.class);
		startActivity(intent);
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
}
