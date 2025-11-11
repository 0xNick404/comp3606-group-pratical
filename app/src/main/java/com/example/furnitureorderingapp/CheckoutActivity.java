package com.example.furnitureorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {
    private TextView userIdentifier, itemsListing, totalValue, chosenDate;
    private int subTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        itemsListing = findViewById(R.id.itemsListing);
        totalValue = findViewById(R.id.totalValue);
        userIdentifier = findViewById(R.id.userIdentifier);
        chosenDate = findViewById(R.id.chosenDate);
        CalendarView shipDate = findViewById(R.id.shipDate);

        String name = getIntent().getStringExtra("Name");
        ArrayList<Integer> prices = getIntent().getIntegerArrayListExtra("Prices");

        if (prices != null) {
            for (int price : prices) {
                subTotal += price;
            }
        }

        userIdentifier.setText(name);
        itemsListing.setText(String.valueOf(getIntent().getStringArrayListExtra("Items")));
        totalValue.setText("$" + subTotal);

        shipDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
            chosenDate.setText(selectedDate);
        });
    }

    public void onPlaceOrder(View view) {
        String selectedDate = chosenDate.getText().toString();
        if (selectedDate.isEmpty() || selectedDate.equals("Select a Date")) {
            Toast.makeText(this, "Please Select a Shipping Date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(CheckoutActivity.this, OrderConfirmationActivity.class);
        intent.putExtra("USER_FULL_NAME", userIdentifier.getText().toString());
        intent.putExtra("SHIP_DATE", selectedDate);
        intent.putExtra("TOTAL", subTotal);
        intent.putStringArrayListExtra("ITEMS", getIntent().getStringArrayListExtra("Items"));

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();



    }
}
