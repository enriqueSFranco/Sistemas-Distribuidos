/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.general;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

/**
 *
 * @author Dave
 */
public class Configuracion implements Serializable {
    
    protected int puerto;
    
    protected List<InfoServidor> servidores;
    protected InetAddress servidor_principal;
    protected InetAddress servidor_sincronizacion;

    public Configuracion() {
    }

    public Configuracion(int puerto, List<InfoServidor> servidores, InetAddress servidor_principal, InetAddress servidor_sincronizacion) {
        this.puerto = puerto;
        this.servidores = servidores;
        this.servidor_principal = servidor_principal;
        this.servidor_sincronizacion = servidor_sincronizacion;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public List<InfoServidor> getServidores() {
        return servidores;
    }

    public void setServidores(List<InfoServidor> servidores) {
        this.servidores = servidores;
    }

    public InetAddress getServidor_principal() {
        return servidor_principal;
    }

    public void setServidor_principal(InetAddress servidor_principal) {
        this.servidor_principal = servidor_principal;
    }

    public InetAddress getServidor_sincronizacion() {
        return servidor_sincronizacion;
    }

    public void setServidor_sincronizacion(InetAddress servidor_sincronizacion) {
        this.servidor_sincronizacion = servidor_sincronizacion;
    }
    
}
