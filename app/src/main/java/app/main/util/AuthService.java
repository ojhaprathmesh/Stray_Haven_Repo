package app.main.util;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Service class for authentication operations using Firebase Auth
 */
public class AuthService {

    private final Context context;
    private final PreferenceManager preferenceManager;
    private final FirebaseAuth firebaseAuth;

    // Interface for authentication callbacks
    public interface AuthCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public AuthService(Context context) {
        this.context = context;
        this.preferenceManager = new PreferenceManager(context);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Login a user with email and password using Firebase Auth
     */
    public void login(String email, String password, final AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Save user data
                            saveUserData(firebaseUser);
                            callback.onSuccess();
                        } else {
                            callback.onFailure("Authentication failed");
                        }
                    } else {
                        // Handle specific error cases
                        String errorMessage = getAuthErrorMessage(task.getException());
                        callback.onFailure(errorMessage);
                    }
                });
    }

    /**
     * Register a new user with email and password using Firebase Auth
     */
    public void register(String email, String password, final AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Save user data
                            saveUserData(firebaseUser);
                            callback.onSuccess();
                        } else {
                            callback.onFailure("Registration failed");
                        }
                    } else {
                        // Handle specific error cases
                        String errorMessage = getAuthErrorMessage(task.getException());
                        callback.onFailure(errorMessage);
                    }
                });
    }

    /**
     * Send password reset email using Firebase Auth
     */
    public void resetPassword(String email, final AuthCallback callback) {
        if (!AuthValidator.isValidEmail(email)) {
            callback.onFailure("Please enter a valid email address");
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Password reset email sent to " + email, Toast.LENGTH_LONG).show();
                        callback.onSuccess();
                    } else {
                        String errorMessage = getAuthErrorMessage(task.getException());
                        callback.onFailure(errorMessage);
                    }
                });
    }

    /**
     * Handle Google Sign In with Firebase Auth
     * This method should be called after the GoogleSignIn intent result
     * 
     * @param idToken     ID token obtained from GoogleSignIn
     * @param email       Email address from the Google account
     * @param displayName Display name from the Google account
     * @param callback    Callback to handle success/failure
     */
    public void firebaseAuthWithGoogle(String idToken, String email, String displayName, final AuthCallback callback) {
        // If ID token is available, use it for Firebase authentication
        if (idToken != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Save user data
                                saveUserData(firebaseUser);
                                callback.onSuccess();
                            } else {
                                callback.onFailure("Authentication failed");
                            }
                        } else {
                            String errorMessage = getAuthErrorMessage(task.getException());
                            callback.onFailure(errorMessage);
                        }
                    });
        } 
        // If no ID token is available but we have email, try to use that as a fallback
        // This is not secure for production but allows development/testing
        else if (email != null) {
            // Create a temporary authentication for development purposes
            // In a real app, this should be properly authenticated with Firebase
            preferenceManager.setLoggedIn(true);
            preferenceManager.setUserEmail(email);
            if (displayName != null) {
                preferenceManager.setUserName(displayName);
            }
            callback.onSuccess();
            Toast.makeText(context, "Limited Google Sign-In successful (development mode)", Toast.LENGTH_SHORT).show();
        } else {
            callback.onFailure("Google Sign In failed: Account information not available");
        }
    }

    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    /**
     * Get current user
     */
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    /**
     * Log out the current user
     */
    public void logOut() {
        firebaseAuth.signOut();
        preferenceManager.clearUserData();
    }

    /**
     * Save user data to SharedPreferences
     */
    private void saveUserData(FirebaseUser firebaseUser) {
        preferenceManager.setLoggedIn(true);
        preferenceManager.setUserEmail(firebaseUser.getEmail());
        preferenceManager.setUserId(firebaseUser.getUid());

        // Save display name if available
        if (firebaseUser.getDisplayName() != null) {
            preferenceManager.setUserName(firebaseUser.getDisplayName());
        }
    }

    /**
     * Get a user-friendly error message for Firebase Auth exceptions
     */
    private String getAuthErrorMessage(Exception exception) {
        if (exception == null) {
            return "Authentication failed";
        }

        if (exception instanceof FirebaseAuthInvalidUserException) {
            return "No account found with this email";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "Invalid email or password";
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return "Email is already in use";
        } else {
            return exception.getMessage();
        }
    }
} 