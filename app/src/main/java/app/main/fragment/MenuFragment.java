package app.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.main.R;

public class MenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comp_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton notificationBtn = view.findViewById(R.id.notification);
        ImageButton profileBtn = view.findViewById(R.id.profile);

        notificationBtn.setOnClickListener(v -> {
            // Implement logic for notification button click
            Toast.makeText(requireContext(), "Notifications Clicked", Toast.LENGTH_SHORT).show();
        });

        profileBtn.setOnClickListener(v -> {
            // Implement logic for profile button click
            Toast.makeText(requireContext(), "Profile Clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
