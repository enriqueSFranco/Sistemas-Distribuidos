/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmf.practica2.gui;

/**
 *
 * @author Dave
 */
public class Programa {
    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiClock().setVisible(true);
            }
        });
    }
}
