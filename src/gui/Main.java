package gui;

import javax.swing.SwingUtilities;
import gui.login.LoginFrame;
import database.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {

    	
    	DatabaseInitializer db = new DatabaseInitializer();
    	db.initialize();
        SwingUtilities.invokeLater(() -> {

            new LoginFrame();

        });
    }
}