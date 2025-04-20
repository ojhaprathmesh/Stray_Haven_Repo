package app.main.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
}
