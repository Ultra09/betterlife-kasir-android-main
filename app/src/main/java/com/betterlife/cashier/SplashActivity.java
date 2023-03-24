package com.betterlife.cashier;

import android.Manifest;
import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.betterlife.cashier.utils.Shared;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

public class SplashActivity extends Activity {
    private ImageView logo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shared.initialize(getBaseContext());
        setContentView(R.layout.activity_splash);
        logo1 = (ImageView) findViewById(R.id.logo1);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                YoYo.with(Techniques.FlipOutX).duration(1000).withListener(new AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator arg0) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationRepeat(Animator arg0) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationEnd(Animator arg0) {
                        // TODO Auto-generated method stub
                        YoYo.with(Techniques.FlipInX).duration(1000).withListener(new AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator arg0) {
                                // TODO Auto-generated method stub
                                logo1.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animator arg0) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onAnimationEnd(Animator arg0) {
                                // TODO Auto-generated method stub
                                YoYo.with(Techniques.TakingOff).duration(1000).delay(3000).withListener(new AnimatorListener() {

                                    @Override
                                    public void onAnimationStart(Animator arg0) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator arg0) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator arg0) {
                                        // TODO Auto-generated method stub
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                                            } else {
                                                goNext();
                                            }
                                        } else {
                                            goNext();
                                        }
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator arg0) {
                                        // TODO Auto-generated method stub
                                    }
                                }).playOn(findViewById(R.id.logo1));
                            }

                            @Override
                            public void onAnimationCancel(Animator arg0) {
                                // TODO Auto-generated method stub
                            }
                        }).playOn(findViewById(R.id.logo1));
                    }

                    @Override
                    public void onAnimationCancel(Animator arg0) {
                        // TODO Auto-generated method stub
                    }
                }).playOn(findViewById(R.id.logo1));
            }
        }, 3000);


    }

    private void goNext() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(anim.fade_in, anim.fade_out);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allgranted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
                allgranted = false;
            }
        }

        if (!allgranted) {
            Toast.makeText(SplashActivity.this, "All permission are required", Toast.LENGTH_LONG).show();
            finish();
        } else {
            goNext();
        }

    }
}
