package app.main.activity;

import static app.main.util.UI.*;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import app.main.R;
import app.main.adapter.ViewPagerAdapter;
import app.main.component.FloatingMenuManager;

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
        setupFloatingMenu();
        setupRedirectButtons();
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

    private void setupFloatingMenu() {
        View mainButton = findViewById(R.id.main_nav_button);
        View button1 = findViewById(R.id.nav_button1);
        View button2 = findViewById(R.id.nav_button2);
        View button3 = findViewById(R.id.nav_button3);
        View button4 = findViewById(R.id.nav_button4);

        FloatingMenuManager menuManager = new FloatingMenuManager(
                mainButton, button1, button2, button3, button4);

        // Set click listeners for each button
        menuManager.setButton1ClickListener(v -> {
            Toast.makeText(this, "Button 1 clicked", Toast.LENGTH_SHORT).show();
            // Add your action here
        });

        menuManager.setButton2ClickListener(v -> {
            Toast.makeText(this, "Button 2 clicked", Toast.LENGTH_SHORT).show();
            // Add your action here
        });

        menuManager.setButton3ClickListener(v -> {
            Toast.makeText(this, "Button 3 clicked", Toast.LENGTH_SHORT).show();
            // Add your action here
        });

        menuManager.setButton4ClickListener(v -> {
            Toast.makeText(this, "Button 4 clicked", Toast.LENGTH_SHORT).show();
            // Add your action here
        });
    }

    private void setupRedirectButtons() {
        // Set up click listener for the "Contact the nearest NGO" section
        ConstraintLayout contactRedirect = findViewById(R.id.redirect_contact);
        if (contactRedirect != null) {
            contactRedirect.setOnClickListener(v -> {
                // Launch the NGO Details Activity with shared element transition
                Intent intent = new Intent(HomeActivity.this, NGODetailsActivity.class);

                View floatingMenuContainer = findViewById(R.id.floating_menu_container);

                // Create the transition
                androidx.core.app.ActivityOptionsCompat options =
                        androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this,
                                floatingMenuContainer,
                                "floating_menu"
                        );

                startActivity(intent, options.toBundle());
            });
        }

        // Set up click listener for the invitation code section (if needed in the future)
        ConstraintLayout invitationRedirect = findViewById(R.id.redirect_invitation);
        if (invitationRedirect != null) {
            invitationRedirect.setOnClickListener(v -> {
                Toast.makeText(this, "Invitation code feature coming soon!", Toast.LENGTH_SHORT).show();
                // Add the invitation code functionality here when ready
            });
        }
    }
}
