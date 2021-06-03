/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion;

import ljdc.practica3_refactorizacion.general.gui.Selector;

/**
 *
 * @author Dave
 */
public class Aplicacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new Selector().setVisible(true);
        });
    }
    
}
