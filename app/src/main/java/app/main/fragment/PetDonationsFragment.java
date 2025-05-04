package app.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import app.main.R;
import app.main.activity.FundraisersActivity;
import app.main.adapter.PetDonationAdapter;
import app.main.model.PetDonation;

public class PetDonationsFragment extends Fragment implements PetDonationAdapter.OnDonateClickListener {

    private RecyclerView recyclerView;
    private LinearLayout dotsIndicator;
    private List<PetDonation> petDonations;
    private int currentPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comp_pet_donations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.petDonationsRecycler);
        dotsIndicator = view.findViewById(R.id.dotsIndicator);
        view.findViewById(R.id.viewAllButton).setOnClickListener(v -> {
            // Start FundraisersActivity when View All button is clicked
            Intent intent = new Intent(requireContext(), FundraisersActivity.class);
            startActivity(intent);
        });

        // Set up data
        setupDonationData();

        // Set up RecyclerView
        setupRecyclerView();

        // Set up indicator dots
        setupDotIndicator();
    }

    private void setupDonationData() {
        // Sample data - in a real app, this would come from a data source
        petDonations = Arrays.asList(
                new PetDonation(
                        R.drawable.img_pet1,
                        "\"Support Leena during her recovery after a severe accident!\"",
                        101416,
                        398584,
                        25
                ),
                new PetDonation(
                        R.drawable.img_pet2,
                        "\"Help Oliver get the surgery he desperately needs!\"",
                        78500,
                        150000,
                        12
                ),
                new PetDonation(
                        R.drawable.img_pet3,
                        "\"Save Fluff's life with urgent medical care!\"",
                        45300,
                        110000,
                        8
                ),
                new PetDonation(
                        R.drawable.img_pet4,
                        "\"Tweety needs special treatment for a rare condition!\"",
                        32100,
                        80000,
                        18
                )
        );
    }

    private void setupRecyclerView() {
        // Initialize adapter
        PetDonationAdapter adapter = new PetDonationAdapter(petDonations);
        adapter.setOnDonateClickListener(this);

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Add snap helper for paging effect
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Add scroll listener to update indicator dots
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                    if (position != currentPosition) {
                        updateDotIndicator(position);
                        currentPosition = position;
                    }
                }
            }
        });
    }

    private void setupDotIndicator() {
        for (int i = 0; i < petDonations.size(); i++) {
            ImageView dot = new ImageView(requireContext());

            // Set margin between dots
            int margin = (int) getResources().getDimension(R.dimen.dot_margin);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(margin, 0, margin, 0);
            dot.setLayoutParams(params);

            // Set dot appearance based on position
            dot.setImageResource(i == 0 ?
                    R.drawable.dot_indicator_selected :
                    R.drawable.dot_indicator_default);

            // Add to container
            dotsIndicator.addView(dot);
        }
    }

    private void updateDotIndicator(int position) {
        for (int i = 0; i < dotsIndicator.getChildCount(); i++) {
            ImageView dot = (ImageView) dotsIndicator.getChildAt(i);
            dot.setImageResource(i == position ?
                    R.drawable.dot_indicator_selected :
                    R.drawable.dot_indicator_default);
        }
    }

    @Override
    public void onDonateClick(PetDonation petDonation, int position) {
        Toast.makeText(
                requireContext(),
                "Donating to: " + petDonation.getDescription(),
                Toast.LENGTH_SHORT
        ).show();
    }
} 