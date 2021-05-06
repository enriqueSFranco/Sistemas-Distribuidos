/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.server.backend;

import com.distribuidos.database.Conexion;
import com.distribuidos.ifaces.RMIBackupInterface;
import com.distribuidos.server.gui.BooksView;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.distribuidos.ifaces.RMIClientInterface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author pekochu
 */
public class Server {
    
    private String direccionRespaldo = "localhost";
    
    private int puertoServidor = 2370;
    private int puertoRespaldo = 2371;
    private int puertoCliente = 6000;

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private Registry registry;
    
    private Registry registryClient;
    private RMIClientInterface clienteOP;
    
    private ServerOperation serverOP;
    private ServerBackupOperation serverBackupOP;
    
    private BooksView vista;
    private int sesion = 0;
    
    private boolean isBackup = false;

    public Server(BooksView vista, boolean isBackup) throws RemoteException {
        this.vista = vista;
        this.isBackup =isBackup;
        if (!isBackup){
            registry = LocateRegistry.createRegistry(puertoServidor);
        System.out.println("Principal: " + puertoServidor);
        }else{
            registry = LocateRegistry.createRegistry(puertoRespaldo);
        System.out.println("Respaldo: " + puertoRespaldo);
        }
        actualizarSesion(false);
    }

    public void startRMI() throws RemoteException {
        this.serverOP = new ServerOperation(vista, isBackup);
        registry.rebind("main_books_server", this.serverOP);
        if (isBackup){
            this.serverBackupOP = new ServerBackupOperation(this.vista);
            this.registry.rebind("backup_server", this.serverBackupOP);
        }
    }
    
    public void actualizarSesion(boolean nuevaSesion){
        Conexion con = new Conexion();
        con.crearConexion();
        sesion = con.obtenerSesion();
        if(nuevaSesion){
            sesion++;
            this.serverOP.setReiniciarSesion(true);
        }
        con.desconectar();
    }
    
    public void reiniciarClientes(){
        Conexion con = new Conexion();
        con.crearConexion();
        List<String> ips = con.obtenerClientes();
        
        for(String ip: ips){
            try {
                registryClient = LocateRegistry.getRegistry(ip, puertoCliente);
                clienteOP = (RMIClientInterface) registry.lookup("book_client");
                clienteOP.limpiarDatos();
            } catch (RemoteException ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        con.desconectar();
    }
    
    public void actualizarRespaldo(){
        try {
            Registry registry = LocateRegistry.getRegistry(this.vista.getServer().getDireccionRespaldo(), this.vista.getServer().getPuertoRespaldo());
            RMIBackupInterface respaldoOP = (RMIBackupInterface) registry.lookup("backup_server");
            
            respaldoOP.reiniciarSesion(this.sesion);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Registry getRegistry() {
        return registry;
    }

    public int getSesion() {
        return sesion;
    }

    public String getDireccionRespaldo() {
        return direccionRespaldo;
    }

    public int getPuertoRespaldo() {
        return puertoRespaldo;
    }

}
