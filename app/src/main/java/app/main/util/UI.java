package app.main.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import app.main.R;

public class UI {
    /**
     * Populates a LinearLayout with a number of placeholder NGO story views.
     *
     * @param context   The context used to inflate views. Must not be null.
     * @param container The LinearLayout that will contain the story views. Must not be null.
     * @param count     Number of story views to create.
     */
    @SuppressLint("SetTextI18n")
    public static void populateStoryTray(@NonNull Context context, @NonNull LinearLayout container, int count) {
        LayoutInflater inflater = LayoutInflater.from(context);
        container.removeAllViews();

        for (int i = 0; i < count; i++) {
            View storyView = inflater.inflate(R.layout.comp_story, container, false);
            storyView.setId(View.generateViewId());

            TextView nameTextView = storyView.findViewById(R.id.ngo_name);
            nameTextView.setText("NGO #" + (i + 1));

            container.addView(storyView);
        }
    }

    /**
     * Applies system insets to a view for proper edge-to-edge display.
     * This ensures the view respects system UI elements like status bar and navigation bar.
     *
     * @param activity The activity where the view is located. Must not be null.
     * @param view     The view to apply insets to. Must not be null.
     */
    public static void applySystemInsets(@NonNull AppCompatActivity activity, @NonNull View view) {
        EdgeToEdge.enable(activity);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Enforces light mode in the application regardless of system settings.
     * This ensures consistency in the app's appearance across different device settings.
     *
     * @param activity The activity where the light mode should be enforced. Must not be null.
     */
    public static void enforceLightMode(@NonNull AppCompatActivity activity) {
        // Setting the theme to always use Light Mode (night mode is disabled)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * Displays a welcome toast message to the user.
     * This is typically called when the app is launched or a main screen is entered.
     *
     * @param context The context in which the toast should be shown. Must not be null.
     */
    public static void welcomeUser(@NonNull Context context) {
        Toast.makeText(context, "Welcome to Stray Haven", Toast.LENGTH_SHORT).show();
    }
}
