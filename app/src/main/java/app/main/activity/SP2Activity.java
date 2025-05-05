package app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import app.main.R;

public class SP2Activity extends BaseActivity {

    private Spinner currencySpinner;
    private EditText amountInput;
    private Button btn750, btn1000, btn1500;
    private ImageButton backButton;
    private Button donateButton;
    private RadioButton radioCause1, radioCause2, radioCause3, radioCause4;
    private String selectedCurrency;
    private String selectedCause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp2);

        // Set up UI with common configurations
        setupUI(findViewById(android.R.id.content));

        // Initialize views
        currencySpinner = findViewById(R.id.currency_spinner);
        amountInput = findViewById(R.id.amount_input);
        btn750 = findViewById(R.id.btn750);
        btn1000 = findViewById(R.id.btn1000);
        btn1500 = findViewById(R.id.btn1500);
        backButton = findViewById(R.id.back_button);
        donateButton = findViewById(R.id.donate_button);
        radioCause1 = findViewById(R.id.radio_cause1);
        radioCause2 = findViewById(R.id.radio_cause2);
        radioCause3 = findViewById(R.id.radio_cause3);
        radioCause4 = findViewById(R.id.radio_cause4);

        // Set button listeners to update EditText
        btn750.setOnClickListener(v -> amountInput.setText("750"));
        btn1000.setOnClickListener(v -> amountInput.setText("1000"));
        btn1500.setOnClickListener(v -> amountInput.setText("1500"));

        // Back button listener
        backButton.setOnClickListener(v -> finish());

        // RadioButton listeners to track selected cause
        radioCause1.setOnClickListener(v -> {
            selectedCause = getString(R.string.cause1_title);
            radioCause2.setChecked(false);
            radioCause3.setChecked(false);
            radioCause4.setChecked(false);
        });

        radioCause2.setOnClickListener(v -> {
            selectedCause = getString(R.string.cause2_title);
            radioCause1.setChecked(false);
            radioCause3.setChecked(false);
            radioCause4.setChecked(false);
        });

        radioCause3.setOnClickListener(v -> {
            selectedCause = getString(R.string.cause3_title);
            radioCause1.setChecked(false);
            radioCause2.setChecked(false);
            radioCause4.setChecked(false);
        });

        radioCause4.setOnClickListener(v -> {
            selectedCause = getString(R.string.cause4_title);
            radioCause1.setChecked(false);
            radioCause2.setChecked(false);
            radioCause3.setChecked(false);
        });

        // Donate button listener
        donateButton.setOnClickListener(v -> {
            String amount = amountInput.getText().toString();
            if (!amount.isEmpty() && selectedCause != null) {
                // Navigate to Success Activity
                Intent intent = new Intent(SP2Activity.this, SuccessActivity.class);
                // Pass donation data to success activity
                intent.putExtra("cause", selectedCause);
                intent.putExtra("currency", selectedCurrency);
                intent.putExtra("amount", amount);
                startActivity(intent);
                finish(); // Close this activity
            } else {
                Toast.makeText(this, "Please select a cause and enter an amount", Toast.LENGTH_SHORT).show();
            }
        });

        // Spinner listener
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrency = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCurrency = "INR"; // Default currency
            }
        });
    }
}