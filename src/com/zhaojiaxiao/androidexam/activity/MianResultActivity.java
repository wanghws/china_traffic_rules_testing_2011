package com.jiaogui.androidexam.activity;

import java.util.ArrayList;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidxam.serivce.DBcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MianResultActivity extends Activity {
	private static final String TAG = "MianResultActivity";

	private Button result_back;
	private Button error_exam_s;
	private Button restart_exam_s;
	private Button remove;
	private TextView result_info_s;
	private int grade;
	private int size;
	private ArrayList right;
	private int i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myerror_result);
		MobclickAgent.onEvent(this, "s03_2");
		
		try{
			init();
			onclick();
			showInfo();
		}catch(Exception e){
			Log.e(TAG, e.getMessage(),e);
			Intent intent = new Intent();
			intent.setClass(MianResultActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
	}

	private void init() {
		
		result_back = (Button)findViewById(R.id.result_back);
		result_info_s = (TextView)findViewById(R.id.result_info_s);
		error_exam_s = (Button)findViewById(R.id.error_exam_s);
		restart_exam_s = (Button)findViewById(R.id.restart_exam_s);
		remove = (Button)findViewById(R.id.remove);
		
		Bundle bundle = this.getIntent().getExtras();
		grade = bundle.getInt("grade");
		size = bundle.getInt("size");
		right = bundle.getIntegerArrayList("rightList");
		for (i = 0; i < right.size(); i++) {
			Log.d(i+"次对题", (right.get(i)).toString());
		}
	
		if(i==0){
			remove.setVisibility(View.GONE);
		}
	}

	private void onclick() {
		
		result_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("1", 2);
				intent.putExtras(bundle);
				intent.setClass(MianResultActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		error_exam_s.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MianResultActivity.this, MainErrorActivity.class);
				startActivity(intent);
			}
		});
		restart_exam_s.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MianResultActivity.this, ErrorExamActivity.class);
				startActivity(intent);
			}
		});
		remove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				StringBuffer sql = new StringBuffer();
				sql.append("delete from leaflevel where id in (");
				for (int i = 0; i < right.size()-1; i++) {
				  sql.append(right.get(i).toString()+",");
				}
				sql.append(right.get(right.size()-1));
				sql.append(")");
				DBcontrol.deleteById(MianResultActivity.this.getBaseContext(),sql.toString()); 
				Log.d("sql", sql.toString());
				boolean or = DBcontrol.getnull(MianResultActivity.this);
				if(size==grade){
					Intent intent = new Intent();
					intent.setClass(MianResultActivity.this, NoErrorActivity.class);
					startActivity(intent);
				}else if(or){
					alert("移除成功！");
				    
				}
				
//				boolean or = DBcontrol.getnull(MianResultActivity.this);
//				if(or){
//					Intent intent = new Intent();
//					intent.setClass(MianResultActivity.this, MainErrorActivity.class);
//					startActivity(intent);
//				}
//				Intent intent = new Intent();
//				intent.setClass(MianResultActivity.this, NoErrorActivity.class);
//				startActivity(intent);
				
			}
		});
	}

	private void showInfo() {

		String result = "";
		int x = grade;
		if(grade<0){
			x=0;
		}
		    
			result = "辛苦了：\n共"+size+"道题,您答对了"+x+"道";
		
			result_info_s.setText(result);
		
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("1", 2);
		intent.putExtras(bundle);
		intent.setClass(MianResultActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	private void alert(String message){
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
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
