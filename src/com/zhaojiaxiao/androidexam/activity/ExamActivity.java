package com.jiaogui.androidexam.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.Exam;
import com.jiaogui.androidexam.data.ExamDataUtil;
import com.jiaogui.androidexam.data.Result;
import com.jiaogui.androidexam.ui.ExamProgressBar;
import com.jiaogui.androidxam.serivce.DBcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewFlipper;

public class ExamActivity extends Activity implements OnGestureListener {
	private static final String TAG = "ExamActivity";
	private SimpleDateFormat format = new SimpleDateFormat("mm:ss");

	private ListView ansertListView;

	private Button backButton;
	private Button submitButton;
	private TextView examTimeTextView;
	private TextView examQuestionTextView;
	private ImageView examImageView;
	private ExamProgressBar examProgressBar;
	private ExamTime examTime;
	private GestureDetector detector;
	private ViewFlipper viewFlipper;

	// exam static
	private List<Exam> examList;
	private Map<Long, Exam> selectExamMap;
	private int index = 0;
	private int status = 0;
	private int select_count = 1;
	private int user_time = 0;
	private int grade = 0;
	private int size = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.exam);
        MobclickAgent.onEvent(this, "s03_1");
		
		try {
			init();
			onclick();
			showExam(getExam(index));
			examProgressBar.incrementProgressBy(1);
			alert("向左滑动进入下一题");
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			Intent intent = new Intent();
			intent.setClass(ExamActivity.this, WelcomeActivity.class);
			startActivity(intent);

		}
	}
	private void init() throws Exception {
		examList = ExamDataUtil.randomExam();
		ansertListView = (ListView) findViewById(R.id.answerListView);
		backButton = (Button) findViewById(R.id.exam_back);
		submitButton = (Button) findViewById(R.id.exam_submit);
		examTimeTextView = (TextView) findViewById(R.id.exam_time);
		examQuestionTextView = (TextView) findViewById(R.id.exam_question);
		examImageView = (ImageView) findViewById(R.id.exam_image);
		examProgressBar = (ExamProgressBar) findViewById(R.id.exam_progress);
		viewFlipper = (ViewFlipper) findViewById(R.id.examViewFlipper);
		detector = new GestureDetector(this);

		//
		index = 0;
		status = 0;
		examTimeTextView.setText("45:00");
		ExamDataUtil.initExamErrorList();
		ExamDataUtil.initErrorAnswerList();
		selectExamMap = new HashMap<Long, Exam>();
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
				bundle.putInt("1", 2);
				intent.putExtras(bundle);
				intent.setClass(ExamActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (status == 0) {
//					examTimeTextView.setVisibility(View.VISIBLE);
					examTime = new ExamTime(1000 * 60 * 45, 1000);
					examTime.start();
					submitButton.setText("交卷");
					status = 1;
				} else if (status == 1) {
					if (select_count < size) {
						alert("提示", "您还有未做完的题目，是否提交考卷？");
						return;
					}
					examTime.cancel();
					status = 0;
					// submit
					submitExam();

				}
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
		examQuestionTextView.setText(index + 1 + "." + question);
		if (exam.getImg().equals("")) {
			examImageView.setVisibility(View.GONE);
		} else {
			examImageView.setVisibility(View.VISIBLE);
			examImageView.setImageBitmap(ExamDataUtil.getImage(getResources(),
					exam.getId()));
		}
		ansertListView.setAdapter(new SimpleAdapter(this, getAnswerList(exam),
				R.layout.answer_list, new String[] { "answerTitle",
						"answerItemImage" }, new int[] { R.id.answerTitle,
						R.id.answerItemImage }));
		ansertListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				selectListView(view, position);
				if (status == 0) {
//					examTimeTextView.setVisibility(View.VISIBLE);
					examTime = new ExamTime(1000 * 60 * 45, 1000);
					examTime.start();
					submitButton.setText("交卷");
					status = 1;
				}
				Map<String, Object> map = (Map<String, Object>) ansertListView
						.getItemAtPosition(position);
				String answer = (String) map.get("answer");
				// alert("answer:"+answer);

				Exam exam = getExam(index);

				if (exam.getAnswer().equals(answer)) {// 答对
					// Log.i(TAG,
					// "selectExamMap.get(exam.getId()):"+selectExamMap.toString());
					if (null == selectExamMap.get(exam.getId())
							|| !answer.equals(selectExamMap.get(exam.getId())
									.getUserAnswer())) {
						grade++;
					}

					ExamDataUtil.removeExamErrorList(exam);
					ExamDataUtil.removeErrorAnswerList(exam.getId());
				} else {// 打错
					if (null != selectExamMap.get(exam.getId())
							&& !answer.equals(selectExamMap.get(exam.getId())
									.getUserAnswer())) {
						grade--;
					}

					ExamDataUtil.addExamErrorList(exam);
					System.out.println("存答错到集合" + exam.getId());
					ExamDataUtil.addErrorAnswerList(exam.getId(), answer);

				}
				exam.setUserAnswer(answer);

				selectExamMap.put(exam.getId(), exam);
				if (index + 1 >= examList.size()) {
					// alert("已经是最后一道题");
					alert("提示", "已经是最后一道题目，是否提交考卷？");
					return;
				}

				Context ctx = ExamActivity.this;
				SharedPreferences sp = ctx.getSharedPreferences("SP",
						MODE_PRIVATE);
				int bool = sp.getInt("Condition", 0);
				if (bool == 0) {
					index++;
					showExam(getExam(index));
					examProgressBar.incrementProgressBy(1);
					select_count++;
				}

			}
		});
	}

	// 遍历并选择
	private void selectListView(View selectView, int position) {
		View view = null;
		ImageView img = null;
		// ListView listView = (ListView)findViewById(R.id.answerListView);
		for (int i = 0; i < ansertListView.getChildCount(); i++) {
			view = ansertListView.getChildAt(i);
			img = (ImageView) view.findViewById(R.id.answerItemImage);
			img.setImageResource(R.drawable.select_off);
		}
		img = (ImageView) selectView.findViewById(R.id.answerItemImage);
		img.setImageResource(R.drawable.select_on);
	}

	private void submitExam() {
		// 检查未答题目
		try {
//			new Thread(new Runnable() {
//
//				@Override
//				public synchronized void run() {
					for (Exam exam : examList) {
						// 未答的题
						if (null == selectExamMap.get(exam.getId())) {
							Log.d("----123",
									exam.getId() + "----" + exam.getAnswer()
											+ "----" + exam.getUserAnswer());
							// if (exam.getUserAnswer() == null) {

							DBcontrol.insertError(exam, ExamActivity.this);
							ExamDataUtil.addExamErrorList(exam);
							// }

						} else {
							// 答的题
							if (exam.getAnswer() != null
									&& exam.getUserAnswer() != null
									// 答错的
									&& !exam.getAnswer().equals(
											exam.getUserAnswer())) {
								Log.d("--不等于空",
										exam.getId() + "---"
												+ exam.getUserAnswer());
								DBcontrol.insertError(exam, ExamActivity.this);
							}
						}
					}

//				}
//
//			}).start();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Log.i(TAG,"RandomExamErrorList size:"+ExamDataUtil.getExamErrorList().size());

		Intent intent = new Intent();
		intent.setClass(ExamActivity.this, ResultActivity.class);
		Bundle bundle = new Bundle();
		Result result = new Result();

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format = new SimpleDateFormat("mm分 ss秒");
		String useTime = format.format(new Date(user_time * 1000));

		result.time = useTime;
		result.marks = grade;
		result.date = formatter.format(date);
		DBcontrol.insertResult(result, ExamActivity.this);
		bundle.putInt("useTime", user_time);
		bundle.putInt("grade", grade);

		intent.putExtras(bundle);
		startActivity(intent);

	}

	private boolean selectedExam(Exam exam, String answer) {
		if (null != selectExamMap.get(exam.getId())
				&& null != selectExamMap.get(exam.getId()).getUserAnswer()
				&& selectExamMap.get(exam.getId()).getUserAnswer()
						.equals(answer)) {
			return true;
		}
		return false;
	}

	private List<Map<String, Object>> getAnswerList(Exam exam) {
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		Map<String, Object> row = null;
		String answer = "";
		if (exam.getType().intValue() == 1) {// 选择
			String question = exam.getQuestion().substring(
					exam.getQuestion().indexOf("<BR>"),
					exam.getQuestion().length());
			String[] answerArray = question.split("<BR>");
			for (int i = 1; i < answerArray.length; i++) {
				answer = answerArray[i].trim().substring(0, 1);
				row = new HashMap<String, Object>();
				row.put("answerTitle", answerArray[i]);
				row.put("answer", answerArray[i].trim().substring(0, 1));
				row.put("answerItemImage", R.drawable.select_off);
				if (selectedExam(exam, answer)) {
					row.put("answerItemImage", R.drawable.select_on);
				}
				listItem.add(row);
			}
		} else {//判断
			row = new HashMap<String, Object>();
			row.put("answerTitle", "对");
			row.put("answer", "对");
			row.put("answerItemImage", R.drawable.select_off);
			if (selectedExam(exam, "对")) {
				row.put("answerItemImage", R.drawable.select_on);
			}
			listItem.add(row);
			row = new HashMap<String, Object>();
			row.put("answerTitle", "错");
			row.put("answer", "错");
			row.put("answerItemImage", R.drawable.select_off);
			if (selectedExam(exam, "错")) {
				row.put("answerItemImage", R.drawable.select_on);
			}
			listItem.add(row);
		}
		return listItem;
	}

	class ExamTime extends CountDownTimer {

		public ExamTime(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			alertOK("提示", "您的答题时间已到，点击确定查看成绩");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			user_time++;
			examTimeTextView.setText(format
					.format(new Date(millisUntilFinished)));
		}

	}

	private void alert(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	// left right
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Log.v(TAG, "touched");
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > 50) { //

			// alert("right");
			viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			viewFlipper.showNext();
			if (index + 1 >= size) {
				alert("已经是最后一道题");
				return false;
			}
			index++;
			showExam(getExam(index));
			examProgressBar.incrementProgressBy(1);
		} else if (e1.getX() - e2.getX() < -50) {
			// alert("left");
			viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			viewFlipper.showPrevious();

			if (index - 1 < 0) {
				alert("已经是第一道题");
				return false;
			}
			index--;
			showExam(getExam(index));
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
						dialog.dismiss();
						submitExam();

					}
				}).show();

	}

	private void alertOK(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						submitExam();
					}
				}).show();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("1", 2);
		intent.putExtras(bundle);
		intent.setClass(ExamActivity.this, MainActivity.class);
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
