package com.jiaogui.androidexam.activity;

import java.util.List;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.tools.ResultAdapter;
import com.jiaogui.androidxam.serivce.DBcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ResultTableAvtivity extends Activity {
	private Button table_back;
	private ListView result_list;
	private List resultList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_table);
		MobclickAgent.onEvent(this, "s05");
		init();
		showtable();
		
		table_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("1", 2);
				intent.putExtras(bundle);
				intent.setClass(ResultTableAvtivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}

	

	private void init() {
		table_back = (Button) findViewById(R.id.table_back);
		result_list = (ListView) findViewById(R.id.result_list);
		resultList = DBcontrol.getResult(this);
	}
	
	private void showtable() {
		
		ResultAdapter adapter = new ResultAdapter(this,resultList);
		result_list.setAdapter(adapter);
		
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
