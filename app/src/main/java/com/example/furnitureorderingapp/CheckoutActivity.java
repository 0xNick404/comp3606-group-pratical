package com.example.furnitureorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;
import android.view.View;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {
    private TextView userIdentifier;
    private TextView itemsListing;
    private TextView totalValue;
    private TextView chosenDate;
    private Button placeOrder;
    private int subTotal = 0;
    public CalendarView shipDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        itemsListing = findViewById(R.id.itemsListing);
        totalValue = findViewById(R.id.totalValue);
        userIdentifier = findViewById(R.id.userIdentifier);
        chosenDate = findViewById(R.id.chosenDate);
        shipDate = findViewById(R.id.shipDate);

        String name = getIntent().getStringExtra("Name");
        ArrayList<String> items = getIntent().getStringArrayListExtra("Items");
        ArrayList<Integer> prices = getIntent().getIntegerArrayListExtra("Prices");


        for (int i = 0; i < prices.size(); i++){
            subTotal += prices.get(i);
        }


        userIdentifier.setText(name);
        itemsListing.setText(String.valueOf(items));
        totalValue.setText("$" + String.valueOf(subTotal));


        shipDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                updateChosenDate(selectedDate);
            }
        });

    }

    private void updateChosenDate(String selectedDate) {
        chosenDate.setText(selectedDate);
    }




    public void onPlaceOrder(View view){
        if(chosenDate.getText() == null){
            Toast.makeText(this, "Please Select a Shipping Date", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Intent intent = new Intent(CheckoutActivity.this, HomepageActivity.class);
            intent.putExtra("USER_FULL_NAME", userIdentifier.getText());
            intent.putExtra("Total", String.valueOf(subTotal));
            intent.putExtra("Ship_Date", chosenDate.getText());
            startActivity(intent);

            Toast.makeText(this, "Placing Order", Toast.LENGTH_SHORT).show();
        }
    }
}
