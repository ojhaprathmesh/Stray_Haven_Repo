package app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.main.R;
import app.main.util.AuthService;
import app.main.util.AuthUIHelper;
import app.main.util.AuthValidator;

/**
 * AuthActivity combines login and signup functionality in one screen
 * with smooth transitions between the two modes.
 */
public class AuthActivity extends BaseActivity {

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

    // Helpers and services
    private AuthService authService;
    private AuthUIHelper authUIHelper;

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

        // Initialize helpers and services
        authService = new AuthService(this);
        authUIHelper = new AuthUIHelper(this);

        // Check if user is already logged in
        if (authService.isUserLoggedIn()) {
            navigateToHome();
            return;
        }

        // Initialize UI components
        initializeViews();

        // Set up click listeners
        setupClickListeners();

        // Initial UI setup based on mode
        authUIHelper.updateUIForMode(isLoginMode, btnAction, tvForgotPassword,
                confirmPasswordLayout, tvLoginTab, tvSignUpTab);
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
            authUIHelper.switchToLoginMode(tvLoginTab, tvSignUpTab, btnAction,
                    tvForgotPassword, confirmPasswordLayout,
                    etEmail, etPassword);
        }
    }

    /**
     * Switch to signup mode with animation
     */
    private void switchToSignUpMode() {
        if (isLoginMode) {
            isLoginMode = false;
            authUIHelper.switchToSignUpMode(tvLoginTab, tvSignUpTab, btnAction,
                    tvForgotPassword, confirmPasswordLayout,
                    etEmail, etPassword, etConfirmPassword);
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
        isPasswordVisible = authUIHelper.togglePasswordVisibility(
                etPassword, ivTogglePassword, isPasswordVisible);
    }

    /**
     * Toggle the visibility of the confirm password field
     */
    private void toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = authUIHelper.togglePasswordVisibility(
                etConfirmPassword, ivToggleConfirmPassword, isConfirmPasswordVisible);
    }

    /**
     * Validate input and attempt login
     */
    private void attemptLogin() {
        // Get input values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (!AuthValidator.validateLoginInput(etEmail, etPassword)) {
            return;
        }

        // Call authentication service
        authService.login(email, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess() {
                navigateToHome();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Validate input and attempt registration
     */
    private void attemptSignUp() {
        // Get input values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (!AuthValidator.validateSignupInput(etEmail, etPassword, etConfirmPassword)) {
            return;
        }

        // Call registration service
        authService.register(email, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess() {
                navigateToHome();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
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

        authService.resetPassword(email, new AuthService.AuthCallback() {
            @Override
            public void onSuccess() {
                // Success is handled with Toast in the AuthService
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                etEmail.requestFocus();
            }
        });
    }

    /**
     * Handle Google Sign In
     */
    private void handleGoogleSignIn() {
        authService.googleSignIn(new AuthService.AuthCallback() {
            @Override
            public void onSuccess() {
                navigateToHome();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
} 