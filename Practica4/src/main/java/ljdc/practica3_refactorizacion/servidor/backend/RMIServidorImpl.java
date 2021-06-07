/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.servidor.backend;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ljdc.practica3_refactorizacion.general.ManejadorDeConfiguracion;
import ljdc.practica3_refactorizacion.general.InfoServidor;
import ljdc.practica3_refactorizacion.general.Reloj;
import ljdc.practica3_refactorizacion.general.modelo.Libro;
import ljdc.practica3_refactorizacion.general.modelo.Pedido;
import ljdc.practica3_refactorizacion.general.modelo.Sesion;
import ljdc.practica3_refactorizacion.general.modelo.Usuario;
import ljdc.practica3_refactorizacion.general.modelo.UsuarioSesion;
import ljdc.practica3_refactorizacion.rmi.RMIClienteInterfaz;
import ljdc.practica3_refactorizacion.rmi.RMIRespaldoInterfaz;
import ljdc.practica3_refactorizacion.servidor.backend.basedatos.ConexionBD;
import ljdc.practica3_refactorizacion.servidor.interfaces.GUIServidorInterfaz;
import ljdc.practica3_refactorizacion.rmi.RMIServidorInterfaz;

/**
 *
 * @author Dave
 */
public class RMIServidorImpl extends UnicastRemoteObject implements RMIServidorInterfaz {
    
    private ManejadorDeConfiguracion manejador_config;
    private GUIServidorInterfaz gui;
    private Reloj reloj;

    public RMIServidorImpl(ManejadorDeConfiguracion manejador_config, GUIServidorInterfaz gui, Reloj reloj) throws RemoteException{
        this.manejador_config = manejador_config;
        this.gui = gui;
        this.reloj = reloj;
    }

