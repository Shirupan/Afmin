/**   
 * 文件名：DBFile.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-12-10
 */ 

package com.gigifun.gp.db;

/** 
 * 类名: DBFile</br> 
 * 包名：com.shenyuzhiguang.gamesdkjar.db </br> 
 * 描述: </br>
 * 发布版本号：</br>
 * 开发人员： 谁抢了我的飞宇 </br>
 * QQ：460543600</br>
 * 创建时间： 2014-12-10 
 */

public class DBFile {
    public static final String UID="uid";
    public static final String LNID="lnid";
    public static final String COORDERID="coorderid";
    public static final String ENCODE_COORDERID="encode_coorderid";
    public static final String MODE="mode";
    public static final String COIN="coin";
    public static final String PRODUCT="product";
    public static final String AMOUNT="amount";
    public static final String SKU="sku";
    public static final String CLIENTID="clientid";
    public static final String ISPAYMENT="isPayment";
    public static final String REQUESTSTATUS="requestStatus";
    public static final String REASON="reason";
    public static final String SERVERID="serverid";
    public static final String TRANSACTION_ID="transaction_id";
    public static final String ENCODE_TRANSACTION_ID="encode_transaction_id";
    public static final String ORIGINALJSON="originaljson";
    public static final String SIGNTURE="signture";
    public static final String CURRENT_TIME="current_time";
    public static final String CTEXT="Ctext";
    public static final String ISFIT="isfit";

    
    public static final String CREATE_TABLE="CREATE TABLE syzg_pay_result (_id integer primary key autoincrement,uid varchar(255),lnid varchar(255),coorderid varchar(255), encode_coorderid  text,mode varchar(255), coin varchar(255), product varchar(255),amount varchar(255), sku varchar(255),clientid varchar(255),isPayment varchar(255),requestStatus varchar(255),reason varchar(255),serverid varchar(255),transaction_id varchar(255),encode_transaction_id text,originaljson text,signture text,current_time varchar(255),Ctext varchar(255),isfit varchar(255))";
}
