/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.servidor.backend.basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ljdc.practica3_refactorizacion.general.Reloj;
import ljdc.practica3_refactorizacion.general.modelo.Libro;
import ljdc.practica3_refactorizacion.general.modelo.Pedido;
import ljdc.practica3_refactorizacion.general.modelo.Sesion;
import ljdc.practica3_refactorizacion.general.modelo.Usuario;
import ljdc.practica3_refactorizacion.general.modelo.UsuarioSesion;

/**
 *
 * @author Dave
 */
public class ConexionBD {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://localhost:3306/dsdpractica3";
    
    private Connection obtenerConexion() {
        Connection con = null;
        try {
            // cargamos el jdbc dirver            
            Class.forName(DRIVER);
            // establecemos la conexion             
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        return con;
    }
    
    public Libro obtenerLibroAleatorio(){
        Libro libroAleatorio = null;
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM libro ORDER BY RAND() LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                libroAleatorio = new Libro(rs.getString("isbn"), rs.getString("nombre"), rs.getString("autor"), rs.getString("editorial"), rs.getFloat("precio"), rs.getString("portada_ruta"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return libroAleatorio;
    }
    
    public Libro obtenerLibroAleatorioRestante(){
        Libro libroAleatorio = null;
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM libro WHERE isbn NOT IN " +
                    "(SELECT isbn FROM sesion WHERE idSesion = (SELECT sesion FROM estadoactual)) ORDER BY RAND() LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                libroAleatorio = new Libro(rs.getString("isbn"), rs.getString("nombre"), rs.getString("autor"), rs.getString("editorial"), rs.getFloat("precio"), rs.getString("portada_ruta"));
            } 
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return libroAleatorio;
    }
    
    public List<Libro> obtenerLibrosRestantes(){
        List<Libro> libros = new ArrayList<>();
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM libro WHERE isbn NOT IN (SELECT isbn FROM sesion WHERE idSesion = (SELECT sesion FROM estadoactual))");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                libros.add( new Libro(rs.getString("isbn"), rs.getString("nombre"), rs.getString("autor"), rs.getString("editorial"), rs.getFloat("precio"), rs.getString("portada_ruta")) );
            } 
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return libros;
    }
    
    public int obtenerNumeroSesion(){
        int sesion = -1;
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT sesion FROM estadoactual");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                sesion = rs.getInt("sesion");
            } 
            rs.close();
            ps.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return sesion;
    }
    
    public List<Usuario> obtenerDireccionesDeUsuarios(){
        List<Usuario> lista = null;
        Connection conn = null;
        try {
            conn = obtenerConexion();
            lista = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("SELECT ip, puerto FROM usuario");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                lista.add(new Usuario(rs.getString("ip"), rs.getInt("puerto")));
            }
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return lista;
    }
    
