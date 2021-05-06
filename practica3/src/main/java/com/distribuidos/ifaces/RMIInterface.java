/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.ifaces;

import com.distribuidos.cliente.backend.Libro;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author pekochu
 */
public interface RMIInterface extends Remote {
    public String helloTo(String name) throws RemoteException;
    
    public Libro pedirLibro(String tiempo) throws RemoteException;
}
