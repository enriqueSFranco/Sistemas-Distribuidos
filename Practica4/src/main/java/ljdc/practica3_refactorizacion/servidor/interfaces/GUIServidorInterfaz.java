/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.servidor.interfaces;

import java.util.List;
import javax.swing.JLabel;
import ljdc.practica3_refactorizacion.general.modelo.Libro;

/**
 *
 * @author Dave
 */
public interface GUIServidorInterfaz {
    
    public void mostrar();
    public void limpiarPortada();
    public void actualizarSesion(int sesion);
    public void actualizarReloj(String hora);
    public void actualizarLista(List<Libro> lista);
    public void actualizarImagen(String ruta);
    public JLabel obtenerEtiquetaDeReloj();
}
