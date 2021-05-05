/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.server.backend;

import com.distribuidos.ifaces.RMIInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pekochu
 */
public class ServerOperation extends UnicastRemoteObject implements RMIInterface{

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerOperation.class);

    protected ServerOperation() throws RemoteException {
        super();
    }

    @Override
    public String helloTo(String name) throws RemoteException{
        LOGGER.info(name + " se conecto. Enviando ACK.");
        return "Hola, " + name + ". ACK.";

    }

}
