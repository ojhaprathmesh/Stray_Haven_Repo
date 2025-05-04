package app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.main.R;
import app.main.util.SharedPreferencesManager;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // UI elements
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private ImageView ivTogglePassword;
    private TextView tvSignUpTab;
    private TextView tvForgotPassword;
    private ImageView btnGoogleSignIn;

    // Flag to track password visibility
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Set the content view to the login layout

        // Initialize UI components
        initializeViews();

        // Set up click listeners
        setupClickListeners();
    }

    /**
     * Initialize all the UI views from the layout
     */
    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        tvSignUpTab = findViewById(R.id.tvSignUpTab);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
    }

    /**
     * Set up click listeners for interactive elements
     */
    private void setupClickListeners() {
        // Login button click handler
        btnLogin.setOnClickListener(v -> attemptLogin());

        // Toggle password visibility
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        // Switch to sign up tab
        tvSignUpTab.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish(); // Close this activity when moving to signup
        });

        // Handle forgot password
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());

        // Google Sign In
        btnGoogleSignIn.setOnClickListener(v -> handleGoogleSignIn());
    }

    /**
     * Toggle the visibility of the password
     */
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.ic_eye); // Update with your eye icon resource
        } else {
            // Show password
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//            ivTogglePassword.setImageResource(R.drawable.ic_eye_off); // Update with your eye-off icon resource
        }
        isPasswordVisible = !isPasswordVisible;

        // Maintain cursor position
        etPassword.setSelection(etPassword.getText().length());
    }

    /**
     * Validate input and attempt login
     */
    private void attemptLogin() {
        // Get input values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (!validateInput(email, password)) {
            return;
        }

        // For demo purposes - replace with your authentication logic
        // In a real app, this would connect to a backend service
        if (authenticateUser(email, password)) {
            // Save login state
            saveLoginState(email);

            // Navigate to home screen
            navigateToHome();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Validate user input
     */
    private boolean validateInput(String email, String password) {
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
     * Mock authentication - replace with real authentication in production
     */
    private boolean authenticateUser(String email, String password) {
        // In a real app, replace this with actual authentication logic
        // For demo purposes, accept any valid-format email with password length >= 6
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= 6;
    }

    /**
     * Save login state to SharedPreferences
     */
    private void saveLoginState(String email) {
        // Use SharedPreferencesManager to save login state
        // This is a mock implementation - create this class in your project
        try {
            SharedPreferencesManager.setLoggedIn(this, true);
            SharedPreferencesManager.setUserEmail(this, email);
        } catch (Exception e) {
            Log.e(TAG, "Error saving login state: " + e.getMessage());
        }
    }

    /**
     * Navigate to the home screen
     */
    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Handle forgot password flow
     */
    private void handleForgotPassword() {
        // Implementation for password recovery
        // This could open a dialog or navigate to a password reset screen
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address first", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
        } else {
            // In a real app, initiate password reset process
            Toast.makeText(this, "Password reset email sent to " + email, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Handle Google Sign In
     */
    private void handleGoogleSignIn() {
        // Implement Google Sign In logic
        // This would typically use the Google Sign-In API
        Toast.makeText(this, "Google Sign In coming soon", Toast.LENGTH_SHORT).show();

        // For demo purposes, simulate successful login
        navigateToHome();
    }
}