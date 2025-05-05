package app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import app.main.R;

public class SuccessActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        // Set up UI with common configurations
        setupUI(findViewById(android.R.id.content));

        // Initialize the back to home button
        TextView backToHomeButton = findViewById(R.id.back_to_home);

        // Set click listener for back to home button
        backToHomeButton.setOnClickListener(v -> {
            // Navigate to HomeActivity
            navigateToHome();
        });
        
        // Override back button behavior
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToHome();
            }
        });
    }
    
    private void navigateToHome() {
        // Navigate to HomeActivity
        Intent intent = new Intent(SuccessActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close this activity
    }
}