package com.example.furnitureorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class OrderConfirmationActivity extends AppCompatActivity {

    // Views
    private Button btnSaveOrder, btnOpenOrder, btnUpdateOrder;
    private TextView tvOrderContent;

    // Data from CheckoutActivity
    private String customerName;
    private String shippingDate;
    private int total;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // --- 1. Find all views from the layout ---
        btnSaveOrder = findViewById(R.id.btnSaveOrder);
        btnOpenOrder = findViewById(R.id.btnOpenOrder);
        btnUpdateOrder = findViewById(R.id.btnUpdateOrder);
        tvOrderContent = findViewById(R.id.tvOrderContent);

        // --- 2. Get the data passed from CheckoutActivity ---
        Intent intent = getIntent();
        customerName = intent.getStringExtra("USER_FULL_NAME");
        shippingDate = intent.getStringExtra("SHIP_DATE");
        total = intent.getIntExtra("TOTAL", 0);
        items = intent.getStringArrayListExtra("ITEMS");

        // --- 3. Set the initial state of the screen ---
        tvOrderContent.setText(""); // Start with blank text view
        btnOpenOrder.setEnabled(false);   // Disable "Open" until saved
        btnUpdateOrder.setEnabled(false); // Disable "Update" until saved
    }

    /**
     * Called when the "Save Order" button is clicked.
     */
    public void onSaveOrderClick(View view) {
        // Use FileHelper to save the data
        boolean isSaved = FileHelper.saveOrderReceipt(this, customerName, shippingDate, total, items);

        if (isSaved) {
            Toast.makeText(this, "order.txt file saved", Toast.LENGTH_LONG).show();
            // Update the UI state
            btnSaveOrder.setEnabled(false);   // Disable "Save" because it's done
            btnOpenOrder.setEnabled(true);    // Enable "Open"
            btnUpdateOrder.setEnabled(true);  // Enable "Update"
        } else {
            Toast.makeText(this, "Error: Could not save the order.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when the "Open Order" button is clicked.
     */
    public void onOpenOrderClick(View view) {
        // Use FileHelper to read the data from the file
        String fileContent = FileHelper.readOrder(this);
        // Display the content in the TextView
        tvOrderContent.setText(fileContent);
    }

    /**
     * Called when the "Update Order" button is clicked.
     * Navigates the user back to the homepage to modify their cart.
     */
    public void onUpdateOrderClick(View view) {
        Toast.makeText(this, "Returning to shop to update your order...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, HomepageActivity.class);
        // Pass the user's name back for the greeting
        intent.putExtra("USER_FULL_NAME", customerName);
        // Clear the activity stack so the user starts fresh from the homepage
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close this confirmation activity
    }
}