    @Override
    public void ping() throws RemoteException {
        try {
            System.out.println("Ping desde " + RemoteServer.getClientHost());
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void regitrarse(int puerto, String nombre) throws RemoteException {
        ConexionBD con = new ConexionBD();
        try {
            String ip = RemoteServer.getClientHost();
            boolean existeEnLaBaseDeDatos = con.obtenerDireccionesDeUsuarios().stream().anyMatch(usuario -> usuario.equals(new Usuario(ip, puerto)));
            if (!existeEnLaBaseDeDatos){
                con.agregarUsuario(RemoteServer.getClientHost(), nombre, puerto);
                respaldarUltimoUsuario();
            }
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Libro pedirLibro(int puerto, String tiempo) throws RemoteException {
        Libro libroAleatorio;
        ConexionBD con = new ConexionBD();
        String tiempo_inicio = this.reloj.obtenerHora();
        
        libroAleatorio = con.obtenerLibroAleatorioRestante();
        
        try {
            if(libroAleatorio == null){
                // Si ya no hay libros
                //libroAleatorio = con.obtenerLibroAleatorio();
                reiniciarClientes();
                reiniciarSesion();
                this.gui.limpiarPortada();
                mostrarInformacionDeLibros();
                return libroAleatorio;
            }
            //Si siguen existiendo libros en la sesion
            con.agregarPedido(new Usuario(RemoteServer.getClientHost(), puerto), tiempo, tiempo_inicio, this.reloj, libroAleatorio);
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldarUltimasModificaciones();
        // GUI Cambiar Imagen
        this.gui.actualizarImagen(libroAleatorio.getPortada());
        // GUI Actualizar sesion
        this.gui.actualizarSesion(con.obtenerNumeroSesion());
        // GUI Actualizar lista de libros
        this.gui.actualizarLista(con.obtenerLibrosRestantes());
        return libroAleatorio;
    }
    
    public void mostrarInformacionDeLibros(){
        ConexionBD con = new ConexionBD();
        // GUI Actualizar sesion
        this.gui.actualizarSesion(con.obtenerNumeroSesion());
        // GUI Actualizar lista de libros
        this.gui.actualizarLista(con.obtenerLibrosRestantes());
    }
    
    public void sincronizarBaseDeDatos(){
        ConexionBD con = new ConexionBD();
        Registry registro;
        int sesion = 1;
        List<Usuario> usuarios = null;
        List<Pedido> pedidos = null;
        List<Sesion> sesiones = null;
        List<UsuarioSesion> usuariosesiones = null;
        
        for(InfoServidor servidor :this.manejador_config.getConfiguracion().getServidores()){
            try {
                //System.out.println("Servidor: " + servidor.getDireccion().getHostAddress() + ":" + servidor.getPuerto());
                //System.out.println("Self: " + InetAddress.getByName("localhost").getHostAddress() + ":"+this.manejador_config.getConfiguracion().getPuerto());
                //System.out.println("IP igual: " + (!servidor.getDireccion().getHostAddress().equals(InetAddress.getByName("localhost").getHostAddress())));
                //System.out.println("Puerto igual: "+ (servidor.getPuerto() != this.manejador_config.getConfiguracion().getPuerto()));
                //Busca en la lista de servidores que no sean él mismo
                if(!servidor.getDireccion().getHostAddress().equals(InetAddress.getByName("localhost").getHostAddress())
                        || servidor.getPuerto() != this.manejador_config.getConfiguracion().getPuerto()){
                    //System.out.println("Servidor diferente!");
                    registro = LocateRegistry.getRegistry(servidor.getDireccion().getHostAddress(), servidor.getPuerto());
                    RMIRespaldoInterfaz serv = (RMIRespaldoInterfaz) registro.lookup("respaldo");
                    sesion = serv.obtenerSesion();
                    usuarios = serv.obtenerUsuarios();
                    pedidos = serv.obtenerPedidos();
                    sesiones = serv.obtenerSesiones();
                    usuariosesiones = serv.obtenerUsuarioSesiones();
                    
                    break;
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "Servidor desconocido con datos {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (RemoteException ex) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No se pudo actualizar la sesion en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No hay modulo de respaldo registrado en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            }
        }
        if (usuarios == null || pedidos == null || sesiones == null || usuariosesiones == null){
            Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.INFO, "No se encontraron servidores de respaldo activos. Se asume una base sincronizada");
        }else{
            
            // Recorrer los datos que se tienen y compararlos con los del servidor de respaldo
            con.cambiarSesion(sesion);
            
            for(Usuario u: usuarios){
                con.colocarUsuario(u.getId(), u.getIp(), u.getNombre(), u.getPuerto());
            }
            for(Pedido p: pedidos){
                con.colocarPedido(p.getId(), p.getFecha(), p.getHora_inicio(), p.getHora_fin());
            }
            for(Sesion s: sesiones){
                con.colocarSesion(s.getId_sesion(), s.getId_pedido(), s.getIsbn());
            }
            for(UsuarioSesion us: usuariosesiones){
                con.colocarUsuarioSesion(us.getId(), us.getId_usuario(), us.getId_sesion(), us.getTiempo());
            }
            
            Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.INFO, "Base sincronizada");
        }
        
    }
    
    public void reiniciarSesion(){
        ConexionBD con = new ConexionBD();
        con.cambiarSesion();
        Registry registro;
        for(InfoServidor servidor :this.manejador_config.getConfiguracion().getServidores()){
            try {
                if(!servidor.getDireccion().getHostAddress().equals(InetAddress.getByName("localhost").getHostAddress())
                        || servidor.getPuerto() != this.manejador_config.getConfiguracion().getPuerto()){
                    registro = LocateRegistry.getRegistry(servidor.getDireccion().getHostAddress(), servidor.getPuerto());
                    RMIRespaldoInterfaz serv = (RMIRespaldoInterfaz) registro.lookup("respaldo");
                    serv.reiniciarSesion(con.obtenerNumeroSesion());
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "Servidor desconocido con datos {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (RemoteException ex) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No se pudo actualizar la sesion en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No hay modulo de respaldo registrado en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            }
        }
    }
    
    public void reiniciarClientes(){
        new Thread(() -> {
            ConexionBD con = new ConexionBD();
            Registry registro;
            for (Usuario usu: con.obtenerDireccionesDeUsuarios()){
                try {
                    registro = LocateRegistry.getRegistry(usu.getIp(), usu.getPuerto());
                    RMIClienteInterfaz cliente = (RMIClienteInterfaz) registro.lookup("cliente");
                    cliente.reiniciarSesion();
                } catch (RemoteException ex) {
                    Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No se pudo reiniciar al cliente en {0}:{1}", new Object[]{usu.getIp(), usu.getPuerto()});
                } catch (NotBoundException ex) {
                    Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No hay modulo de cliente en {0}:{1}", new Object[]{usu.getIp(), usu.getPuerto()});
                }
            }
        }).start();
    }
    
    private void respaldarUltimoUsuario(){
        ConexionBD con = new ConexionBD();
        Usuario usu = con.obtenerUltimoUsuario();
        Registry registro;
        for(InfoServidor servidor :this.manejador_config.getConfiguracion().getServidores()){
            try {
                //Si no es el mismo servidor
                if(!servidor.getDireccion().getHostAddress().equals(InetAddress.getByName("localhost").getHostAddress())
                        && servidor.getPuerto() != this.manejador_config.getConfiguracion().getPuerto()){
                    
                    registro = LocateRegistry.getRegistry(servidor.getDireccion().getHostAddress(), servidor.getPuerto());
                    RMIRespaldoInterfaz respaldo = (RMIRespaldoInterfaz) registro.lookup("respaldo");
                    if (usu != null){
                        respaldo.colocarUsuario(usu.getId(), usu.getIp(), usu.getNombre(), usu.getPuerto());
                    }
                    
                }
            } catch (UnknownHostException e) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "Servidor desconocido con datos {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (RemoteException e) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No se pudo respaldar en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No hay modulo de respaldo registrado en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            }
        }
    }
    
    private void respaldarUltimasModificaciones(){
        ConexionBD con = new ConexionBD();
        Pedido pedido = con.obtenerUltimoPedido();
        Sesion sesion = con.obtenerUltimaSesion();
        UsuarioSesion us = con.obtenerUltimoUsuarioSesion();
        if(pedido != null && sesion != null && us != null){
            Registry registro;
            for(InfoServidor servidor :this.manejador_config.getConfiguracion().getServidores()){
                try {
                    if(!servidor.getDireccion().getHostAddress().equals(InetAddress.getByName("localhost").getHostAddress())
                            || servidor.getPuerto() != this.manejador_config.getConfiguracion().getPuerto()){
                        registro = LocateRegistry.getRegistry(servidor.getDireccion().getHostAddress(), servidor.getPuerto());
                        RMIRespaldoInterfaz respaldo = (RMIRespaldoInterfaz) registro.lookup("respaldo");
                        
                        respaldo.colocarPedido(pedido.getId(), pedido.getFecha(), pedido.getHora_inicio(), pedido.getHora_fin());
                        respaldo.colocarSesion(sesion.getId_sesion(), sesion.getId_pedido(), sesion.getIsbn());
                        respaldo.colocarUsuarioSesion(us.getId(), us.getId_usuario(), us.getId_sesion(), us.getTiempo());
                    }
                } catch (UnknownHostException e) {
                Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "Servidor desconocido con datos {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
                } catch (RemoteException e) {
                    Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No se pudo respaldar en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
                } catch (NotBoundException ex) {
                    Logger.getLogger(RMIServidorImpl.class.getName()).log(Level.WARNING, "No hay modulo de respaldo registrado en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
                }
            }
        }
        
    }
    
}
