package com.example.furnitureorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {

    private TextView cartItemCountBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        String name = getIntent().getStringExtra("USER_FULL_NAME");
        ((TextView) findViewById(R.id.tvGreeting)).setText("Welcome back, " + name + "!");

        cartItemCountBadge = findViewById(R.id.cart_badge);
        Button btnAddToCart1 = findViewById(R.id.btnAddToCart1);
        Button btnAddToCart2 = findViewById(R.id.btnAddToCart2);
        Button btnAddToCart3 = findViewById(R.id.btnAddToCart3);
        Button btnAddToCart4 = findViewById(R.id.btnAddToCart4);
        Button btnAddToCart5 = findViewById(R.id.btnAddToCart5);
        Button btnAddToCart6 = findViewById(R.id.btnAddToCart6);

        // Set up the listener for the Cart singleton
        Cart.getInstance().setListener(new Cart.CartChangeListener() {
            @Override
            public void onCartChanged() {
                updateCartBadge();
            }
        });

        // Set click listeners to add items to the Cart
        btnAddToCart1.setOnClickListener(v -> {
            Cart.getInstance().addItem("Brown LoveSeat", 1250);
            Toast.makeText(this, "Brown LoveSeat added", Toast.LENGTH_SHORT).show();
        });
        btnAddToCart2.setOnClickListener(v -> Cart.getInstance().addItem("Pink LoveSeat", 2100));
        btnAddToCart3.setOnClickListener(v -> Cart.getInstance().addItem("Grey Chair", 750));
        btnAddToCart4.setOnClickListener(v -> Cart.getInstance().addItem("Office Chair", 550));
        btnAddToCart5.setOnClickListener(v -> Cart.getInstance().addItem("Brown Chair", 900));
        btnAddToCart6.setOnClickListener(v -> Cart.getInstance().addItem("Purple Loveseat", 2850));

        updateCartBadge(); // Initial update when activity starts
    }

    private void updateCartBadge() {
        int itemCount = Cart.getInstance().getCartItemCount();
        if (itemCount > 0) {
            cartItemCountBadge.setVisibility(View.VISIBLE);
            cartItemCountBadge.setText(String.valueOf(itemCount));
        } else {
            cartItemCountBadge.setVisibility(View.GONE);
        }
    }

    public void onCheckout(View view) {
        Intent intent = new Intent(HomepageActivity.this, CheckoutActivity.class);
        intent.putExtra("Name", getIntent().getStringExtra("USER_FULL_NAME"));
        // Get data from the Cart singleton to pass to the next screen
        intent.putStringArrayListExtra("Items", Cart.getInstance().getItems());
        intent.putIntegerArrayListExtra("Prices", Cart.getInstance().getPrices());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cart.getInstance().removeListener(); // Clean up listener
    }
}
