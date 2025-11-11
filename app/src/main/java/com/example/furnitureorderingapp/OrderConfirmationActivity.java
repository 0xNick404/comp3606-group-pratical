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

    private Button btnCreateReceipt, btnOpenReceipt, btnUpdateShopping, btnUpdateReceipt;
    private TextView tvOrderContent;

    private String customerName;
    private String shippingDate;
    private int total;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // --- 1. Find all views ---
        btnCreateReceipt = findViewById(R.id.btnCreateReceipt);
        btnOpenReceipt = findViewById(R.id.btnOpenReceipt);
        btnUpdateShopping = findViewById(R.id.btnUpdateShopping);
        btnUpdateReceipt = findViewById(R.id.btnUpdateReceipt);
        tvOrderContent = findViewById(R.id.tvOrderContent);

        // --- 2. Get the latest data from the Intent ---
        Intent intent = getIntent();
        customerName = intent.getStringExtra("USER_FULL_NAME");
        shippingDate = intent.getStringExtra("SHIP_DATE");
        total = intent.getIntExtra("TOTAL", 0);
        items = intent.getStringArrayListExtra("ITEMS");

        // --- 3. THE NEW, SIMPLER LOGIC ---
        if (Cart.getInstance().hasInitialOrderBeenSaved()) {
            // --- This is an UPDATE flow ---
            // The user has returned after shopping.
            tvOrderContent.setText(FileHelper.readOrder(this)); // Display old receipt
            btnCreateReceipt.setVisibility(View.GONE); // Hide Create
            btnUpdateReceipt.setVisibility(View.VISIBLE); // Show Update
            btnOpenReceipt.setEnabled(true);
            btnUpdateShopping.setEnabled(true);
        } else {
            // --- This is a NEW ORDER flow ---
            tvOrderContent.setText(""); // Screen is blank
            btnCreateReceipt.setVisibility(View.VISIBLE); // Show Create
            btnUpdateReceipt.setVisibility(View.GONE); // Hide Update
            btnOpenReceipt.setEnabled(false);
            btnUpdateShopping.setEnabled(false);
        }
    }

    // This method now handles BOTH creating and updating the file.
    private void saveOrUpdateFile() {
        boolean isSaved = FileHelper.saveOrderReceipt(this, customerName, shippingDate, total, items);
        if (isSaved) {
            Toast.makeText(this, "order.txt file has been saved/updated!", Toast.LENGTH_LONG).show();
            // After saving, mark the order as saved in our singleton
            Cart.getInstance().markOrderAsSaved();
            // Enable all buttons
            btnOpenReceipt.setEnabled(true);
            btnUpdateShopping.setEnabled(true);
            btnUpdateReceipt.setEnabled(true);
            btnCreateReceipt.setVisibility(View.GONE); // Always hide Create after a save
            btnUpdateReceipt.setVisibility(View.VISIBLE); // Always show Update after a save
        } else {
            Toast.makeText(this, "Error: Could not save the file.", Toast.LENGTH_LONG).show();
        }
    }

    // --- BUTTON CLICK HANDLERS ---

    public void onCreateReceiptClick(View view) {
        // This is now simpler. It just calls our shared save method.
        saveOrUpdateFile();
    }

    public void onUpdateReceiptClick(View view) {
        // This also calls the exact same method. The data (items, total) is already the new, updated data.
        saveOrUpdateFile();
        // As a bonus, immediately show the updated file content
        onOpenReceiptClick(null);
    }

    public void onOpenReceiptClick(View view) {
        String fileContent = FileHelper.readOrder(this);
        tvOrderContent.setText(fileContent);
        Toast.makeText(this, "Displaying content from order.txt", Toast.LENGTH_SHORT).show();
    }

    public void onUpdateShoppingClick(View view) {
        Toast.makeText(this, "Returning to shop to modify your order...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomepageActivity.class);
        intent.putExtra("USER_FULL_NAME", customerName);
        startActivity(intent);
        // We now FINISH this activity. A new one will be created when we return.
        finish();
    }
}
