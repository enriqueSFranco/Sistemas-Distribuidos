package com.distribuidos;

import com.distribuidos.gui.Selector;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        java.awt.EventQueue.invokeLater(() -> {
            new Selector().setVisible(true);
        });
    }
}
