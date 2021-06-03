/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dave
 */
public class ManejadorDeConfiguracion implements Serializable {
    
    private String ruta_archivo = "config";
    
    private final String direccion_por_defecto = "127.0.0.1";
    private final int puerto_por_defecto = 6000;
    
    private Configuracion configuracion;
    
    public ManejadorDeConfiguracion(String ruta_archivo){
        this.ruta_archivo = ruta_archivo;
        obtenerConfiguracion();
    }
    
    private void obtenerConfiguracion(){
        if(!cargarConfiguracion()){
            generarArchivoDeConfiguracion();
        }
    }

    private void generarArchivoDeConfiguracion() {
        try {
            File archivo = Paths.get("./" + ruta_archivo + ".yaml").toFile();
            System.out.println(archivo);
            ObjectMapper transformadorDeObjeto = new ObjectMapper(new YAMLFactory());
            configuracion = new Configuracion();
            configuracion.setPuerto(puerto_por_defecto);
            configuracion.setServidor_principal(InetAddress.getByName(direccion_por_defecto));
            configuracion.setServidor_sincronizacion(InetAddress.getByName(direccion_por_defecto));
            configuracion.setServidores(new ArrayList<>());
            configuracion.getServidores().add(new InfoServidor(InetAddress.getByName(direccion_por_defecto), puerto_por_defecto));
            
            archivo.createNewFile();
            transformadorDeObjeto.writeValue(archivo, configuracion);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ManejadorDeConfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejadorDeConfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean cargarConfiguracion() {
        try {
            File archivo = Paths.get("./" + ruta_archivo + ".yaml").toFile();
            ObjectMapper transformadorDeObjeto = new ObjectMapper(new YAMLFactory());
            if (!archivo.exists()){
                return false;
            }
            this.configuracion = transformadorDeObjeto.readValue(archivo, Configuracion.class);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ManejadorDeConfiguracion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void guardarConfiguracion() {
        try {
            File archivo = Paths.get("./" + ruta_archivo + ".yaml").toFile();
            ObjectMapper transformadorDeObjeto = new ObjectMapper(new YAMLFactory());
            if (!archivo.exists()){
                archivo.createNewFile();
            }
            transformadorDeObjeto.writeValue(archivo, configuracion);
        } catch (IOException ex) {
            Logger.getLogger(ManejadorDeConfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean esPrincipal(){
        return this.configuracion.servidores.stream().filter(
                servidor->servidor.getDireccion().equals(this.configuracion.servidor_principal)
        ).findFirst().isPresent();
    }
    
    public boolean esSincronizador(){
        return this.configuracion.servidores.stream().filter(
                servidor->servidor.getDireccion().equals(this.configuracion.servidor_sincronizacion)
        ).findFirst().isPresent();
    }
    
    public boolean esPrincipal(InetAddress serv){
        return this.configuracion.servidores.stream().filter(
                servidor->servidor.getDireccion().equals(serv)
        ).findFirst().isPresent();
    }
    
    public boolean esSincronizador(InetAddress serv){
        return this.configuracion.servidores.stream().filter(
                servidor->servidor.getDireccion().equals(serv)
        ).findFirst().isPresent();
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }
    
}
