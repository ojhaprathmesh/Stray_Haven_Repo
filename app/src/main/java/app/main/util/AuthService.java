package app.main.util;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

/**
 * Service class for authentication operations
 */
public class AuthService {

    private final Context context;
    private final PreferenceManager preferenceManager;

    // Interface for authentication callbacks
    public interface AuthCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public AuthService(Context context) {
        this.context = context;
        this.preferenceManager = new PreferenceManager(context);
    }

    /**
     * Login a user with email and password
     * This is currently a mock implementation
     */
    public void login(String email, String password, AuthCallback callback) {
        // TODO: Replace with actual API call
        // Example of what the implementation might look like:
        //
        // ApiClient.login(email, password, new ApiCallback() {
        //     @Override
        //     public void onSuccess(User user) {
        //         preferenceManager.setLoggedIn(true);
        //         preferenceManager.setUserEmail(email);
        //         preferenceManager.setUserId(user.getId());
        //         preferenceManager.setUserName(user.getName());
        //         callback.onSuccess();
        //     }
        //
        //     @Override
        //     public void onFailure(Exception e) {
        //         callback.onFailure(e.getMessage());
        //     }
        // });

        // For now, mock implementation
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= 6) {
            preferenceManager.setLoggedIn(true);
            preferenceManager.setUserEmail(email);
            callback.onSuccess();
        } else {
            callback.onFailure("Invalid email or password");
        }
    }

    /**
     * Register a new user
     * This is currently a mock implementation
     */
    public void register(String email, String password, AuthCallback callback) {
        // TODO: Replace with actual API call
        // Example of what the implementation might look like:
        //
        // ApiClient.register(email, password, new ApiCallback() {
        //     @Override
        //     public void onSuccess(User user) {
        //         preferenceManager.setLoggedIn(true);
        //         preferenceManager.setUserEmail(email);
        //         preferenceManager.setUserId(user.getId());
        //         preferenceManager.setUserName(user.getName());
        //         callback.onSuccess();
        //     }
        //
        //     @Override
        //     public void onFailure(Exception e) {
        //         callback.onFailure(e.getMessage());
        //     }
        // });

        // For now, mock implementation
        preferenceManager.setLoggedIn(true);
        preferenceManager.setUserEmail(email);
        callback.onSuccess();
    }

    /**
     * Send password reset email
     * This is currently a mock implementation
     */
    public void resetPassword(String email, AuthCallback callback) {
        // TODO: Replace with actual API call

        // For now, mock implementation
        if (AuthValidator.isValidEmail(email)) {
            Toast.makeText(context, "Password reset email sent to " + email, Toast.LENGTH_LONG).show();
            callback.onSuccess();
        } else {
            callback.onFailure("Please enter a valid email address");
        }
    }

    /**
     * Handle Google Sign In
     * This is currently a mock implementation
     */
    public void googleSignIn(AuthCallback callback) {
        // TODO: Implement Google Sign-In API integration
        Toast.makeText(context, "Google Sign In coming soon", Toast.LENGTH_SHORT).show();

        // For demo purposes, simulate successful login
        callback.onSuccess();
    }

    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return preferenceManager.isLoggedIn();
    }

    /**
     * Log out the current user
     */
    public void logOut() {
        preferenceManager.clearUserData();
    }
} 