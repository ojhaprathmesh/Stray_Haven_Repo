package app.main.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.main.R;

/**
 * AuthActivity combines login and signup functionality in one screen
 * with smooth transitions between the two modes.
 */
public class AuthActivity extends BaseActivity {

    private static final String TAG = "AuthActivity";

    // UI elements
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnAction;
    private ImageView ivTogglePassword;
    private ImageView ivToggleConfirmPassword;
    private TextView tvLoginTab;
    private TextView tvSignUpTab;
    private TextView tvForgotPassword;
    private ImageView btnGoogleSignIn;
    private LinearLayout confirmPasswordLayout;

    // Authentication state
    private boolean isLoginMode = true;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    // SharedPreferences constants
    private static final String PREF_NAME = "StrayHavenPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Disable animations as early as possible
        if (getIntent().getBooleanExtra("FROM_SPLASH", false)) {
            overridePendingTransition(0, 0);
        }

        super.onCreate(savedInstanceState);

        // Apply a different entrance transition if coming from splash
        boolean fromSplash = getIntent().getBooleanExtra("FROM_SPLASH", false);
        if (fromSplash) {
            // If coming from splash, don't animate
            overridePendingTransition(0, 0);
        }

        setContentView(R.layout.activity_auth);

        // Check if user is already logged in
        if (isLoggedIn()) {
            navigateToHome();
            return;
        }

        // Initialize UI components
        initializeViews();

        // Set up click listeners
        setupClickListeners();

