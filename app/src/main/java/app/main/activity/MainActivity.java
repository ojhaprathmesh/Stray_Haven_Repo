package app.main.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import app.main.R;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private LinearLayout locationButton;
    private TextView locationText;
    private ImageView locationArrow;
    
    // Dropdown components
    private PopupWindow popupWindow;
    private View dropdownView;
    private TextView gurugramItem, rewariItem, sonipatItem, delhiItem, bhiwadiItem;
    private TextView currentSelectedItem = null;
    
    // Resources
    private Drawable selectedItemBg;
    private int defaultTextColor;
    private int selectedTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize resources
        selectedItemBg = ContextCompat.getDrawable(this, R.drawable.dropdown_item_selected_bg);
        defaultTextColor = ContextCompat.getColor(this, android.R.color.black);
        selectedTextColor = ContextCompat.getColor(this, R.color.green);
        
        // Initialize UI components
        locationButton = findViewById(R.id.locationButton);
        locationText = findViewById(R.id.locationText);
        locationArrow = findViewById(R.id.locationArrow);
        
        // Ensure location button is visible
        if (locationButton != null) {
            locationButton.setVisibility(View.VISIBLE);
        }
        
        // Initialize dropdown
        initializeDropdown();
        
        // Set click listener for location button
        locationButton.setOnClickListener(v -> toggleDropdown());
    }
    
    private void initializeDropdown() {
        // Inflate dropdown layout
        LayoutInflater inflater = LayoutInflater.from(this);
        dropdownView = inflater.inflate(R.layout.location_dropdown_menu, null);
        
        // Create PopupWindow
        popupWindow = new PopupWindow(
            dropdownView,
            320,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        );
        
        // Set elevation to create shadow
        popupWindow.setElevation(8f);
        
        // Get dropdown item views
        gurugramItem = dropdownView.findViewById(R.id.gurugramItem);
        rewariItem = dropdownView.findViewById(R.id.rewariItem);
        sonipatItem = dropdownView.findViewById(R.id.sonipatItem);
        delhiItem = dropdownView.findViewById(R.id.delhiItem);
        bhiwadiItem = dropdownView.findViewById(R.id.bhiwadiItem);
        
        // Set click listeners for dropdown items
        OnClickListener itemClickListener = v -> {
            // Update selection state
            TextView clickedItem = (TextView) v;
            updateSelectionState(clickedItem);
            
            // Update location text and dismiss popup
            locationText.setText(clickedItem.getText());
            popupWindow.dismiss();
            
            // Update arrow icon
            locationArrow.setImageResource(R.drawable.ic_dropdown);
        };
        
        gurugramItem.setOnClickListener(itemClickListener);
        rewariItem.setOnClickListener(itemClickListener);
        sonipatItem.setOnClickListener(itemClickListener);
        delhiItem.setOnClickListener(itemClickListener);
        bhiwadiItem.setOnClickListener(itemClickListener);
        
        // Set dismiss listener to update arrow icon
        popupWindow.setOnDismissListener(() -> 
            locationArrow.setImageResource(R.drawable.ic_dropdown)
        );
        
        // Make sure popup is not showing by default
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
    
    private void updateSelectionState(TextView selectedItem) {
        // Clear previous selection
        if (currentSelectedItem != null) {
            currentSelectedItem.setBackground(null);
            currentSelectedItem.setTextColor(defaultTextColor);
        }
        
        // Set new selection
        selectedItem.setBackground(selectedItemBg);
        selectedItem.setTextColor(ContextCompat.getColor(this, R.color.green));
        currentSelectedItem = selectedItem;
    }
    
    private void toggleDropdown() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            // Change arrow direction
            locationArrow.setImageResource(R.drawable.ic_dropdown_up);
            
            // Show dropdown immediately below location button with matching width
            popupWindow.showAsDropDown(locationButton, 0, 0, Gravity.END);
        }
    }
    
    // Find the current selected item based on location text
    private void syncSelectedState() {
        String currentLocation = locationText.getText().toString();
        
        // Find and select the matching item
        TextView itemToSelect = null;
        if (currentLocation.equals(gurugramItem.getText())) {
            itemToSelect = gurugramItem;
        } else if (currentLocation.equals(rewariItem.getText())) {
            itemToSelect = rewariItem;
        } else if (currentLocation.equals(sonipatItem.getText())) {
            itemToSelect = sonipatItem;
        } else if (currentLocation.equals(delhiItem.getText())) {
            itemToSelect = delhiItem;
        } else if (currentLocation.equals(bhiwadiItem.getText())) {
            itemToSelect = bhiwadiItem;
        }
        
        if (itemToSelect != null) {
            updateSelectionState(itemToSelect);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Initialize selection state if needed
        if (!"Location".equals(locationText.getText().toString())) {
            syncSelectedState();
        }
    }
}
