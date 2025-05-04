package app.main.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.main.util.UI;

/**
 * Base activity class that provides common functionality for all activities.
 * Centralizes common setup operations to reduce code duplication in child activities.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply light mode as default for all activities
        UI.enforceLightMode(this);
    }

    /**
     * Set up the UI with common configurations.
     * This should be called immediately after setContentView() in child activities.
     * Handles edge-to-edge display and system insets.
     *
     * @param rootView The root view of the activity layout
     */
    protected void setupUI(View rootView) {
        // Apply edge-to-edge display and system insets
        UI.applySystemInsets(this, rootView);
    }
}
