package gui;

import javax.swing.SwingUtilities;

import gui.login.LoginFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new LoginFrame();

        });
    }
}