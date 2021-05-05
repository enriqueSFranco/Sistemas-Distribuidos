/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.server.backend;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pekochu
 */
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private Registry registry;

    public Server(int port) throws RemoteException {
        registry = LocateRegistry.createRegistry(port);        
    }

    public void startRMI() throws RemoteException {
        registry.rebind("main_books_server", new ServerOperation());
    }

    public Registry getRegistry() {
        return registry;
    }

}
