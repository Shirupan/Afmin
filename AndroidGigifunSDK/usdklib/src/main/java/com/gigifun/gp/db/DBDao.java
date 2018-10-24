/**   
 * 文件名：DBDao.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-5-26
 */ 

package com.gigifun.gp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/** 
 * 类名: DBDao</br> 
 * 包名：com.ln.lngamesdkjar.db </br> 
 * 描述: </br>
 * 发布版本号：</br>
 * 开发人员： 谁抢了我的飞宇 </br>
 * QQ：460543600</br>
 * 创建时间： 2014-5-26 
 */

public class DBDao {

	/**
	 * 根据数据库和contentvalues进行插入操作
	 * @param db
	 * @param values
     * @return
     */
	 public static long insertPay(SQLiteDatabase db,ContentValues values){
		 
		return db.insert(DBOpenHelper.tab_syzg, null, values);
	 }

	/**
	 * 根据数据库和加密id删除对应数据行
	 * @param db
	 * @param encode_coorderid
	 * @return
     */
	 public static int delPay(SQLiteDatabase db,String encode_coorderid){
		 
		return db.delete(DBOpenHelper.tab_syzg, "encode_coorderid=?", new String[]{encode_coorderid});
	 }

	/**
	 * 订单号、lnid、uid三者确定一个订单，再提供values更改订单内内容
	 * @param db
	 * @param values
	 * @param whereArgs
     * @return
     */
	 public static int updataPayResult(SQLiteDatabase db,ContentValues values,String[] whereArgs){
		return db.update(DBOpenHelper.tab_syzg, values, "uid=? and lnid=? and encode_coorderid=?", whereArgs);
	 }

	/**
	 * 返回一个可供遍历 的Cursor
	 * @param db
	 * @return
     */
	 public static Cursor queryDB(SQLiteDatabase db){
		return db.query(DBOpenHelper.tab_syzg, null, null, null, null, null, null);
	 }

	/**
	 * 根据sku,交易id，uid lnid,返回对应行
	 * @param db
	 * @param sku
	 * @param transactionId
	 * @param uid
	 * @param lnid
     * @return
     */
	 public static Cursor queryGooglePay(SQLiteDatabase db,String sku,String transactionId,String uid,String lnid){
		 return db.query(DBOpenHelper.tab_syzg,
				 null,
				 "(mode='1' and isPayment='isPaying' and (requestStatus='2' or requestStatus='3') and encode_transaction_id='"+ transactionId +"') or (mode='1' and isPayment='before Paying' and (coorderid is not null) and requestStatus='1' and sku='"+ sku +"' and uid='"+ uid +"' and lnid='"+ lnid +"')",
				 null, null, null, null);
	 }

	/**
	 *
	 * @param db
	 * @param uid
	 * @param lnid
     * @return
     */
	 public static Cursor queryNoUnconsumeGoodsButQueryGooglePay(SQLiteDatabase db,String uid,String lnid){
		 return db.query(DBOpenHelper.tab_syzg, null, "(mode='1' and isPayment='isPaying' and (coorderid is not null) and (requestStatus='2' or requestStatus='3') and (sku is not null) and (encode_transaction_id is not null)) or (mode='1' and isPayment='before Paying' and (coorderid is not null) and requestStatus='1' and uid='"+ uid +"' and lnid='"+ lnid +"' and (sku is not null))", null, null, null, null);
	 }
	 
	 public static Cursor queryOtherPay(SQLiteDatabase db,String uid,String lnid){
		return db.query(DBOpenHelper.tab_syzg, null, "((mode='2' or mode='3') and  isPayment='isPaying' and (coorderid is not null) and (requestStatus='2' or requestStatus='3'))", null, null, null, null);
	 }
	 
	 public static int tableIsExists(String table){
		  SQLiteDatabase db=DatabaseManager.getInstance().openDatabase();
		  String sql = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name="+table;  
	        Cursor cur = db.rawQuery(sql, null);  
	        int count = -1;  
	        while (cur.moveToNext()) {  
	            count = cur.getInt(0);  
	        }  
	        if (count <= 0) {  
	            // 表不存在  
	        	System.out.println("表不存在");
	        	
	        } else {  
	            System.out.println("表存在");
	        } 
	        
	        return count;
	 }
	 
}
