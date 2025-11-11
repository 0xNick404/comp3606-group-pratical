package com.example.furnitureorderingapp;

import android.content.Context;
import android.util.Log; // Import Log for debugging
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHelper {
    private static final String FILE_NAME = "order.txt";
    private static final String TAG = "FileHelper"; // Tag for logging

    /**
     * Saves a new order receipt by OVERWRITING the file.
     * This now uses FileOutputStream, similar to your teacher's example.
     */
    public static boolean saveOrderReceipt(Context context, String customerName, String shippingDate, int totalCost, ArrayList<String> items) {
        // 1. Build the entire receipt string first.
        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append("Customer: ").append(customerName).append("\n");
        receiptContent.append("Shipping Date: ").append(shippingDate).append("\n");
        receiptContent.append("Total Cost: $").append(totalCost).append("\n");
        receiptContent.append("--- Items Purchased ---\n");

        if (items != null) {
            for (String item : items) {
                receiptContent.append("- ").append(item).append("\n");
            }
        }

        // 2. Write the string to the file using FileOutputStream.
        //    Context.MODE_PRIVATE is the default and means OVERWRITE.
        try (FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            outputStream.write(receiptContent.toString().getBytes());
            Log.d(TAG, "File saved successfully.");
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Save failed", e);
            return false;
        }
    }

    /**
     * Appends new items to an existing order receipt.
     * This uses FileOutputStream in APPEND mode.
     */
    public static boolean appendItemsToOrder(Context context, ArrayList<String> newItems, int newTotal) {
        try {
            // read the file to update the total.
            String existingContent = readOrder(context);
            if (existingContent.contains("Error:")) {
                return false; // Can't update a file that doesn't exist.
            }

            // Create a new version of the content with the updated total.
            // safer than trying to modify the file line by line.
            String[] lines = existingContent.split("\n");
            StringBuilder updatedContent = new StringBuilder();
            for (String line : lines) {
                if (line.startsWith("Total Cost:")) {
                    updatedContent.append("Total Cost: $").append(newTotal).append("\n");
                } else {
                    updatedContent.append(line).append("\n");
                }
            }

            // Now, append the new items to this updated content.
            for (String newItem : newItems) {
                updatedContent.append("- ").append(newItem).append("\n");
            }

            // OVERWRITE the old file with the completely new content.
            // This is a more robust way to "append" while updating the total
            try (FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
                outputStream.write(updatedContent.toString().getBytes());
                Log.d(TAG, "File appended successfully.");
                return true;
            }

        } catch (IOException e) {
            Log.e(TAG, "Append failed", e);
            return false;
        }
    }

    /**
     * Reads the entire content of the order.txt file.
     *  FileInputStream
     */
    public static String readOrder(Context context) {
        StringBuilder content = new StringBuilder();
        try (FileInputStream inputStream = context.openFileInput(FILE_NAME);
             Scanner scanner = new Scanner(inputStream)) {

            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            Log.d(TAG, "File read successfully.");
            return content.toString();
        } catch (IOException e) {
            Log.e(TAG, "Read failed", e);
            return "Error: " + e.getMessage();
        }
    }
}
