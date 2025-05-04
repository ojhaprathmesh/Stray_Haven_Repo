package app.main.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.main.R;
import app.main.adapter.PetDonationAdapter;
import app.main.model.PetDonation;

public class FundraisersActivity extends AppCompatActivity {

    private RecyclerView fundraisersRecyclerView;
    private PetDonationAdapter petDonationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundraisers);

        // Initialize views
        fundraisersRecyclerView = findViewById(R.id.fundraisersRecyclerView);
        
        // Set up RecyclerView with demo data
        setupFundraisersRecyclerView();
    }

    private void setupFundraisersRecyclerView() {
        // Create adapter with demo data (replace with real data from your database)
        List<PetDonation> donationsList = getDemoPetDonations();
        petDonationAdapter = new PetDonationAdapter(donationsList);
        
        // Set adapter to RecyclerView
        fundraisersRecyclerView.setAdapter(petDonationAdapter);
    }
    
    // Demo data method - replace with your actual data source
    private List<PetDonation> getDemoPetDonations() {
        List<PetDonation> donations = new ArrayList<>();
        
        // Add demo items
        donations.add(new PetDonation(
                R.drawable.img_pet1,
                "Support Leena during her recovery after a severe accident!",
                1014160,
                3985840,
                25
        ));
        
        donations.add(new PetDonation(
                R.drawable.img_pet2,
                "Help Oliver get the surgery he desperately needs!",
                785000,
                1500000,
                12
        ));
        
        donations.add(new PetDonation(
                R.drawable.img_pet3,
                "Save Fluff's life with urgent medical care!",
                453000,
                1100000,
                8
        ));
        
        donations.add(new PetDonation(
                R.drawable.img_pet4,
                "Tweety needs special treatment for a rare condition!",
                321000,
                800000,
                18
        ));
        
        return donations;
    }
} 