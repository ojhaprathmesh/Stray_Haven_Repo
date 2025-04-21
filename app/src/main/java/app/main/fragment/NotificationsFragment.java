package app.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.main.R;
import app.main.adapter.NotificationAdapter;
import app.main.model.Notification;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comp_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.notificationsRecycler);
        emptyStateText = view.findViewById(R.id.emptyStateText);

        // Set up RecyclerView
        setupRecyclerView();
        
        // Load placeholder data
        loadPlaceholderData();
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter();
        
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
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