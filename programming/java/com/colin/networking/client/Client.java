package com.colin.networking.client;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Client {
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
       UIManager.setLookAndFeel(new NimbusLookAndFeel());
        new ClientFrame();
    }
}