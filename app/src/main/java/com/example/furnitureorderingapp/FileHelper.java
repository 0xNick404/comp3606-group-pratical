package com.example.furnitureorderingapp;

import android.content.Context;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner; // Import Scanner

public class FileHelper {
    private static final String FILE_NAME = "order.txt"; // Changed name as requested

    // This method to SAVE the file is still needed and is correct
    public static boolean saveOrderReceipt(Context context, String customerName, String shippingDate, int totalCost, ArrayList<String> items) {
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

        File file = new File(context.getFilesDir(), FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(receiptContent.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- READ THE FILE ---
    public static String readOrder(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        StringBuilder content = new StringBuilder();

        // Use a try-with-resources block for the Scanner
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            return content.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Error: order.txt not found."; // Return an error message
        }
    }
}
