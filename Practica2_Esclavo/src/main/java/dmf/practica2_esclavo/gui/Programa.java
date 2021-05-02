/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmf.practica2_esclavo.gui;

import dmf.practica2_esclavo.backend.Configuracion;
import dmf.practica2_esclavo.backend.Red;
import dmf.practica2_esclavo.backend.Reloj;
import java.util.Random;

/**
 *
 * @author Dave
 */
public class Programa {
    private static Ventana v;
    private static Red red;
    private static Reloj reloj;
    
    private static Thread hiloReloj;
    private static Thread hiloRed;
    
    public static void main(String args[]) {
        
        Random r = new Random();
        //Ejecutamos GUI
        v = new Ventana();
        v.setVisible(true);

        Configuracion.cargarDatosDeRed();
        //
        v.setTitle("Reloj " + Configuracion.identificador);
        //Inicializamos variables de red y reloj sin ejecutar
        red = new Red(Configuracion.identificador, Configuracion.interfaz, Configuracion.ip, Configuracion.puerto);
        reloj = new Reloj(v.getLbl_hora(), v.getLbl_delay(), r.nextInt(24), r.nextInt(60), r.nextInt(60));

        //Ejecutamos hilo de control de red
        red.prepararCanal();
        hiloRed = new Thread(new Runnable() {
            @Override
            public void run() {
                red.cicloPrincipal(reloj);
            }
        });
        hiloRed.start();

        //Ejecutamos hilo de control de reloj
        hiloReloj = new Thread(reloj);
        hiloReloj.start();
        
        //Una vez inicializamos todo, solicitamos la hora al maestro
        red.solicitarReloj();
        
    }
}
