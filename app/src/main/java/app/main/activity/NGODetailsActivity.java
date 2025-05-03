package app.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.main.R;
import app.main.adapter.NGOAdapter;
import app.main.model.NGO;

public class NGODetailsActivity extends BaseActivity {

    private CardView locationDropdown;
    private TextView locationText;
    private EditText searchEditText;
    private TextView seeAllText;
    private FloatingActionButton quickActionFab;
    private RecyclerView ngoRecyclerView;
    private NGOAdapter ngoAdapter;
    
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
    }
    
    private void initViews() {
        locationDropdown = findViewById(R.id.location_dropdown);
        locationText = findViewById(R.id.location_text);
        searchEditText = findViewById(R.id.search_edit_text);
        seeAllText = findViewById(R.id.see_all_text);
        quickActionFab = findViewById(R.id.quick_action_fab);
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
        
        // FAB
        quickActionFab.setOnClickListener(v -> {
            Toast.makeText(this, "Quick action clicked", Toast.LENGTH_SHORT).show();
            // Implement quick action
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
        PopupMenu popupMenu = new PopupMenu(this, locationDropdown);
        
        // Add items from the locationsList
        for (String location : locationsList) {
            popupMenu.getMenu().add(location);
        }
        
        // Set click listener
        popupMenu.setOnMenuItemClickListener(item -> {
            locationText.setText(item.getTitle());
            // In a real app, filter NGOs by location
            Toast.makeText(this, "Location set to: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });
        
        popupMenu.show();
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