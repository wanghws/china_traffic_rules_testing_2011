package com.jiaogui.androidexam.activity;

import java.util.List;
import java.util.Map;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.Exam;
import com.jiaogui.androidexam.data.ExamDataUtil;
import com.jiaogui.androidexam.ui.ExamProgressBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

//小结测试错题
public class SubjectErrorActivity extends Activity implements OnGestureListener {
	private static final String TAG = "SubjectErrorActivity";

	// ui
	// private ListView ansertListView;
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
	private TextView subjectTitleTextiew;
	private ScrollView contentScrollView;

	// exam static
	private int index = 0;
	private int size = 0;
	private String subId;
	private String topId;
	private List<Exam> examList;
	private String subjectTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.subject_error);
		MobclickAgent.onEvent(this, "s02_6");

		try {
			init();
			onclick();
			showExam(getExam(index));
			examProgressBar.incrementProgressBy(1);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			Intent intent = new Intent();
			intent.setClass(SubjectErrorActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}

	}

	private void init() throws Exception {
		// ansertListView = (ListView)findViewById(R.id.answerListView);
		linearLayout = (LinearLayout) findViewById(R.id.answerListView);
		backButton = (Button) findViewById(R.id.exam_back);
		restartButton = (Button) findViewById(R.id.exam_restart);
		examSuggestTextView = (TextView) findViewById(R.id.exam_suggest);
		examQuestionTextView = (TextView) findViewById(R.id.exam_question);
		examImageView = (ImageView) findViewById(R.id.exam_image);
		examProgressBar = (ExamProgressBar) findViewById(R.id.exam_progress);
		viewFlipper = (ViewFlipper) findViewById(R.id.examViewFlipper);
		detector = new GestureDetector(this);
		// subjectTitleTextiew = (TextView)findViewById(R.id.subject_title);
		contentScrollView = (ScrollView) findViewById(R.id.content_scroll_view);

		//
		Bundle bundle = this.getIntent().getExtras();
		subId = bundle.getString("subId");
		topId = bundle.getString("topId");
		// topId = (String)bundle.get("topId");
		index = 0;
		Map<String, Object> map = ExamDataUtil.getSubjectExamList(subId);
		examList = ExamDataUtil.getExamErrorList();
		subjectTitle = (String) map.get("subName");
		// subjectTitleTextiew.setText(subId+" "+subjectTitle);
		size = examList.size();

		examProgressBar.setMax(size);
		setProgress(examProgressBar.getProgress() * size);
		setSecondaryProgress(examProgressBar.getSecondaryProgress() * size);
	}

	private void onclick() {
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("subId", subId);
				bundle.putString("topId", topId);
				intent.putExtras(bundle);
				intent.setClass(SubjectErrorActivity.this,
						SubjectActivity.class);
				startActivity(intent);
			}
		});
		restartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("subId", subId);
				bundle.putString("topId", topId);
				intent.putExtras(bundle);
				intent.setClass(SubjectErrorActivity.this,
						SubjectExamActivity.class);
				startActivity(intent);
			}
		});
	}

	private Exam getExam(int index) {
		return examList.get(index);
	}

	private void showExam(Exam exam) {
		String question = "";
		if (exam.getType().intValue() == 1) {
			question = exam.getQuestion().substring(0,
					exam.getQuestion().indexOf("<BR>"));
		} else {
			question = exam.getQuestion();
		}
		examQuestionTextView.setText(subId+" "+subjectTitle + "\n" +(index + 1) + "." + question);
		if (exam.getImg().equals("")) {
			examImageView.setVisibility(View.GONE);
		} else {
			examImageView.setVisibility(View.VISIBLE);
			examImageView.setImageBitmap(ExamDataUtil.getImage(getResources(),
					exam.getId()));
		}
		examSuggestTextView.setText(exam.getAnalyse());

		showAnswerList(exam);

	}

	private void showAnswerList(Exam exam) {
		inflater = LayoutInflater.from(this);
		TextView answerText = null;
		// TextView tipText = null;
		ImageView goodimage = null;
		ImageView select_button = null;
		// ImageView lineImage = null;
		linearLayout.removeAllViews();
		View view = null;
		String userAnswer = getMyErrorExam(exam);
		if (exam.getType().intValue() == 1) {// 选择
			String question = exam.getQuestion().substring(
					exam.getQuestion().indexOf("<BR>"),
					exam.getQuestion().length());
			String[] answerArray = question.split("<BR>");
			String answer = "";
			for (int i = 1; i < answerArray.length; i++) {
				view = inflater.inflate(R.layout.show_answer, null);
				answerText = (TextView) view.findViewById(R.id.answerTitle);
				select_button = (ImageView) view
						.findViewById(R.id.select_button);

				// tipText = (TextView)view.findViewById(R.id.answerTip);
				// goodimage =
				// (ImageView)view.findViewById(R.id.answerItemImage);
				// lineImage = (ImageView)view.findViewById(R.id.line_image);
				answer = answerArray[i].trim().substring(0, 1);
				answerText.setText(answerArray[i]);
				// tipText.setText("");
				// goodimage.setImageResource(R.drawable.null_icon);
				select_button.setImageResource(R.drawable.select_off);
				if (answer.equals(exam.getAnswer())) {
					select_button.setImageResource(R.drawable.select_right);
					// goodimage.setImageResource(R.drawable.yes);
					// tipText.setText("正确答案");
				} else if (answer.equals(userAnswer)
						&& !userAnswer.equals(exam.getAnswer())) {
					select_button.setImageResource(R.drawable.select_worng);
					// goodimage.setImageResource(R.drawable.no);
					// tipText.setText("我的答案");
				} else if (i == (answerArray.length - 1)) {
					// lineImage.setVisibility(View.GONE);
					select_button.setImageResource(R.drawable.select_off);
				}
				linearLayout.addView(view);
			}
		} else {
			view = inflater.inflate(R.layout.show_answer, null);
			answerText = (TextView) view.findViewById(R.id.answerTitle);
			select_button = (ImageView) view.findViewById(R.id.select_button);
			// tipText = (TextView)view.findViewById(R.id.answerTip);
			// goodimage = (ImageView)view.findViewById(R.id.answerItemImage);
			answerText.setText("对");
			// tipText.setText("");
			select_button.setImageResource(R.drawable.select_off);
			if ("对".equals(exam.getAnswer())) {
				select_button.setImageResource(R.drawable.select_right);
				// tipText.setText("正确答案");
			}
			if ("对".equals(userAnswer) && !userAnswer.equals(exam.getAnswer())) {
				select_button.setImageResource(R.drawable.select_worng);
				// tipText.setText("我的答案");
			}
			linearLayout.addView(view);

			view = inflater.inflate(R.layout.show_answer, null);
			answerText = (TextView) view.findViewById(R.id.answerTitle);
			select_button = (ImageView) view.findViewById(R.id.select_button);
			// tipText = (TextView)view.findViewById(R.id.answerTip);
			// goodimage = (ImageView)view.findViewById(R.id.answerItemImage);
			// lineImage = (ImageView)view.findViewById(R.id.line_image);
			answerText.setText("错");
			// tipText.setText("");
			// goodimage.setImageResource(R.drawable.null_icon);
			select_button.setImageResource(R.drawable.select_off);
			if ("错".equals(exam.getAnswer())) {
				select_button.setImageResource(R.drawable.select_right);
				// goodimage.setImageResource(R.drawable.yes);
				// tipText.setText("正确答案");
			}
			if ("错".equals(userAnswer) && !userAnswer.equals(exam.getAnswer())) {
				// goodimage.setImageResource(R.drawable.no);
				select_button.setImageResource(R.drawable.select_worng);
				// tipText.setText("我的答案");
			}
			// lineImage.setVisibility(View.GONE);
			linearLayout.addView(view);
		}
	}

	/*
	 * private List<Map<String, Object>> getAnswerList(Exam exam){
	 * List<Map<String, Object>> listItem = new ArrayList<Map<String,
	 * Object>>(); Map<String, Object> row = null; String answer = ""; String
	 * userAnswer = getMyErrorExam(exam); if(exam.getType().intValue()==1){//选择
	 * String question =
	 * exam.getQuestion().substring(exam.getQuestion().indexOf("<BR>"),
	 * exam.getQuestion().length()); String[] answerArray =
	 * question.split("<BR>"); for(int i=1;i<answerArray.length;i++){ row = new
	 * HashMap<String, Object>(); answer = answerArray[i].trim().substring(0,
	 * 1); row.put("answerTitle",answerArray[i]); row.put("answer",answer);
	 * row.put("answerItemImage",R.drawable.null_icon);
	 * if(answer.equals(exam.getAnswer())){
	 * row.put("answerItemImage",R.drawable.yes); row.put("answerTip","正确答案"); }
	 * if(answer.equals(userAnswer)&&!userAnswer.equals(exam.getAnswer())){
	 * row.put("answerItemImage",R.drawable.no); row.put("answerTip","我的答案"); }
	 * 
	 * listItem.add(row); } }else{ row = new HashMap<String, Object>();
	 * row.put("answerTitle", "对"); row.put("answer","对");
	 * row.put("answerItemImage",R.drawable.null_icon);
	 * if("对".equals(exam.getAnswer())){
	 * row.put("answerItemImage",R.drawable.yes); row.put("answerTip","正确答案"); }
	 * if("对".equals(userAnswer)&&!userAnswer.equals(exam.getAnswer())){
	 * row.put("answerItemImage",R.drawable.no); row.put("answerTip","我的答案"); }
	 * listItem.add(row); row = new HashMap<String, Object>();
	 * row.put("answerTitle", "错"); row.put("answer","错");
	 * row.put("answerItemImage",R.drawable.null_icon);
	 * if("错".equals(exam.getAnswer())){
	 * row.put("answerItemImage",R.drawable.yes); row.put("answerTip","正确答案"); }
	 * if("错".equals(userAnswer)&&!userAnswer.equals(exam.getAnswer())){
	 * row.put("answerItemImage",R.drawable.no); row.put("answerTip","我的答案"); }
	 * listItem.add(row); } return listItem; }
	 */
	private String getMyErrorExam(Exam exam) {
		String answer = ExamDataUtil.getErrorAnswer(exam.getId());
		if (null == answer)
			return "";
		return answer;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { Log.v(TAG,
	 * "touched"); return this.detector.onTouchEvent(event); }
	 */

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		try {
			// TODO Auto-generated method stub
			if (e1.getX() - e2.getX() > 50) { //

				// alert("right");
				if (index + 1 >= size) {
					alert("已经是最后一道题");
					return false;
				}
				index++;
				showExam(getExam(index));
				examProgressBar.incrementProgressBy(1);
			} else if (e1.getX() - e2.getX() < -50) {

				// alert("left");
				if (index - 1 < 0) {
					alert("已经是第一道题");
					return false;
				}
				index--;
				showExam(getExam(index));
				examProgressBar.incrementProgressBy(-1);
			}
			return false;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return false;
		}
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

	private void alert(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
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
		Bundle bundle = new Bundle();
		bundle.putString("subId", subId);
		bundle.putString("topId", topId);
		intent.putExtras(bundle);
		intent.setClass(SubjectErrorActivity.this, SubjectActivity.class);
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