        // Initial UI setup based on mode
        updateUIForMode();
    }

    @Override
    public void onAttachedToWindow() {
        // Disable animations when window is attached
        if (getIntent().getBooleanExtra("FROM_SPLASH", false)) {
            overridePendingTransition(0, 0);
        }
        super.onAttachedToWindow();
    }

    /**
     * Initialize all the UI views from the layout
     */
    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnAction = findViewById(R.id.btnAction);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirmPassword);
        tvLoginTab = findViewById(R.id.tvLoginTab);
        tvSignUpTab = findViewById(R.id.tvSignUpTab);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
    }

    /**
     * Set up click listeners for interactive elements
     */
    private void setupClickListeners() {
        // Action button click handler (Login or Signup)
        btnAction.setOnClickListener(v -> handleAuthAction());

        // Toggle password visibility
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        // Toggle confirm password visibility
        ivToggleConfirmPassword.setOnClickListener(v -> toggleConfirmPasswordVisibility());

        // Switch between login and signup tabs
        tvLoginTab.setOnClickListener(v -> switchToLoginMode());
        tvSignUpTab.setOnClickListener(v -> switchToSignUpMode());

        // Handle forgot password
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());

        // Google Sign In
        btnGoogleSignIn.setOnClickListener(v -> handleGoogleSignIn());
    }

    /**
     * Switch to login mode with animation
     */
    private void switchToLoginMode() {
        if (!isLoginMode) {
            isLoginMode = true;

            // Change tab appearances
            tvLoginTab.setTextColor(getResources().getColor(android.R.color.white));
            tvLoginTab.setBackgroundResource(R.drawable.bg_tab_selected);
            tvSignUpTab.setTextColor(getResources().getColor(android.R.color.black));
            tvSignUpTab.setBackgroundResource(android.R.color.transparent);

            // Update button text
            btnAction.setText("Log In");

            // Hide confirm password with animation
            Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // Hide forgot password initially so it can be animated in
                    tvForgotPassword.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Show forgot password option with a nice animation
                    tvForgotPassword.setVisibility(View.VISIBLE);
                    tvForgotPassword.setAlpha(0f);
                    tvForgotPassword.setTranslationY(10f); // Slight offset to animate from

                    // Animate in with fade and slight upward movement
                    tvForgotPassword.animate()
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
            etEmail.setError(null);
            etPassword.setError(null);
        }
    }

    /**
     * Switch to signup mode with animation
     */
    private void switchToSignUpMode() {
        if (isLoginMode) {
            isLoginMode = false;

            // Change tab appearances
            tvSignUpTab.setTextColor(getResources().getColor(android.R.color.white));
            tvSignUpTab.setBackgroundResource(R.drawable.bg_tab_selected);
            tvLoginTab.setTextColor(getResources().getColor(android.R.color.black));
            tvLoginTab.setBackgroundResource(android.R.color.transparent);

            // Update button text
            btnAction.setText("Sign Up");

            // Hide forgot password option immediately
            tvForgotPassword.setVisibility(View.GONE);

            // Show confirm password with animation
            confirmPasswordLayout.setVisibility(View.VISIBLE);
            Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            confirmPasswordLayout.startAnimation(slideIn);

            // Clear errors
            etEmail.setError(null);
            etPassword.setError(null);
            etConfirmPassword.setError(null);
        }
    }

    /**
     * Update UI elements based on current mode
     */
    private void updateUIForMode() {
        if (isLoginMode) {
            btnAction.setText("Log In");
            tvForgotPassword.setVisibility(View.VISIBLE);
            tvForgotPassword.setAlpha(1f); // Ensure full visibility
            confirmPasswordLayout.setVisibility(View.GONE);

            tvLoginTab.setTextColor(getResources().getColor(android.R.color.white));
            tvLoginTab.setBackgroundResource(R.drawable.bg_tab_selected);
            tvSignUpTab.setTextColor(getResources().getColor(android.R.color.black));
            tvSignUpTab.setBackgroundResource(android.R.color.transparent);
        } else {
            btnAction.setText("Sign Up");
            tvForgotPassword.setVisibility(View.GONE);
            confirmPasswordLayout.setVisibility(View.VISIBLE);

            tvSignUpTab.setTextColor(getResources().getColor(android.R.color.white));
            tvSignUpTab.setBackgroundResource(R.drawable.bg_tab_selected);
            tvLoginTab.setTextColor(getResources().getColor(android.R.color.black));
            tvLoginTab.setBackgroundResource(android.R.color.transparent);
        }
    }

    /**
     * Handle the authentication action (login or signup)
     */
    private void handleAuthAction() {
        if (isLoginMode) {
            attemptLogin();
        } else {
            attemptSignUp();
        }
    }

    /**
     * Toggle the visibility of the password
     */
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.ic_eye);
        } else {
            // Show password
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            // Uncomment when eye-off icon is available
            // ivTogglePassword.setImageResource(R.drawable.ic_eye_off);
        }
        isPasswordVisible = !isPasswordVisible;

        // Maintain cursor position
        etPassword.setSelection(etPassword.getText().length());
    }

    /**
     * Toggle the visibility of the confirm password field
     */
    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            // Hide password
            etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye);
        } else {
            // Show password
            etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            // Uncomment when eye-off icon is available
            // ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye_off);
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible;

        // Maintain cursor position
        etConfirmPassword.setSelection(etConfirmPassword.getText().length());
    }

    /**
     * Validate input and attempt login
     */
    private void attemptLogin() {
        // Get input values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (!validateLoginInput(email, password)) {
            return;
        }

        // Call authentication service
        authenticateUser(email, password);
    }

    /**
     * Validate login input fields
     */
    private boolean validateLoginInput(String email, String password) {
        boolean isValid = true;

        // Check email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        // Check password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        return isValid;
    }

    /**
     * Validate input and attempt registration
     */
    private void attemptSignUp() {
        // Get input values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate input
        if (!validateSignupInput(email, password, confirmPassword)) {
            return;
        }

        // Call registration service
        registerUser(email, password);
    }

    /**
     * Validate signup input fields
     */
    private boolean validateSignupInput(String email, String password, String confirmPassword) {
        boolean isValid = true;

        // Check email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        // Check password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        // Check confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm your password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            isValid = false;
        } else {
            etConfirmPassword.setError(null);
        }

        return isValid;
    }

    /**
     * Authenticate user (will be replaced with actual API call)
     */
    private void authenticateUser(String email, String password) {
        // TODO: Replace with actual API call
        // Example of what the implementation might look like:
        //
        // ApiClient.login(email, password, new AuthCallback() {
        //     @Override
        //     public void onSuccess(User user) {
        //         saveLoginState(email, user.getId(), user.getName());
        //         navigateToHome();
        //     }
        //
        //     @Override
        //     public void onFailure(Exception e) {
        //         Toast.makeText(AuthActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        //     }
        // });

        // For now, mock implementation
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= 6) {
            setLoggedIn(true);
            setUserEmail(email);
            navigateToHome();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Register a new user (will be replaced with actual API call)
     */
    private void registerUser(String email, String password) {
        // TODO: Replace with actual API call
        // Example of what the implementation might look like:
        //
        // ApiClient.register(email, password, new AuthCallback() {
        //     @Override
        //     public void onSuccess(User user) {
        //         saveLoginState(email, user.getId(), user.getName());
        //         navigateToHome();
        //     }
        //
        //     @Override
        //     public void onFailure(Exception e) {
        //         Toast.makeText(AuthActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        //     }
        // });

        // For now, mock implementation
        setLoggedIn(true);
        setUserEmail(email);
        navigateToHome();
    }

    /**
     * Navigate to the home screen
     */
    private void navigateToHome() {
        Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Handle forgot password flow
     */
    private void handleForgotPassword() {
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address first", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
        } else {
            // TODO: Replace with actual API call for password reset
            Toast.makeText(this, "Password reset email sent to " + email, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Handle Google Sign In
     */
    private void handleGoogleSignIn() {
        // TODO: Implement Google Sign-In API integration
        Toast.makeText(this, "Google Sign In coming soon", Toast.LENGTH_SHORT).show();

        // For demo purposes, simulate successful login
        navigateToHome();
    }

    // ----- SharedPreferences Methods -----

    /**
     * Get the SharedPreferences instance
     */
    private SharedPreferences getSharedPreferences() {
        return getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Save the login status
     */
    private void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    /**
     * Check if user is logged in
     */
    private boolean isLoggedIn() {
        return getSharedPreferences().getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Save user email
     */
    private void setUserEmail(String email) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    /**
     * Get stored user email
     */
    private String getUserEmail() {
        return getSharedPreferences().getString(KEY_USER_EMAIL, "");
    }

    /**
     * Save user name
     */
    private void setUserName(String name) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    /**
     * Get stored user name
     */
    private String getUserName() {
        return getSharedPreferences().getString(KEY_USER_NAME, "");
    }

    /**
     * Save user ID
     */
    private void setUserId(String userId) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    /**
     * Get stored user ID
     */
    private String getUserId() {
        return getSharedPreferences().getString(KEY_USER_ID, "");
    }

    /**
     * Clear all user data from SharedPreferences (logout)
     */
    private void clearUserData() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.apply();
    }
} 