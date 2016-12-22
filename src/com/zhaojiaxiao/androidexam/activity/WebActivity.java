package com.jiaogui.androidexam.activity;



import com.jiaogui.androidexam.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class WebActivity extends Activity {
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		WebView webview = (WebView) findViewById(R.id.web);
		
		Bundle bundle = this.getIntent().getExtras();
		url = null;
		try{
		    if(bundle.containsKey("url")){
			url = bundle.getString("url");
		    }
		    
		    webview.loadUrl("http://"+url);
		}catch(Exception e){
			Intent intent = new Intent();
			intent.setClass(WebActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
	}
}
