package service;

import java.io.*;
import java.util.*;

public class CategoryService {

    private static final String FILE = "data/categories.txt";

    // READ categories
    public List<String> getCategories() {

        List<String> categories = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    categories.add(line.trim());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // ADD category
    public void addCategory(String category) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {

            writer.write(category);
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // DELETE category
    public void deleteCategory(String category) {

        List<String> categories = getCategories();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {

            for (String c : categories) {

                if (!c.equalsIgnoreCase(category)) {
                    writer.write(c);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}