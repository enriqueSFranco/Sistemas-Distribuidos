package com.distribuidos.database;

import com.distribuidos.cliente.backend.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Enrique
 */
public class Conexion {
  
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://localhost:3306/dsdpractica3";
    
    private Statement stmt;
    private ResultSet rs;
    public Connection conn;
        
    public Connection getConexion() {
        return conn;
    }
    
    public boolean crearConexion() {
        try {
            // cargamos el jdbc dirver            
            Class.forName(DRIVER);
            // establecemos la conexion             
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return false;
        }
        return true;
    }
    
    public void desconectar(){
        try {
            if (conn != null)
                conn.close();
            conn = null;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Libro obtenerLibroAleatorio(){
        Libro libroAleatorio = null;
        try {
            
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM libro ORDER BY RAND() LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                libroAleatorio = new Libro(rs.getString("isbn"), rs.getString("nombre"), rs.getString("autor"), rs.getString("editorial"), rs.getFloat("precio"), rs.getString("portada_ruta"));
            } 
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            return libroAleatorio;
        }
    }
    
    public Libro obtenerLibroAleatorioRestante(){
        Libro libroAleatorio = null;
        try {
            
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM libro WHERE isbn NOT IN " +
                    "(SELECT isbn FROM sesion WHERE idSesion = (SELECT MAX(idSesion) FROM sesion)) ORDER BY RAND() LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                libroAleatorio = new Libro(rs.getString("isbn"), rs.getString("nombre"), rs.getString("autor"), rs.getString("editorial"), rs.getFloat("precio"), rs.getString("portada_ruta"));
            } 
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            return libroAleatorio;
        }
    }
    
    public List<Libro> obtenerLibrosRestantes(int sesion){
        List<Libro> libros = new ArrayList<>();
        try {
            
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM libro WHERE isbn NOT IN (SELECT isbn FROM sesion WHERE idSesion = ?)");
            ps.setInt(1, sesion);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                libros.add( new Libro(rs.getString("isbn"), rs.getString("nombre"), rs.getString("autor"), rs.getString("editorial"), rs.getFloat("precio"), rs.getString("portada_ruta")) );
            } 
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return libros;
    }
    
    public int obtenerSesion(){
        int sesion = -1;
        try {
            
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COALESCE(MAX(idSesion),0) as id_sesion FROM sesion");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                sesion = rs.getInt("id_sesion");
            } 
            rs.close();
            ps.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sesion;
    }
    
    public List<String> obtenerClientes(){
        List<String> lista = null;
        try {
            lista = new ArrayList<String>();
            PreparedStatement ps = conn.prepareStatement("SELECT ip FROM usuario GROUP BY ip");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                lista.add(rs.getString("ip"));
            }
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }
    
