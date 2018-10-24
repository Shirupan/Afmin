package com.gigifun.gp.utils;

import okhttp3.Call;

public interface UcallBack {

	void onResponse(String response, int arg1);
	void onError(Call arg0, Exception arg1, int arg2);
}
