/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at


       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */
package com.stone.lib.plugin;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.Map;

public class PluginResult {

	private final Status status;
    /**
     * 信息的类型
     * */
	private final int msgType;
    /**
     * 字符串信息
     * */
	private String strMsg;
	/**
     * 原类型信息
     * */
	private Object rawMsg;


    public static final int MESSAGE_TYPE_STRING = 1;
    public static final int MESSAGE_TYPE_JSON_OBJECT = 2;
    public static final int MESSAGE_TYPE_JSON_ARRAY = 3;
    public static final int MESSAGE_TYPE_NUMBER = 4;
    public static final int MESSAGE_TYPE_BOOLEAN = 5;
    public static final int MESSAGE_TYPE_NULL = 6;
    public static final int MESSAGE_TYPE_MAP = 7;
    public static final int MESSAGE_TYPE_OBJECT = 8;

    private static String[] StatusMessages = new String[] { "OK", "Error",
            "Cancel", "Wait" };

    public static enum Status {

        OK(0), ERROR(1), CANCEL(2), WAIT(3);

        Status(int state) {
        }

    }

	public PluginResult(Status status) {
		this(status, StatusMessages[status.ordinal()]);
	}

	public PluginResult(Status status, String message) {
		this.status = status;
		msgType = message == null ? MESSAGE_TYPE_NULL : MESSAGE_TYPE_STRING;

		strMsg = message;
		rawMsg = message;
	}

	public PluginResult(Status status, JsonArray message) {
		this.status = status;
		msgType = message == null ? MESSAGE_TYPE_NULL : MESSAGE_TYPE_JSON_ARRAY;

		strMsg = message != null ? message.toString() : null;
		rawMsg = message;
	}

	public PluginResult(Status status, JsonObject message) {
		this.status = status;
		msgType = message == null ? MESSAGE_TYPE_NULL : MESSAGE_TYPE_JSON_OBJECT;

		strMsg = message != null ? message.toString() : null;
		rawMsg = message;
	}
	
	public PluginResult(Status status, Object message) {
		this.status = status;
		msgType = message == null ? MESSAGE_TYPE_NULL : MESSAGE_TYPE_OBJECT;

		strMsg = message != null ? message.toString() : null;
		rawMsg = message;
	}

	public PluginResult(Status status, Map<String, Object> message) {
		this.status = status;
		msgType = message == null ? MESSAGE_TYPE_NULL : MESSAGE_TYPE_MAP;

		strMsg = message != null ? message.toString() : null;
		rawMsg = message;
	}
	
	
	public PluginResult(Status status, int i) {
		this.status = status;
		msgType = MESSAGE_TYPE_NUMBER;

		strMsg = String.valueOf(i);
		rawMsg = i;
	}

	public PluginResult(Status status, float f) {
		this.status = status;
		msgType = MESSAGE_TYPE_NUMBER;

		strMsg = String.valueOf(f);
		rawMsg = f;
	}

	public PluginResult(Status status, boolean b) {
		this.status = status;
		msgType = MESSAGE_TYPE_BOOLEAN;

		strMsg = String.valueOf(b);
		rawMsg = b;
	}

	public Status getStatus() {
		return this.status;
	}

	public int getMessageType() {
		return msgType;
	}

	public String getMessage() {
		return strMsg;
	}

	public Object getRawMessage() {
		return rawMsg;
	}

	public static PluginResult from(Object object) {
		Gson gson = new Gson();
		PluginResult result = new PluginResult(Status.OK,
				gson.toJson(object));
		return result;
	}

	public Object asObject(Type t) {
		Gson gson = new Gson();
		String msg = getMessage();
		return gson.fromJson(msg, t);
	}

	public Object asObject(Class<?> glass) {
		Gson gson = new Gson();
		String msg = getMessage();
		return gson.fromJson(msg, glass);
	}

}
