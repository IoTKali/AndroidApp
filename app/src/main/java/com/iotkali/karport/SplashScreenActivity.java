package com.iotkali.karport;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private User user;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);
        getActionBar().hide();
        user = new User(getSharedPreferences("User", Context.MODE_PRIVATE));

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(!user.isOnline()){
                    Intent mainIntent = new Intent(SplashScreenActivity.this,PreLoginActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }else{
                    Intent mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}