    /**
     * Metodo para cargar un pedido acorde a la sesion existente
     * @param ip Direccion del usuario que solicito un libro
     * @param tiempo Tiempo del usuario en que se pidio el libro 
     * @param libro Libro aleatorio que se entrego al usuario
     * @param crearSesion Opcion para crear una nueva sesion, dejando atrás las anteriores sesiones sin eliminarlas de la base
     **/
    public void agregarPedido(String ip, String tiempo, Libro libro, boolean crearSesion){
        try {
            PreparedStatement ps;
            ResultSet rs;
            int id_pedido, id_sesion;
            
            
            ps = conn.prepareStatement(
                    "INSERT INTO pedido (fecha, hora_inicio, hora_fin) values ( (SELECT DATE(NOW())),?,(SELECT TIME(NOW())) )");
            ps.setString(1, tiempo);
            ps.execute();
            ps.close();
            
            ps = conn.prepareStatement("SELECT COALESCE(MAX(idPedido), 0) as idPedido FROM pedido");
            rs = ps.executeQuery();
            if (rs.next()){
                id_pedido = rs.getInt("idPedido");
            }else{
                id_pedido = 0;
            }
            rs.close();
            ps.close();
            
            ps = conn.prepareStatement("SELECT COALESCE(MAX(idSesion),0) as id_sesion FROM sesion");
            rs = ps.executeQuery();
            if (rs.next()){
                id_sesion = rs.getInt("id_sesion");
            }else{
                id_sesion = 0;
            }
            rs.close();
            ps.close();
            
            ps = conn.prepareStatement("INSERT INTO sesion values (?,?,?)");
            if(crearSesion){
                ps.setInt(1, id_sesion+1);
            }else{
                ps.setInt(1, id_sesion);
            }
            ps.setString(2, libro.getIsbn());
            ps.setInt(3, id_pedido);
            ps.execute();
            ps.close();
            
            ps = conn.prepareStatement("INSERT INTO usuariosesion (idSesion, idUsuario, tiempo) VALUES (?,(SELECT idUsuario FROM usuario WHERE ip=? ORDER BY idUsuario DESC LIMIT 1),?)");
            ps.setInt(1, id_sesion);
            ps.setString(2, ip);
            ps.setString(3, tiempo);
            ps.execute();
            ps.close();
            
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void agregarUsuario(String ip, String nombre){
        try {
            PreparedStatement ps;
            
            ps = conn.prepareStatement("INSERT INTO usuario (ip, nombre) VALUES (?,?)");
            ps.setString(1, ip);
            ps.setString(2, nombre);
            ps.execute();
            ps.close();
            
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void colocarUsuario(int id_usuario, String ip, String nombre){
        try {
            PreparedStatement ps;
            
            ps = conn.prepareStatement("INSERT INTO usuario VALUES (?,?,?)");
            ps.setInt(1, id_usuario);
            ps.setString(2, ip);
            ps.setString(3, nombre);
            ps.execute();
            ps.close();
            
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void colocarPedido(int id_peticion, String fecha, String hora_inicio, String hora_fin) {
        try {
            PreparedStatement ps;
            
            ps = conn.prepareStatement("INSERT INTO pedido VALUES (?,?,?,?)");
            ps.setInt(1, id_peticion);
            ps.setString(2, fecha);
            ps.setString(3, hora_inicio);
            ps.setString(4, hora_fin);
            ps.execute();
            ps.close();
            
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void colocarSesion(int id_sesion, int id_pedido, String isbn) {
        try {
            PreparedStatement ps;
            
            ps = conn.prepareStatement("INSERT INTO sesion VALUES (?,?,?)");
            ps.setInt(1, id_sesion);
            ps.setString(2, isbn);
            ps.setInt(3, id_pedido);
            ps.execute();
            ps.close();
            
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void colocarUsuarioSesion(int id_SU, int id_usuario, int id_sesion, String tiempo) {
        try {
            PreparedStatement ps;
            
            ps = conn.prepareStatement("INSERT INTO usuariosesion VALUES (?,?,?,?)");
            ps.setInt(1, id_SU);
            ps.setInt(2, id_sesion);
            ps.setInt(3, id_usuario);
            ps.setString(4, tiempo);
            ps.execute();
            ps.close();
            
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public ResultSet obtenerUltimoUsuario(){
        PreparedStatement ps;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM usuario ORDER BY idUsuario DESC LIMIT 1");
            rs = ps.executeQuery();
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
        return rs;
    }
    
    public ResultSet obtenerUltimoPedido(){
        PreparedStatement ps;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM pedido ORDER BY idPedido DESC LIMIT 1");
            rs = ps.executeQuery();
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
        return rs;
    }
    
    public ResultSet obtenerUltimaSesion(){
        PreparedStatement ps;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM sesion ORDER BY idPedido DESC LIMIT 1");
            rs = ps.executeQuery();
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
        return rs;
    }
    
    public ResultSet obtenerUltimoUsuarioSesion(){
        PreparedStatement ps;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM usuariosesion ORDER BY idSU DESC LIMIT 1");
            rs = ps.executeQuery();
        } catch (Exception e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
        return rs;
    }
    
     /**
     * Metodo para crear un nuevo usuario
     * @param id 
     * @param ip 
     * @param nombre
     **/
    public void crearUsuario(int id, String ip, String nombre) {
        try {
            this.stmt = this.conn.createStatement(); // creamos la sentencia sql
            String insert = "INSERT INTO usuario(idUsuario, ip, nombre) SELECT ";
            insert += id + ",INET6_ATON('";
            insert += ip + "'),'";
            insert += nombre + "' WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE idUsuario = '";
            insert += id + "' OR IP = INET6_ATON('";
            insert += ip + "'))";
            this.stmt.executeUpdate(insert);
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    /**
     * Metodo para cargar un pedido
     * @param id
     * @param fecha
     * @param hora_inicio
     * @param hora_fin
     **/
    public void cargarPedido(int id, String fecha, String hora_inicio, String hora_fin, int libro, int usuario) {
        try {
            this.stmt = this.conn.createStatement();
            String insert = "INSERT INTO pedido(id, fecha, hora_inicio, hora_fin)";
            insert += id + ",";
            insert += fecha + ",";
            insert += hora_inicio + ",";
            insert += hora_fin + ",";
            insert += libro + ",";
            insert += usuario + " WHERE NOT EXIST (SELECT 1 FROM pedido WHERE ID = '";
            insert += id + "')";
            this.stmt.executeUpdate(insert);
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "error al cargar pedido: " + ex);
        }
    }
}