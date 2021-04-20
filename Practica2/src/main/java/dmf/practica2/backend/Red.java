/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmf.practica2.backend;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dave
 */
public class Red {
    //Instrucciones que recive el maestro
    private static final int CONECTAR = 0; //Recive Maestro
    private static final int SOLICITAR = 1; //Recive Maestro
    
    //Instrucciones que recive el esclavo
    private static final int CONFIRMAR = 2; //Recive Esclavo
    private static final int REEMPLAZAR = 3; //Recive Esclavo
    
    private String interfaz;
    private String ip;
    private int puerto;
    private int identificador;
    
    private int tam_buffer = 4 + 4 * 5; // 4 para la instruccion entera, 4 para el identificador y 4*n de datos del reloj
    
    private SocketAddress socketMulticast;
    private DatagramChannel canalMulticast;
    private Selector selector;
    
    private boolean continuar = true;
    private boolean cambiando = false;
    
    public Red(int identificador, String interfaz, String ip, int puerto){
        this.ip = ip;
        this.puerto = puerto;
        this.identificador = identificador;
        this.interfaz = interfaz;
    }
    
    public void cicloPrincipal(Reloj r1, Reloj r2, Reloj r3){
        try {
            ByteBuffer b = ByteBuffer.allocate( this.tam_buffer );
            int instruccion, numeroReloj;
            while( this.continuar ){
                this.selector.select();
                Iterator<SelectionKey> iterador = this.selector.selectedKeys().iterator();
                while(iterador.hasNext()){
                    SelectionKey llave = (SelectionKey) iterador.next();
                    iterador.remove();
                    if(llave.isReadable()){
                        DatagramChannel canal = (DatagramChannel) llave.channel();
                        Arrays.fill( b.array(),(byte) 0 );
                        b.clear();
                        canal.receive(b);
                        b.flip();
                        
                        instruccion = b.getInt();
                        // Funciones pensadas antes de la explicación de la practica donde se tenía en cuenta la conexión
                        // de un reloj esclavo y la solicitud del esclavo al maestro. En practica nunca se pasa por este
                        // código
                        switch(instruccion){
                            case Red.CONECTAR:
                                confirmarConexion();
                                break;
                            case Red.SOLICITAR:
                                numeroReloj = b.getInt();
                                switch (numeroReloj) {
                                    case 1:
                                        enviarReloj(numeroReloj, r1);
                                        break;
                                    case 2:
                                        enviarReloj(numeroReloj, r2);
                                        break;
                                    case 3:
                                        enviarReloj(numeroReloj, r3);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                        }
                    }
                }
            }
            cerrarCanal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     *  prepararCanal - Se prepara toda la configuracion multicast del canal para poder
     *      comunicarse con los esclavos
     */
    public void prepararCanal(){
        try {
            NetworkInterface ni;
            InetAddress grupo;
            
            ni = NetworkInterface.getByName( this.interfaz );
            grupo = InetAddress.getByName(this.ip);
            
            this.socketMulticast = new InetSocketAddress(this.ip, this.puerto);
            this.canalMulticast = DatagramChannel.open(StandardProtocolFamily.INET);
            this.canalMulticast.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            this.canalMulticast.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            this.canalMulticast.configureBlocking(false);
            
            this.selector = Selector.open();
            
            this.canalMulticast.join(grupo, ni);
            this.canalMulticast.socket().bind(new InetSocketAddress(this.puerto));
            
            this.canalMulticast.register(this.selector, SelectionKey.OP_READ);
            
        } catch (Exception e) {
            Logger.getLogger(Red.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     *  cerrarCanal - 
     */
    public void cerrarCanal(){
        try {
            this.canalMulticast.close();
        } catch (IOException ex) {
            Logger.getLogger(Red.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviarReloj(int identificadorReloj, Reloj r){
        try {
            //Un byte para mensaje y 4*n datos enteros a enviar
            ByteBuffer b = ByteBuffer.allocate( this.tam_buffer );

            b.clear();
            b.putInt(Red.REEMPLAZAR);
            b.putInt(identificadorReloj);
            b.putInt(r.getHoras());
            b.putInt(r.getMinutos());
            b.putInt(r.getSegundos());
            b.putInt(r.getDelay());
            b.flip();
            this.canalMulticast.send(b, this.socketMulticast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void confirmarConexion(){
        try {
            //Un byte para mensaje y 4*n datos enteros a enviar
            ByteBuffer b = ByteBuffer.allocate( this.tam_buffer );

            b.clear();
            b.putInt(Red.CONFIRMAR);
            b.flip();
            this.canalMulticast.send(b, this.socketMulticast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
