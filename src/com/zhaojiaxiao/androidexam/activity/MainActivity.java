package com.jiaogui.androidexam.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.Exam;
import com.jiaogui.androidexam.data.ExamDataUtil;
import com.jiaogui.androidexam.data.Result;
import com.jiaogui.androidexam.serivce.ImageService;
import com.jiaogui.androidexam.ui.MD5;
import com.jiaogui.androidxam.serivce.DBcontrol;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Button studyButton;
	private Button realButton;
	private Button myErrorButton;
	private Button historyButton;
	private Button moreButton;
	private Button shareButton;
	private Button aboutButton;
	private Button main_setting;
	private Button main_fankui;
	private List<Exam> examList;
	private List<Result> resultList;
	private ImageView poster;
	private Bitmap bit;

	private String httpurl;
	private String url;
	private String image;
	private byte[] data;
	private Handler handler2;

	private int num;
	private int num2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		MobclickAgent.onEvent(this, "s01");
		Bundle bundle = this.getIntent().getExtras();

		int i = bundle.getInt("1");
		if(ExamDataUtil.getTopicList()==null){
			
			ExamDataUtil.load(getResources());
		}
		
		if (i==1) {
			Timer timer = new Timer();
			poster = (ImageView) findViewById(R.id.poster1);
			timer.schedule(task, 6000*2);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						
						String x = readJsonList("http://www./mobileData/advertisingInfo");
						JSONObject jsonObject = new JSONObject(x);
						image = jsonObject.getString("image");
						url = jsonObject.getString("url");
						data = ImageService.getImage("http://www./images/tuiguangtu.jpg");
						handler2.sendMessage(handler2.obtainMessage(22, url));
						handler2.sendMessage(handler2.obtainMessage(33, data));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();

			
			handler2 = new Handler() {
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 22:
						httpurl = url.toString();
						break;
					case 33:
						bit = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						poster.setImageBitmap(bit);
						break;
					}
					super.handleMessage(msg);
				}
			};
			initButton();
			onClick();
			try {
			   poster.setOnClickListener(new OnClickListener() {
				    @Override
				   public void onClick(View v) {
					Intent intent = new Intent();
					  intent.setAction("android.intent.action.VIEW");    
		              Uri content_url = Uri.parse(httpurl);   
		              intent.setData(content_url);           
		              intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");   
		              startActivity(intent);
				   }
			   });
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, WelcomeActivity.class);
				startActivity(intent);
			}
		} else if(i==2){
			initButton();
			onClick();
		}


		
	}

	private void initButton() {
		studyButton = (Button) findViewById(R.id.main_chapter);
		realButton = (Button) findViewById(R.id.main_real);
		myErrorButton = (Button) findViewById(R.id.main_error1);
		historyButton = (Button) findViewById(R.id.main_history1);
		moreButton = (Button) findViewById(R.id.main_more);

		// shareButton = (Button) findViewById(R.id.radio_share);
		// aboutButton = (Button) findViewById(R.id.radio_about);
		// main_setting = (Button) findViewById(R.id.main_setting);
		// main_fankui = (Button) findViewById(R.id.main_fankui);

		// imageView2 = (TextView) findViewById(R.id.imageView2);

		examList = DBcontrol.getErrorAll(this);
		resultList = DBcontrol.getResult(this);
		num2 = resultList.size();
		num = examList.size();

	}

	public static String readJsonList(String url) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		String key = getKey();
		request.setHeader("key", key);
		String response = httpclient.execute(request,
				new BasicResponseHandler());
		return response;
	}

	private static String getKey() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String time = formatter.format(new Date());
		String key = MD5.getMD5(time + "zhaojiaxiaomeiyoumima");
		return key;
	}

	private void onClick() {
		studyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, TopicActivity.class);
				startActivity(intent);
			}
		});
		realButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ExamActivity.class);
				startActivity(intent);
			}
		});

		myErrorButton.setOnClickListener(new OnClickListener() {

			// Exam exam = DBcontrol.getErrorAll(MainActivity.this);
			@Override
			public void onClick(View v) {

				if (num == 0) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, NoErrorActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, MainErrorActivity.class);
					startActivity(intent);
				}
			}
		});

		historyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (num2 == 0) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, NoExamActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this,
							ResultTableAvtivity.class);
					startActivity(intent);
				}
			}
		});
		moreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MoreActivity.class);
				startActivity(intent);
			}
		});


	}

	private void shareList() {
		final String[] items = getResources()
				.getStringArray(R.array.share_item);
		new AlertDialog.Builder(this)
				.setTitle("请选择要分享的网站：")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// alert("select : "+items[which]);
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, ShareActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("type", 2);
						bundle.putInt("item", which);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	@Override
	public void onBackPressed() {
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		home.addCategory(Intent.CATEGORY_HOME);
		startActivity(home);
		return;
	}

	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				poster.setVisibility(View.GONE);
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			overridePendingTransition(android.R.anim.fade_out,
					android.R.anim.fade_in);
			dialog();
			return false;
		}
		return false;
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				int currentVersion = android.os.Build.VERSION.SDK_INT;
				if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(startMain);
					System.exit(0);
				} else {
					ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					am.restartPackage(getPackageName());
				}

			}

		});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
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
