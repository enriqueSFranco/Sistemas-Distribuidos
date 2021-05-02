/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmf.practica2_esclavo.backend;

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
    
    // 4 bytes para comando
    // 4 bytes para identificador
    // 4 bytes para cada dato: horas, minutos, segundos, delay
    // Total: 24
    private int tam_buffer = 24; 
    
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
    
    public void cicloPrincipal(Reloj r){
        try {
            ByteBuffer b = ByteBuffer.allocate( this.tam_buffer );
            int instruccion, ident;
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
                        // La instruccion de CONFIRMAR se descarta en la version final
                        switch(instruccion){
                            case Red.CONFIRMAR:
                                solicitarReloj();
                                break;
                            case Red.REEMPLAZAR:
                                ident = b.getInt();
                                if(ident == this.identificador || ident == 0)
                                    recibirReloj(b, r);
                                //System.out.println("Instruccion: " + instruccion + "\nID: " + ident + "\nMiID: " + this.identificador);
                                break;
                            default:
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
    
    public void prepararCanal(){
        try {
            NetworkInterface ni;
            InetAddress grupo;
            
            ni = NetworkInterface.getByName(this.interfaz);
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
    
    public void cerrarCanal(){
        try {
            this.canalMulticast.close();
        } catch (IOException ex) {
            Logger.getLogger(Red.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void solicitarReloj(){
        try {
            ByteBuffer b = ByteBuffer.allocate( this.tam_buffer );

            b.clear();
            b.putInt(Red.SOLICITAR);
            b.flip();
            this.canalMulticast.send(b, this.socketMulticast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void recibirReloj(ByteBuffer b, Reloj r){
        try {
            r.editarReloj(b.getInt(), b.getInt(), b.getInt(), b.getInt());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
