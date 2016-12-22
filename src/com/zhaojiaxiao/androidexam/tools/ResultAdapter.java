package com.jiaogui.androidexam.tools;

import java.util.List;

import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.Result;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResultAdapter extends BaseAdapter {

	private List<?> resultList;
	private LayoutInflater layoutinflater;
	public ResultAdapter(Context context,List<?> resultList) {

		this.resultList = resultList;
		layoutinflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return resultList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return resultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		
			convertView = layoutinflater.inflate(R.layout.result_item,null);
			TextView marks = (TextView)convertView.findViewById(R.id.marks);
			TextView time = (TextView)convertView.findViewById(R.id.time);
			TextView date = (TextView)convertView.findViewById(R.id.date);
			Result result = (Result) resultList.get(position);
			Log.d("分数", result.time);
			if(result.marks>90){
				marks.setTextColor(android.graphics.Color.GREEN);
				marks.setText((result.marks).toString()+"分");
			}
			marks.setTextColor(android.graphics.Color.RED);
			marks.setText((result.marks).toString()+"分");
			
			time.setText(result.time);
			date.setText(result.date);
		return convertView;
	}

}
