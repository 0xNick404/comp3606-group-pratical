package com.example.furnitureorderingapp;

import java.util.ArrayList;

public class Cart {
    // 1. Static instance for the singleton
    private static Cart instance;

    // 2. The list of items in the cart
    private ArrayList<String> items;

    public interface CartChangeListener {
        void onCartChanged();
    }

    // 2. A listener variable
    private CartChangeListener listener;

    // 3. A method to set the listener
    public void setListener(CartChangeListener listener) {
        this.listener = listener;
    }
    // --- End of New Code ---


    private Cart() {
        items = new ArrayList<>();
    }

    public static synchronized Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addItem(String itemName) {
        items.add(itemName);
        // --- Updated Code ---
        // 4. Notify the listener that the cart has changed
        if (listener != null) {
            listener.onCartChanged();
        }
        // --- End of Updated Code ---
    }

    public int getCartItemCount() {
        return items.size();
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void removeListener() {
        listener = null;
    }

    public void clearCart() {
        items.clear();
    }

}
