package com.jiaogui.androidexam.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.tools.JacksonUtil;

/** 
 * @Title: RecommendActivity.java 
 * @author fusz
 * @date   2012-9-12
 * @version
 */
public class RecommendActivity extends Activity {
	ListView listView;
	RecommendAdapter recommendAdapter;
	List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
	Button btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recommend_list);
		listView=(ListView)findViewById(R.id.recommend_list);
		btn_back=(Button)findViewById(R.id.btn_back);
		recommendAdapter=new RecommendAdapter(this, data);
		listView.setAdapter(recommendAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Map<String,Object> map=data.get(arg2);
				String url=(String)map.get("url");
				Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			    it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			    startActivity(it);
			}
		});
		btn_back.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		new AsyncTask<Object, Void, List<Map<String,Object>>>() {
			@Override
			protected List<Map<String,Object>> doInBackground(Object... params) {
				try{
					return JacksonUtil.readJson2List("http://www./android.txt");
				}catch (Exception e) {
					return null;
				}
			}
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
			@Override
			protected void onPostExecute(List<Map<String,Object>> result) {
				if(result!=null)
				data.addAll(result);
				recommendAdapter.notifyDataSetChanged();
				super.onPostExecute(result);
			}
		}.execute();
	}
	
	class RecommendAdapter extends BaseAdapter {
		private Map<Integer, View> viewMap = new HashMap<Integer, View>();
		private Context context;
		private List<Map<String,Object>> data;
	    public RecommendAdapter(Context context,List<Map<String,Object>> list) {
	    	super();
	        this.context=context;
	        data=list;
	    }
		public int getCount() {
			return data==null?0:data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	ViewHolder holder;
	    	View rowView = this.viewMap.get(position);
	    	if(rowView==null){
	    		rowView = LayoutInflater.from(context).inflate(R.layout.recommend_item, null);
		    	holder=new ViewHolder();
		    	holder.title=(TextView)rowView.findViewById(R.id.tab_line_item_name);
		    	holder.description=(TextView)rowView.findViewById(R.id.tab_line_item_city);
		    	holder.logo=(ImageView)rowView.findViewById(R.id.tab_line_item_icon);
		    	rowView.setTag(holder);
		    	final Map<String,Object> map=data.get(position);
		    	holder.title.setText((String)map.get("title"));
		    	holder.description.setText((String)map.get("description"));
	    		if(!holder.logo.isDrawingCacheEnabled()){
	    			new LogoTask().execute(holder.logo,(String)map.get("logo"));
	    			holder.logo.setDrawingCacheEnabled(true);
	    		}
	    		viewMap.put(position, rowView);
	    	}else{
	    		holder=(ViewHolder)rowView.getTag();
	    	}
	        return rowView;
	    }
	    class ViewHolder{
			public TextView title;
			public TextView description;
			public ImageView logo;
	    }
	}
	class LogoTask extends AsyncTask<Object, Object, BitmapDrawable>{
    	ImageView imageView;
		@Override
		protected BitmapDrawable doInBackground(Object... params) {
			BitmapDrawable bd=null;
			imageView=(ImageView)params[0];
			String url=(String) params[1];
			try{
				bd=(BitmapDrawable)Drawable.createFromStream((new URL(url)).openStream(),"tmp.jpg");
	    	}catch (Exception e) {
	    		e.printStackTrace();
	    	}
			return bd;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(BitmapDrawable result) {
			if(result!=null){
				imageView.setImageBitmap(result.getBitmap());
			}
			super.onPostExecute(result);
		}
 }

}
