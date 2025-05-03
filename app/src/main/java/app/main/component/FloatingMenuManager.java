package app.main.component;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class FloatingMenuManager {
    private static final String TAG = "FloatingManager";
    private final View mainButton;
    private final View button1; // Straight up
    private final View button2; // 30 degrees
    private final View button3; // 60 degrees
    private final View button4; // Straight left

    private boolean isMenuOpen = false;
    private static final float TRANSLATION_DISTANCE = 400f; // Distance in dp

    public FloatingMenuManager(View mainButton, View button1, View button2, View button3, View button4) {
        this.mainButton = mainButton;
        this.button1 = button1;
        this.button2 = button2;
        this.button3 = button3;
        this.button4 = button4;

        setupClickListeners();
    }

    private void setupClickListeners() {
        mainButton.setOnClickListener(v -> toggleMenu());
    }

    public void toggleMenu() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }

        // Rotate the main button
        float startRotation = isMenuOpen ? 90f : 0f;
        float endRotation = isMenuOpen ? 0f : 90f;

        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(
                mainButton, "rotation", startRotation, endRotation);
        rotateAnimator.setDuration(300);
        rotateAnimator.start();

        isMenuOpen = !isMenuOpen;
    }

    private void openMenu() {
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);

        // Reset alpha for all buttons
        button1.setAlpha(0f);
        button2.setAlpha(0f);
        button3.setAlpha(0f);
        button4.setAlpha(0f);

        AnimatorSet animatorSet = new AnimatorSet();

        // Button 1 - Straight up (90 degrees)
        animatorSet.play(createButtonAnimator(button1, 0, -TRANSLATION_DISTANCE, 0, 0));

        // Button 2 - 30 degrees from vertical
        float translationX2 = (float) (-Math.sin(Math.toRadians(30)) * TRANSLATION_DISTANCE);
        float translationY2 = (float) (-Math.cos(Math.toRadians(30)) * TRANSLATION_DISTANCE);
        animatorSet.play(createButtonAnimator(button2, 0, translationY2, 0, translationX2));

        // Button 3 - 60 degrees from vertical
        float translationX3 = (float) (-Math.sin(Math.toRadians(60)) * TRANSLATION_DISTANCE);
        float translationY3 = (float) (-Math.cos(Math.toRadians(60)) * TRANSLATION_DISTANCE);
        animatorSet.play(createButtonAnimator(button3, 0, translationY3, 0, translationX3));
        Log.d(TAG, "X distance:" + String.valueOf(translationX3));

        // Button 4 - Straight left (180 degrees)
        animatorSet.play(createButtonAnimator(button4, 0, 0, 0, -TRANSLATION_DISTANCE));

        animatorSet.start();
    }

    private void closeMenu() {
        AnimatorSet animatorSet = new AnimatorSet();

        // Button 1 - Straight up
        animatorSet.play(createCloseButtonAnimator(button1, button1.getTranslationY(), 0, button1.getTranslationX(), 0));

        // Button 2 - 30 degrees
        animatorSet.play(createCloseButtonAnimator(button2, button2.getTranslationY(), 0, button2.getTranslationX(), 0));

        // Button 3 - 60 degrees
        animatorSet.play(createCloseButtonAnimator(button3, button3.getTranslationY(), 0, button3.getTranslationX(), 0));

        // Button 4 - Straight left
        animatorSet.play(createCloseButtonAnimator(button4, button4.getTranslationY(), 0, button4.getTranslationX(), 0));

        Log.d(TAG, "Button 4 close animation starting");

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isMenuOpen) {
                    button1.setVisibility(View.INVISIBLE);
                    button2.setVisibility(View.INVISIBLE);
                    button3.setVisibility(View.INVISIBLE);
                    button4.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animatorSet.start();
    }

    private AnimatorSet createButtonAnimator(View button,
                                             float startY, float endY,
                                             float startX, float endX) {
        ObjectAnimator translateY = ObjectAnimator.ofFloat(
                button, "translationY", startY, endY);

        ObjectAnimator translateX = ObjectAnimator.ofFloat(
                button, "translationX", startX, endX);

        // Always animate alpha from 0->1 for opening and 1->0 for closing
        boolean isOpening = startX == 0 && startY == 0;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(
                button, "alpha", isOpening ? 0f : 1f, isOpening ? 1f : 0f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(translateY, translateX, alpha);
        set.setDuration(300);

        // Use Overshoot interpolator for easeInBack effect (when menu opens)
        set.setInterpolator(new OvershootInterpolator(1.5f));

        return set;
    }

    private AnimatorSet createCloseButtonAnimator(View button,
                                                  float startY, float endY,
                                                  float startX, float endX) {
        ObjectAnimator translateY = ObjectAnimator.ofFloat(
                button, "translationY", startY, endY);

        ObjectAnimator translateX = ObjectAnimator.ofFloat(
                button, "translationX", startX, endX);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(
                button, "alpha", 1f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(translateY, translateX, alpha);
        set.setDuration(400); // Increase duration for more visible effect

        // Use AnticipateInterpolator for the proper ease-out back effect
        // The tension value controls how much "back" effect there is
        set.setInterpolator(new android.view.animation.AnticipateInterpolator(2.0f));

        return set;
    }

    // Set custom click listeners for each menu button
    public void setButton1ClickListener(View.OnClickListener listener) {
        button1.setOnClickListener(v -> {
            listener.onClick(v);
            toggleMenu();
        });
    }

    public void setButton2ClickListener(View.OnClickListener listener) {
        button2.setOnClickListener(v -> {
            listener.onClick(v);
            toggleMenu();
        });
    }

    public void setButton3ClickListener(View.OnClickListener listener) {
        button3.setOnClickListener(v -> {
            listener.onClick(v);
            toggleMenu();
        });
    }

    public void setButton4ClickListener(View.OnClickListener listener) {
        button4.setOnClickListener(v -> {
            listener.onClick(v);
            toggleMenu();
        });
    }
} 