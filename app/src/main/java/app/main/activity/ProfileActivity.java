package app.main.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import app.main.R;
import app.main.util.AuthService;
import app.main.util.PreferenceManager;

public class ProfileActivity extends BaseActivity {

    private AlertDialog supportDialog;
    private AuthService authService;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore firestore;

    // User data
    private String userName = "";
    private String userEmail = "";
    private String userPhotoUrl = "";
    private int gemsCount = 0;
    private int livesCount = 0;
    private int donationsCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase services
        authService = new AuthService(this);
        preferenceManager = new PreferenceManager(this);
        firestore = FirebaseFirestore.getInstance();

        // Set up UI with common configurations
        setupUI(findViewById(R.id.profile_container));

        // Set up back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> handleBackPressed());

        // Setup support option
        LinearLayout supportOption = findViewById(R.id.support);
        supportOption.setOnClickListener(v -> showSupportOverlay());

        // Setup other click listeners
        setupMenuOptions();

        // Setup edit profile button
        TextView editProfileBtn = findViewById(R.id.edit_profile_btn);
        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Load user data from Firebase
        loadUserData();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void setupMenuOptions() {
        // Become a GEM button
        LinearLayout becomeGem = findViewById(R.id.become_gem);
        becomeGem.setOnClickListener(v -> {
            // Handle become a GEM click
            // In a real app, this might open a subscription page
            Toast.makeText(this, "Become a GEM feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Donation History button
        LinearLayout donationHistory = findViewById(R.id.donation_history);
        donationHistory.setOnClickListener(v -> {
            // Handle donation history click
            // In a real app, this might open a donation history page
            Toast.makeText(this, "Donation history feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Refer & Earn button
        LinearLayout referEarn = findViewById(R.id.refer_earn);
        referEarn.setOnClickListener(v -> {
            // Handle refer & earn click
            // In a real app, this might open a referral page
            Toast.makeText(this, "Refer & Earn feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Logout option (in ProfileActivity)
        LinearLayout logoutOption = findViewById(R.id.logout_option);
        if (logoutOption != null) {
            logoutOption.setOnClickListener(v -> {
                showLogoutConfirmation();
            });
        }
    }

    private void loadUserData() {
        // Get the current Firebase user
        FirebaseUser currentUser = authService.getCurrentUser();

        if (currentUser != null) {
            // Get basic user info from Firebase Auth
            userEmail = currentUser.getEmail() != null ? currentUser.getEmail() : "";
            userName = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : preferenceManager.getUserName();
            userPhotoUrl = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "";

            // If name is still empty, use email as fallback
            if (userName.isEmpty()) {
                userName = userEmail.split("@")[0]; // Use part before @ as name
            }

            // Get additional user data from Firestore
            String userId = currentUser.getUid();
            firestore.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve user data from Firestore
                            if (documentSnapshot.contains("name") && !documentSnapshot.getString("name").isEmpty()) {
                                userName = documentSnapshot.getString("name");
                            }

                            if (documentSnapshot.contains("gems")) {
                                gemsCount = documentSnapshot.getLong("gems").intValue();
                            }

                            if (documentSnapshot.contains("lives_supported")) {
                                livesCount = documentSnapshot.getLong("lives_supported").intValue();
                            }

                            if (documentSnapshot.contains("donations")) {
                                donationsCount = documentSnapshot.getLong("donations").intValue();
                            }
                        } else {
                            // Create a new user document if it doesn't exist
                            createNewUserDocument(userId);
                        }

                        // Update UI with retrieved data
                        updateUI();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure - use locally stored data as fallback
                        userName = preferenceManager.getUserName();
                        userEmail = preferenceManager.getUserEmail();

                        // Update UI with basic data
                        updateUI();

                        Toast.makeText(this, "Failed to load user data from server", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // User is not signed in, use locally stored data as fallback
            userName = preferenceManager.getUserName();
            userEmail = preferenceManager.getUserEmail();

            // Update UI with basic data
            updateUI();

            // Check if user should be redirected to login
            if (!preferenceManager.isLoggedIn()) {
                navigateToLogin();
            }
        }
    }

    private void createNewUserDocument(String userId) {
        // Create initial user data
        FirebaseUser user = authService.getCurrentUser();

        if (user != null) {
            // Initialize user data document
            firestore.collection("users").document(userId)
                    .set(new java.util.HashMap<String, Object>() {{
                        put("name", user.getDisplayName() != null ? user.getDisplayName() : "");
                        put("email", user.getEmail() != null ? user.getEmail() : "");
                        put("gems", 0);
                        put("lives_supported", 0);
                        put("donations", 0);
                        put("created_at", new java.util.Date());
                    }})
                    .addOnSuccessListener(aVoid -> {
                        // Document created successfully
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this,
                                "Failed to create user profile. Please try again.",
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateUI() {
        // Update UI with user data
        TextView userNameView = findViewById(R.id.user_name);
        TextView gemsCountView = findViewById(R.id.gems_count);
        TextView livesCountView = findViewById(R.id.lives_count);
        TextView donationCountView = findViewById(R.id.donation_count);
        TextView profileInitial = findViewById(R.id.profile_initial);
        ShapeableImageView profileImage = findViewById(R.id.profile_image);

        // Set user name
        userNameView.setText(userName);

        // Set initial in profile image if no photo is available
        if (!isProfileImageSet(profileImage)) {
            String initial = "";
            if (userName != null && !userName.isEmpty()) {
                initial = String.valueOf(userName.charAt(0));
            }

            // Show the initial and hide the image
            profileInitial.setText(initial.toUpperCase());
            profileInitial.setVisibility(View.VISIBLE);
        } else {
            // Hide the initial and show the image
            profileInitial.setVisibility(View.GONE);
        }

        // Set counts
        gemsCountView.setText(String.valueOf(gemsCount));
        livesCountView.setText(String.valueOf(livesCount));
        donationCountView.setText(String.valueOf(donationsCount));
    }

    private boolean isProfileImageSet(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        boolean hasImage = (drawable != null);

        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
            }
        }

        return hasImage;
    }

    private void showSupportOverlay() {
        // Create the support dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View supportView = LayoutInflater.from(this).inflate(R.layout.overlay_support, null);
        builder.setView(supportView);

        // Set up click listeners for phone and email options
        LinearLayout phoneOption = supportView.findViewById(R.id.phone_option);
        LinearLayout emailOption = supportView.findViewById(R.id.email_option);
        TextView phoneNumber = supportView.findViewById(R.id.phone_number);
        TextView emailAddress = supportView.findViewById(R.id.email_address);

        phoneOption.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
            startActivity(intent);
            supportDialog.dismiss();
        });

        emailOption.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + emailAddress.getText().toString()));
            startActivity(intent);
            supportDialog.dismiss();
        });

        supportDialog = builder.create();
        supportDialog.show();
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
        Intent intent = new Intent(ProfileActivity.this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void handleBackPressed() {
        finish();
    }
} 