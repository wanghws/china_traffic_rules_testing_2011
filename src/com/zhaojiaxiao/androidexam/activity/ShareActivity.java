package com.jiaogui.androidexam.activity;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.widget.TextView;

public class ShareActivity extends Activity{
	private static final String TAG = "ShareActivity";
	
	//ui
	private Button backButton;
	private TextView shareTitleTextView;
	private WebView shareWebView; 
	private int grade;
	private int time;
	private int size;
	private String subId;
	private String topId;
	private int type;
	private int item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.share);
		MobclickAgent.onEvent(this, "s06_1");
		try{
			init();
			onclick();
			if(type==0){
				showExamInfo();
			}else if(type==1){
				showSubjectInfo();
			}else{
				showMyInfo();
			}
			
		}catch(Exception e){
			Log.e(TAG, e.getMessage(),e);
		}
		
	}
	private void init()throws Exception{
		backButton = (Button)findViewById(R.id.exam_back);
		shareWebView = (WebView)findViewById(R.id.web_share);
		shareTitleTextView = (TextView)findViewById(R.id.share_title);
		
		Bundle bundle = this.getIntent().getExtras();
		item = bundle.getInt("item");
		grade = bundle.getInt("grade");
		time = bundle.getInt("useTime");
		grade = bundle.getInt("grade");
		type = bundle.getInt("type");
		size = bundle.getInt("size");
        subId = bundle.getString("subId");
        topId = (String)bundle.get("topId");
	}
	private void onclick(){
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if(type==0){
					intent.setClass(ShareActivity.this, ResultActivity.class);
					Bundle bundle = new Bundle();
			    	bundle.putInt("useTime", time);
			    	bundle.putInt("grade", grade);
					intent.putExtras(bundle);
				}else if(type==1){
					intent.setClass(ShareActivity.this, SubjectResultActivity.class);
					Bundle bundle = new Bundle();
			    	//bundle.putInt("useTime", time);
			    	bundle.putInt("grade", grade);
			    	bundle.putInt("size", size);
			    	bundle.putString("subId",subId);
					bundle.putString("topId",topId);
					intent.putExtras(bundle);
				}else{
					Bundle bundle = new Bundle();
					bundle.putInt("1", 2);
					intent.putExtras(bundle);
					intent.setClass(ShareActivity.this, MainActivity.class);
				}
				
				startActivity(intent);
			}
		});
	}
	private void showExamInfo()throws Exception{
		
		String[] items = getResources().getStringArray(R.array.share_item);
		shareTitleTextView.setText(items[item]);
		//SimpleDateFormat format = new SimpleDateFormat("mm分 ss秒");
		//String useTime = format.format(new Date(time*1000));

		String info = "我正在使用android手机版练习交规模拟考试，得了"+grade+"分！你也来试试吧，http://www./software/jiaogui/";
		info = URLEncoder.encode(info, "UTF-8");
		shareWebView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
			    return true;
			}
		});
		if(item==0){
			shareWebView.loadUrl("http://v.t.sina.com.cn/share/share.php?title="+info);
		}else{
			shareWebView.loadUrl("http://v.t.qq.com/share/share.php?title="+info);
		}
		
	}
	private void showSubjectInfo()throws Exception{
		String[] items = getResources().getStringArray(R.array.share_item);
		shareTitleTextView.setText(items[item]);
		String info = "我正在使用android手机版练习交规考试的"+subId+"节：共"+size+"题，我答对"+grade+"题！你也来试试吧，http://www./software/jiaogui/";
		info = URLEncoder.encode(info, "UTF-8");
		
		shareWebView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
			    return true;
			}
		});
		if(item==0){
			shareWebView.loadUrl("http://v.t.sina.com.cn/share/share.php?title="+info);
		}else{
			shareWebView.loadUrl("http://v.t.qq.com/share/share.php?title="+info);
		}
	}
	
	private void showMyInfo()throws Exception{
		String[] items = getResources().getStringArray(R.array.share_item);
		shareTitleTextView.setText(items[item]);
		String info = "我正在使用Android手机版，你也来试试吧，http://www./software/jiaogui/";
		info = URLEncoder.encode(info, "UTF-8");
		shareWebView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
			    return true;
			}
		});
		if(item==0){
			shareWebView.loadUrl("http://v.t.sina.com.cn/share/share.php?title="+info);
		}else{
			shareWebView.loadUrl("http://v.t.qq.com/share/share.php?title="+info);
		}
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		if(type==0){
			intent.setClass(ShareActivity.this, ResultActivity.class);
			Bundle bundle = new Bundle();
	    	bundle.putInt("useTime", time);
	    	bundle.putInt("grade", grade);
			intent.putExtras(bundle);
		}else if(type==1){
			intent.setClass(ShareActivity.this, SubjectResultActivity.class);
			Bundle bundle = new Bundle();
	    	//bundle.putInt("useTime", time);
	    	bundle.putInt("grade", grade);
	    	bundle.putInt("size", size);
	    	bundle.putString("subId",subId);
			bundle.putString("topId",topId);
			intent.putExtras(bundle);
		}else{
			Bundle bundle = new Bundle();
			bundle.putInt("1", 2);
			intent.putExtras(bundle);
			intent.setClass(ShareActivity.this, MainActivity.class);
		}
		
		startActivity(intent);
	}
	
	
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
}
