package app.main.util;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

/**
 * Utility class for validating authentication inputs
 */
public class AuthValidator {

    /**
     * Validate login input fields
     *
     * @param emailField    Email EditText field
     * @param passwordField Password EditText field
     * @return true if all inputs are valid
     */
    public static boolean validateLoginInput(EditText emailField, EditText passwordField) {
        boolean isValid = true;
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Check email
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Please enter a valid email");
            isValid = false;
        } else {
            emailField.setError(null);
        }

        // Check password
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            passwordField.setError(null);
        }

        return isValid;
    }

    /**
     * Validate signup input fields
     *
     * @param emailField           Email EditText field
     * @param passwordField        Password EditText field
     * @param confirmPasswordField Confirm password EditText field
     * @return true if all inputs are valid
     */
    public static boolean validateSignupInput(EditText emailField, EditText passwordField, EditText confirmPasswordField) {
        boolean isValid = true;
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        // Check email
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Please enter a valid email");
            isValid = false;
        } else {
            emailField.setError(null);
        }

        // Check password
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            passwordField.setError(null);
        }

        // Check confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordField.setError("Please confirm your password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordField.setError("Passwords do not match");
            isValid = false;
        } else {
            confirmPasswordField.setError(null);
        }

        return isValid;
    }

    /**
     * Validates if an email string is properly formatted
     *
     * @param email The email string to validate
     * @return true if email is valid
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
} 