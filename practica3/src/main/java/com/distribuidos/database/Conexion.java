package com.distribuidos.database;

import java.sql.*;
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
    public static Connection conn;
        
    public Connection getConexion() {
        return conn;
    }
    
    public void Desconectar() {
        conn = null;
    }
    
    public boolean crearConexion() {
        try {
            // cargamos el jdbc dirver            
            Class.forName(DRIVER);
            // establecemos la conexion             
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
            return false;
        }
        return true;
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