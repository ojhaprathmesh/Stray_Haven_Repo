package app.main.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.main.R;
import app.main.adapter.NGOAdapter;
import app.main.component.FloatingMenuManager;
import app.main.model.NGO;

public class NGODetailsActivity extends BaseActivity {

    private CardView locationDropdown;
    private TextView locationText;
    private ImageView locationDropdownIcon;
    private EditText searchEditText;
    private TextView seeAllText;
    private RecyclerView ngoRecyclerView;
    private NGOAdapter ngoAdapter;
    private FloatingMenuManager floatingMenuManager;
    
    // Customizable properties
    private List<String> locationsList = new ArrayList<>(Arrays.asList(
            "Delhi", "Mumbai", "Bangalore", "Chennai", "Kolkata"
    ));
    private int maxNGOsToDisplay = 3; // Default, can be changed
    private boolean showingAll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_details);
        
        enforceLightMode();
        applySystemInsets(this, findViewById(R.id.ngo_details_container));
        
        initViews();
        setupRecyclerView();
        setupClickListeners();
        setupFloatingMenu();
    }
    
    private void initViews() {
        locationDropdown = findViewById(R.id.location_dropdown);
        locationText = findViewById(R.id.location_text);
        locationDropdownIcon = findViewById(R.id.location_dropdown_icon);
        searchEditText = findViewById(R.id.search_edit_text);
        seeAllText = findViewById(R.id.see_all_text);
        ngoRecyclerView = findViewById(R.id.ngo_recycler_view);
    }
    
    private void setupRecyclerView() {
        // Create a list of sample NGOs
        List<NGO> ngoList = generateSampleNGOs();
        
        // Initialize the adapter
        ngoAdapter = new NGOAdapter(this, getVisibleNGOs(ngoList), (ngo, position) -> {
            Toast.makeText(this, "Clicked on " + ngo.getName(), Toast.LENGTH_SHORT).show();
            // In a real app, navigate to NGO details screen
        });
        
        // Set up the RecyclerView
        ngoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ngoRecyclerView.setAdapter(ngoAdapter);
    }
    
    private void setupFloatingMenu() {
        View mainButton = findViewById(R.id.main_nav_button);
        View button1 = findViewById(R.id.nav_button1);
        View button2 = findViewById(R.id.nav_button2);
        View button3 = findViewById(R.id.nav_button3);
        View button4 = findViewById(R.id.nav_button4);
        
        floatingMenuManager = new FloatingMenuManager(
                mainButton, button1, button2, button3, button4);
        
        // Set click listeners for each button
        floatingMenuManager.setButton1ClickListener(v -> {
            Toast.makeText(this, "Button 1 clicked", Toast.LENGTH_SHORT).show();
            // Add your action here
        });
        
        floatingMenuManager.setButton2ClickListener(v -> {
            Toast.makeText(this, "Button 2 clicked", Toast.LENGTH_SHORT).show();
            // Add your action here
        });
        
        floatingMenuManager.setButton3ClickListener(v -> {
            Toast.makeText(this, "Button 3 clicked", Toast.LENGTH_SHORT).show();
            // Add your action here
        });
        
        floatingMenuManager.setButton4ClickListener(v -> {
            Toast.makeText(this, "Button 4 clicked", Toast.LENGTH_SHORT).show();
            // Add your action here
        });
    }
    
    private List<NGO> generateSampleNGOs() {
        // In a real app, this would come from an API or database
        List<NGO> ngos = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            NGO ngo = new NGO(
                    "Friends",
                    "Delhi",
                    "Cows, dogs, cats",
                    "05644-237315",
                    "SAVE, LOVE, ADOPT."
            );
            ngos.add(ngo);
        }
        
        return ngos;
    }
    
    private List<NGO> getVisibleNGOs(List<NGO> allNGOs) {
        if (showingAll || allNGOs.size() <= maxNGOsToDisplay) {
            return allNGOs;
        } else {
            return allNGOs.subList(0, maxNGOsToDisplay);
        }
    }
    
    private void setupClickListeners() {
        // Location dropdown
        locationDropdown.setOnClickListener(v -> showLocationDropdown());
        
        // See All button
        seeAllText.setOnClickListener(v -> {
            showingAll = !showingAll;
            seeAllText.setText(showingAll ? "SHOW LESS" : "SEE ALL");
            ngoAdapter.updateData(getVisibleNGOs(generateSampleNGOs()));
        });
        
        // Search
        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Toast.makeText(this, "Search focused", Toast.LENGTH_SHORT).show();
                // Implement search functionality
            }
        });
    }
    
    private void showLocationDropdown() {
        // Create a custom dropdown instead of using PopupMenu
        // Inflate custom dropdown layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View customMenuView = inflater.inflate(R.layout.dropdown_location_menu, null);
        
        // Create the popup window
        PopupWindow popupWindow = new PopupWindow(
                customMenuView,
                locationDropdown.getWidth() + 40, // Match the width of location dropdown
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );
        
        // Set up the dropdown items
        LinearLayout menuItemsContainer = customMenuView.findViewById(R.id.dropdown_items_container);
        menuItemsContainer.removeAllViews();
        
        // Add menu items
        for (String location : locationsList) {
            TextView item = (TextView) inflater.inflate(R.layout.item_dropdown_location, menuItemsContainer, false);
            item.setText(location);
            item.setOnClickListener(v -> {
                locationText.setText(location);
                popupWindow.dismiss();
                // In a real app, filter NGOs by location
                Toast.makeText(this, "Location set to: " + location, Toast.LENGTH_SHORT).show();
            });
            menuItemsContainer.addView(item);
        }
        
        // Set background drawable
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.dialog_holo_light_frame));
        
        // Show at the right position - aligned to the right edge of the location dropdown
        popupWindow.setElevation(8f);
        
        // Flip the dropdown arrow icon when showing/dismissing
        locationDropdownIcon.setRotation(180); // Point upward when open
        
        // Set dismiss listener to reset the arrow
        popupWindow.setOnDismissListener(() -> locationDropdownIcon.setRotation(0));
        
        // Show the popup window
        // The x-coordinate must match the end of the location dropdown
        int[] location = new int[2];
        locationDropdown.getLocationOnScreen(location);
        
        // Show below the dropdown button
        popupWindow.showAtLocation(
                locationDropdown,
                Gravity.NO_GRAVITY,
                location[0] - 20, // X position
                location[1] + locationDropdown.getHeight() // Y position
        );
    }
    
    // Public methods that can be called to customize the view
    
    /**
     * Set the list of available locations for the dropdown
     * @param locations List of location names
     */
    public void setLocationsList(List<String> locations) {
        this.locationsList.clear();
        this.locationsList.addAll(locations);
    }
    
    /**
     * Set the maximum number of NGOs to display before clicking "See All"
     * @param maxCount Maximum number of NGOs
     */
    public void setMaxNGOsToDisplay(int maxCount) {
        this.maxNGOsToDisplay = maxCount;
        if (ngoAdapter != null) {
            ngoAdapter.updateData(getVisibleNGOs(generateSampleNGOs()));
        }
    }
} 