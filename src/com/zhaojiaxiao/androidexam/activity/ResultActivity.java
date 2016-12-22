package com.jiaogui.androidexam.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity{
	private static final String TAG = "ResultActivity";
	
	//ui
	private Button backButton;
	private Button errorButton;
	private Button restartButton;
	private Button shareButton;
	private TextView resultInfoView;
	private int grade;
	private int time;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.result);
		MobclickAgent.onEvent(this, "s03_1");
		
		try{
			init();
			onclick();
			showInfo();
		}catch(Exception e){
			Log.e(TAG, e.getMessage(),e);
			Intent intent = new Intent();
			intent.setClass(ResultActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
		
	}
	private void init()throws Exception{
		backButton = (Button)findViewById(R.id.exam_back);
		errorButton = (Button)findViewById(R.id.error_exam);
		restartButton = (Button)findViewById(R.id.restart_exam);
		shareButton = (Button)findViewById(R.id.share);
		resultInfoView = (TextView)findViewById(R.id.result_info);
		
		Bundle bundle = this.getIntent().getExtras();
		grade = bundle.getInt("grade");
		time = bundle.getInt("useTime");
		if(grade==100){
			errorButton.setVisibility(View.GONE);
		}
	}
	private void onclick(){
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("1", 2);
				intent.putExtras(bundle);
				intent.setClass(ResultActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		restartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ResultActivity.this, ExamActivity.class);
				startActivity(intent);
			}
		});
		shareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shareList();
				
			}
		});
		errorButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ResultActivity.this, MyErrorActivity.class);
				/*Bundle bundle = new Bundle();
		    	bundle.putInt("useTime", time);
		    	bundle.putInt("grade", grade);
				intent.putExtras(bundle);*/
				startActivity(intent);
			}
		});
	}
	private void showInfo()throws Exception{
		
		SimpleDateFormat format = new SimpleDateFormat("mm分 ss秒");
		String useTime = format.format(new Date(time*1000));
		String result = "";
		if(grade>=90){
			result = "恭喜你：\n    通过了本次交规模拟考试，请继续努力，多加练习!\n";
		}else{
			result = "辛苦了：\n    很遗憾，您没能通过本次交规模拟考试，请继续努力，多加练习!\n";
		}
		String info = result+"您的得分："+grade+"分\n答题时间："+useTime;
		resultInfoView.setText(info);
	}
	
	private void shareList(){
		final String[] items = getResources().getStringArray(R.array.share_item);
		new AlertDialog.Builder(this)
		.setTitle("请选择要分享的网站：")
		.setItems(items, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which) {
				//alert("select : "+items[which]);
				Intent intent = new Intent();
				intent.setClass(ResultActivity.this, ShareActivity.class);
				Bundle bundle = new Bundle();
		    	bundle.putInt("useTime", time);
		    	bundle.putInt("type", 0);
		    	bundle.putInt("grade", grade);
		    	bundle.putInt("item", which);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which) {
				dialog.dismiss();
			}
		}).show();
	}
	private void alert(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("1", 2);
		intent.putExtras(bundle);
		intent.setClass(ResultActivity.this, MainActivity.class);
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
