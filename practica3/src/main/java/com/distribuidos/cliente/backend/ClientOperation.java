/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.cliente.backend;

import com.distribuidos.cliente.gui.MainView;
import com.distribuidos.ifaces.RMIInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.distribuidos.ifaces.RMIClientInterface;
import java.rmi.AccessException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

/**
 *
 * @author pekochu
 */
public class ClientOperation extends UnicastRemoteObject implements RMIClientInterface {
    
    private RMIInterface toServer;
    private Registry registry;
    private Registry registryClient;
    
    private MainView vista;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientOperation.class);
    
    private String ipServidor = "26.152.135.13";
    private String ipRespaldo = "26.58.72.131";
    
    private int puertoServidor = 2370;
    private int puertoRespaldo = 2371;
    private int puertoCliente = 6000;
    
    public ClientOperation(MainView vista) throws RemoteException, NotBoundException{
        this.vista = vista;
        try {
            registry = LocateRegistry.getRegistry(ipServidor, puertoServidor);
            toServer = (RMIInterface) registry.lookup("main_books_server");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Servidor principal no responde. Conectando a respaldo");
            //java.util.logging.Logger.getLogger(ClientOperation.class.getName()).log(Level.SEVERE, null, ex);
            registry = LocateRegistry.getRegistry(ipRespaldo, puertoRespaldo);
            toServer = (RMIInterface) registry.lookup("main_books_server");
        }
        
        try {
            registryClient = LocateRegistry.createRegistry(puertoCliente);
            registryClient.rebind("book_client", this);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(ClientOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void conectarConRespaldo() throws RemoteException, NotBoundException{
        registry = LocateRegistry.getRegistry(ipRespaldo, puertoRespaldo);
        toServer = (RMIInterface) registry.lookup("main_books_server");
    }
    
    public void sayHello(String txt) throws RemoteException {
        String response = toServer.helloTo(txt);
        LOGGER.info("Respuesta: {}", response);
    }
    
    public String pedirLibro() throws RemoteException, NotBoundException {
        Libro libro = null;
        try {
            libro = toServer.pedirLibro(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(ClientOperation.class.getName()).log(Level.SEVERE, null, ex);
            conectarConRespaldo();
            
        }
        String respuesta = "Error: <No se pudo obtener un libro>";
        if (libro != null){
            respuesta = 
                    "ISBN-13: " + libro.getIsbn() +
                    "\nNombre: " + libro.getNombre() +
                    "\nAutor: " + libro.getAutor() + 
                    "\nEditorial: " + libro.getEditorial() +
                    "\nPrecio: " + libro.getPrecio();
        }
        return respuesta;
    }
    
     public Registry getRegistry() {
        return registry;
    }

    @Override
    public void limpiarDatos() throws RemoteException {
        this.vista.limpiarDatos();
    }
}
