package app.main.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import app.main.R;

public class HomeActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout storyContainer = findViewById(R.id.story_tray);
        LayoutInflater inflater = LayoutInflater.from(this);

        storyContainer.removeAllViews(); // <-- Remove existing views from the container

        int ngoCount = 16; // <-- Replace with actual NGO count from NGO API

        for (int i = 0; i < ngoCount; i++) {
            View storyView = inflater.inflate(R.layout.comp_story, storyContainer, false);

            storyView.setId(View.generateViewId());  // <-- Generate a unique ID for each view

            TextView nameTextView = storyView.findViewById(R.id.ngo_name);
            nameTextView.setText("NGO #" + (i + 1)); // Will replace with data from NGO API

            storyContainer.addView(storyView);
        }
    }
}
