package app.main.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import app.main.R;

public class SplashActivity extends BaseActivity {

    private ConstraintLayout rootLayout;
    private ImageView logoImageView;
    private TextView appNameTextView;
    
    // Animation durations will be fetched from resources
    private int initialDelay;
    private int transitionDuration;
    private int rotationDuration;
    private int finalDelay;
    private int homeTransitionDuration;
    private float logoScaleFactor;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Initialize views
        rootLayout = findViewById(R.id.splash_root);
        logoImageView = findViewById(R.id.splash_logo);
        appNameTextView = findViewById(R.id.splash_app_name);
        
        // Load animation duration values from resources
        loadAnimationParameters();
        
        // Start the animation sequence after the initial delay
        new Handler(Looper.getMainLooper()).postDelayed(this::startAnimationSequence, initialDelay);
    }
    
    /**
     * Loads all animation parameters from resources
     * This allows easy adjustment through XML resources
     */
    private void loadAnimationParameters() {
        initialDelay = getResources().getInteger(R.integer.splash_initial_delay);
        transitionDuration = getResources().getInteger(R.integer.splash_transition_duration);
        rotationDuration = getResources().getInteger(R.integer.splash_rotation_duration);
        finalDelay = getResources().getInteger(R.integer.splash_final_delay);
        homeTransitionDuration = getResources().getInteger(R.integer.home_transition_duration);
        logoScaleFactor = getResources().getFloat(R.dimen.logo_scale_factor);
    }
    
    private void startAnimationSequence() {
        // Stage 1 to Stage 2: Change background color and move logo and text
        ObjectAnimator bgColorAnimation = ObjectAnimator.ofArgb(
                rootLayout, 
                "backgroundColor", 
                getResources().getColor(R.color.green, null),
                getResources().getColor(R.color.blue_50, null)
        );
        
        // Move logo to center
        ObjectAnimator logoTranslateX = ObjectAnimator.ofFloat(
                logoImageView,
                "translationX",
                0f,
                appNameTextView.getX() / 2 // Move halfway toward text position
        );
        
        // Push text out of screen
        ObjectAnimator textTranslateX = ObjectAnimator.ofFloat(
                appNameTextView,
                "translationX",
                0f,
                rootLayout.getWidth() // Move off screen to the right
        );
        
        // Fade out text
        ObjectAnimator textAlpha = ObjectAnimator.ofFloat(
                appNameTextView,
                "alpha",
                1f,
                0f
        );
        
        AnimatorSet stage1to2 = new AnimatorSet();
        stage1to2.playTogether(bgColorAnimation, logoTranslateX, textTranslateX, textAlpha);
        stage1to2.setDuration(transitionDuration);
        stage1to2.setInterpolator(new LinearInterpolator());
        
        // Stage 2 to Stage 3: Rotate and scale logo
        ObjectAnimator rotation = ObjectAnimator.ofFloat(
                logoImageView,
                "rotation",
                0f,
                360f
        );
        rotation.setDuration(rotationDuration);
        rotation.setInterpolator(new LinearInterpolator());
        
        // Create scale animation with custom behavior
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0f, 1f);
        scaleAnimator.setDuration(rotationDuration);
        scaleAnimator.setInterpolator(new LinearInterpolator());
        scaleAnimator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            float scale;
            
            if (fraction <= 0.5f) {
                // First half: Scale up to logoScaleFactor
                scale = 1f + ((logoScaleFactor - 1f) * (fraction / 0.5f));
            } else {
                // Second half: Scale down to original size
                scale = logoScaleFactor - ((logoScaleFactor - 1f) * ((fraction - 0.5f) / 0.5f));
            }
            
            logoImageView.setScaleX(scale);
            logoImageView.setScaleY(scale);
        });
        
        AnimatorSet stage2to3 = new AnimatorSet();
        stage2to3.playTogether(rotation, scaleAnimator);
        
        // Stage 3 to Stage 4: Change background color back to green and move logo and text back
        ObjectAnimator bgColorAnimation2 = ObjectAnimator.ofArgb(
                rootLayout, 
                "backgroundColor", 
                getResources().getColor(R.color.blue_50, null),
                getResources().getColor(R.color.green, null)
        );
        
        // Move logo back to original position
        ObjectAnimator logoTranslateXBack = ObjectAnimator.ofFloat(
                logoImageView,
                "translationX",
                appNameTextView.getX() / 2, // From center position
                0f // Back to original position
        );
        
        // Bring text back on screen
        ObjectAnimator textTranslateXBack = ObjectAnimator.ofFloat(
                appNameTextView,
                "translationX",
                rootLayout.getWidth(), // From off screen
                0f // Back to original position
        );
        
        // Fade in text
        ObjectAnimator textAlphaBack = ObjectAnimator.ofFloat(
                appNameTextView,
                "alpha",
                0f,
                1f
        );
        
        AnimatorSet stage3to4 = new AnimatorSet();
        stage3to4.playTogether(bgColorAnimation2, logoTranslateXBack, textTranslateXBack, textAlphaBack);
        stage3to4.setDuration(transitionDuration);
        stage3to4.setInterpolator(new LinearInterpolator());
        
        // Chain all animations in sequence
        AnimatorSet completeAnimation = new AnimatorSet();
        completeAnimation.play(stage1to2).before(stage2to3);
        completeAnimation.play(stage2to3).before(stage3to4);
        
        completeAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Wait for the final delay and then start the HomeActivity
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startHomeActivity();
                }, finalDelay);
            }
        });
        
        completeAnimation.start();
    }
    
    private void startHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        
        // Apply slide animations for transition to HomeActivity
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
} 