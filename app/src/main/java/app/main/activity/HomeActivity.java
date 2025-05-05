package app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
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
import app.main.util.UI;

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

        // Set up UI with common configurations
        setupUI(findViewById(R.id.home));

        // Show welcome message
        UI.welcomeUser(this);

        // Initialize UI components
        UI.populateStoryTray(this, findViewById(R.id.story_tray), 10);
        setupAdvCarousel();
        setupFloatingMenu();
        setupRedirectButtons();
        setupTopMenuButtons();
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
            try {
                Toast.makeText(this, "Opening Subscribe and Win...", Toast.LENGTH_SHORT).show();
                // Launch SP1 Activity for Subscribe and Win
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, app.main.activity.SP1Activity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        menuManager.setButton2ClickListener(v -> {
            Toast.makeText(this, "Button 2 clicked", Toast.LENGTH_SHORT).show();
            // Add your action here
        });

        menuManager.setButton3ClickListener(v -> {
            try {
                // Launch SP2Activity for donation
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, app.main.activity.SP2Activity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
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

    private void setupTopMenuButtons() {
        // Set up profile button click listener
        ImageButton profileButton = findViewById(R.id.profile);
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> {
                // Navigate to profile activity with transition
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                // Apply custom transition animation
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
        }

        // Set up notification button click listener
        ImageButton notificationButton = findViewById(R.id.notification);
        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                // Navigate to notifications activity (already implemented)
                Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
                startActivity(intent);
            });
        }
    }
}
