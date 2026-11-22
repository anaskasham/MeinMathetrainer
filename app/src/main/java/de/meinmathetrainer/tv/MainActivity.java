package de.meinmathetrainer.tv;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.Locale;

public class MainActivity extends Activity {
    private WebView webView;
    private ToneGenerator tones;
    private TextToSpeech tts;
    private boolean pageReady = false;
    private boolean ttsReady = false;

    public class AndroidSound {
        @JavascriptInterface public void playCorrect() {
            runOnUiThread(() -> { if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_ACK, 260); });
        }
        @JavascriptInterface public void playWrong() {
            runOnUiThread(() -> { if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_NACK, 300); });
        }
        @JavascriptInterface public void speak(String text, String language) {
            if (text == null || text.trim().isEmpty()) return;
            runOnUiThread(() -> {
                if (tts != null && ttsReady) {
                    Locale locale = (language != null && language.toLowerCase().startsWith("ar"))
                        ? new Locale("ar") : Locale.GERMANY;
                    int result = tts.setLanguage(locale);
                    if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts.setSpeechRate(0.88f);
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "mathtrainer-" + System.currentTimeMillis());
                    }
                }
            });
        }
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        tones = new ToneGenerator(AudioManager.STREAM_MUSIC, 90);
        tts = new TextToSpeech(this, status -> ttsReady = (status == TextToSpeech.SUCCESS));
        hideSystemUi();
        webView = new WebView(this);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.setBackgroundColor(0xFF0E3A42);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setDefaultTextEncodingName("UTF-8");
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(true);
        webView.addJavascriptInterface(new AndroidSound(), "AndroidSound");
        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView view, String url) {
                pageReady = true;
                view.requestFocus(View.FOCUS_DOWN);
                view.evaluateJavascript("document.body.setAttribute('data-android-tv','ready')", null);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        setContentView(webView);
        webView.loadUrl("file:///android_asset/index.html");
        webView.requestFocus();
    }

    private void hideSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private boolean sendRemoteKey(String key) {
        if (webView == null || !pageReady) return false;
        String safeKey = key.replace("'", "");
        webView.evaluateJavascript("window.handleRemoteKey && window.handleRemoteKey('" + safeKey + "')", null);
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
                    if (pageReady && webView != null) {
                        webView.evaluateJavascript("window.handleBackKey && window.handleBackKey()", null);
                        return true;
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override protected void onResume() { super.onResume(); hideSystemUi(); }

    @Override protected void onDestroy() {
        pageReady = false;
        if (webView != null) { webView.removeJavascriptInterface("AndroidSound"); webView.destroy(); }
        if (tones != null) { tones.release(); tones = null; }
        if (tts != null) { tts.stop(); tts.shutdown(); tts = null; }
        super.onDestroy();
    }
}
