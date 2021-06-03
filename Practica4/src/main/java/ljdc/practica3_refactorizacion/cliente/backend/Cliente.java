/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.cliente.backend;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import ljdc.practica3_refactorizacion.cliente.gui.GUICliente;
import ljdc.practica3_refactorizacion.cliente.interfaces.ClienteInterfaz;
import ljdc.practica3_refactorizacion.cliente.interfaces.GUIClienteInterfaz;
import ljdc.practica3_refactorizacion.general.ManejadorDeConfiguracion;
import ljdc.practica3_refactorizacion.general.RMISincronizacionImpl;
import ljdc.practica3_refactorizacion.general.Reloj;
import ljdc.practica3_refactorizacion.general.gui.GUIModificacion;

/**
 *
 * @author Dave
 */
public class Cliente implements ClienteInterfaz{
    
    private ManejadorDeConfiguracion manejadorConfiguracion;
    
    private RMIClienteImpl modulo_cliente;
    private RMISincronizacionImpl modulo_sincronizacion;
    
    private GUIClienteInterfaz gui;
    
    private Thread hiloReloj;
    private Reloj reloj;
    
    public Cliente (){
        try {
            this.manejadorConfiguracion = new ManejadorDeConfiguracion("cliente");
            
            // Creamos la interfaz grafica
            this.gui = new GUICliente(this);
            
            // Crear el reloj
            Random r = new Random();
            this.reloj = new Reloj(this.gui.obtenerEtiquetaDeReloj(), r.nextInt(24), r.nextInt(60), r.nextInt(60));
            
            Registry  registro = LocateRegistry.createRegistry(this.manejadorConfiguracion.getConfiguracion().getPuerto());
            // - Instanciamos implementaciones RMI
            // - Servidor general
            this.modulo_cliente = new RMIClienteImpl(manejadorConfiguracion, gui, reloj);
            // - Servidor sincronizacion
            //this.modulo_sincronizacion = new RMIServidorImpl(gui);
            
            // - Enlazamos interfaces RMI
            // - Servidor general
            registro.rebind("cliente", modulo_cliente);
            // - Cliente sincronizacion
            //registro.rebind("sincronizacion", modulo_sincronizacion);
            
            //Registrarse en servidor
            this.modulo_cliente.registrarse();
            
            // Mostramos la intergaz grafica
            this.gui.mostrar();
            
            //Cargar el reloj
            this.hiloReloj = new Thread(reloj);
            this.hiloReloj.start();
            
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String pedirLibro() {
        return this.modulo_cliente.pedirLibro();
    }

    @Override
    public void modificarReloj() {
        // Detener el reloj
        this.reloj.detenerReloj();
        // - Mostrar gui de modificacion de reloj
        GUIModificacion gui_mod = new GUIModificacion(this.reloj);
        gui_mod.setVisible(true);
    }
    
}
