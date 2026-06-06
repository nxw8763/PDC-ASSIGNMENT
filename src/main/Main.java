package main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import gui.login.LoginFrame;
import database.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {

	   if (!ApplicationLock.acquire()) {

	        JOptionPane.showMessageDialog(
	                null,
	                "Application is already running."
	        );

	        System.exit(0);
	    }


	   try {

		    DatabaseInitializer.initialize();

		} catch (Exception e) {

		    JOptionPane.showMessageDialog(
		            null,
		            "Failed to initialize database:\n" + e.getMessage(),
		            "Startup Error",
		            JOptionPane.ERROR_MESSAGE
		    );

		    System.exit(1);
		}
        SwingUtilities.invokeLater(() -> {

            new LoginFrame();

        });
    }
}