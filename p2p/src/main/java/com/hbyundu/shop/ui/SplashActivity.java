package com.hbyundu.shop.ui;

import android.content.Intent;
import android.os.Bundle;

import com.hbyundu.shop.R;
import com.hbyundu.shop.ui.home.HomeActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        enterHome();
    }

    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}