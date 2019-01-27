package alarma.example.com.alarmaph;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by joshua on 2/24/2018.
 */

public class pdf_webview extends Activity{
    WebView wb;
    ProgressDialog pDialog;
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Bundle bundle = getIntent().getExtras();

        String thisTitle = bundle.getString("title");
        String thisAuthor = bundle.getString("author");
        String thisLink = bundle.getString("link");
        init(thisTitle,thisAuthor,thisLink);
        listener();
    }

    private void init(String title, String author, String link) {
        wb = (WebView) findViewById(R.id.webview);
        wb.getSettings().setJavaScriptEnabled(true);

        pDialog = new ProgressDialog(pdf_webview.this);
        pDialog.setTitle(title);
        pDialog.setMessage("Loading "+title+" by "+author);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        wb.loadUrl(link);

    }

    private void listener() {
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });
    }
}