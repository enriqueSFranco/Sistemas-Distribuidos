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
import com.distribuidos.models.ServerConnect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author pekochu
 */
public class Server {
    
    private ServerConnect preferences;
    private String direccionRespaldo = "26.58.72.131";
    
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
        this.isBackup = isBackup;
        
        getCurrentPreferences();
        this.puertoServidor = preferences.getWorkingPort();
        this.puertoRespaldo = preferences.getBackupPort();
        this.direccionRespaldo = preferences.getBackupAddress().getHostAddress();
        this.puertoCliente = preferences.getClientPort();
        
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
                clienteOP = (RMIClientInterface) registryClient.lookup("book_client");
                System.out.println("IP:"+ip);
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
    
    public void getCurrentPreferences(){
        try{
            // Loading the YAML file from the /resources folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path connectFile = Paths.get("./", "servidor.yaml");
            File file = connectFile.toFile();
            
            // Instantiating a new ObjectMapper as a YAMLFactory
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            
            if(!file.exists()){
                file.createNewFile();
                om.writeValue(file, new ServerConnect(2370, 
                        InetAddress.getByName("0.0.0.0"), 2371, 6000));
            } 
            
            this.preferences = om.readValue(file, ServerConnect.class);
        }catch(IOException iox){
            LOGGER.error("Error en I/O", iox);
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
