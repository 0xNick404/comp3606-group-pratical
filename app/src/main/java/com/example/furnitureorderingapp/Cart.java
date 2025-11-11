package com.example.furnitureorderingapp;

import java.util.ArrayList;

/** * A singleton class that manages the state of the shopping cart for the entire application.
 * It holds the list of items, their prices, and tracks whether a receipt has been
 * saved for the current user session.
 */
public class Cart {

    // Interface for the homepage to listen for changes (e.g., to update the cart badge)
    public interface CartChangeListener {
        void onCartChanged();
    }

    // --- Singleton Instance ---
    private static Cart instance;

    // --- Member Variables ---
    private CartChangeListener listener;
    private ArrayList<String> items;
    private ArrayList<Integer> prices;

    /**
     * This boolean is the "memory" of the app for a user's session.
     * It tracks if an order has been created and saved to a file, which helps
     * the OrderConfirmationActivity decide which buttons to show.
     * It is set to 'true' after a successful file save and reset to 'false' in clearCart().
     */
    private boolean hasInitialOrderBeenSaved = false;

    /**
     * Private constructor to prevent creating more than one instance of the Cart.
     */
    private Cart() {
        items = new ArrayList<>();
        prices = new ArrayList<>();
    }

    /**
     * Gets the single, shared instance of the Cart class.
     */
    public static synchronized Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }


    // --- Methods for Managing the "Saved" State ---

    /**
     * Sets the flag to true, indicating that an order file has been saved for this session.
     * This is called from OrderConfirmationActivity after a successful file write.
     */
    public void markOrderAsSaved() {
        this.hasInitialOrderBeenSaved = true;
    }

    /**
     * Checks if an order has been saved during this session.
     * This is called by OrderConfirmationActivity to determine its initial UI state.
     * @return true if an order has been saved, false otherwise.
     */
    public boolean hasInitialOrderBeenSaved() {
        return this.hasInitialOrderBeenSaved;
    }


    // --- Core Cart and Listener Methods ---

    public void setListener(CartChangeListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public void addItem(String item, int price) {
        items.add(item);
        prices.add(price);
        // Notify the UI that the cart has changed
        if (listener != null) {
            listener.onCartChanged();
        }
    }

    /**
     * Clears all items and prices from the cart and, crucially, resets the
     * hasInitialOrderBeenSaved flag. This is called from LoginActivity
     * to ensure every new user session starts fresh.
     */
    public void clearCart() {
        items.clear();
        prices.clear();
        hasInitialOrderBeenSaved = false; // Reset the saved state for the new session
        if (listener != null) {
            listener.onCartChanged(); // Update UI (e.g., reset the cart badge)
        }
    }


    // --- Standard Getters ---

    public ArrayList<String> getItems() {
        return items;
    }



    public ArrayList<Integer> getPrices() {
        return prices;
    }

    public int getCartItemCount() {
        return items.size();
    }

    public int getTotalPrice() {
        int total = 0;
        for (int price : prices) {
            total += price;
        }
        return total;
    }






}
