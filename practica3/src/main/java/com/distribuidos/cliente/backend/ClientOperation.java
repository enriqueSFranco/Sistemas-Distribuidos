/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.cliente.backend;

import com.distribuidos.ifaces.RMIInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pekochu
 */
public class ClientOperation {
    
    private RMIInterface toServer;
    private Registry registry;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientOperation.class);
    
    public ClientOperation(String addr, int port) throws NotBoundException, RemoteException{
        registry = LocateRegistry.getRegistry(addr, port);
        toServer = (RMIInterface) registry.lookup("main_books_server");
    }
    
    public void sayHello(String txt) throws RemoteException {
        String response = toServer.helloTo(txt);
        LOGGER.info("Respuesta: {}", response);
    }
    
     public Registry getRegistry() {
        return registry;
    }
}
