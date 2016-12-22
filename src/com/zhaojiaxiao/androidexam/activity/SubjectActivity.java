package com.jiaogui.androidexam.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.Exam;
import com.jiaogui.androidexam.data.ExamDataUtil;
import com.jiaogui.androidxam.serivce.DBcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SubjectActivity extends Activity{
	private static final String TAG = "SubjectActivity";
	private ListView subjectListView;
	private String topId;
	private Button backButton;
	private String subId;
	private int p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.subject);
		MobclickAgent.onEvent(this, "s02_2");
		
		try{
			Bundle bundle = this.getIntent().getExtras();
			topId = null;
			if(bundle.containsKey("topId")){
				topId = bundle.getString("topId");
			}
			subId = null;
			if(bundle.containsKey("subId")){
				subId = bundle.getString("subId");
			}
			
			backButton  =(Button)findViewById(R.id.exam_back);
			backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(SubjectActivity.this, TopicActivity.class);
					Bundle bundle = new Bundle();
					//bundle.putString("subId",subId);
					bundle.putString("topId",topId);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			subjectListView = (ListView)findViewById(R.id.topicListView);
			subjectListView.setAdapter(new SimpleAdapter(this,
					getSubjectList(),
					R.layout.subjectlist,
					new String[]{"SubjectItemTitle"},
					new int[]{R.id.SubjectItemTitle}));
			subjectListView.setOnItemClickListener(new OnItemClickListener() {  
	            @Override  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	            	Map<String, Object> map = (Map<String, Object>) subjectListView.getItemAtPosition(arg2);
	            	String sub = (String)map.get("subId");
	            	/*if(null!=subId&&sub.equals(subId)){
	            		arg0.setBackgroundResource(R.drawable.banner);
	            	}*/
	            	selectType(sub);
	            }  
	        }); 
		}catch(Exception e){
			Log.e(TAG, e.getMessage(), e);
			Intent intent = new Intent();
			intent.setClass(SubjectActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
	}
	private List<Map<String, Object>> getSubjectList(){
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		List<Map<String,Object>> list = ExamDataUtil.getTopicList();
		List<Map<String,Object>> subjectList = null;
		for(Map<String,Object> map:list){
			//Log.i(TAG, " map.get():"+map.get("topId")+" topId:"+topId);
			if(map.get("topId").toString().equals(topId)){
				subjectList = (List<Map<String,Object>>)map.get("subjectList");
				//Log.i(TAG,subjectList.toString());
				break;
			}
		}
		if(null==subjectList)return listItem;
		Map<String, Object> row = null;
		int k=1;
		
		for(Map<String,Object> subject:subjectList){
			row  = new HashMap<String, Object>();
//			Log.d("~~~~~~~~~~~~~~SubId~~~~~~~~~~~~", subject.get("subId").toString());
			String subId = subject.get("subId").toString();
			p = DBcontrol.findcountBySubId(this, subId);
			row.put("SubjectItemTitle", subject.get("subId")+"."+subject.get("subName")+"  "+p+"/"+((List<Exam>)subject.get("examList")).size());
			row.put("subId", subject.get("subId"));
			listItem.add(row);
			k++;
			p=0;
			
		}
		return listItem;
	}
	private void alert(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	final String[] items = {"查看小节题目", "小节模拟测试"}; 
	private void selectType(final String subId){
		new AlertDialog.Builder(this)
		.setTitle("选择类型:")
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
		    public void onClick(DialogInterface dialog, int item) {
		    	if(item==0){
		    		Intent intent = new Intent();
					intent.setClass(SubjectActivity.this, SubjectStudyActivity.class);
					Bundle bundle = new Bundle();
			    	bundle.putString("subId", subId);
					intent.putExtras(bundle);
					startActivity(intent);
		    	}else{
		    		Intent intent = new Intent();
					intent.setClass(SubjectActivity.this, SubjectExamActivity.class);
					Bundle bundle = new Bundle();
			    	bundle.putString("subId", subId);
					intent.putExtras(bundle);
					startActivity(intent);
		    	}
		    }
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(SubjectActivity.this, TopicActivity.class);
		Bundle bundle = new Bundle();
		//bundle.putString("subId",subId);
		bundle.putString("topId",topId);
		intent.putExtras(bundle);
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
