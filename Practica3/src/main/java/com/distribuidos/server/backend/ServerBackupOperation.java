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
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Dave
 */
public class ServerBackupOperation extends UnicastRemoteObject implements RMIBackupInterface {
    
    private BooksView vista;
    
    public ServerBackupOperation(BooksView vista) throws RemoteException{
        this.vista = vista;
    }
    
    @Override
    public void colocarUsuario(int id_usuario, String ip, String nombre) throws RemoteException {
        Conexion con = new Conexion();
        con.crearConexion();
        
        con.colocarUsuario(id_usuario, ip, nombre);
        
        con.desconectar();
    }

    @Override
    public void colocarPedido(int id_peticion, String fecha, String hora_inicio, String hora_fin) throws RemoteException {
        Conexion con = new Conexion();
        con.crearConexion();
        
        con.colocarPedido(id_peticion, fecha, hora_inicio, hora_fin);
        
        con.desconectar();
        vista.getServer().actualizarSesion(false);
        vista.actualizarSesion();
        vista.colocarListaDeLibros();
    }

    @Override
    public void colocarSesion(int id_sesion, int id_pedido, String isbn) throws RemoteException {
        Conexion con = new Conexion();
        con.crearConexion();
        
        con.colocarSesion(id_sesion, id_pedido, isbn);
        
        con.desconectar();
        vista.getServer().actualizarSesion(false);
        vista.actualizarSesion();
        vista.colocarListaDeLibros();
    }

    @Override
    public void colocarUsuarioSesion(int id_SU, int id_usuario, int id_sesion, String tiempo) throws RemoteException {
        Conexion con = new Conexion();
        con.crearConexion();
        
        con.colocarUsuarioSesion(id_SU, id_usuario, id_sesion, tiempo);
        
        con.desconectar();
        vista.getServer().actualizarSesion(false);
        vista.actualizarSesion();
        vista.colocarListaDeLibros();
    }

    @Override
    public void reiniciarSesion(int sesion) throws RemoteException {
        vista.getServer().actualizarSesion(true);
        vista.actualizarSesion();
        vista.colocarListaDeLibros();
    }
    
}
