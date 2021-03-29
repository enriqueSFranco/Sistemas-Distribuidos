package com.distribuidos.pract1;

import com.distribuidos.pract1.gui.GuiClock;

public class Application {

    public static void main(String[] args){
        // Inicializar GUI
        java.awt.EventQueue.invokeLater(() -> new GuiClock().setVisible(true));
    }

}
