package com.jiaogui.androidexam.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobclick.android.MobclickAgent;
import com.jiaogui.androidexam.R;
import com.jiaogui.androidexam.data.Exam;
import com.jiaogui.androidexam.data.ExamDataUtil;
import com.jiaogui.androidexam.ui.ExamProgressBar;
import com.jiaogui.androidxam.serivce.DBcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

//我的错题-->错题练习
public class ErrorExamActivity extends Activity implements OnGestureListener {
	private static final String TAG = "ErrorExamActivity";

	private ListView ansertListView;
	private Button backButton;
	private Button submitButton;
	private TextView examQuestionTextView;
	private ImageView examImageView;
	private ExamProgressBar examProgressBar;
	private GestureDetector detector;
	private ViewFlipper viewFlipper;

	// exam static
	private List<Exam> examList;
	private int index = 0;
	private int size = 0;
	private int select_count = 1;
	private int grade = 0;
	private String subId;
	private String topId;
	private Map<Long, Exam> selectExamMap;
	private List<Integer> right = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.error_exam);
		MobclickAgent.onEvent(this, "s04_2");

		try {
			init();
			onclick();
			showExam(getExam(index));
			examProgressBar.incrementProgressBy(1);
			alert("向左滑动进入下一题");
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			Intent intent = new Intent();
			intent.setClass(ErrorExamActivity.this, WelcomeActivity.class);
			startActivity(intent);
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
				intent.setClass(ErrorExamActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (select_count < size) {
					alert("提示", "您还有未做完的题目，是否提交考卷？");
					return;
				}
				// submit
				submitExam();
			}
		});

	}

	private void init() throws Exception {

		ansertListView = (ListView) findViewById(R.id.answerListView_ex);
		backButton = (Button) findViewById(R.id.exam_back_ex);
		submitButton = (Button) findViewById(R.id.exam_submit_ex);
		examQuestionTextView = (TextView) findViewById(R.id.exam_question_ex);
		examImageView = (ImageView) findViewById(R.id.exam_image_ex);
		examProgressBar = (ExamProgressBar) findViewById(R.id.exam_progress_ex);
		viewFlipper = (ViewFlipper) findViewById(R.id.examViewFlipper_ex);
		detector = new GestureDetector(this);
//		subject_title = (TextView) findViewById(R.id.subject_title_ex);

		index = 0;
		examList = DBcontrol.getErrorAll(this);
		// subjectTitle = (String) map.get("subName");

		// topId = (String) map.get("topId");
		size = examList.size();
//		subject_title.setText(examList.get(index).getSubId() + " "
//				+ examList.get(index).getSubName());
		selectExamMap = new HashMap<Long, Exam>();
		examProgressBar.setMax(size);
		setProgress(examProgressBar.getProgress() * size);
		setSecondaryProgress(examProgressBar.getSecondaryProgress() * size);
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
		examQuestionTextView.setText(examList.get(index).getSubId() + " "+ examList.get(index).getSubName() + "\n" +(index + 1) + "." + question);
		// examQuestionTextView.setText(index+1+"."+question);
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
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				selectListView(view, position);
				Map<String, Object> map = (Map<String, Object>) ansertListView
						.getItemAtPosition(position);
				String answer = (String) map.get("answer");

				Exam exam = getExam(index);
				String r = exam.getAnswer();

				Log.d(TAG, r);

				if (exam.getAnswer().equals(answer)) {// 答对

					if (null == selectExamMap.get(exam.getId())
							|| !answer.equals(selectExamMap.get(exam.getId())
									.getUserAnswer())) {

						// int erroenum = exam.getId().intValue();
						int e = exam.getId().intValue();
						right.add(e);
						grade++;
					}

				} else {// 答错

					if (null != selectExamMap.get(exam.getId())
							&& !answer.equals(selectExamMap.get(exam.getId())
									.getUserAnswer())) {
						int erroenum = exam.getId().intValue();

						for (int i = 0; i < right.size(); i++) {

							if (right.get(i).equals(erroenum))
								right.remove(i);
						}
						grade--;
					}
				}

				exam.setUserAnswer(answer);
				selectExamMap.put(exam.getId(), exam);

				if (index + 1 >= size) {
					// alert("已经是最后一道题");
					alert("提示", "已经是最后一道题目，是否提交考卷？");

					return;
				}
				Context ctx = ErrorExamActivity.this;
				SharedPreferences sp = ctx.getSharedPreferences("SP",
						MODE_PRIVATE);
				int bool = sp.getInt("Condition", 4);
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
			new Thread(new Runnable() {

				@Override
				public synchronized void run() {
					for (Exam exam : examList) {

						if (null == selectExamMap.get(exam.getId())) {
							Log.d("----123",
									exam.getAnswer() + "----"
											+ exam.getUserAnswer());
							if (exam.getUserAnswer() == null) {
//								ExamDataUtil.addExamErrorList(exam);
//								DBcontrol.insertError(exam, ErrorExamActivity.this);
							}
						} else {

							if (exam.getAnswer() != null
									&& exam.getUserAnswer() != null
									&& !exam.getAnswer().equals(
											exam.getUserAnswer())) {
								Log.d("--不等于空",
										exam.getId() + "---"
												+ exam.getUserAnswer());
								// DBcontrol.insertError(exam, cx);
							}
						}

					}
				}

			}).start();
		} catch (Exception e) {
			System.out.println(e);
		}

		Intent intent = new Intent();
		intent.setClass(ErrorExamActivity.this, MianResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("size", size);
		bundle.putInt("grade", grade);
		bundle.putIntegerArrayList("rightList", (ArrayList<Integer>) right);
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
				row = new HashMap<String, Object>();
				answer = answerArray[i].trim().substring(0, 1);
				row.put("answerTitle", answerArray[i]);
				row.put("answer", answer);
				row.put("answerItemImage", R.drawable.select_off);
				if (selectedExam(exam, answer)) {
					row.put("answerItemImage", R.drawable.select_on);
				}
				listItem.add(row);

			}

		} else {
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

	private void alert(String message) {
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
						submitExam();
					}
				}).show();
	}

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
				alert("提示", "已经是最后一道题目，是否提交考卷？");
				return false;
			}
			index++;
			showExam(getExam(index));
			examProgressBar.incrementProgressBy(1);
		} else if (e1.getX() - e2.getX() < -50) {

			viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			viewFlipper.showPrevious();
			// alert("left");
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

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("1", 2);
		bundle.putString("subId", subId);
		bundle.putString("topId", topId);
		intent.putExtras(bundle);
		intent.setClass(ErrorExamActivity.this, MainActivity.class);
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