    public Usuario obtenerUltimoUsuario(){
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        Usuario ultimo_usuario = null;
        try {
            conn = obtenerConexion();
            ps = conn.prepareStatement("SELECT * FROM usuario ORDER BY idUsuario DESC LIMIT 1");
            rs = ps.executeQuery();
            if(rs.next()){
                ultimo_usuario = new Usuario(rs.getInt("idUsuario"), rs.getString("nombre"), rs.getString("ip"), rs.getInt("puerto"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return ultimo_usuario;
    }
    
    public Pedido obtenerUltimoPedido(){
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        Pedido pedido =null;
        try {
            conn = obtenerConexion();
            ps = conn.prepareStatement("SELECT * FROM pedido ORDER BY idPedido DESC LIMIT 1");
            rs = ps.executeQuery();
            if(rs.next()){
                pedido = new Pedido(rs.getInt("idPedido"), rs.getString("fecha"), rs.getString("hora_inicio"), rs.getString("hora_fin"));
            }
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return pedido;
    }
    
    public Sesion obtenerUltimaSesion(){
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        Sesion sesion = null;
        try {
            conn = obtenerConexion();
            ps = conn.prepareStatement("SELECT * FROM sesion ORDER BY idPedido DESC LIMIT 1");
            rs = ps.executeQuery();
            
            if(rs.next()){
                sesion = new Sesion(rs.getInt("idSesion"), rs.getInt("idPedido"), rs.getString("isbn"));
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return sesion;
    }
    
    public UsuarioSesion obtenerUltimoUsuarioSesion(){
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        UsuarioSesion us = null;
        try {
            conn = obtenerConexion();
            ps = conn.prepareStatement("SELECT * FROM usuariosesion ORDER BY idSU DESC LIMIT 1");
            rs = ps.executeQuery();
            
            if(rs.next()){
                us = new UsuarioSesion(rs.getInt("idSU"), rs.getInt("idUsuario"), rs.getInt("idSesion"),rs.getString("tiempo"));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return us;
    }
    
    public List<Usuario> obtenerTodosLosUsuarios(){
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        List<Usuario> usuarios = new ArrayList<>();
        
        try {
            conn = obtenerConexion();
            ps = conn.prepareStatement("SELECT * FROM usuario");
            rs = ps.executeQuery();
            
            while(rs.next()){
                usuarios.add(new Usuario(rs.getInt("idUsuario"), rs.getString("nombre"), rs.getString("ip"), rs.getInt("puerto")));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return usuarios;
    }
    
    public List<Pedido> obtenerTodosLosPedidos(){
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        List<Pedido> pedidos = new ArrayList<>();
        
        try {
            conn = obtenerConexion();
            ps = conn.prepareStatement("SELECT * FROM pedido");
            rs = ps.executeQuery();
            
            while(rs.next()){
                pedidos.add(new Pedido(rs.getInt("idPedido"), rs.getString("fecha"), rs.getString("hora_inicio"), rs.getString("hora_fin")));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return pedidos;
    }
    
    public List<Sesion> obtenerTodasLasSesiones(){
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        List<Sesion> sesiones = new ArrayList<>();
        
        try {
            conn = obtenerConexion();
            ps = conn.prepareStatement("SELECT * FROM sesion");
            rs = ps.executeQuery();
            
            while(rs.next()){
                sesiones.add(new Sesion(rs.getInt("idSesion"), rs.getInt("idPedido"), rs.getString("isbn")));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return sesiones;
    }
    
    public List<UsuarioSesion> obtenerTodasLasUsuarioSesiones(){
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        List<UsuarioSesion> usuariosesion = new ArrayList<>();
        
        try {
            conn = obtenerConexion();
            ps = conn.prepareStatement("SELECT * FROM usuariosesion");
            rs = ps.executeQuery();
            
            while(rs.next()){
                usuariosesion.add(new UsuarioSesion(rs.getInt("idSU"), rs.getInt("idUsuario"),rs.getInt("idSesion"),  rs.getString("tiempo")));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return usuariosesion;
    }
    
    /**
     * Metodo para cargar un pedido acorde a la sesion existente
     * @param usuario Direccion y puerto del usuario que solicito un libro
     * @param tiempo Tiempo del usuario en que se pidio el libro 
     * @param tiempo_inicio Tiempo inicial del procesamiento de la peticion
     * @param reloj Reloj para saber el cambio de hora final una vez se completa la query
     * @param libro Libro aleatorio que se entrego al usuario
     **/
    public void agregarPedido(Usuario usuario, String tiempo, String tiempo_inicio, Reloj reloj, Libro libro){
        
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps;
            ResultSet rs;
            int id_pedido, id_sesion;
            
            
            ps = conn.prepareStatement(
                    "INSERT INTO pedido (fecha, hora_inicio, hora_fin) values ( (SELECT DATE(NOW())),?,? )");
            ps.setString(1, tiempo_inicio);
            ps.setString(2, reloj.obtenerHora());
            ps.execute();
            ps.close();
            
            ps = conn.prepareStatement("SELECT COALESCE(MAX(idPedido), 1) as idPedido FROM pedido");
            rs = ps.executeQuery();
            if (rs.next()){
                id_pedido = rs.getInt("idPedido");
            }else{
                id_pedido = 1;
            }
            rs.close();
            ps.close();
            
            ps = conn.prepareStatement("SELECT sesion FROM estadoactual");
            rs = ps.executeQuery();
            if (rs.next()){
                id_sesion = rs.getInt("sesion");
            }else{
                id_sesion = 1;
            }
            rs.close();
            ps.close();
            
            ps = conn.prepareStatement("INSERT INTO sesion values (?,?,?)");
            ps.setInt(1, id_sesion);
            ps.setString(2, libro.getIsbn());
            ps.setInt(3, id_pedido);
            ps.execute();
            ps.close();
            
            ps = conn.prepareStatement("INSERT INTO usuariosesion (idSesion, idUsuario, tiempo) VALUES (?,(SELECT idUsuario FROM usuario WHERE ip=? AND puerto=? ORDER BY idUsuario DESC LIMIT 1),?)");
            ps.setInt(1, id_sesion);
            ps.setString(2, usuario.getIp());
            ps.setInt(3, usuario.getPuerto());
            ps.setString(4, tiempo);
            ps.execute();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void agregarUsuario(String ip, String nombre, int puerto){
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps;
            ResultSet rs;
            
            ps = conn.prepareStatement("SELECT * FROM USUARIO WHERE nombre=? AND ip=? AND puerto=?");
            ps.setString(1, nombre);
            ps.setString(2, ip);
            ps.setInt(3, puerto);
            rs = ps.executeQuery();
            
            if(!rs.next()){//No esta registrado el cliente con el puerto como identificador
                ps = conn.prepareStatement("INSERT INTO usuario (ip, nombre, puerto) VALUES (?,?,?)");
                ps.setString(1, ip);
                ps.setString(2, nombre);
                ps.setInt(3, puerto);
                ps.execute();
            }
            //Ya esta registrado el cliente con el puerto como identificador
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void cambiarSesion(){
        int id_sesion = 0;
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps;
            ResultSet rs;
            
            ps = conn.prepareStatement("SELECT sesion FROM estadoactual");
            rs = ps.executeQuery();
            if (rs.next()){
                id_sesion = rs.getInt("sesion");
            }
            rs.close();
            ps.close();
            
            ps = conn.prepareStatement("UPDATE estadoactual SET sesion=? WHERE id=1");
            ps.setInt(1, id_sesion+1);
            ps.execute();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void cambiarSesion(int sesion){
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps;
            
            ps = conn.prepareStatement("UPDATE estadoactual SET sesion=? WHERE id=1");
            ps.setInt(1, sesion);
            ps.execute();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void colocarUsuario(int id_usuario, String ip, String nombre, int puerto){
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps;
            
            ps = conn.prepareStatement("INSERT INTO usuario (idUsuario, ip, nombre, puerto) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE ip=?, puerto=?, nombre=?");
            ps.setInt(1, id_usuario);
            ps.setString(2, ip);
            ps.setString(3, nombre);
            ps.setInt(4, puerto);
            ps.setString(5, ip);
            ps.setInt(6, puerto);
            ps.setString(7, nombre);
            ps.execute();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void colocarPedido(int id_peticion, String fecha, String hora_inicio, String hora_fin) {
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps;
            
            ps = conn.prepareStatement("INSERT INTO pedido VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE fecha=?,hora_inicio=?,hora_fin=?");
            ps.setInt(1, id_peticion);
            ps.setString(2, fecha);
            ps.setString(3, hora_inicio);
            ps.setString(4, hora_fin);
            ps.setString(5, fecha);
            ps.setString(6, hora_inicio);
            ps.setString(7, hora_fin);
            ps.execute();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void colocarSesion(int id_sesion, int id_pedido, String isbn) {
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps;
            
            ps = conn.prepareStatement("INSERT INTO sesion VALUES (?,?,?) ON DUPLICATE KEY UPDATE idPedido=?");
            ps.setInt(1, id_sesion);
            ps.setString(2, isbn);
            ps.setInt(3, id_pedido);
            ps.setInt(4, id_pedido);
            ps.execute();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void colocarUsuarioSesion(int id_SU, int id_usuario, int id_sesion, String tiempo) {
        Connection conn = null;
        try {
            conn = obtenerConexion();
            PreparedStatement ps;
            //System.out.println("ID: "+id_SU + "\tIdUsu: "+id_usuario+"\tIdSes: "+id_sesion+"\tTiempo: "+tiempo);
            ps = conn.prepareStatement("INSERT INTO usuariosesion VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE idSesion=?,idUsuario=?, tiempo=?");
            ps.setInt(1, id_SU);
            ps.setInt(2, id_sesion);
            ps.setInt(3, id_usuario);
            ps.setString(4, tiempo);
            ps.setInt(5, id_sesion);
            ps.setInt(6, id_usuario);
            ps.setString(7, tiempo);
            //System.out.println("Query: "+ps.toString());
            ps.execute();
            ps.close();
            
        } catch (SQLException e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
