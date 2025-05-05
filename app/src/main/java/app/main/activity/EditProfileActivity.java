package app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import app.main.R;
import app.main.util.AuthService;
import app.main.util.PreferenceManager;

public class EditProfileActivity extends BaseActivity {

    private LinearLayout personalDetailsContent, taxDetailsContent;
    private ImageView personalDetailsToggle, taxDetailsToggle;
    private boolean isPersonalDetailsExpanded = false;
    private boolean isTaxDetailsExpanded = false;

    private EditText editName, editMobile, editEmail;
    private EditText editPan, editDob, editNationality, editAddress;
    private TextView profileInitial;

    private AuthService authService;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore firestore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase services
        authService = new AuthService(this);
        preferenceManager = new PreferenceManager(this);
        firestore = FirebaseFirestore.getInstance();

        // Get current user ID
        FirebaseUser currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            // User is not logged in, redirect to login
            navigateToLogin();
            return;
        }

        // Set up UI with common configurations
        setupUI(findViewById(android.R.id.content));

        // Initialize views
        initViews();

        // Set up click listeners
        setupListeners();

        // Load user data
        loadUserData();

        // Handle back button
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void initViews() {
        // Get section views
        personalDetailsContent = findViewById(R.id.personal_details_content);
        taxDetailsContent = findViewById(R.id.tax_details_content);
        personalDetailsToggle = findViewById(R.id.personal_details_toggle);
        taxDetailsToggle = findViewById(R.id.tax_details_toggle);

        // Get input fields
        editName = findViewById(R.id.edit_name);
        editMobile = findViewById(R.id.edit_mobile);
        editEmail = findViewById(R.id.edit_email);
        editPan = findViewById(R.id.edit_pan);
        editDob = findViewById(R.id.edit_dob);
        editNationality = findViewById(R.id.edit_nationality);
        editAddress = findViewById(R.id.edit_address);
        profileInitial = findViewById(R.id.profile_initial);
    }

    private void setupListeners() {
        // Back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Personal details section toggle
        LinearLayout personalDetailsHeader = findViewById(R.id.personal_details_header);
        personalDetailsHeader.setOnClickListener(v -> togglePersonalDetails());

        // Tax details section toggle
        LinearLayout taxDetailsHeader = findViewById(R.id.tax_details_header);
        taxDetailsHeader.setOnClickListener(v -> toggleTaxDetails());

        // Date of birth field - show date picker when clicked
        EditText dobField = findViewById(R.id.edit_dob);
        dobField.setOnClickListener(v -> showDatePicker(dobField));

        // Edit profile image
        ImageView editProfileImage = findViewById(R.id.edit_profile_image);
        editProfileImage.setOnClickListener(v -> selectProfileImage());

        // Delete account option
        LinearLayout deleteAccountOption = findViewById(R.id.delete_account_option);
        deleteAccountOption.setOnClickListener(v -> showDeleteConfirmation());

        // Logout option
        LinearLayout logoutOption = findViewById(R.id.logout_option);
        logoutOption.setOnClickListener(v -> showLogoutConfirmation());

        // Save button
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> saveProfileChanges());
    }

    private void togglePersonalDetails() {
        isPersonalDetailsExpanded = !isPersonalDetailsExpanded;
        personalDetailsContent.setVisibility(isPersonalDetailsExpanded ? View.VISIBLE : View.GONE);
        personalDetailsToggle.setRotation(isPersonalDetailsExpanded ? 180 : 0);
    }

    private void toggleTaxDetails() {
        isTaxDetailsExpanded = !isTaxDetailsExpanded;
        taxDetailsContent.setVisibility(isTaxDetailsExpanded ? View.VISIBLE : View.GONE);
        taxDetailsToggle.setRotation(isTaxDetailsExpanded ? 180 : 0);
    }

    private void loadUserData() {
        // Expand personal details section by default
        togglePersonalDetails();

        // Get the current Firebase user
        FirebaseUser currentUser = authService.getCurrentUser();

        if (currentUser != null) {
            // Set email (cannot be edited)
            String email = currentUser.getEmail();
            if (email != null && !email.isEmpty()) {
                editEmail.setText(email);
                editEmail.setEnabled(false); // Email cannot be changed
            }

            // Set initial name from Firebase Auth
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                editName.setText(displayName);

                // Set profile initial
                profileInitial.setText(String.valueOf(displayName.charAt(0)).toUpperCase());
            } else {
                String nameFromPrefs = preferenceManager.getUserName();
                if (!nameFromPrefs.isEmpty()) {
                    editName.setText(nameFromPrefs);
                    profileInitial.setText(String.valueOf(nameFromPrefs.charAt(0)).toUpperCase());
                }
            }

            // Get additional user data from Firestore
            firestore.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Populate form fields with Firestore data
                            populateFieldsFromFirestore(documentSnapshot);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to load profile data", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // User is not logged in, fallback to preferences
            String nameFromPrefs = preferenceManager.getUserName();
            String emailFromPrefs = preferenceManager.getUserEmail();

            if (!nameFromPrefs.isEmpty()) {
                editName.setText(nameFromPrefs);
                profileInitial.setText(String.valueOf(nameFromPrefs.charAt(0)).toUpperCase());
            }

            if (!emailFromPrefs.isEmpty()) {
                editEmail.setText(emailFromPrefs);
            }
        }
    }

    private void populateFieldsFromFirestore(DocumentSnapshot document) {
        // Fill in personal details
        if (document.contains("name") && document.getString("name") != null) {
            editName.setText(document.getString("name"));

            // Update initial
            String name = document.getString("name");
            if (!name.isEmpty()) {
                profileInitial.setText(String.valueOf(name.charAt(0)).toUpperCase());
            }
        }

        if (document.contains("mobile")) {
            editMobile.setText(document.getString("mobile"));
        }

        // Fill in tax details
        if (document.contains("pan_number")) {
            editPan.setText(document.getString("pan_number"));
        }

        if (document.contains("dob")) {
            editDob.setText(document.getString("dob"));
        }

        if (document.contains("nationality")) {
            editNationality.setText(document.getString("nationality"));
        }

        if (document.contains("address")) {
            editAddress.setText(document.getString("address"));
        }
    }

    private void showDatePicker(EditText dateField) {
        // Create a DatePickerDialog
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // Format the date as DD-MM-YYYY
                    String formattedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year);
                    dateField.setText(formattedDate);
                },
                2000, 0, 1); // Default date (Jan 1, 2000)

        datePickerDialog.show();
    }

    private void selectProfileImage() {
        // In a real app, you would show an image selection dialog or launch camera/gallery intent
        // For simplicity, we're not implementing the full functionality in this demo
        Toast.makeText(this, "Profile image selection coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Delete the user account from Firebase Auth and Firestore
            deleteUserAccount();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteUserAccount() {
        FirebaseUser user = authService.getCurrentUser();
        if (user != null) {
            // First delete Firestore data
            firestore.collection("users").document(userId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Then delete Firebase Auth account
                        user.delete()
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(EditProfileActivity.this,
                                            "Account deleted successfully", Toast.LENGTH_SHORT).show();

                                    // Clear local data
                                    authService.logOut();

                                    // Navigate to login screen
                                    navigateToLogin();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(EditProfileActivity.this,
                                            "Failed to delete account: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditProfileActivity.this,
                                "Failed to delete account data: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void showLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", (dialog, which) -> {
            // Perform logout
            authService.logOut();
            // Navigate to login screen
            navigateToLogin();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(EditProfileActivity.this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void saveProfileChanges() {
        // Get values from form
        String name = editName.getText().toString().trim();
        String mobile = editMobile.getText().toString().trim();
        String pan = editPan.getText().toString().trim();
        String dob = editDob.getText().toString().trim();
        String nationality = editNationality.getText().toString().trim();
        String address = editAddress.getText().toString().trim();

        // Validate input
        if (name.isEmpty()) {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }

        // Create data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("mobile", mobile);
        userData.put("pan_number", pan);
        userData.put("dob", dob);
        userData.put("nationality", nationality);
        userData.put("address", address);
        userData.put("updated_at", new java.util.Date());

        // Save to Firestore
        firestore.collection("users").document(userId)
                .update(userData)
                .addOnSuccessListener(aVoid -> {
                    // Also update display name in Firebase Auth if changed
                    FirebaseUser user = authService.getCurrentUser();
                    if (user != null && !name.equals(user.getDisplayName())) {
                        user.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build());
                    }

                    // Update local preferences
                    preferenceManager.setUserName(name);

                    // Show success message
                    showSuccessDialog();
                })
                .addOnFailureListener(e -> {
                    // If document doesn't exist, create it
                    if (e.getMessage() != null && e.getMessage().contains("No document to update")) {
                        firestore.collection("users").document(userId)
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    // Update Firebase Auth if needed
                                    FirebaseUser user = authService.getCurrentUser();
                                    if (user != null) {
                                        user.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build());
                                    }

                                    // Update local preferences
                                    preferenceManager.setUserName(name);

                                    // Show success message
                                    showSuccessDialog();
                                })
                                .addOnFailureListener(e2 -> {
                                    Toast.makeText(EditProfileActivity.this,
                                            "Failed to save profile: " + e2.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(EditProfileActivity.this,
                                "Failed to save profile: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSuccessDialog() {
        // Show a success message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Your profile has been updated successfully.");
        builder.setPositiveButton("OK", (dialog, which) -> finish());
        builder.show();
    }
} 