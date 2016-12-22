package com.jiaogui.androidxam.serivce;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db extends SQLiteOpenHelper {

	public Db(Context context) {
		super(context, "dataCar.db", null, 3);
	
	}

	@Override
	public void onCreate(SQLiteDatabase db ) {
	  
		db.execSQL("CREATE TABLE leaflevel (id integer  PRIMARY KEY ,analyse TEXT(4096) DEFAULT NULL" +
				",answer TEXT(4096) DEFAULT NULL,question TEXT(64) DEFAULT NULL,subId TEXT(1024) DEFAULT" +
				" NULL,subName TEXT(1024) DEFAULT NULL,topId integer,topName TEXT(1024) DEFAULT NULL,area TEXT(1024) DEFAULT NULL,img TEXT(1024) DEFAULT NULL,status integer,type integer,unknow TEXT(1024) DEFAULT NULL,year integer,userAnswer TEXT(1024) DEFAULT NULL,errorNum integer DEFAULT 1)");
		db.execSQL("CREATE TABLE resulttable(marks integer ,time TEXT(50), date TEXT(50))");
		db.execSQL("CREATE TABLE notes(subId TEXT(50) PRIMARY KEY,rightNum integer)");
	}
	//按照id查询
	public Cursor getErrorQuestionById(SQLiteDatabase db,Long id) {
		Cursor c = db.query("leaflevel", new String[]{"errorNum"}, "id=?", new String[]{id.toString()}, null, null, null);
		return c;
	}
	public Cursor getrigthById(SQLiteDatabase db,Long id) {
		Cursor c = db.query("notes", new String[]{"subId","id"}, "id=?", new String[]{id.toString()}, null, null, null);
		return c;
	}
	public Cursor getrigthBySubId(SQLiteDatabase db,String subId) {
		Cursor c = db.query("notes", new String[]{"subId","id"}, "subId=?", new String[]{subId}, null, null, null);
		return c;
	}
	//按照SubId查询数量
	public Cursor getcountBySubId(SQLiteDatabase db,String subId){
		Cursor c = db.rawQuery("select count(*) notesNum from notes where subId ="+" ' "+subId+" ' ", null);
		return c; 
	}
	//按照subId查询答对次数
	public Cursor getrightBySubId(SQLiteDatabase db,String subId){
		Cursor c = db.rawQuery("select * from notes   where subId ="+" ' "+subId+" ' ", null);
		return c; 
	}

	//判断表是否为空
	public Cursor getornull(SQLiteDatabase db){
		
		Cursor c = db.rawQuery("select count(0) from leaflevel",null);
		
		return c;
	}
	//查询数据库表错误游标
	public Cursor getErrorQuestion(SQLiteDatabase db) {
		Cursor c = db.query("leaflevel", new String[]{"errorNum","id","analyse"
				,"answer","question","subId","subName","topId","topName"
				,"area","img","status","type","unknow","year","userAnswer"}, null, null, null, null, null);
		return c;
	}
	
	//查询数据库表结果游标
	public Cursor getResult(SQLiteDatabase db) {
		Cursor c = db.query("resulttable", new String[]{"marks","time","date"}, null, null, null, null, null);
		return c;
	}
	public Cursor querynotes(SQLiteDatabase db) {
		Cursor c = db.query("notes", new String[]{"subId"}, null, null, null, null, null);
		return c;
	}
	public Cursor querynotesnum(SQLiteDatabase db,String subId) {
		Cursor c = db.rawQuery("select * from notes where subId ="+"'"+subId+"'", null);
		return c;
	}
	
	
	//按照id修改
	public int updateLeaflevelById(SQLiteDatabase db,ContentValues values,Long id){
		return db.update("leaflevel", values, " id = ?", new String[]{id.toString()});
	}
	//插入数据
	public int insertLeaflevel(SQLiteDatabase db,ContentValues values){
		
		return (int) db.insert("leaflevel", null, values);
	}
    public int insertresulttable(SQLiteDatabase db,ContentValues values){
		
		return (int) db.insert("resulttable", null, values);
	}
    public void insertright(SQLiteDatabase db,ContentValues values){
    	db.insert("notes", null, values);
    	
    }
    
    
    //根据id删除item
	public int deletenotesById(SQLiteDatabase db,String subId){
		return  db.delete("notes", " subId = ? ", new String[]{subId});
	}
	public int deleteLeafLevelById(SQLiteDatabase db,Long id){
		return  db.delete("LeafLevel", " id = ? ", new String[]{id.toString()});
	}
	
	//根据id删除item
		public void deleteLeafLevel(SQLiteDatabase db,String sql){
			  db.execSQL(sql);
		}
	

    //删除leafLevel(我的错题)表里的所有数据
		public int DeleteLeafLevelForAll(SQLiteDatabase db){
			return  db.delete("LeafLevel", null, null);
		}	
	//删除resulttable(考试成绩)表里的所有数据
		public int DeleteResultTableForAll(SQLiteDatabase db){
			return  db.delete("resulttable", null, null);
		}
		//删除notestable(考试成绩)表里的所有数据
		public int DeletenotesTableForAll(SQLiteDatabase db){
			return  db.delete("notes", null, null);
		}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	

	

}