package app.main.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import app.main.R;

public class NGODetailsActivity extends BaseActivity {

    private NGO currentNGO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_details);
        
        enforceLightMode();
        applySystemInsets(this, findViewById(R.id.ngo_details_container));
        
        // For demonstration, create a sample NGO
        // In a real app, this data would come from an intent or database
        currentNGO = new NGO(
                "Friends Animal Foundation",
                "Delhi",
                "Cows, dogs, cats",
                "05644-237315",
                "SAVE, LOVE, ADOPT."
        );
        
        setupViews();
        setupBackButton();
        setupContactButton();
    }
    
    private void setupViews() {
        TextView nameTextView = findViewById(R.id.ngo_name);
        TextView locationTextView = findViewById(R.id.ngo_location);
        TextView petsTextView = findViewById(R.id.ngo_pets);
        TextView contactTextView = findViewById(R.id.ngo_contact);
        TextView taglineTextView = findViewById(R.id.ngo_tagline);
        
        // Set data to views
        nameTextView.setText(currentNGO.getName());
        locationTextView.setText(currentNGO.getLocation());
        petsTextView.setText(currentNGO.getPets());
        contactTextView.setText(currentNGO.getContact());
        taglineTextView.setText("-" + currentNGO.getTagline());
    }
    
    private void setupBackButton() {
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }
    
    private void setupContactButton() {
        Button contactButton = findViewById(R.id.contact_button);
        contactButton.setOnClickListener(v -> {
            // Call the NGO
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + currentNGO.getContact()));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Could not make the call", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 