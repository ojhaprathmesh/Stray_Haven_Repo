package app.main.util;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import app.main.R;

/**
 * Helper class for Auth UI transitions and animations
 */
public class AuthUIHelper {

    private final Context context;

    public AuthUIHelper(Context context) {
        this.context = context;
    }

    /**
     * Switch to login mode with animation
     */
    public void switchToLoginMode(TextView loginTab, TextView signUpTab, Button actionButton,
                                  TextView forgotPassword, LinearLayout confirmPasswordLayout,
                                  EditText emailField, EditText passwordField) {
        // Change tab appearances
        loginTab.setTextColor(ContextCompat.getColor(context, R.color.white));
        loginTab.setBackgroundResource(R.drawable.bg_tab_selected);
        signUpTab.setTextColor(ContextCompat.getColor(context, R.color.black));
        signUpTab.setBackgroundResource(android.R.color.transparent);

        // Update button text
        actionButton.setText(R.string.btn_log_in);

        // Hide confirm password with animation
        Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Hide forgot password initially so it can be animated in
                forgotPassword.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Show forgot password option with a nice animation
                forgotPassword.setVisibility(View.VISIBLE);
                forgotPassword.setAlpha(0f);
                forgotPassword.setTranslationY(10f); // Slight offset to animate from

                // Animate in with fade and slight upward movement
                forgotPassword.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(300)
                        .setInterpolator(new android.view.animation.DecelerateInterpolator())
                        .start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeated
            }
        });

        confirmPasswordLayout.startAnimation(slideOut);
        confirmPasswordLayout.setVisibility(View.GONE);

        // Clear errors
        emailField.setError(null);
        passwordField.setError(null);
    }

    /**
     * Switch to signup mode with animation
     */
    public void switchToSignUpMode(TextView loginTab, TextView signUpTab, Button actionButton,
                                   TextView forgotPassword, LinearLayout confirmPasswordLayout,
                                   EditText emailField, EditText passwordField, EditText confirmPasswordField) {
        // Change tab appearances
        signUpTab.setTextColor(ContextCompat.getColor(context, R.color.white));
        signUpTab.setBackgroundResource(R.drawable.bg_tab_selected);
        loginTab.setTextColor(ContextCompat.getColor(context, R.color.black));
        loginTab.setBackgroundResource(android.R.color.transparent);

        // Update button text
        actionButton.setText(R.string.btn_sign_up);

        // Hide forgot password option immediately
        forgotPassword.setVisibility(View.GONE);

        // Show confirm password with animation
        confirmPasswordLayout.setVisibility(View.VISIBLE);
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        confirmPasswordLayout.startAnimation(slideIn);

        // Clear errors
        emailField.setError(null);
        passwordField.setError(null);
        confirmPasswordField.setError(null);
    }

    /**
     * Toggle the visibility of a password field
     */
    public boolean togglePasswordVisibility(EditText passwordField, ImageView toggleIcon, boolean isCurrentlyVisible) {
        if (isCurrentlyVisible) {
            // Hide password
            passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggleIcon.setImageResource(R.drawable.ic_eye);
        } else {
            // Show password
            passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            // Uncomment when eye-off icon is available
            // toggleIcon.setImageResource(R.drawable.ic_eye_off);
        }

        // Maintain cursor position
        passwordField.setSelection(passwordField.getText().length());

        // Return the new state
        return !isCurrentlyVisible;
    }

    /**
     * Update UI elements based on current mode
     */
    public void updateUIForMode(boolean isLoginMode, Button actionButton, TextView forgotPassword,
                                LinearLayout confirmPasswordLayout, TextView loginTab, TextView signUpTab) {
        if (isLoginMode) {
            actionButton.setText(R.string.btn_log_in);
            forgotPassword.setVisibility(View.VISIBLE);
            forgotPassword.setAlpha(1f); // Ensure full visibility
            confirmPasswordLayout.setVisibility(View.GONE);

            loginTab.setTextColor(ContextCompat.getColor(context, R.color.white));
            loginTab.setBackgroundResource(R.drawable.bg_tab_selected);
            signUpTab.setTextColor(ContextCompat.getColor(context, R.color.black));
            signUpTab.setBackgroundResource(android.R.color.transparent);
        } else {
            actionButton.setText(R.string.btn_sign_up);
            forgotPassword.setVisibility(View.GONE);
            confirmPasswordLayout.setVisibility(View.VISIBLE);

            signUpTab.setTextColor(ContextCompat.getColor(context, R.color.white));
            signUpTab.setBackgroundResource(R.drawable.bg_tab_selected);
            loginTab.setTextColor(ContextCompat.getColor(context, R.color.black));
            loginTab.setBackgroundResource(android.R.color.transparent);
        }
    }
} 