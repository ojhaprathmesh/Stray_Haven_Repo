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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.imageview.ShapeableImageView;

import app.main.R;

public class ProfileActivity extends BaseActivity {

    private AlertDialog supportDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        // In a real app, you would load user data here
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
        });

        // Donation History button
        LinearLayout donationHistory = findViewById(R.id.donation_history);
        donationHistory.setOnClickListener(v -> {
            // Handle donation history click
            // In a real app, this might open a donation history page
        });

        // Refer & Earn button
        LinearLayout referEarn = findViewById(R.id.refer_earn);
        referEarn.setOnClickListener(v -> {
            // Handle refer & earn click
            // In a real app, this might open a referral page
        });
    }

    private void loadUserData() {
        // Load user data from database or preferences
        // For this demo, we'll use static data
        TextView userName = findViewById(R.id.user_name);
        TextView gemsCount = findViewById(R.id.gems_count);
        TextView livesCount = findViewById(R.id.lives_count);
        TextView donationCount = findViewById(R.id.donation_count);
        TextView profileInitial = findViewById(R.id.profile_initial);
        ShapeableImageView profileImage = findViewById(R.id.profile_image);

        String userNameString = getString(R.string.username);

        // Set user name
        userName.setText(userNameString);

        // Set initial in profile image if no photo is available
        if (!isProfileImageSet(profileImage)) {
            String initial = "";
            if (userNameString != null && !userNameString.isEmpty()) {
                initial = String.valueOf(userNameString.charAt(0));
            }

            // Show the initial and hide the image
            profileInitial.setText(initial.toUpperCase());
            profileInitial.setVisibility(View.VISIBLE);
        } else {
            // Hide the initial and show the image
            profileInitial.setVisibility(View.GONE);
        }

        // Set counts
        gemsCount.setText("0");
        livesCount.setText("0");
        donationCount.setText("0");
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

    private void handleBackPressed() {
        finish();
    }
} 