/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmf.practica2_esclavo.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dave
 */
public class Configuracion {
    
    public static String interfaz = "enp0s8";
    public static String ip = "230.1.1.1";
    public static int puerto = 6000;
    public static int identificador = 0;
    
    public static void cargarDatosDeRed(){
        try {
            File myObj = new File("red.conf");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String dato = myReader.nextLine();
                if(dato.startsWith("interfaz=")){
                    interfaz = dato.replace("interfaz=", "");
                }
                if(dato.startsWith("ip=")){
                    ip = dato.replace("ip=", "");
                }
                if(dato.startsWith("puerto=")){
                    puerto = Integer.parseInt(dato.replace("puerto=", ""));
                }
                if(dato.startsWith("reloj=")){
                    identificador = Integer.parseInt(dato.replace("reloj=", ""));
                }
                System.out.println(dato);
            }
            myReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
