package app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.main.R;
import app.main.util.SharedPreferencesManager;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    // UI elements
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnSignUp;
    private ImageView ivTogglePassword;
    private ImageView ivToggleConfirmPassword;
    private TextView tvLoginTab;
    private ImageView btnGoogleSignIn;

    // Flags to track password visibility
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // Set the content view to signup layout

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
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirmPassword);
        tvLoginTab = findViewById(R.id.tvLoginTab);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
    }

    /**
     * Set up click listeners for interactive elements
     */
    private void setupClickListeners() {
        // Sign Up button click handler
        btnSignUp.setOnClickListener(v -> attemptSignUp());

        // Toggle password visibility
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility(etPassword, ivTogglePassword, isPasswordVisible));

        // Toggle confirm password visibility
        ivToggleConfirmPassword.setOnClickListener(v -> toggleConfirmPasswordVisibility());

        // Switch to login tab
        tvLoginTab.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close this activity when moving to login
        });

        // Google Sign In
        btnGoogleSignIn.setOnClickListener(v -> handleGoogleSignIn());
    }

    /**
     * Toggle the visibility of the password field
     */
    private void togglePasswordVisibility(EditText passwordField, ImageView toggleIcon, boolean isVisible) {
        if (isVisible) {
            // Hide password
            passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggleIcon.setImageResource(R.drawable.ic_eye); // Update with your eye icon resource
        } else {
            // Show password
            passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//            toggleIcon.setImageResource(R.drawable.ic_eye_off); // Update with your eye-off icon resource
        }

        // Maintain cursor position
        passwordField.setSelection(passwordField.getText().length());

        isPasswordVisible = !isPasswordVisible;
    }

    /**
     * Toggle the visibility of the confirm password field
     */
    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            // Hide password
            etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye); // Update with your eye icon resource
        } else {
            // Show password
            etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//            ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye_off); // Update with your eye-off icon resource
        }

        // Maintain cursor position
        etConfirmPassword.setSelection(etConfirmPassword.getText().length());

        isConfirmPasswordVisible = !isConfirmPasswordVisible;
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
        if (!validateInput(email, password, confirmPassword)) {
            return;
        }

        // For demo purposes - replace with your registration logic
        // In a real app, this would connect to a backend service
        if (registerUser(email, password)) {
            // Save login state
            saveLoginState(email);

            // Navigate to home screen
            navigateToHome();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Validate user input
     */
    private boolean validateInput(String email, String password, String confirmPassword) {
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
     * Mock registration - replace with real registration in production
     */
    private boolean registerUser(String email, String password) {
        // In a real app, replace this with actual registration logic
        // For demo purposes, accept any valid registration
        return true;
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
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Handle Google Sign In
     */
    private void handleGoogleSignIn() {
        // Implement Google Sign In logic
        // This would typically use the Google Sign-In API
        Toast.makeText(this, "Google Sign Up coming soon", Toast.LENGTH_SHORT).show();

        // For demo purposes, simulate successful registration
        navigateToHome();
    }
}