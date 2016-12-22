package com.jiaogui.androidexam.activity;

import java.util.List;
import java.util.Map;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.Exam;
import com.jiaogui.androidexam.data.ExamDataUtil;
import com.jiaogui.androidexam.ui.ExamProgressBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class SubjectStudyActivity extends Activity implements OnGestureListener{
private static final String TAG = "SubjectStudyActivity";
	
	//ui
	//private ListView ansertListView;
	private LinearLayout linearLayout;
	private LayoutInflater inflater;
	
	private Button backButton;
	private Button restartButton;
	private TextView examSuggestTextView;
	private TextView examQuestionTextView;
	private ImageView examImageView;
	private ExamProgressBar examProgressBar;
	private GestureDetector detector;
	private ViewFlipper viewFlipper;
	private ScrollView contentScrollView;
	
	//exam static
	private String subId;
	private String topId;
	private int index = 0;
	private int size = 0;
	private List<Exam> examList;
	private String subjectTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.subject_study);
		MobclickAgent.onEvent(this, "s02_3");
		
		try{
			init();
			onclick();
			showExam(getExam(index));
			examProgressBar.incrementProgressBy(1);
			alert("向左滑动进入下一题");
		}catch(Exception e){
			Log.e(TAG, e.getMessage(),e);
			Intent intent = new Intent();
			intent.setClass(SubjectStudyActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
		
	}
	
	
	private void init()throws Exception{
		//ansertListView = (ListView)findViewById(R.id.answerListView);
		linearLayout = (LinearLayout)findViewById(R.id.answerListView);
		backButton = (Button)findViewById(R.id.exam_back);
		restartButton = (Button)findViewById(R.id.exam_restart);
		examSuggestTextView = (TextView)findViewById(R.id.exam_suggest);
		examQuestionTextView = (TextView)findViewById(R.id.exam_question);
		examImageView = (ImageView)findViewById(R.id.exam_image);
		examProgressBar = (ExamProgressBar)findViewById(R.id.exam_progress);
		viewFlipper = (ViewFlipper) findViewById(R.id.examViewFlipper);  
		detector = new GestureDetector(this);
//		subjectTitleTextiew = (TextView)findViewById(R.id.subject_title);
		contentScrollView = (ScrollView)findViewById(R.id.content_scroll_view);
        
        
        //
        Bundle bundle = this.getIntent().getExtras();
        subId = bundle.getString("subId");
        index = 0;
        Map<String,Object> map = ExamDataUtil.getSubjectExamList(subId);
        examList = (List<Exam>)map.get("examList");
        subjectTitle = (String)map.get("subName");
        topId = (String)map.get("topId");
        size = examList.size();
//        subjectTitleTextiew.setText(subId+" "+subjectTitle);
        Log.i(TAG, "size:"+size);
        examProgressBar.setMax(size);
        setProgress(examProgressBar.getProgress() * size);
        setSecondaryProgress(examProgressBar.getSecondaryProgress() * size);

	}
	private void onclick(){
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SubjectStudyActivity.this, SubjectActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("subId",subId);
				bundle.putString("topId",topId);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		restartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SubjectStudyActivity.this, SubjectExamActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("subId",subId);
				bundle.putString("topId",topId);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	private Exam getExam(int index){
		Log.i(TAG, "index:"+index);
		return examList.get(index);
	}
	private void showExam(Exam exam){
		String question = "";
		if(exam.getType().intValue()==1){
			question = exam.getQuestion().substring(0, exam.getQuestion().indexOf("<BR>"));
		}else{
			question = exam.getQuestion();
		}
		examQuestionTextView.setText(subId+" "+subjectTitle + "\n" +(index+1)+"."+question);
		if(exam.getImg().equals("")){
			examImageView.setVisibility(View.GONE);
		}else{
			examImageView.setVisibility(View.VISIBLE);
			examImageView.setImageBitmap(ExamDataUtil.getImage(getResources(), exam.getId()));
		}
		examSuggestTextView.setText(exam.getAnalyse());
		
		showAnswerList(exam);
		
	}
	private void showAnswerList(Exam exam){
		inflater = LayoutInflater.from(this);
		TextView answerText = null;
//		TextView tipText = null;
//		ImageView goodimage = null;
//		ImageView lineImage = null;
		ImageView select_button = null;
		linearLayout.removeAllViews();
		View view  = null;
		//String userAnswer = getMyErrorExam(exam);
		if(exam.getType().intValue()==1){//选择
			String question = exam.getQuestion().substring(exam.getQuestion().indexOf("<BR>"), exam.getQuestion().length());
			String[] answerArray = question.split("<BR>");
			String answer = "";
			for(int i=1;i<answerArray.length;i++){
				view = inflater.inflate(R.layout.show_answer,null);
				answerText = (TextView)view.findViewById(R.id.answerTitle);
//				tipText = (TextView)view.findViewById(R.id.answerTip);
//				goodimage = (ImageView)view.findViewById(R.id.answerItemImage);
//				lineImage = (ImageView)view.findViewById(R.id.line_image);
				select_button = (ImageView)view.findViewById(R.id.select_button);
				answer = answerArray[i].trim().substring(0, 1);
				
				answerText.setText(answerArray[i]);
				if(answer.equals(exam.getAnswer())){
//					goodimage.setVisibility(View.VISIBLE);
//					goodimage.setImageResource(R.drawable.yes);
//					tipText.setText("正确答案");
//					tipText.setVisibility(View.VISIBLE);
					select_button.setImageResource(R.drawable.select_right);
				}else{
					select_button.setImageResource(R.drawable.select_off);
//					goodimage.setVisibility(View.GONE);
//					tipText.setText("");
//					tipText.setVisibility(View.GONE);
				}
				if(i==(answerArray.length-1)){
//					lineImage.setVisibility(View.GONE);
				}
				linearLayout.addView(view);
			}
		}else{
			view = inflater.inflate(R.layout.show_answer,null);
			answerText = (TextView)view.findViewById(R.id.answerTitle);
//			tipText = (TextView)view.findViewById(R.id.answerTip);
//			goodimage = (ImageView)view.findViewById(R.id.answerItemImage);
			select_button = (ImageView)view.findViewById(R.id.select_button);
			answerText.setText("对");
			if("对".equals(exam.getAnswer())){
				select_button.setImageResource(R.drawable.select_right);
//				goodimage.setVisibility(View.VISIBLE);
//				goodimage.setImageResource(R.drawable.yes);
//				tipText.setText("正确答案");
//				tipText.setVisibility(View.VISIBLE);
			}else{
				select_button.setImageResource(R.drawable.select_off);
//				goodimage.setVisibility(View.GONE);
//				tipText.setText("");
//				tipText.setVisibility(View.GONE);
			}
			linearLayout.addView(view);
			
			
			view = inflater.inflate(R.layout.show_answer,null);
			answerText = (TextView)view.findViewById(R.id.answerTitle);
//			tipText = (TextView)view.findViewById(R.id.answerTip);
//			goodimage = (ImageView)view.findViewById(R.id.answerItemImage);
//			lineImage = (ImageView)view.findViewById(R.id.line_image);
			select_button = (ImageView)view.findViewById(R.id.select_button);
			answerText.setText("错");
			if("错".equals(exam.getAnswer())){
				select_button.setImageResource(R.drawable.select_right);
//				goodimage.setVisibility(View.VISIBLE);
//				goodimage.setImageResource(R.drawable.yes);
//				tipText.setText("正确答案");
//				tipText.setVisibility(View.VISIBLE);
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

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	/*@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        Log.v(TAG, "touched");  
        return this.detector.onTouchEvent(event);  
    }*/

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		//Log.i(TAG, "e1.x:"+e1.getX()+" e2.x:"+e2.getX()+" e1.y:"+e1.getY()+" e2.y:"+e2.getY());
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX()>50) {  //
			
			//alert("right");
			if(index+1>=size){
				alert("提示:","你已到了最后一道题目，是否进入小节模拟测试?");
				return false;
			}
			index++;
            showExam(getExam(index));
			examProgressBar.incrementProgressBy(1);
        } else if (e1.getX() - e2.getX()<-50) {  
        	
			//alert("left");
        	if(index-1<0){
        		alert("已经是第一道题");
				return false;
        	}
        	index--;
            showExam(getExam(index));
            examProgressBar.incrementProgressBy(-1);
        }else {  
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
	private void alert(String message){
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	private void alert(String title,String message){
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
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
				intent.setClass(SubjectStudyActivity.this, SubjectExamActivity.class);
				Bundle bundle = new Bundle();
		    	bundle.putString("subId", subId);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		})
		.show();
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
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(SubjectStudyActivity.this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("subId",subId);
		bundle.putString("topId",topId);
		bundle.putInt("1", 2);
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
