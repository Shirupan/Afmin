

package com.gigifun.gp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;


public class StoreFacebookInfo {
    private SharedPreferences preferences;
    private HashMap<String, String> map;

    public StoreFacebookInfo(Context context, SharedPreferences preferences, HashMap<String, String> map) {
        this.preferences = context.getSharedPreferences("MyCount", Context.MODE_PRIVATE);
        this.map = map;
    }

    public void storeFacebookInfo() {
        if (null == map || map.isEmpty()) {
            return;
        }
        map.put("id", map.get("Fbuid"));
        LogUtil.k("保存fb用户信息的id==" + map.get("Fbuid"));
        map.put("Sdktype", "0");

        String clientSecret = (String) map.get("Client_secret");
        String clientId = (String) map.get("Client_id");
        String id = (String) map.get("id");

        String email = (String) map.get("email");
        if (null == email) {
            email = "";
        }
        String firstName = (String) map.get("first_name");
        if (null == firstName) {
            firstName = "";
        }
        String gender = (String) map.get("gender");
        if (null == gender) {
            gender = "";
        }
        String lastName = (String) map.get("last_name");
        if (null == lastName) {
            lastName = "";
        }
        String link = (String) map.get("link");
        if (null == link) {
            link = "";
        }
        String locale = (String) map.get("locale");
        if (null == locale) {
            locale = "";
        }
        String name = (String) map.get("name");
        if (null == name) {
            name = "";
        }
        String timezone = (String) map.get("timezone");
        if (null == timezone) {
            timezone = "";
        }
        String verified = (String) map.get("verified");
        if (null == verified) {
            verified = "";
        }
        String updatedTime = (String) map.get("updated_time");
        if (null == updatedTime) {
            updatedTime = "";
        }

        if (preferences.getString("Client_secret", "").equals(clientSecret) && preferences.getString("Client_id", "").equals(clientId)
                && preferences.getString("id", "").equals(id) && preferences.getString("email", "").equals(email)
                && preferences.getString("first_name", "").equals(firstName) && preferences.getString("gender", "").equals(gender)
                && preferences.getString("last_name", "").equals(lastName) && preferences.getString("link", "").equals(link)
                && preferences.getString("locale", "").equals(locale) && preferences.getString("name", "").equals(name)
                && preferences.getString("timezone", "").equals(timezone) && preferences.getString("verified", "").equals(verified)
                && preferences.getString("updated_time", "").equals(updatedTime)) {
            return;
        }

        UhttpUtil.post(UgameUtil.getInstance().FACEBOOK_STORE, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                if (null == response) {
                    return;
                }
                try {
                    JSONObject jo = new JSONObject(response);
                    String status = jo.optString("Status");
                    if ("1".equals(status)) {
                        Editor editor = preferences.edit();
                        editor.putString("Client_secret", (String) map.get("Client_secret"));
                        editor.putString("Client_id", (String) map.get("Client_id"));
                        editor.putString("id", (String) map.get("id"));
                        editor.putString("email", (String) map.get("email"));
                        editor.putString("first_name", (String) map.get("first_name"));
                        editor.putString("gender", (String) map.get("gender"));
                        editor.putString("last_name", (String) map.get("last_name"));
                        editor.putString("link", (String) map.get("link"));
                        editor.putString("locale", (String) map.get("locale"));
                        editor.putString("name", (String) map.get("name"));
                        editor.putString("timezone", (String) map.get("timezone"));
                        editor.putString("verified", (String) map.get("verified"));
                        editor.putString("updated_time", (String) map.get("updated_time"));
                        editor.commit();

                    } else if ("0".equals(status)) {
                        Log.d("StorefbInfo->post", " post fail");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                // TODOst Auto-generated method stub
                Log.d("StorefbInfo->post", " post error");
            }
        });

    }
}
