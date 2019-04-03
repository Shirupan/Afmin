package com.gigifun.wszj;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.gigifun.gp.utils.LogUtil;
import com.gtoken.unclejoe.R;

public class WxActivity extends Activity {

    private WebView mWebView;
    private WebSettings webSettings;
    private String payinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx);

       mWebView = (WebView) findViewById(R.id.wv_wxpay);

        Intent intent = getIntent();
        payinfo = intent.getStringExtra("payinfo");

        mWebView.requestFocusFromTouch();
        webSettings=mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);

                if(!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity( intent );
                }catch(Exception e){

                }
                return true;
            }

            //            處理瀏覽器的按鍵
            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

        });
        LogUtil.k("webview地址=="+payinfo);
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        mWebView.loadUrl(payinfo);
    }

}

