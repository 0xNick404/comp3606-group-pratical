package com.example.furnitureorderingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CheckoutActivity extends AppCompatActivity {
    private static final String TAG = "FurnitureApp";
    private static final String FILENAME = "order_file.txt";

    private TextView userIdentifier;
    private TextView itemsListing;
    private TextView totalValue;
    private TextView chosenDate;
    private Button btnSave, btnOpen;
    private int subTotal = 0;
    public CalendarView shipDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        btnSave    = findViewById(R.id.placeOrder);
        btnOpen    = findViewById(R.id.openOrder);

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
            if (btnSave != null) btnSave.setText("Saving Order to File...");
            saveFile();
        }
    }

    public void onOpenOrder(View view){
        if (btnOpen != null) btnOpen.setText("Opening File\n" + FILENAME + "....");
        openFile();
    }

    public void onHomeBtnClicked(View view){
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

            Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveFile(){
        StringBuilder allStr = new StringBuilder();

        try (FileOutputStream outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            String[] lines = {
                    "Order for " + userIdentifier.getText(),
                    "Order Total: $" + String.valueOf(subTotal),
                    "Shipping Date: " + chosenDate.getText().toString()
            };
            for (String line : lines) {
                allStr.append(line).append('\n');
                outputStream.write((line + "\n").getBytes());
            }
            outputStream.write("END\n".getBytes());
            Toast.makeText(this, allStr.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Save failed", e);
            Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "Trying to save to File...");
    }

    public void openFile(){
        Toast.makeText(this, "Attempting to read file " + FILENAME, Toast.LENGTH_LONG).show();

        try (FileInputStream inputStream = openFileInput(FILENAME);
             Scanner in = new Scanner(inputStream)) {

            StringBuilder allStr = new StringBuilder();
            int count = 0;

            while (in.hasNextLine()) {
                String line = in.nextLine();
                if ("END".equals(line)) break;

                count++;
                allStr.append(line).append('\n');
                Toast.makeText(this, line + " (line " + count + ")", Toast.LENGTH_LONG).show();
            }

            Toast.makeText(this, "All info read:\n" + allStr, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Open failed", e);
            Toast.makeText(this, "Open failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "Trying to open and read File...");
    }
}
