package com.gigifun.gp.listener;

public interface OnLoginListener {
        public void onLoginSuccessful(String sdkUid);

        public void onLoginFailed(String reason);
    }