package com.gigifun.gp.listener;

public interface OnThirdPurchaseListener {
        public void onPurchaseSuccessful(String orderId);

        public void onPurchaseFailed(String reason);
    }