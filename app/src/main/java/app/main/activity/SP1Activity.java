package app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import app.main.R;

public class SP1Activity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp1);

        // Set up UI with common configurations
        setupUI(findViewById(android.R.id.content));

        // Set up click listeners for the Subscribe & Win buttons
        setupSubscribeButtons();
    }

    private void setupSubscribeButtons() {
        // Get references to both Subscribe & Win buttons
        Button topSubscribeButton = findViewById(R.id.btn_subscribe_top);
        Button bottomSubscribeButton = findViewById(R.id.btn_subscribe_bottom);

        // Set click listener for top button
        topSubscribeButton.setOnClickListener(v -> {
            navigateToSP2();
        });

        // Set click listener for bottom button
        bottomSubscribeButton.setOnClickListener(v -> {
            navigateToSP2();
        });
    }

    private void navigateToSP2() {
        // Navigate to SP2Activity
        Intent intent = new Intent(SP1Activity.this, SP2Activity.class);
        startActivity(intent);
    }
}