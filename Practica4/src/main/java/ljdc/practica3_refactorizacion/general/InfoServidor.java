/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.general;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author Dave
 */
public class InfoServidor implements Serializable {
    private InetAddress direccion;
    private int puerto;

    public InfoServidor(InetAddress direccion, int puerto) {
        this.direccion = direccion;
        this.puerto = puerto;
    }

    public InfoServidor() {
    }

    public InetAddress getDireccion() {
        return direccion;
    }

    public void setDireccion(InetAddress direccion) {
        this.direccion = direccion;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
    
}
