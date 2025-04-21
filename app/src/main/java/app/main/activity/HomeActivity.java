package app.main.activity;

import static app.main.util.UI.*;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import app.main.R;
import app.main.adapter.ViewPagerAdapter;

public class HomeActivity extends BaseActivity {

    private ViewPager2 viewPager; // For the advertisement carousel
    private final Handler handler = new Handler(Looper.getMainLooper()); // Handler for scheduling auto-scroll
    private Runnable runnable; // For handling auto-scrolling of the carousel
    private int currentPosition; // Current position of the ad carousel

    // List of layout resources for the advertisement carousel
    private final List<Integer> adLayouts = Arrays.asList(
            R.layout.layout_ad1,
            R.layout.layout_ad2,
            R.layout.layout_ad3
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Set the content view to the corresponding XML layout

        enforceLightMode();
        applySystemInsets(this, findViewById(R.id.home));
        welcomeUser();
        populateStoryTray(this, findViewById(R.id.story_tray), 10);
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

    private void setupAdvCarousel() {
        // Set up the advertisement carousel (view pager)
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabIndicator);

        // Create an adapter for ViewPager2 and set it to the viewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(adLayouts);
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
                currentPosition = (currentPosition + 1) % adLayouts.size();
                viewPager.setCurrentItem(currentPosition, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}
