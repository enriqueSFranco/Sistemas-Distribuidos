/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmf.interfacesdered;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dave
 */
public class Red {
    public void mostrarInterfaces(){
        try {
            Enumeration<NetworkInterface> nets =  NetworkInterface.getNetworkInterfaces();
            Collections.list(nets).forEach(netint -> {
                mostrarInformacionDeInterfaz(netint);
            });
        } catch (SocketException ex) {
            Logger.getLogger(Red.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mostrarInformacionDeInterfaz(NetworkInterface netint){
        System.out.printf("Interfaz: %s\n", netint.getDisplayName());
        System.out.printf("Nombre: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses =  netint.getInetAddresses();
        Collections.list(inetAddresses).forEach(inetAddress -> {
            System.out.printf("InetAddress: %s\n", inetAddress);
        });
        System.out.printf("\n");
    }
    
    public static void main(String args[]){
        new Red().mostrarInterfaces();
    }
}
