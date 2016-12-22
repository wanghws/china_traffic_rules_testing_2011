package com.jiaogui.androidexam.activity;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NoExamActivity extends Activity {
	private static final String TAG = "NoErrorActivity";
	
	private Button noexam_back;
	private Button exam_button_n;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noexam);
		MobclickAgent.onEvent(this, "s05");
		try {
			init();
			onclick();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			Intent intent = new Intent();
			intent.setClass(NoExamActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
		
		
		
	}

	private void init() {
		// TODO Auto-generated method stub
		noexam_back = (Button) findViewById(R.id.noexam_back);
		exam_button_n = (Button) findViewById(R.id.exam_button_n);
	}

	private void onclick() {
		// TODO Auto-generated method stub
		exam_button_n.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(NoExamActivity.this, ExamActivity.class);
				startActivity(intent);
			}
		});
		noexam_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("1", 2);
				intent.putExtras(bundle);
				intent.setClass(NoExamActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
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
