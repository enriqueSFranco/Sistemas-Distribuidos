/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.cliente.interfaces;

import javax.swing.JLabel;

/**
 *
 * @author Dave
 */
public interface GUIClienteInterfaz {
    
    public void mostrar();
    public void limpiarCliente();
    public void actualizarReloj(String hora);
    public void actualizarLibro(String libro);
    public boolean reiniciarSesion();
    public JLabel obtenerEtiquetaDeReloj();
    
}
