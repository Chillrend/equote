package com.bulog.equote;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bulog.equote.utils.JSInterceptor;
import com.bulog.equote.utils.SPService;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private SPService sharedPreferenceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        sharedPreferenceService = new SPService(this);

        WebViewClient eventClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:HtmlViewer.showHTML" +
                        "(document.getElementsByTagName('pre')[0].innerHTML);");
            }
        };

        webView = findViewById(R.id.webView);
        //Manually setting user agent of the webview
        //This is rather a hacky fix, find a better way to do this later on
        webView.getSettings().setUserAgentString(System.getProperty("http.agent"));

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(eventClient);
        webView.loadUrl("http://prokhus.southeastasia.cloudapp.azure.com/public/index.php/api/oauth/google/redirect");
        webView.addJavascriptInterface(new JSInterceptor(this, sharedPreferenceService), "HtmlViewer");

    }
}
