/**   
 * 文件名：DBOpenHelper.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-5-26
 */ 

package com.gigifun.gp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/** 
 * 类名: DBOpenHelper</br> 
 * 包名：com.ln.lngamesdkjar.db </br> 
 * 描述: </br>
 * 发布版本号：</br>
 * 开发人员： 谁抢了我的飞宇 </br>
 * QQ：460543600</br>
 * 创建时间： 2014-5-26 
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String db="db_ugame.db";
    public static final String tab_syzg="syzg_pay_result";

   
	/** 
	 * 描述: </br>
	 * 开发人员：杜逸平</br>
	 * 创建时间：2014-5-26</br>
	 * @param context

	 */ 
	public DBOpenHelper(Context context) {
		super(context, db, null, 4);
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 * 开发人员：杜逸平
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//String sql="create table if not exists syzg_pay_result (_id integer primary key autoincrement,uid varchar(255),lnid varchar(255),coorderid varchar(255), encode_coorderid  text,mode varchar(255), coin varchar(255), product varchar(255),amount varchar(255), sku varchar(255),clientid varchar(255),isPayment varchar(255),requestStatus varchar(255),reason varchar(255),serverid varchar(255),transaction_id varchar(255),encode_transaction_id text,originaljson text,signture text,current_time varchar(255))";
	    db.execSQL(DBFile.CREATE_TABLE);

	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 * 开发人员：杜逸平
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion>oldVersion){
				db.execSQL("DROP TABLE IF EXISTS "+tab_syzg);
				onCreate(db);
		}
	}

}
