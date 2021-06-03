/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.servidor.backend;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import ljdc.practica3_refactorizacion.general.ManejadorDeConfiguracion;
import ljdc.practica3_refactorizacion.general.RMISincronizacionImpl;
import ljdc.practica3_refactorizacion.general.Reloj;
import ljdc.practica3_refactorizacion.general.gui.GUIModificacion;
import ljdc.practica3_refactorizacion.servidor.gui.GUIServidor;
import ljdc.practica3_refactorizacion.servidor.interfaces.GUIServidorInterfaz;
import ljdc.practica3_refactorizacion.servidor.interfaces.ServidorInterfaz;

/**
 *
 * @author Dave
 */
public class Servidor implements ServidorInterfaz {
    
    private ManejadorDeConfiguracion manejadorConfiguracion;
    
    private RMIServidorImpl modulo_servidor;
    private RMIRespaldoImpl modulo_respaldo;
    private RMISincronizacionImpl modulo_sincronizacion;
    
    private GUIServidorInterfaz gui;
    
    private Thread hiloReloj;
    private Reloj reloj;

    public Servidor() {
        try {
            // Cargamos configuracion
            this.manejadorConfiguracion = new ManejadorDeConfiguracion("servidor");
            
            // Creamos la interfaz grafica
            this.gui = new GUIServidor(this);
            
            // Creamos el reloj
            Random r = new Random();
            this.reloj = new Reloj(this.gui.obtenerEtiquetaDeReloj(), r.nextInt(24), r.nextInt(60), r.nextInt(60));
            
            Registry  registro = LocateRegistry.createRegistry(this.manejadorConfiguracion.getConfiguracion().getPuerto());
            // - Instanciamos implementaciones RMI
            // - Servidor general
            this.modulo_servidor = new RMIServidorImpl(manejadorConfiguracion, gui, reloj);
            // - Servidor respaldo
            this.modulo_respaldo = new RMIRespaldoImpl(gui);
            // - Servidor sincronizacion
            //this.modulo_sincronizacion = new RMIServidorImpl(gui);
            
            // - Enlazamos interfaces RMI
            // Servidor general
            registro.rebind("servidor", modulo_servidor);
            // - Servidor respaldo
            registro.rebind("respaldo", modulo_respaldo);
            // - Servidor sincronizacion
            //registro.rebind("sincronizacion", modulo_sincronizacion);
            
            // - Solicitamos actualizacion de base de datos a servidores activos
            this.modulo_servidor.sincronizarBaseDeDatos();
            
            this.modulo_servidor.mostrarInformacionDeLibros();
            
            // Mostramos la intergaz grafica
            this.gui.mostrar();
            
            //Cargar el reloj
            this.hiloReloj = new Thread(reloj);
            this.hiloReloj.start();
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void modificarReloj() {
        // Detener el reloj
        this.reloj.detenerReloj();
        // - Mostrar gui de modificacion de reloj
        GUIModificacion gui_mod = new GUIModificacion(this.reloj);
        gui_mod.setVisible(true);
    }

    /*
    Usado por la GUI para reiniciar la sesion en caso de presionar el boton reiniciar
    */
    @Override
    public void reiniciarSesion() {
        this.modulo_servidor.reiniciarSesion();
        this.modulo_servidor.reiniciarClientes();
    }
    
    
}
