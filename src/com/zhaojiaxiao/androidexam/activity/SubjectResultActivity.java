package com.jiaogui.androidexam.activity;

import java.util.Map;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.ExamDataUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SubjectResultActivity extends Activity{
	private static final String TAG = "SubjectResultActivity";
	
	//ui
	private Button backButton;
	private Button nextButton;
	private Button errorButton;
	private Button restartButton;
	private Button shareButton;
	private TextView resultInfoView;
	private int grade;
	private int size;
	private String subId;
	private String topId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.subject_result);
		MobclickAgent.onEvent(this, "s02_5");
		
		try{
			init();
			onclick();
			showInfo();
		}catch(Exception e){
			Log.e(TAG, e.getMessage(),e);
			Intent intent = new Intent();
			intent.setClass(SubjectResultActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
		
	}
	private void init()throws Exception{
		backButton = (Button)findViewById(R.id.exam_back);
		nextButton = (Button)findViewById(R.id.error_next);
		errorButton = (Button)findViewById(R.id.error_exam);
		restartButton = (Button)findViewById(R.id.restart_exam);
		shareButton = (Button)findViewById(R.id.share);
		resultInfoView = (TextView)findViewById(R.id.result_info);
		
		Bundle bundle = this.getIntent().getExtras();
		grade = bundle.getInt("grade");
		size = bundle.getInt("size");
        subId = bundle.getString("subId");
        topId = (String)bundle.get("topId");
        if(size==grade){
        	errorButton.setVisibility(View.GONE);
        }
	}
	private void onclick(){
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("subId",subId);
				bundle.putString("topId",topId);
				intent.putExtras(bundle);
				intent.setClass(SubjectResultActivity.this, SubjectActivity.class);
				startActivity(intent);
			}
		});
		restartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SubjectResultActivity.this, SubjectExamActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("subId",subId);
				bundle.putString("topId",topId);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		shareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shareList();
				
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String,Object> map = ExamDataUtil.getNextSubjectExamList(subId);
				if(null==map){
					alert("已经是最后一小节");
					return;
				}
				
				Intent intent = new Intent();
				intent.setClass(SubjectResultActivity.this, SubjectStudyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("subId",(String)map.get("subId"));
				bundle.putString("topId",topId);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		errorButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SubjectResultActivity.this, SubjectErrorActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("subId",subId);
				Log.d(">>>>>>>>>subId", subId);
				bundle.putString("topId",topId);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	private void showInfo()throws Exception{
		String result = "";
		if(grade==size){
			result = "恭喜你,已通过本小节模拟测试\n共"+size+"题,全部正确";
		}else{
			result = "抱歉,未通过本小节模拟测试\n共"+size+"题,答对"+grade+"题";
		}
		//String info = "共"+size+"道题，\n继续努力，多加练习。";
		resultInfoView.setText(result);
	}
	
	private void shareList(){
		final String[] items = getResources().getStringArray(R.array.share_item);
		new AlertDialog.Builder(this)
		.setTitle("请选择要分享的网站：")
		.setItems(items, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which) {
				//alert("select : "+items[which]);
				Intent intent = new Intent();
				intent.setClass(SubjectResultActivity.this, ShareActivity.class);
				Bundle bundle = new Bundle();
		    	bundle.putInt("grade", grade);
		    	bundle.putInt("size", size);
		    	bundle.putInt("type", 1);
		    	bundle.putInt("item", which);
		    	bundle.putString("subId", subId);
		    	bundle.putString("topId", topId);
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
		bundle.putString("subId",subId);
		bundle.putString("topId",topId);
		intent.putExtras(bundle);
		intent.setClass(SubjectResultActivity.this, SubjectActivity.class);
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
