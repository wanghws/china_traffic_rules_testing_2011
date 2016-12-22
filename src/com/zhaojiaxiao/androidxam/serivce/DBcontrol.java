package com.jiaogui.androidxam.serivce;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jiaogui.androidexam.data.Exam;
import com.jiaogui.androidexam.data.Notes;
import com.jiaogui.androidexam.data.Result;

public class DBcontrol {

	public static void insertResult(Result result, Context cx) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		ContentValues cv = new ContentValues();
		Log.d("marks", (result.marks).toString());
		Log.d("time", result.time);
		Log.d("date", (result.date).toString());
		cv.put("marks", result.marks);
		cv.put("time", result.time);
		cv.put("date", result.date);

		db.insertresulttable(sd, cv);

		sd.close();
		db.close();
	}

	
	public static void deleteById(Context cx, String sql) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		db.deleteLeafLevel(sd, sql);
		sd.close();
		db.close();
	}

	public static void deleteNotesItem(Context cx, String subId) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		db.deletenotesById(sd, subId);
		sd.close();
		db.close();
	}

	public static int findcountBySubId(Context cx, String subId) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		Cursor c = db.querynotesnum(sd, subId);
		// int i = c.getInt(c.getColumnIndex("rightNum"));
		
			if (c.moveToFirst()) {
				c.moveToFirst();
//				Log.d("C是啥", c.toString());
				int a = c.getInt(c.getColumnIndex("rightNum"));
				c.close();
				db.close();
				sd.close();
				return a;
				
			} else {
				c.close();
				db.close();
				sd.close();
				return 0;
			}
	
			

		

	}

	public static int findCursorBynotes(Context cx, String subId) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		Cursor c = db.getrigthBySubId(sd, subId);
		int i = c.getCount();
		c.close();
		sd.close();
		db.close();
		return i;
	}

	// 删除leaflevel所有数据
	public static void DeleteLeafLevelForAll(Context cx) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		db.DeleteLeafLevelForAll(sd);
		sd.close();
		db.close();
	}

	// 删除resulttable所有数据
	public static void DeleteResultTableForAll(Context cx) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		db.DeleteResultTableForAll(sd);
		sd.close();
		db.close();
	}
	//删除notes表所有数据
	public static void DeletenotesTableForAll(Context cx) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		db.DeletenotesTableForAll(sd);
		sd.close();
		db.close();
	}



	public static void insertrightNum(String subId, int grade, Context cx) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("SubId", subId.toString());
		cv.put("rightNum", grade);
		db.insertright(sd, cv);
		sd.close();
		db.close();
	}

	public static  synchronized void insertError(Exam exam, Context cx) {
		Db db = new Db(cx);
		SQLiteDatabase sd = db.getReadableDatabase();
		Cursor c = db.getErrorQuestionById(sd, exam.getId());
		ContentValues cv = new ContentValues();
		int num = c.getCount();
		if (num > 0) {
			// 如果存在数据，数量加一
			while (c.moveToNext()) {
				Log.d("--#####", "错题重复,errorNum+1》》》》》》》");
				int errNum = Integer.parseInt(c.getString(c
						.getColumnIndex("errorNum"))) + 1;
				cv.put("errorNum", errNum);
				db.updateLeaflevelById(sd, cv, exam.getId());
			}
		} else {
			Log.d("--######", "错题未重复,直接存入");
			cv.put("id", exam.getId());
			cv.put("analyse", exam.getAnalyse());
			cv.put("answer", exam.getAnswer());
			cv.put("question", exam.getQuestion());
			cv.put("subId", exam.getSubId());
			cv.put("subName", exam.getSubName());
			cv.put("topId", exam.getTopId());
			cv.put("topName", exam.getTopName());
			cv.put("area", exam.getArea());
			cv.put("img", exam.getImg());
			cv.put("status", exam.getStatus());
			cv.put("type", exam.getType());
			cv.put("unknow", exam.getUnknow());
			cv.put("userAnswer", exam.getUserAnswer());
			cv.put("errorNum", 1);
			db.insertLeaflevel(sd, cv);
		}
		c.close();
		db.close();
	}

	// 获得全部错题
	public static List<Exam> getErrorAll(Context cx) {
		Db db = new Db(cx);

		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor c = db.getErrorQuestion(sd);
		ArrayList<Exam> examList = new ArrayList<Exam>();
		try {
			while (c.moveToNext()) {
				Exam exam = new Exam();
				exam.setAnalyse(c.getString(c.getColumnIndex("analyse")));
				exam.setAnswer(c.getString(c.getColumnIndex("answer")));
				exam.setId(c.getLong(c.getColumnIndex("id")));
				exam.setQuestion(c.getString(c.getColumnIndex("question")));
				exam.setSubId(c.getString(c.getColumnIndex("subId")));
				exam.setSubName(c.getString(c.getColumnIndex("subName")));
				exam.setTopId(c.getLong(c.getColumnIndex("topId")));
				exam.setTopName(c.getString(c.getColumnIndex("topName")));
				exam.setArea(c.getString(c.getColumnIndex("area")));
				exam.setImg(c.getString(c.getColumnIndex("img")));
				exam.setStatus(c.getInt(c.getColumnIndex("status")));
				exam.setType(c.getInt(c.getColumnIndex("type")));
				exam.setUnknow(c.getString(c.getColumnIndex("unknow")));
				exam.setUserAnswer(c.getString(c.getColumnIndex("userAnswer")));
				exam.setErrorNum(c.getInt(c.getColumnIndex("errorNum")));
				examList.add(exam);
				exam = null;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			db.close();
			c.close();
		}
		return examList;

	}

	// 获得对题的题号
	public static List<Notes> getNotes(Context cx) {
		Db db = new Db(cx);

		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor c = db.querynotes(sd);
		ArrayList<Notes> notesList = new ArrayList<Notes>();

		try {
			while (c.moveToNext()) {
				Notes notes = new Notes();
				notes.subId = c.getString(c.getColumnIndex("subId"));
				notes.rightNum = c.getInt(c.getColumnIndex("rightNum"));
				notesList.add(notes);
				notes = null;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			db.close();
			c.close();
		}

		return notesList;
	}

	// 根据subId获取错题次数
	public static void getrightBySubId(Context cx, String subId) {
		Db db = new Db(cx);

		SQLiteDatabase sd = db.getReadableDatabase();
		Cursor c = db.getrightBySubId(sd, subId);

		// boolean yn = c.isNull(i);

		db.close();
		c.close();

	}

	// 获得成绩表
	public static List<Result> getResult(Context cx) {

		Db db = new Db(cx);

		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor c = db.getResult(sd);
		ArrayList<Result> resultList = new ArrayList<Result>();

		try {
			while (c.moveToNext()) {
				Result result = new Result();
				result.marks = c.getInt(c.getColumnIndex("marks"));
				result.time = c.getString(c.getColumnIndex("time"));
				result.date = c.getString(c.getColumnIndex("date"));
				resultList.add(result);
				result = null;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			sd.close();
			db.close();
			c.close();
		}

		return resultList;
	}
	
	//查询表是否为空
	
	public static boolean getnull(Context cx){
		Db db = new Db(cx);

		SQLiteDatabase sd = db.getReadableDatabase();
		Cursor c = db.getornull(sd);
		if(c.moveToNext()){
			db.close();
			c.close();
			return true;
			
		}else{
			db.close();
			c.close();
			return false;
		}
		
		
	}

	
}
