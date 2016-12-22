package com.jiaogui.androidexam.activity;


import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidxam.serivce.DBcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CheckBox;

public class SettingActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

	private Button backButton;
	private CheckBox check;
	private Button clean_result;
	private Button clean_myerror;
	private Button clean_exam;
	private Button clean_all;
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		MobclickAgent.onEvent(this, "s06_3");
		init();
		onClick();
	}

	

	private void init() {
		backButton = (Button)findViewById(R.id.set_back);
		check = (CheckBox)findViewById(R.id.check);
		clean_result = (Button)findViewById(R.id.clean_result);
		clean_myerror = (Button)findViewById(R.id.clean_myerror);
		clean_exam = (Button)findViewById(R.id.clean_exam);
		clean_all = (Button)findViewById(R.id.clean_all);
		check.setOnCheckedChangeListener(this);
		
		Context ctx = SettingActivity.this;       
	    sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
	    int bool = sp.getInt("Condition",0);
	    if(bool==0){
	    	check.setChecked(true);
	    }else{
	    	check.setChecked(false);
	    }
	}
	
	private void onClick() {
		
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SettingActivity.this, MoreActivity.class);
				startActivity(intent);
			}
		});
		
		clean_result.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alert0("提示","确定要清空吗？");
				
			}
		});
		clean_myerror.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alert1("提示","确定要清空吗？");
				
			}
		});
		clean_exam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alert2("提示","确定要清空吗？");
			}
		});
		clean_all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alert3("提示","确定要清空吗？");
				
			}
		});
		
	}

	private void alert0(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBcontrol.DeleteResultTableForAll(SettingActivity.this);
			}
		}).show();
	}
	private void alert1(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DBcontrol.DeleteLeafLevelForAll(SettingActivity.this);
					}
				}).show();
	}
	private void alert2(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBcontrol.DeletenotesTableForAll(SettingActivity.this);
			}
		}).show();
	}
	
	private void alert3(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBcontrol.DeleteLeafLevelForAll(SettingActivity.this);
				DBcontrol.DeleteResultTableForAll(SettingActivity.this);	
				DBcontrol.DeletenotesTableForAll(SettingActivity.this);
			}
		}).show();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Editor editor = sp.edit();
		if (isChecked) {
			check.setText("答题自动前进");
			editor.putInt("Condition",0);
			editor.commit();
			
		} else {
			check.setText("答题自动前进关闭");
			editor.putInt("Condition",1);
			editor.commit();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent intent = new Intent();
		intent.setClass(SettingActivity.this, MoreActivity.class);
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
