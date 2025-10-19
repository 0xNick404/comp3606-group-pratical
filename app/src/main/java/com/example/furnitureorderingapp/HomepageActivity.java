package com.example.furnitureorderingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;
import android.view.View;


public class HomepageActivity extends AppCompatActivity {

    private TextView cartItemCountBadge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String name = getIntent().getStringExtra("USER_FULL_NAME");

        ((TextView)findViewById(R.id.tvGreeting)).setText("Welcome back " + name + "!");

        // Find your views
        cartItemCountBadge = findViewById(R.id.cart_badge);
        Button btnAddToCart1 = findViewById(R.id.btnAddToCart1);
        Button btnAddToCart2 = findViewById(R.id.btnAddToCart2);
        Button btnAddToCart3 = findViewById(R.id.btnAddToCart3);
        Button btnAddToCart4 = findViewById(R.id.btnAddToCart4);
        Button btnAddToCart5 = findViewById(R.id.btnAddToCart5);
        Button btnAddToCart6 = findViewById(R.id.btnAddToCart6);

        // --- Set up the listener for the Cart singleton ---
        Cart.getInstance().setListener(new Cart.CartChangeListener() {
            @Override
            public void onCartChanged() {
                updateCartBadge();
            }
        });

        // Set click listeners
        btnAddToCart1.setOnClickListener(v -> {
            Cart.getInstance().addItem("Brown LoveSeat");
            updateCartBadge();
        });

        btnAddToCart2.setOnClickListener(v -> {
            Cart.getInstance().addItem("Pink LoveSeat");
            updateCartBadge();
        });

        btnAddToCart3.setOnClickListener(v -> {
            Cart.getInstance().addItem("Grey Chair");
            Toast.makeText(this, "Grey Chair added to cart!", Toast.LENGTH_SHORT).show();
        });

        btnAddToCart4.setOnClickListener(v -> {
            Cart.getInstance().addItem("Office Chair");
            Toast.makeText(this, "Office Chair added to cart!", Toast.LENGTH_SHORT).show();
        });

        btnAddToCart5.setOnClickListener(v -> {
            Cart.getInstance().addItem("Brown Chair");
            Toast.makeText(this, "Brown Chair added to cart!", Toast.LENGTH_SHORT).show();
        });

        btnAddToCart6.setOnClickListener(v -> {
            Cart.getInstance().addItem("Purple Loveseat");
            Toast.makeText(this, "Purple Loveseat added to cart!", Toast.LENGTH_SHORT).show();
        });




        // Also update the badge when the activity starts, in case the cart already has items
        updateCartBadge();
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
    @Override
    protected void onDestroy() {
        // Clean up the listener when the activity is destroyed to prevent memory leaks
        Cart.getInstance().removeListener();
        super.onDestroy();
    }



}
