/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.server.backend;

import com.distribuidos.cliente.backend.Libro;
import com.distribuidos.database.Conexion;
import com.distribuidos.ifaces.RMIBackupInterface;
import com.distribuidos.ifaces.RMIInterface;
import com.distribuidos.server.gui.BooksView;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pekochu
 */
public class ServerOperation extends UnicastRemoteObject implements RMIInterface{

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerOperation.class);

    private BooksView vista;
    
    private boolean isBackup = false;
    private boolean reiniciarSesion = false;
    
    protected ServerOperation(BooksView vista, boolean isBackup) throws RemoteException {
        super();
        this.vista = vista;
        this.isBackup = isBackup;
    }

    @Override
    public String helloTo(String name) throws RemoteException{
        LOGGER.info(name + " se conecto. Enviando ACK.");
        Conexion con = new Conexion();
        if (con.crearConexion()){
            try {
                con.agregarUsuario(getClientHost(), name);
            } catch (ServerNotActiveException ex) {
                java.util.logging.Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
            con.desconectar();
            respaldarUltimoUsuario();
        }
        return "Hola, " + name + ". ACK.";
    }

    @Override
    public Libro pedirLibro(String tiempo) throws RemoteException {
        Libro libroAleatorio = null;
        try {
            Conexion con = new Conexion();
            if (con.crearConexion()){
                libroAleatorio = con.obtenerLibroAleatorioRestante();

                if (libroAleatorio != null){ 
                        // Si siguen existiendo libros en la sesion, se compara si se reinicio la sesion desde el servidor
                        
                        if (reiniciarSesion){
                            con.agregarPedido(getClientHost(), tiempo, libroAleatorio, reiniciarSesion);
                            reiniciarSesion = false;
                            //Reiniciar datos de los clientes
                            this.vista.getServer().reiniciarClientes();
                        }else{
                            con.agregarPedido(getClientHost(), tiempo, libroAleatorio, false);
                        }
                        
                        
                }else{ // Ya se acabaron, reiniciar la sesión
                    libroAleatorio = con.obtenerLibroAleatorio();
                    con.agregarPedido(getClientHost(), tiempo, libroAleatorio, true);
                    
                    //Reiniciar datos de los clientes
                    this.vista.getServer().reiniciarClientes();
                }

                this.vista.cambiarImagen(libroAleatorio.getPortada());
                //System.out.println("Tiempo: " + tiempo);
                con.desconectar();
            }
            if (!this.isBackup)
                respaldarUltimasModificaciones();
            vista.getServer().actualizarSesion(reiniciarSesion);
            vista.actualizarSesion();
            vista.colocarListaDeLibros();
        } catch (ServerNotActiveException ex) {
            java.util.logging.Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return libroAleatorio;
    }
    
    public void respaldarUltimoUsuario(){
        try {
            Conexion con  = new Conexion();
            con.crearConexion();
            
            ResultSet rs = con.obtenerUltimoUsuario();
            
            Registry registry = LocateRegistry.getRegistry(this.vista.getServer().getDireccionRespaldo(), this.vista.getServer().getPuertoRespaldo());
            RMIBackupInterface respaldoOP = (RMIBackupInterface) registry.lookup("backup_server");
            
            if(rs.next()){
                respaldoOP.colocarUsuario(rs.getInt("idUsuario"), rs.getString("ip"), rs.getString("nombre"));
            }
            con.desconectar();
            System.out.println("Respaldado");
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            java.util.logging.Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void respaldarUltimasModificaciones(){
        try {
            Conexion con  = new Conexion();
            con.crearConexion();
            
            ResultSet rsPedido = con.obtenerUltimoPedido();
            ResultSet rsSesion = con.obtenerUltimaSesion();
            ResultSet rsUsuarioSesion = con.obtenerUltimoUsuarioSesion();
            
            Registry registry = LocateRegistry.getRegistry(this.vista.getServer().getDireccionRespaldo(), this.vista.getServer().getPuertoRespaldo());
            RMIBackupInterface respaldoOP = (RMIBackupInterface) registry.lookup("backup_server");
            
            if(rsPedido.next()){
                respaldoOP.colocarPedido(rsPedido.getInt("idPedido"), rsPedido.getString("fecha"), rsPedido.getString("hora_inicio"), rsPedido.getString("hora_fin"));
            }
            if(rsSesion.next()){
                respaldoOP.colocarSesion(rsSesion.getInt("idSesion"), rsSesion.getInt("idPedido"), rsSesion.getString("isbn"));
            }
            if(rsUsuarioSesion.next()){
                respaldoOP.colocarUsuarioSesion(rsUsuarioSesion.getInt("idSU"), rsUsuarioSesion.getInt("idUsuario"), rsUsuarioSesion.getInt("idSesion"),rsUsuarioSesion.getString("tiempo"));
            }
            con.desconectar();
            System.out.println("Respaldado");
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            java.util.logging.Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setReiniciarSesion(boolean reiniciarSesion) {
        this.reiniciarSesion = reiniciarSesion;
    }

}
