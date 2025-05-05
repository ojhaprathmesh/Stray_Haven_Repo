package app.main.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.main.R;

public class AdoptionActivity extends AppCompatActivity {

    RecyclerView petRecyclerView;
    PetAdapter adapter;
    ArrayList<Pet> petList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption);

        petRecyclerView = findViewById(R.id.petRecyclerView);
        petList = new ArrayList<>();

        // Add pets
        petList.add(new Pet("Beagle", R.drawable.beagle));
        petList.add(new Pet("Tofy", R.drawable.tofy));
        petList.add(new Pet("Beagle", R.drawable.beagle));
        petList.add(new Pet("Tofy", R.drawable.tofy));

        // Add 4 more pets
        petList.add(new Pet("Shaggy", R.drawable.shaggy));
        petList.add(new Pet("Bob", R.drawable.bob));
        petList.add(new Pet("Husky", R.drawable.husky));
        petList.add(new Pet("Kitty", R.drawable.cat));

        adapter = new PetAdapter(this, petList);
        petRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        petRecyclerView.setAdapter(adapter);
    }
}
