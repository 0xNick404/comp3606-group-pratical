package com.example.furnitureorderingapp;

import java.util.ArrayList;

public class Cart {

    public interface CartChangeListener {
        void onCartChanged();
    }

    private static Cart instance;
    private CartChangeListener listener;

    private ArrayList<String> items;
    private ArrayList<Integer> prices;

    private Cart() {
        items = new ArrayList<>();
        prices = new ArrayList<>();
    }

    public static synchronized Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void setListener(CartChangeListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public void addItem(String item, int price) {
        items.add(item);
        prices.add(price);
        if (listener != null) {
            listener.onCartChanged();
        }
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public ArrayList<Integer> getPrices() {
        return prices;
    }

    public int getCartItemCount() {
        return items.size();
    }

    // A method to clear the cart after checkout
    public void clearCart() {
        items.clear();
        prices.clear();
        if (listener != null) {
            listener.onCartChanged(); // Notify UI to reset badge
        }
    }
}
