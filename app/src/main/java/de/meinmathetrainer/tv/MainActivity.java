package de.meinmathetrainer.tv;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;
    private ToneGenerator tones;

    public class AndroidSound {
        @JavascriptInterface public void playCorrect() {
            runOnUiThread(() -> { if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_ACK, 220); });
        }
        @JavascriptInterface public void playWrong() {
            runOnUiThread(() -> { if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_NACK, 260); });
        }
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        tones = new ToneGenerator(AudioManager.STREAM_MUSIC, 85);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        webView = new WebView(this);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.setBackgroundColor(0xFFEEF5FB);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setDefaultTextEncodingName("UTF-8");
        s.setMediaPlaybackRequiresUserGesture(false);
        webView.addJavascriptInterface(new AndroidSound(), "AndroidSound");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        setContentView(webView);
        webView.loadUrl("file:///android_asset/index.html");
        webView.requestFocus();
    }

    private boolean sendRemoteKey(String key) {
        if (webView == null) return false;
        webView.evaluateJavascript("window.handleRemoteKey && window.handleRemoteKey('" + key + "')", null);
        return true;
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_UP: return sendRemoteKey("ArrowUp");
                case KeyEvent.KEYCODE_DPAD_DOWN: return sendRemoteKey("ArrowDown");
                case KeyEvent.KEYCODE_DPAD_LEFT: return sendRemoteKey("ArrowLeft");
                case KeyEvent.KEYCODE_DPAD_RIGHT: return sendRemoteKey("ArrowRight");
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER: return sendRemoteKey("Enter");
                case KeyEvent.KEYCODE_BACK:
                    if (webView != null && webView.canGoBack()) { webView.goBack(); return true; }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override protected void onDestroy() {
        if (webView != null) { webView.removeJavascriptInterface("AndroidSound"); webView.destroy(); }
        if (tones != null) { tones.release(); tones = null; }
        super.onDestroy();
    }
}
