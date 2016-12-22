package com.jiaogui.androidexam.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ExamDataUtil {
	private static boolean INIT = false;
	private static final String TAG = "ExamDataUtil";
	private static List<Integer> ALL_EXAM;
	private static List<Exam> EXAM_LIST = new ArrayList<Exam>();
	private static List<Map<String,Object>> TOPIC_LIST;
	private static List<Map<String,Object>> SUBJECT_LIST;
	private static List<Exam> RANDOM_EXAM_LIST;
	
	
	//随机答题错误列表
	private static List<Exam> EXAM_ERROR_LIST;
	public static void initset(){
		EXAM_ERROR_LIST.clear();
	}
	
	public static List<Exam> getExamErrorList(){
		return EXAM_ERROR_LIST;
	}
	public static void addExamErrorList(Exam exam){
		EXAM_ERROR_LIST.add(exam);
	}
	public static void removeExamErrorList(Exam exam){
		EXAM_ERROR_LIST.remove(exam);
	}
	public static void initExamErrorList(){
		EXAM_ERROR_LIST = new ArrayList<Exam>();
	}
	public static Exam getErrorExam(Long id){
		for(Exam exam:EXAM_ERROR_LIST){
			if(id.intValue()==exam.getId().intValue()){
				return exam;
			}
		}
		return null;
	}
	//随机答题选择错误列表
	private static Map<Long,String> ANSWER_MAP;
	
	public static Map<Long,String> getErrorAnswerList(){
		return ANSWER_MAP;
	}
	public static String getErrorAnswer(Long id){
		return ANSWER_MAP.get(id);
	}
	public static void addErrorAnswerList(Long id,String answer){
		ANSWER_MAP.put(id,answer);
	}
	public static void removeErrorAnswerList(Long id){
		ANSWER_MAP.remove(id);
	}
	public static void initErrorAnswerList(){
		ANSWER_MAP = new HashMap<Long,String>();
	}
	
	public static List<Map<String,Object>> getTopicList(){
		return TOPIC_LIST;
	}
	public static List<Map<String,Object>> getSubjectList(){
		return SUBJECT_LIST;
	}
	public static List<Exam> getRandomExamList(){
		return RANDOM_EXAM_LIST;
	}
	/**
	 * 根据小节id取小节所有题目
	 * */
	public static Map<String,Object> getSubjectExamList(String subId){
		int i=0;
		for(Map<String,Object> map:SUBJECT_LIST){
			Object wer = SUBJECT_LIST.get(i);
			Log.d(">>>>>>>>>>>>>>>取题目", wer.toString());
			if(subId.equals(String.valueOf(map.get("subId")))){
				return map;
			}
			i++;
		}
		return null;
	}
	/**
	 * 根据小节id取小节所有题目
	 * */
	public static Map<String,Object> getNextSubjectExamList(String subId){
		int i=0;
		int k = 0;
		for(Map<String,Object> map:SUBJECT_LIST){
			
			if(subId.equals(String.valueOf(map.get("subId")))){
				k = i;
				break;
			}
			i++;
		}
		if(k+1>=SUBJECT_LIST.size()){
			return null;
		}
		return SUBJECT_LIST.get(k+1);
	}
	public static void load(Resources resources){
		if(INIT){
			return;
		}
		try{
			readTopic(resources);
			readSubject(resources);
			List<Map<String,Object>> list;
			for(Map<String,Object> topic:TOPIC_LIST){
				list = new ArrayList<Map<String,Object>>();
				for(Map<String,Object> subject:SUBJECT_LIST){
					if(subject.get("topId").toString().equals(topic.get("topId").toString())){
						list.add(subject);
					}
				}
				topic.put("subjectList", list);
				topic.put("subjectSize", list.size());
			}
			readAllExam(resources);
			readExamList(resources);
			List<Integer> idList;
			List<Exam> examList;
			for(Map<String,Object> subject:SUBJECT_LIST){
				idList = (List<Integer>)subject.get("list");
				examList = new ArrayList<Exam>();
				for(int id:idList){
					for(Exam exam:EXAM_LIST){
						if(id==exam.getId().intValue()){
							examList.add(exam);
						}
					}
				}
				subject.put("examList", examList);
			}
			//RANDOM_EXAM_LIST = randomExam();
//			INIT = true;
		}catch(Exception e){
			Log.e(TAG,e.getMessage(),e);
		}
		Log.i("out", ">>>>>>>>OOOOOOK>>>>>>>>>>");
	}
	public static List<Exam> randomExam()throws Exception{
		List<Exam> allExam  = EXAM_LIST;
		Collections.shuffle(allExam);
	    List<Exam> list = new ArrayList<Exam>();
	    for (int i = 0; i<100; i++) {
	        list.add(allExam.get(i));
	    }
	    allExam = null;
	    return list;
    } 
	public static void readAllExam(Resources resources){
		try{
			InputStream in = resources.getAssets().open("data/all.txt");
			ObjectMapper mapper = new ObjectMapper();
			ALL_EXAM = mapper.readValue(in,new ArrayList<Integer>().getClass());
			//Log.i(TAG, "ALL_EXAM:"+ALL_EXAM.size());
		}catch(Exception e){
			Log.e(TAG,e.getMessage(),e);
		}
	}
	public static void readTopic(Resources resources){
		try{
			InputStream in = resources.getAssets().open("data/topic.txt");
			ObjectMapper mapper = new ObjectMapper();
			TOPIC_LIST = mapper.readValue(in,new ArrayList<Map<String,Object>>().getClass());
			//Log.i(TAG, "TOPIC_LIST:"+TOPIC_LIST.toString());
			/*for(Map topic:TOPIC_LIST){
				Log.i(TAG, "TOPIC_LIST:"+topic.get("topName"));
			}*/
		}catch(Exception e){
			Log.e(TAG,e.getMessage(),e);
		}
	}
	public static void readSubject(Resources resources){
		try{
			InputStream in = resources.getAssets().open("data/subject.txt");
			ObjectMapper mapper = new ObjectMapper();
			SUBJECT_LIST = mapper.readValue(in,new ArrayList<Map<String,Object>>().getClass());
			//Log.i(TAG, "SUBJECT_LIST:"+SUBJECT_LIST.toString());
			/*for(Map<String,Object> subject:SUBJECT_LIST){
				Log.i(TAG, "SUBJECT :"+subject.get("subName") +" list:"+subject.get("list"));
			}*/
		}catch(Exception e){
			Log.e(TAG,e.getMessage(),e);
		}
	}
	public static void readExamList(Resources resources){
		try{
			InputStream in = null;
			ObjectMapper mapper = new ObjectMapper();
			for(int id:ALL_EXAM){
				in = resources.getAssets().open("data/"+id+".txt");
				EXAM_LIST.add((Exam)mapper.readValue(in,new Exam().getClass()));
			}
			//Log.i(TAG, "EXAM_LIST:"+EXAM_LIST.size());
		}catch(Exception e){
			Log.e(TAG,e.getMessage(),e);
		}
	}
	public static Bitmap getImage(Resources resources,Long id){
		try{
			InputStream in = resources.getAssets().open("image/"+id+".png");
			//FileInputStream fis = new FileInputStream("image/"+id+".png");
			return BitmapFactory.decodeStream(in); 
		}catch(Exception e){
			Log.e(TAG,e.getMessage(),e);
			return null;
		}
	}
	
//	public static  boolean getCondition(){
//		Context ctx = this;       
//		SharedPreferences  sp = this.getSharedPreferences("SP", MODE_PRIVATE);
//		
//		return true;
//	}

	
	
}
