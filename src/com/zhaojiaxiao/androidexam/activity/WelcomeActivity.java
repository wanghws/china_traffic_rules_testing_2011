package com.jiaogui.androidexam.activity;

import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.ExamDataUtil;
import com.jiaogui.androidxam.serivce.Db;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WelcomeActivity extends Activity {
	private static final String TAG = "WelcomeActivity";
	private static final String URL = "http://feedback./android.jpg";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		createTable();
		
		try {
			new Handler().postDelayed(new Runnable() {
				public void run() {
//					total();
					ExamDataUtil.load(getResources());
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("1", 1);
					intent.putExtras(bundle);
					intent.setClass(WelcomeActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}, 1000);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void createTable() {
		Db db = new Db(this.getBaseContext());
		db.getReadableDatabase();
		db.close();
	}

	private void total() {
		try {
			URL getUrl = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) getUrl
					.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			// Log.i(TAG,is.toString());
			// Thread.sleep(1000);
			is.close();
			conn.disconnect();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			//
		}
	}

}