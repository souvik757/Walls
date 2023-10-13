package com.example.saikouwalls.Views.WebViews;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView ;
import android.webkit.WebSettings ;
import android.webkit.WebViewClient ;

import android.os.Bundle;

import com.example.saikouwalls.R;

public class PhotographInfoWebActivity extends AppCompatActivity {
    private WebView mWebView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photograph_info_web);
        String photographerUrl = getIntent().getStringExtra("PhotographerInfoUrl") ;
        mWebView = (WebView) findViewById(R.id.webview) ;
        mWebView.setWebViewClient(new WebViewClient()) ;
        mWebView.loadUrl(photographerUrl) ;
        WebSettings webSettings = mWebView.getSettings() ;
        webSettings.setJavaScriptEnabled(true);
    }

    public void cancelWebView(View view) {
        finish() ;
    }

    public class mWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true ;
        }
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack())
            mWebView.goBack() ;
        else
            super.onBackPressed() ;
    }
}