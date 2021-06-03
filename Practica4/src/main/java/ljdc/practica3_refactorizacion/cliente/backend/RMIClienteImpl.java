/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.cliente.backend;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import ljdc.practica3_refactorizacion.cliente.interfaces.GUIClienteInterfaz;
import ljdc.practica3_refactorizacion.general.InfoServidor;
import ljdc.practica3_refactorizacion.general.ManejadorDeConfiguracion;
import ljdc.practica3_refactorizacion.general.Reloj;
import ljdc.practica3_refactorizacion.general.modelo.Libro;
import ljdc.practica3_refactorizacion.rmi.RMIClienteInterfaz;
import ljdc.practica3_refactorizacion.rmi.RMIServidorInterfaz;

/**
 *
 * @author Dave
 */
public class RMIClienteImpl extends UnicastRemoteObject implements RMIClienteInterfaz {
    
    private ManejadorDeConfiguracion manejadorConfiguracion;
    
    private GUIClienteInterfaz gui;
    private Reloj reloj;
    
    public RMIClienteImpl(ManejadorDeConfiguracion manejadorConfiguracion, GUIClienteInterfaz gui, Reloj reloj){
        this.manejadorConfiguracion = manejadorConfiguracion;
        this.gui = gui;
        this.reloj = reloj;
    }

    @Override
    public void reiniciarSesion() throws RemoteException {
        // Mostrar si se desea reiniciar o salir
        if (this.gui.reiniciarSesion()){
            this.gui.limpiarCliente();
        }else{
            System.exit(0);
        }
    }
    
    public void registrarse(){
        Registry registro;
        for(InfoServidor servidor: manejadorConfiguracion.getConfiguracion().getServidores()){
            try {
                registro = LocateRegistry.getRegistry(servidor.getDireccion().getHostAddress(), servidor.getPuerto());
                RMIServidorInterfaz serv = (RMIServidorInterfaz) registro.lookup("servidor");
                serv.regitrarse(this.manejadorConfiguracion.getConfiguracion().getPuerto(), InetAddress.getLocalHost().getHostName());
                
                break;
            } catch (UnknownHostException ex) {
                Logger.getLogger(RMIClienteImpl.class.getName()).log(Level.WARNING, "Servidor desconocido con datos {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClienteImpl.class.getName()).log(Level.WARNING, "No se pudo registrar en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIClienteImpl.class.getName()).log(Level.WARNING, "No hay modulo de servidor registrado en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            }
        }
    }
    
    public String pedirLibro(){
        String libroStr = "<No hay servicio disponible>";
        Libro libro = null;
        Registry registro;
        for(InfoServidor servidor: manejadorConfiguracion.getConfiguracion().getServidores()){
            try {
                registro = LocateRegistry.getRegistry(servidor.getDireccion().getHostAddress(), servidor.getPuerto());
                RMIServidorInterfaz serv = (RMIServidorInterfaz) registro.lookup("servidor");
                libro = serv.pedirLibro(this.manejadorConfiguracion.getConfiguracion().getPuerto(), this.reloj.obtenerHora());
                
                if(libro != null){
                    libroStr = 
                            "ISBN-13: " + libro.getIsbn() +
                            "\nNombre: " + libro.getNombre() +
                            "\nAutor: " + libro.getAutor() + 
                            "\nEditorial: " + libro.getEditorial() +
                            "\nPrecio: " + libro.getPrecio();
                }else{
                    libroStr = "<Lo sentimos. Ya no quedan libros>";
                }
                
                break;
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClienteImpl.class.getName()).log(Level.WARNING, "No se pudo pedir en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIClienteImpl.class.getName()).log(Level.WARNING, "No hay modulo de servidor registrado en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            }
        }
        
        return libroStr;
    }
    
}

