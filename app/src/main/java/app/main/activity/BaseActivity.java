package app.main.activity;

import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class BaseActivity extends AppCompatActivity {
    protected static void applySystemInsets(AppCompatActivity activity, View view) {
        EdgeToEdge.enable(activity);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected void enforceLightMode() {
        // Setting the theme to always use Light Mode (night mode is disabled)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    protected void welcomeUser() {
        Toast.makeText(this, "Welcome to Stray Haven", Toast.LENGTH_SHORT).show();
    }
}

