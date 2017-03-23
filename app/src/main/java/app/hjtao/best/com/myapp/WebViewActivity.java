package app.hjtao.best.com.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends Activity {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}
