package com.jiaogui.androidexam.activity;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MoreActivity extends Activity {

	private static final String TAG = "MoreActivity";
	private Button more_fankui;
	private Button more_share;
	private Button more_setting;
	private Button more_about;
	private Button more_back;
	private Button more_app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		MobclickAgent.onEvent(this, "s06_1");
		try {
			initButton();
			onClick();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			Intent intent = new Intent();
			intent.setClass(MoreActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}

	}

	private void initButton() {
		// TODO Auto-generated method stub
		more_fankui = (Button) findViewById(R.id.more_fankui);
		more_share = (Button) findViewById(R.id.more_share);
		more_setting = (Button) findViewById(R.id.more_setting);
		more_about = (Button) findViewById(R.id.more_about);
		more_back = (Button) findViewById(R.id.more_back);
		more_app = (Button) findViewById(R.id.more_app);
	}

	private void onClick() {
		// TODO Auto-generated method stub
		more_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("1", 2);
				intent.putExtras(bundle);
				intent.setClass(MoreActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		more_fankui.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setAction("android.intent.action.VIEW");
//				Uri content_url = Uri
//						.parse("http://www./fankui/androidjiaogui.html");
//				intent.setData(content_url);
//				intent.setClassName("com.android.browser",
//						"com.android.browser.BrowserActivity");
//				startActivity(intent);
				Intent intent = new Intent();
				intent.setClass(MoreActivity.this, FeedBackActivity.class);
				startActivity(intent);
			}
		});
		more_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shareList();
			}
		});
		more_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MoreActivity.this, SettingActivity.class);
				startActivity(intent);
			}
		});
		more_about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MoreActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		});
		more_app.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MoreActivity.this, RecommendActivity.class);
				startActivity(intent);
			}
		});
	}

	private void shareList() {
		final String[] items = getResources()
				.getStringArray(R.array.share_item);
		new AlertDialog.Builder(this)
				.setTitle("请选择要分享的网站：")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// alert("select : "+items[which]);
						Intent intent = new Intent();
						intent.setClass(MoreActivity.this, ShareActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("type", 2);
						bundle.putInt("item", which);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("1", 2);
		intent.putExtras(bundle);
		intent.setClass(MoreActivity.this, MainActivity.class);
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
