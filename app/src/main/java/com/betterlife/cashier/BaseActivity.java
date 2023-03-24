package com.betterlife.cashier;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
    String currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentLang = getResources().getConfiguration().locale.getLanguage();
    }

    @Override
    protected void onResume() {
        if(!currentLang.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLang = getResources().getConfiguration().locale.getLanguage();

            recreate();
        }

        super.onResume();
    }
}