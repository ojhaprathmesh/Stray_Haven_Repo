package app.main.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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

    private ViewPager2 viewPager; // For the advertisement carousel
    private final Handler handler = new Handler(Looper.getMainLooper()); // Handler for scheduling auto-scroll
    private Runnable runnable; // For handling auto-scrolling of the carousel
    private int currentPosition; // Current position of the ad carousel

    // List of image resources for the advertisement carousel
    private final List<Integer> images = Arrays.asList(
            R.drawable.ad1,
            R.drawable.ad2,
            R.drawable.ad3
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Set the content view to the corresponding XML layout

        enforceLightMode();
        initSystemInsets();
        welcomeUser();
        populateStoryTray();
        setupAdvCarousel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Remove the auto-scroll handler when the activity is paused
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable); // Clear the existing runnable before re-posting
        handler.postDelayed(runnable, 3000); // Repost the runnable after a delay of 3000ms (3 seconds)
    }

    private void enforceLightMode() {
        // Setting the theme to always use Light Mode (night mode is disabled)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void initSystemInsets() {
        // This method sets the padding of the main view to account for system bars (like status bar, navigation bar)
        EdgeToEdge.enable(this); // Enables edge-to-edge support (content extends under system bars)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Get system bars insets
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); // Set padding
            return insets; // Return insets for further processing if needed
        });
    }

    private void welcomeUser() {
        Toast.makeText(this, "Welcome to Stray Haven", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    private void populateStoryTray() {
        LinearLayout storyContainer = findViewById(R.id.story_tray);
        LayoutInflater inflater = LayoutInflater.from(this);
        storyContainer.removeAllViews(); // Clear any previous views in the story container

        int ngoCount = 16; // Placeholder count of NGOs, replace with actual data from an API
        for (int i = 0; i < ngoCount; i++) {
            View storyView = inflater.inflate(R.layout.comp_story, storyContainer, false);
            storyView.setId(View.generateViewId()); // Generates a unique ID for the view

            TextView nameTextView = storyView.findViewById(R.id.ngo_name);
            nameTextView.setText("NGO #" + (i + 1)); // Set the NGO name (just a placeholder for now)

            storyContainer.addView(storyView); // Add the new story view to the container
        }
    }

    private void setupAdvCarousel() {
        // Set up the advertisement carousel (view pager)
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabIndicator);

        // Create an adapter for ViewPager2 and set it to the viewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(images);
        viewPager.setAdapter(adapter);

        // Link the TabLayout with ViewPager2 using a TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // TabLayoutMediator will update the tabs based on the ViewPager2's current item
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position; // Sync the position on manual swipe
            }
        });

        setupAutoScroll(); // Set up auto-scrolling of the carousel
    }

    private void setupAutoScroll() {
        // Handles auto-scrolling of the advertisement carousel every 3 seconds
        runnable = new Runnable() {
            @Override
            public void run() {
                currentPosition = (currentPosition + 1) % images.size();
                viewPager.setCurrentItem(currentPosition, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}
