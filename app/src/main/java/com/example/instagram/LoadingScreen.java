package com.example.instagram;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.Transformation;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

public class LoadingScreen extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
//        Objects.requireNonNull(getSupportActionBar()).hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar=findViewById(R.id.progress_bar);
        textView=findViewById(R.id.text_view);
        LottieAnimationView slideImageView=(LottieAnimationView)findViewById(R.id.img);
        slideImageView.setSpeed(0.6f);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);
        progressBarAnimation();
    }

    private void progressBarAnimation() {
        ProgressBarAnimation progressBarAnimation=new ProgressBarAnimation(this,progressBar,textView,0f,100f);
        progressBarAnimation.setDuration(3000);
        progressBar.setAnimation(progressBarAnimation);

    }




}