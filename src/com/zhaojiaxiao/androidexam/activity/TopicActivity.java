package com.jiaogui.androidexam.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.ExamDataUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TopicActivity extends Activity{
	private static final String TAG = "TopicActivity";
	private ListView topicListView;
	private Button backButton;
	private String topId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.topic);
		MobclickAgent.onEvent(this, "s02_1");
		try{
			Bundle bundle = this.getIntent().getExtras();
			topId = null;
			if(null!=bundle&&bundle.containsKey("topId")){
				topId = bundle.getString("topId");
			}
			backButton  =(Button)findViewById(R.id.exam_back);
			backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("1", 2);
					intent.putExtras(bundle);
					intent.setClass(TopicActivity.this, MainActivity.class);
					startActivity(intent);
				}
			});
			topicListView = (ListView)findViewById(R.id.topicListView);
			topicListView.setAdapter(new SimpleAdapter(this,
					getTopicList(),
					R.layout.topiclist,
					new String[]{"ItemTitle"},
					new int[]{R.id.ItemTitle}));
			topicListView.setOnItemClickListener(new OnItemClickListener() {  
	            @Override  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {  
	            	Map<String, Object> map = (Map<String, Object>) topicListView.getItemAtPosition(arg2);
	            	String top = (String)map.get("topId");
	            	//alert("topId:"+);
	            	/*if(null!=topId&&top.equals(topId)){
	            		arg0.setBackgroundResource(R.drawable.banner);
	            	}*/
	            	Intent intent = new Intent();
					intent.setClass(TopicActivity.this, SubjectActivity.class);
	            	Bundle bundle = new Bundle();
					bundle.putString("topId",top);
					intent.putExtras(bundle);
					startActivity(intent);
	            }  
	        }); 
			//topicListView.setAdapter(arrayAdapter);
			
		}catch(Exception e){
			Log.e(TAG, e.getMessage(), e);
			Intent intent = new Intent();
			intent.setClass(TopicActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
		 
		
	}
	private List<Map<String, Object>> getTopicList(){
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		Map<String, Object> row = null;
		int k=1;
		List<Map<String,Object>> topicList = ExamDataUtil.getTopicList();
		for(Map<String,Object> topic:topicList){
			row  = new HashMap<String, Object>();
			row.put("ItemTitle", k+"."+ topic.get("topName"));//+"("+topic.get("subjectSize")+")"
			row.put("topId",topic.get("topId"));
			listItem.add(row);
			/*if(null!=topId&&topId.equals(topic.get("topId"))){
				Log.i(TAG, "selected:"+topId+" k:"+k);
				topicListView.setSelection(1);
			}*/
			k++;
		}
		return listItem;
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("1", 2);
		intent.putExtras(bundle);
		intent.setClass(TopicActivity.this, MainActivity.class);
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
