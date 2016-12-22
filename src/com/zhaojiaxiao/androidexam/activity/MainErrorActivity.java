package com.jiaogui.androidexam.activity;

import java.util.List;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.Exam;
import com.jiaogui.androidexam.data.ExamDataUtil;
import com.jiaogui.androidexam.ui.ExamProgressBar;
import com.jiaogui.androidxam.serivce.DBcontrol;
import com.jiaogui.androidxam.serivce.Db;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

//错题解析
public class MainErrorActivity extends Activity implements OnGestureListener {
	private static final String TAG = "MainErrorActivity";

	private Button backButton;
	private Button exam_button_e;
	private GestureDetector detector;
	private int index = 0;
	private int size = 0;
	private List<Exam> examList;
	private ViewFlipper flipper;
	private LayoutInflater inflater;
	private TextView answer_title;
	private ImageView examImageView;
	private ScrollView contentScrollView;
	private ExamProgressBar examProgressBar;
	private TextView subject_title_e;

	private ViewFlipper viewFlipper;

	private TextView examQuestionTextView;
	private TextView examSuggestTextView;
	private LinearLayout linearLayout;
	private TextView errornum_e;
	private Button delete_item;
	private Exam exam;
	private Long errId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.merror_study);
		MobclickAgent.onEvent(this, "s04_1");
		Log.i("输出", "!!!!!!!!!!!!!!!!!!!!!!!!");
		try {
			init();
			showExam(getExam(index));
			onclick();
			examProgressBar.incrementProgressBy(1);

		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			Intent intent = new Intent();
			intent.setClass(MainErrorActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}

	}

	private void init() {

		backButton = (Button) findViewById(R.id.exam_back);
		exam_button_e = (Button) findViewById(R.id.exam_button_e);
		delete_item = (Button) findViewById(R.id.delete_item);
		// flipper = (ViewFlipper) this.findViewById(R.id.mErrorFlipper);
		viewFlipper = (ViewFlipper) findViewById(R.id.examViewFlipper);
		examQuestionTextView = (TextView) findViewById(R.id.exam_question_e);
		examSuggestTextView = (TextView) findViewById(R.id.exam_suggest_e);
		errornum_e = (TextView) findViewById(R.id.errornum_e);
		linearLayout = (LinearLayout) findViewById(R.id.answerListView_e);
		examImageView = (ImageView) findViewById(R.id.exam_image_e);
		contentScrollView = (ScrollView) findViewById(R.id.content_scroll_view_e);
		examProgressBar = (ExamProgressBar) findViewById(R.id.exam_progress_e);

		detector = new GestureDetector(this);
		index = 0;
		examList = DBcontrol.getErrorAll(this);

		int errorNum = examList.get(index).getErrorNum();
		errornum_e.setText("共答错" + errorNum + "次");

		size = examList.size();

		examList.get(index).getSubId();
//		subject_title_e.setText(examList.get(index).getSubId() + " "
//				+ examList.get(index).getSubName());

		examProgressBar.setMax(size);
		setProgress(examProgressBar.getProgress() * size);
		setSecondaryProgress(examProgressBar.getSecondaryProgress() * size);

	}

	private void showExam(Exam exam) {

		String question = "";
		if (exam.getType() == 1) {
			question = exam.getQuestion().substring(0,
					exam.getQuestion().indexOf("<BR>"));
		} else {
			question = exam.getQuestion();
		}
		examQuestionTextView.setText(examList.get(index).getSubId() + " "+ examList.get(index).getSubName() + "\n" + (index + 1) + "." + question);
		if (exam.getImg().equals("")) {
			examImageView.setVisibility(View.GONE);
		} else {
			examImageView.setVisibility(View.VISIBLE);
			examImageView.setImageBitmap(ExamDataUtil.getImage(getResources(),
					exam.getId()));
		}
		examSuggestTextView.setText(exam.getAnalyse());

		// 获得错题次数
		int errornum = exam.getErrorNum();
		errornum_e.setText("共答错" + errornum + "次");
		errId = exam.getId();

		showAnswerList(exam);
	}

	private void showAnswerList(Exam exam) {
		inflater = LayoutInflater.from(this);
		TextView answerText = null;
//		TextView tipText = null;
//		ImageView goodimage = null;
//		ImageView lineImage = null;
		ImageView select_button = null;
		linearLayout.removeAllViews();
		View view = null;
		if (exam.getType().intValue() == 1) {// 选择
			String question = exam.getQuestion().substring(
					exam.getQuestion().indexOf("<BR>"),
					exam.getQuestion().length());
			String[] answerArray = question.split("<BR>");
			String answer = "";
			for (int i = 1; i < answerArray.length; i++) {
				view = inflater.inflate(R.layout.show_answer, null);
				answerText = (TextView) view.findViewById(R.id.answerTitle);
				select_button = (ImageView)view.findViewById(R.id.select_button);
//				tipText = (TextView) view.findViewById(R.id.answerTip_i);
//				goodimage = (ImageView) view
//						.findViewById(R.id.answerItemImage_i);
//				lineImage = (ImageView) view.findViewById(R.id.line_image);
				answer = answerArray[i].trim().substring(0, 1);
				answerText.setText(answerArray[i]);
				if (answer.equals(exam.getAnswer())) {
					
					select_button.setImageResource(R.drawable.select_right);
//					goodimage.setVisibility(View.VISIBLE);
//					goodimage.setImageResource(R.drawable.yes);
//					tipText.setText("正确答案");
//					tipText.setVisibility(View.VISIBLE);
				} else if (answer.equals(exam.getUserAnswer())) {
					select_button.setImageResource(R.drawable.select_worng);
//					goodimage.setVisibility(View.VISIBLE);
//					goodimage.setImageResource(R.drawable.no);
//					tipText.setText("我的答案");
//					tipText.setVisibility(View.VISIBLE);
				} else {
					select_button.setImageResource(R.drawable.select_off);
//					goodimage.setVisibility(View.GONE);
//					tipText.setText("");
//					tipText.setVisibility(View.GONE);
				}
//				if (i == (answerArray.length - 1)) {
//					lineImage.setVisibility(View.GONE);
//					select_button.setImageResource(R.drawable.select_off);
//					
//				}
				linearLayout.addView(view);
			}
		} else {//判断
			view = inflater.inflate(R.layout.show_answer, null);
			answerText = (TextView) view.findViewById(R.id.answerTitle);
			select_button = (ImageView)view.findViewById(R.id.select_button);
//			tipText = (TextView) view.findViewById(R.id.answerTip_i);
//			goodimage = (ImageView) view.findViewById(R.id.answerItemImage_i);
			answerText.setText("对");
			if ("对".equals(exam.getAnswer())) {
				select_button.setImageResource(R.drawable.select_right);
//				goodimage.setVisibility(View.VISIBLE);
//				goodimage.setImageResource(R.drawable.yes);
//				tipText.setText("正确答案");
//				tipText.setVisibility(View.VISIBLE);
			}else if("对".equals(exam.getUserAnswer())){
				select_button.setImageResource(R.drawable.select_worng); 
			}else {
				select_button.setImageResource(R.drawable.select_off);
//				goodimage.setVisibility(View.GONE);
//				tipText.setText("");
//				tipText.setVisibility(View.GONE);
			}
			linearLayout.addView(view);

			view = inflater.inflate(R.layout.show_answer, null);
			answerText = (TextView) view.findViewById(R.id.answerTitle);
//			tipText = (TextView) view.findViewById(R.id.answerTip_i);
//			goodimage = (ImageView) view.findViewById(R.id.answerItemImage_i);
//			lineImage = (ImageView) view.findViewById(R.id.line_image);
			select_button = (ImageView)view.findViewById(R.id.select_button);
			answerText.setText("错");
			if ("错".equals(exam.getAnswer())) {
				select_button.setImageResource(R.drawable.select_right);
//				goodimage.setVisibility(View.VISIBLE);
//				goodimage.setImageResource(R.drawable.yes);
//				tipText.setText("正确答案");
//				tipText.setVisibility(View.VISIBLE);
			} else if("错".equals(exam.getUserAnswer())){
				select_button.setImageResource(R.drawable.select_worng);
			}else{
				select_button.setImageResource(R.drawable.select_off);
//				goodimage.setVisibility(View.GONE);
//				tipText.setText("");
//				tipText.setVisibility(View.GONE);
			}
//			lineImage.setVisibility(View.GONE);
			linearLayout.addView(view);
		}
	}

	private void onclick() {
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("1", 2);
				intent.putExtras(bundle);
				intent.setClass(MainErrorActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		exam_button_e.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainErrorActivity.this, ErrorExamActivity.class);
				startActivity(intent);
			}
		});
		delete_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog();

			}
		});

	}

	// 获取单条数据
	private Exam getExam(int index) {
		Log.i(TAG, "index:" + index);
		return examList.get(index);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		detector.onTouchEvent(ev); // 让GestureDetector响应触碰事件
		super.dispatchTouchEvent(ev); // 让Activity响应触碰事件
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		contentScrollView.onTouchEvent(event); // 让ScrollView响应触碰事件
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 50) { //←

			if (index + 1 >= size) {
				alert("提示:", "你已到了最后一道题目，是否进入错题练习?");
				return false;
			}
			index++;
			showExam(getExam(index));
			examProgressBar.setMax(size);
			examProgressBar.incrementProgressBy(1);
		} else if (e1.getX() - e2.getX() < -50) {  //→

			// alert("left");
			if (index - 1 < 0) {
				alert("已经是第一道题");
				return false;
			}
			index--;
			showExam(getExam(index));
			examProgressBar.setMax(size);
			examProgressBar.incrementProgressBy(-1);
		} else {
			return false;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { // TODO
	 * Auto-generated method stub contentScrollView.onTouchEvent(event); //
	 * 让ScrollView响应触碰事件 return false; }
	 */

	private void alert(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainErrorActivity.this);
		builder.setMessage("确定要删除吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Db db = new Db(MainErrorActivity.this);
				SQLiteDatabase sd = db.getReadableDatabase();
				db.deleteLeafLevelById(sd, errId);
				examList.remove(index);
				size = examList.size();
				sd.close();
				db.close();
				if(size==0){
					Intent intent = new Intent();
					intent.setClass(MainErrorActivity.this,NoErrorActivity.class);
					startActivity(intent);
//					alertshort("我晕，你给删没了！");
				}else if(size==index){
					index = index-1;
					showExam(examList.get(index));
					size = examList.size();
					examProgressBar.setMax(size);
//					index = index-1;
					setProgress(examProgressBar.getProgress() * size);
					setSecondaryProgress(examProgressBar.getSecondaryProgress() * size);
					examProgressBar.incrementProgressBy(0);
				}else if(index==0){
					showExam(examList.get(index));
					size = examList.size();
					examProgressBar.setMax(size);
					setProgress(examProgressBar.getProgress() * size);
					setSecondaryProgress(examProgressBar.getSecondaryProgress() * size);
					examProgressBar.incrementProgressBy(0);
				}else{
//					index= index-1;
					showExam(examList.get(index)); 
					size = examList.size();
					examProgressBar.setMax(size);
					setProgress(examProgressBar.getProgress() * size);
					setSecondaryProgress(examProgressBar.getSecondaryProgress() * (size));
					examProgressBar.incrementProgressBy(0);
				}
				
			}
		});
		
		builder.create().show();
	}

	private void alertshort(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	private void alert(String title, String message) {
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
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(MainErrorActivity.this,
								ErrorExamActivity.class);
						startActivity(intent);
					}
				}).show();
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
