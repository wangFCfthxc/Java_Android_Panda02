package tw.org.iii.panda02;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public MainActivity(){
        Log.d("panda","MainActivity()");
    }
    private WebView webview;
    private TextView username;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private UIHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("appdata",MODE_PRIVATE);
        editor = sp.edit();

        username = (TextView)findViewById(R.id.username);
        webview = (WebView)findViewById(R.id.webview);
        initWebView();
    }
    private void initWebView(){
        WebViewClient client = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String name = sp.getString("username","guest");
                webview.loadUrl("javascript:showName('" + name + "')");

            }
        };

        webview.setWebViewClient(client);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

        webview.addJavascriptInterface(new BradJS(), "myjs");

        webview.loadUrl("file:///android_asset/Panda.html");
    }
    public void next(View v){
        webview.goForward();
    }
    public void prev(View v){
        webview.goBack();
    }
    public void reload(View v){
        webview.reload();
    }
    public void lottery(View v){
        String name = "Panda";
        webview.loadUrl("javascript:showName('" + name + "')");
    }
    public class BradJS {
        @JavascriptInterface
        public void getName(String info) {
            Log.d("brad", "getName()" + info);
//            username.setText(info);

            editor.putString("username", info);
            editor.commit();
        }
    }
        private class UIHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String info = msg.getData().getString("info");
                username.setText(info);
            }
        }
}
