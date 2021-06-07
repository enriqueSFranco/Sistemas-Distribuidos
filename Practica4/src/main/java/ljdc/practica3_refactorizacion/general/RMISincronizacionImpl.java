/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.general;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ljdc.practica3_refactorizacion.general.modelo.Usuario;
import ljdc.practica3_refactorizacion.rmi.RMIServidorInterfaz;
import ljdc.practica3_refactorizacion.rmi.RMISincronizacionInterfaz;
import ljdc.practica3_refactorizacion.servidor.backend.basedatos.ConexionBD;
import ljdc.practica3_refactorizacion.servidor.interfaces.GUIServidorInterfaz;

/**
 *
 * @author Dave
 */
public class RMISincronizacionImpl extends UnicastRemoteObject implements RMISincronizacionInterfaz {
    
    private int espera = 5000;
    private long limiteDesface = 60 * 60000; //60 minutos o 1 hora
    
    private ManejadorDeConfiguracion manejador_config;
    private GUIServidorInterfaz gui;
    private Reloj r;
    
    private Thread hiloSincronizacion;
    
    public RMISincronizacionImpl(ManejadorDeConfiguracion manejador_config, GUIServidorInterfaz gui, Reloj r)  throws RemoteException{
        this.manejador_config = manejador_config;
        this.gui = gui;
        this.r = r;
        
        this.hiloSincronizacion = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        sincronizar();
                        Thread.sleep(espera);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(RMISincronizacionImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }
    
    public void iniciarSincronizacion(){
        this.hiloSincronizacion.start();
    }
    
    private void sincronizar(){
        Registry registro;
        int mi_id = 0;
        long mi_tiempo, mi_tiempo_2, D, tiempoDispositivo;
        ConexionBD con = new ConexionBD();
        
        List<InfoServidor> dispositivos = new ArrayList<>();
        List<InfoServidor> dispositivosDesfazados = new ArrayList<>();
        List<Long> Di = new ArrayList<>();
        List<Long> Ai = new ArrayList<>();
        
        for(InfoServidor servidor : this.manejador_config.getConfiguracion().getServidores()){
            
            try {
                // Soy yo? Si soy yo sincronizar al resto
                if( servidor.getDireccion().getHostAddress().equals(InetAddress.getByName("localhost").getHostAddress()) && 
                        servidor.getPuerto() == this.manejador_config.getConfiguracion().getPuerto()){
                    //System.out.println("Soy el sincronizador");
                    this.gui.agregarInfoSincronizacion("Sincronizando...");
                    //*** Sincronizar
                    mi_tiempo = this.r.getMilisegundos();
                    mi_tiempo += this.r.getHoras() * 3600000;
                    mi_tiempo += this.r.getMinutos() * 60000;
                    mi_tiempo += this.r.getSegundos() * 1000;
                    // Obtener tiempos de servidores
                    for(InfoServidor serv : this.manejador_config.getConfiguracion().getServidores()){
                        try {
                            registro = LocateRegistry.getRegistry(serv.getDireccion().getHostAddress(), serv.getPuerto());
                            RMISincronizacionInterfaz dispo = (RMISincronizacionInterfaz)registro.lookup("sincronizacion");
                            tiempoDispositivo = dispo.obtenerTiempo(mi_tiempo);
                            if (-limiteDesface > tiempoDispositivo || tiempoDispositivo > limiteDesface){// Se desfasa demasiado el tiempo
                                //Agregar a dispositivos desfazados y contiuar con el siguiente dispositivo
                                dispositivosDesfazados.add(serv);
                                continue;
                            }
                            Di.add(tiempoDispositivo);
                            dispositivos.add(serv);
                            
                            //Si soy yo guarda el id
                            if( serv.getDireccion().getHostAddress().equals(InetAddress.getByName("localhost").getHostAddress()) && 
                                serv.getPuerto() == this.manejador_config.getConfiguracion().getPuerto()){
                                mi_id = Di.size()-1;
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(RMISincronizacionImpl.class.getName()).log(Level.WARNING, "No se pudo sincronizar al servidor en {0}:{1}", new Object[]{serv.getDireccion().getHostAddress(), serv.getPuerto()});
                        } catch (NotBoundException ex) {
                            Logger.getLogger(RMISincronizacionImpl.class.getName()).log(Level.WARNING, "No hay modulo de sincronizacion en {0}:{1}", new Object[]{serv.getDireccion().getHostAddress(), serv.getPuerto()});
                        }
                    }
                    // Obtener tiempos de clientes
                    for (Usuario usu: con.obtenerDireccionesDeUsuarios()){
                        try {
                            registro = LocateRegistry.getRegistry(usu.getIp(), usu.getPuerto());
                            RMISincronizacionInterfaz dispo = (RMISincronizacionInterfaz)registro.lookup("sincronizacion");
                            tiempoDispositivo = dispo.obtenerTiempo(mi_tiempo);
                            if (-limiteDesface > tiempoDispositivo || tiempoDispositivo > limiteDesface){// Se desfasa demasiado el tiempo
                                //Agregar a dispositivos desfazados y contiuar con el siguiente dispositivo
                                dispositivosDesfazados.add(new InfoServidor(InetAddress.getByName(usu.getIp()), usu.getPuerto()));
                                continue;
                            }
                            Di.add(tiempoDispositivo);
                            dispositivos.add(new InfoServidor(InetAddress.getByName(usu.getIp()), usu.getPuerto()));
                        } catch (RemoteException ex) {
                            Logger.getLogger(RMISincronizacionImpl.class.getName()).log(Level.WARNING, "No se pudo sincronizar al cliente en {0}:{1}", new Object[]{usu.getIp(), usu.getPuerto()});
                        } catch (NotBoundException ex) {
                            Logger.getLogger(RMISincronizacionImpl.class.getName()).log(Level.WARNING, "No hay modulo de sincronizacion en {0}:{1}", new Object[]{usu.getIp(), usu.getPuerto()});
                        }
                    }
                    // Recalcular tiempo
                    mi_tiempo_2 = this.r.getMilisegundos();
                    mi_tiempo_2 += this.r.getHoras() * 3600000;
                    mi_tiempo_2 += this.r.getMinutos() * 60000;
                    mi_tiempo_2 += this.r.getSegundos() * 1000;
                    System.out.println("Tiempo1: "+ mi_tiempo + "\tTiempo2: "+ mi_tiempo_2);
                    D = 0;
                    for (int i = 0; i<Di.size() ;i++ ){
                        Di.set(i, Di.get(i) - (mi_tiempo_2 - mi_tiempo) / 2);
                        D += Di.get(i);
                    }
                    D = D / Di.size();
                    System.out.println("D:"+D);
                    for( int i = 0; i<Di.size() ;i++ ){
                        Ai.add(D-Di.get(i));
                        //System.out.println("Disp: " + dispositivos.get(i).getDireccion().getHostAddress() + ":"+dispositivos.get(i).getPuerto() + "\tAjuste: "+Ai.get(i));
                        this.gui.agregarInfoSincronizacion("\tDisp: " +dispositivos.get(i).getDireccion().getHostAddress()+":"+dispositivos.get(i).getPuerto()+ "\tAjuste: "+Ai.get(i));
                    }
                    for( int i = 0; i<Di.size() ; i++ ){
                        registro = LocateRegistry.getRegistry(dispositivos.get(i).getDireccion().getHostAddress(), dispositivos.get(i).getPuerto());
                        RMISincronizacionInterfaz dispoSinc = (RMISincronizacionInterfaz)registro.lookup("sincronizacion");
                        // poner el ajuste haciendo las diviciones correspondientes
                        dispoSinc.cambiarTiempo( Ai.get(i) );
                    }
                    //Calcular la hora para los dispositivos desincronizados y enviar modificarla
                    mi_tiempo_2 += Ai.get(mi_id);
                    for(InfoServidor dispoDesfazado: dispositivosDesfazados){
                        registro = LocateRegistry.getRegistry(dispoDesfazado.getDireccion().getHostAddress(), dispoDesfazado.getPuerto());
                        RMISincronizacionInterfaz dispoDesf = (RMISincronizacionInterfaz)registro.lookup("sincronizacion");
                        dispoDesf.cambiarTiempo((int)(mi_tiempo_2/3600000), (int)((mi_tiempo_2%3600000)/60000), (int)((mi_tiempo_2%60000)/1000), (int) (mi_tiempo_2%1000));
                        this.gui.agregarInfoSincronizacion("\tDisp: " +dispoDesfazado.getDireccion().getHostAddress()+":"+dispoDesfazado.getPuerto()+ "\tAjuste Completo: "+mi_tiempo_2);
                    }
                    
                    System.out.println("\n\n");
                    break;
                }
                // No soy yo, hacer ping para verificar que el servidor anterior esta bien
                registro = LocateRegistry.getRegistry(servidor.getDireccion().getHostAddress(), servidor.getPuerto());
                RMIServidorInterfaz serv = (RMIServidorInterfaz) registro.lookup("servidor");
                
                serv.ping();
                
                // Si hizo bien el ping termina
                break;
            } catch (UnknownHostException ex) {
                Logger.getLogger(RMISincronizacionImpl.class.getName()).log(Level.WARNING, "Sincronizador desconocido en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (RemoteException ex) {
                Logger.getLogger(RMISincronizacionImpl.class.getName()).log(Level.WARNING, "Sincronizador no activo en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            } catch (NotBoundException ex) {
                Logger.getLogger(RMISincronizacionImpl.class.getName()).log(Level.WARNING, "No hay modulo de sincronizacion en {0}:{1}", new Object[]{servidor.getDireccion().getHostAddress(), servidor.getPuerto()});
            }
            
        }
    }

    @Override
    public long obtenerTiempo(long tiempo) {
        long mi_tiempo = this.r.getMilisegundos();
        
        mi_tiempo += this.r.getHoras() * 3600000;
        mi_tiempo += this.r.getMinutos() * 60000;
        mi_tiempo += this.r.getSegundos() * 1000;
        System.out.println("Tiempo: " + tiempo + "\tMiTiempo:"+mi_tiempo);
        System.out.println("Hora:"+r.obtenerHora());
        System.out.println("Regrese: "+(mi_tiempo - tiempo));
        return mi_tiempo - tiempo;
    }

    @Override
    public void cambiarTiempo(long ajuste) {
        // Colocar el tiempo original menos el delay con modulo 24:60:60
        long mi_tiempo = this.r.getMilisegundos();
        mi_tiempo += this.r.getHoras() * 3600000;
        mi_tiempo += this.r.getMinutos() * 60000;
        mi_tiempo += this.r.getSegundos() * 1000;
        mi_tiempo += ajuste;
        System.out.println("HoraCalculada:"+(int)(mi_tiempo/3600000)+":"+ (int)((mi_tiempo%3600000)/60000)+":"+(int)((mi_tiempo%60000)/1000));
        this.r.editarReloj((int)(mi_tiempo/3600000), (int)((mi_tiempo%3600000)/60000), (int)((mi_tiempo%60000)/1000), this.r.getDelay());
        this.r.setMilisegundos(mi_tiempo%1000);
    }
    
    @Override
    public void cambiarTiempo(int horas, int minutos, int segundos, int delta) {
        // Colocar el tiempo original menos el delay con modulo 24:60:60
        this.r.editarReloj(horas, minutos, segundos, this.r.getDelay());
        this.r.setMilisegundos(delta);
    }

    public int getEspera() {
        return espera;
    }

    public void setEspera(int espera) {
        this.espera = espera;
    }
    
}
