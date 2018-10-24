

package com.gigifun.gp.db;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;



public class DatabaseManager {
	 private AtomicInteger mOpenCounter = new AtomicInteger();
	 private static DatabaseManager instance;
	 private static DBOpenHelper mDBOpenHelper;
	 private SQLiteDatabase mDatabase;
	 
	 private int inrCount=-1;
	 private int decCount=-1;
	 public DatabaseManager(){
		 
	 }
	 
	 public static synchronized void initializeInstance(Context context){
		 if(null==instance){
			 instance=new DatabaseManager();
			 mDBOpenHelper=new DBOpenHelper(context);
		 }
	 }
	 
	 public static synchronized  DatabaseManager getInstance(){
		 if(null==instance){
			 throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
	                    " is not initialized, call initializeInstance(..) method first.");
		 }
		 return instance;
	 }
	 
	 public synchronized SQLiteDatabase openDatabase() {
		 inrCount=mOpenCounter.incrementAndGet();
	        if( inrCount== 1) {
	            mDatabase = mDBOpenHelper.getWritableDatabase();
	        }
	        return mDatabase;
	    }

	 public synchronized void closeDatabase() {
		 decCount=mOpenCounter.decrementAndGet();
	        if( decCount== 0) {
	            mDatabase.close();
	        }
	 }
	 
	 
	 public int getInrCount(){
		 return inrCount;
	 }
	 
	 public int getDecCount(){
		 return decCount;
	 }
}
