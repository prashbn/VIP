package au.com.confess.confess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;


public class FacebookLoginActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        WebView mWebView = (WebView) findViewById(R.id.webView);
        mWebView.loadUrl("http://www.tamilchurch.org.au");
    }




    @Override
    public void onClick(View v) {

    }
}
