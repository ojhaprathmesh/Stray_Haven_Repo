package app.main.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import app.main.R;
import app.main.adapters.ViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private int currentPosition = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // App related code
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        Toast.makeText(this, "Welcome to Stray Haven", Toast.LENGTH_SHORT).show(); // <-- Welcome message

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Dynamically added story components
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

        // ViewPager Setup
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabIndicator);

        List<Integer> images = Arrays.asList(
                R.drawable.ad1,
                R.drawable.ad2,
                R.drawable.ad3
        );

        ViewPagerAdapter adapter = new ViewPagerAdapter(images);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
        }).attach();

        // Auto-scroll setup
        runnable = new Runnable() {
            @Override
            public void run() {
                currentPosition = (currentPosition + 1) % images.size();
                Log.d("currentPosition", String.valueOf(currentPosition));
                viewPager.setCurrentItem(currentPosition, true);
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(runnable, 3000); // Initial auto-scroll trigger
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Prevent duplicate scheduling
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable); // Clear before re-posting
        handler.postDelayed(runnable, 3000);
    }
}
