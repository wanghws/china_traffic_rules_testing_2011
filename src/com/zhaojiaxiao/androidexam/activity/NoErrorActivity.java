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
import android.widget.TextView;

public class NoErrorActivity extends Activity {
	private static final String TAG = "NoErrorActivity";
	
	private Button myerror_back;
	private Button study_button;
	private Button exam_button;
	private TextView noerror_text;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noerror);
		MobclickAgent.onEvent(this, "s04_1");
		
		try {
			init();
			onclick();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			Intent intent = new Intent();
			intent.setClass(NoErrorActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
		
	}
	private void init() {
		// TODO Auto-generated method stub
		myerror_back = (Button) findViewById(R.id.myerror_back);
		study_button = (Button) findViewById(R.id.study_button);
		exam_button = (Button) findViewById(R.id.exam_button);
		noerror_text = (TextView) findViewById(R.id.noerror_text);
		noerror_text.setText("你没有错题，请进入：");
	}
	private void onclick() {

		myerror_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("1", 2);
				intent.putExtras(bundle);
				intent.setClass(NoErrorActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		study_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(NoErrorActivity.this, TopicActivity.class);
				startActivity(intent);
			}
		});
		exam_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(NoErrorActivity.this, ExamActivity.class);
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
