package app.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import app.main.R;

public class AdoptionDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_detail);

        // Get the pet details from the intent
        String petName = getIntent().getStringExtra("pet_name");
        int petImageResource = getIntent().getIntExtra("pet_image", R.drawable.dog_image); // Default image if none provided

        // Find the views and set the pet information
        TextView petNameTextView = findViewById(R.id.petName);
        ImageView petImageView = findViewById(R.id.petImage);
        TextView petInfoTitle = findViewById(R.id.petInfoTitle);

        if (petName != null) {
            petNameTextView.setText(petName.toUpperCase());
            // Update the "Let's Know" section title with the pet name
            petInfoTitle.setText("Let's Know " + petName.toUpperCase());
        }

        // Set the pet image
        petImageView.setImageResource(petImageResource);

        // Set up back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
} 