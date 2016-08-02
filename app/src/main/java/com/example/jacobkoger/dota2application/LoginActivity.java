package com.example.jacobkoger.dota2application;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class LoginActivity extends AppCompatActivity {

    public static final String WEBSITE = "Dota 2 Application";

    String userID = "";
    WebView LoginWebView;
    CheckBox doNotShowAgainCB;
    Button changeActivityButton;

    boolean isChecked;
    boolean show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs2 = this.getSharedPreferences("player_id", Context.MODE_PRIVATE);
        SharedPreferences prefs = this.getSharedPreferences("show", Context.MODE_PRIVATE);
        SharedPreferences keyprefs = this.getSharedPreferences("key", Context.MODE_PRIVATE);

        Log.d("prefs", prefs2.getString("player_id", "none"));
        show = prefs.getBoolean("show", true);

        if (!prefs2.contains("player_id")) {
            if (show) {
                setupView();
            } else {
                if (!keyprefs.contains("key")) {
                    Log.d("ran!", "ran2");
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            if (!keyprefs.contains("key")) {
            }
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void setupView() {
        doNotShowAgainCB = (CheckBox) findViewById(R.id.donotshowbutton);
        LoginWebView = (WebView) findViewById(R.id.webView);
        changeActivityButton = (Button) findViewById(R.id.SkipButton);
        LoginWebView.getSettings().setJavaScriptEnabled(true);
        LoginWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Uri Url = Uri.parse(url);

                if (Url.getAuthority().equals(WEBSITE.toLowerCase())) {
                    LoginWebView.stopLoading();

                    Uri userAccountUrl = Uri.parse(Url.getQueryParameter("openid.identity"));
                    userID = userAccountUrl.getLastPathSegment();

                    SharedPreferences sharedPreferences = getSharedPreferences("player_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("player_id", userID);
                    editor.apply();


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        doNotShowAgainCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked = b;
                SharedPreferences sharedPreferences = getSharedPreferences("show", Context.MODE_PRIVATE);
                sharedPreferences.edit()
                        .putBoolean("show", !b)
                        .apply();
            }
        });
        String url = "https://steamcommunity.com/openid/login?openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select" +
                "&openid.identity=http://specs.openid.net/auth/2.0/identifier_select" +
                "&openid.mode=checkid_setup" +
                "&openid.ns=http://specs.openid.net/auth/2.0" +
                "&openid.realm=https://" + WEBSITE +
                "&openid.return_to=https://" + WEBSITE + "";
        LoginWebView.loadUrl(url);
    }

}

