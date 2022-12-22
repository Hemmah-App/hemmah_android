package com.google.hemmah.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.hemmah.R;

import java.io.InputStream;

public class WebVideoActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;
    private static final String BASE_URL_DISABLED =
            "https://meet.jit.si/Hemmah" +
                    "#config.prejoinPageEnabled=false" +
                    "&config.disableDeepLinking=true" +
                    "&config.startWithVideoMuted=false" +
                    "&config.startWithAudioMuted=false";
    private static final String BASE_URL_VOLUNTEER =
            "https://meet.jit.si/Hemmah" +
                    "#config.prejoinPageEnabled=false" +
                    "&config.disableDeepLinking=true" +
                    "&config.startWithVideoMuted=true" +
                    "&config.startWithAudioMuted=false";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_video);
        mWebView = findViewById(R.id.meeting_WV);
        //enable java script
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //calling the method which injects the css code
                customizeCss();
                super.onPageFinished(view, url);

            }
        });


        mWebView.loadUrl(BASE_URL_DISABLED);
    }









    private void customizeCss() {
        try {
            InputStream inputStream = getAssets().open("style.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            mWebView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

