package app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import app.main.R;

public class SuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        // Initialize the back to home button
        TextView backToHomeButton = findViewById(R.id.back_to_home);

        // Set click listener for back to home button
        backToHomeButton.setOnClickListener(v -> {
            // Navigate to HomeActivity
            Intent intent = new Intent(SuccessActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close this activity
        });
    }

    @Override
    public void onBackPressed() {
        // Navigate to HomeActivity when back button is pressed
        Intent intent = new Intent(SuccessActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}