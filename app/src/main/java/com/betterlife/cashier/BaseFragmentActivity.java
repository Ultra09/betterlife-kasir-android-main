package com.betterlife.cashier;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseFragmentActivity extends FragmentActivity {
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