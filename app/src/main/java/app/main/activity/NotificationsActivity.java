package app.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.main.R;
import app.main.adapter.NotificationAdapter;
import app.main.model.Notification;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Set up back navigation
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        // Set up back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize views
        recyclerView = findViewById(R.id.notificationsRecycler);
        emptyStateText = findViewById(R.id.emptyStateText);

        // Set up recycler view
        setupRecyclerView();

        // Load placeholder data
        loadPlaceholderData();
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadPlaceholderData() {
        // Placeholder data - will be replaced with real data from Firebase
        List<Notification> notifications = new ArrayList<>();

        notifications.add(new Notification(
                R.drawable.img_pet1,
                "John Doe donated ₹5,000 to help Leena recover",
                "2 hours ago"
        ));

        notifications.add(new Notification(
                R.drawable.img_pet2,
                "Sarah Smith shared Oliver's story",
                "5 hours ago"
        ));

        notifications.add(new Notification(
                R.drawable.img_pet3,
                "Mike Johnson commented on Fluff's progress",
                "1 day ago"
        ));

        notifications.add(new Notification(
                R.drawable.img_pet4,
                "Amy Parker followed your fundraiser for Tweety",
                "2 days ago"
        ));

        adapter.submitList(notifications);
        updateEmptyState(notifications);
    }

    private void updateEmptyState(List<Notification> notifications) {
        if (notifications.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
} 