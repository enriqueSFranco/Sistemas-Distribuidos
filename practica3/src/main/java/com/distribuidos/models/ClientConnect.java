/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.models;

import java.net.InetAddress;

/**
 *
 * @author pekochu
 */
public class ClientConnect {
    
    private InetAddress mainServer;
    private InetAddress backupServer;
    private int mainServerPort;
    private int backupServerPort;

    public ClientConnect(InetAddress mainServer, InetAddress backupServer, 
            int mainServerPort, int backupServerPort) {
        this.mainServer = mainServer;
        this.backupServer = backupServer;
        this.mainServerPort = mainServerPort;
        this.backupServerPort = backupServerPort;
    }
    
    public ClientConnect(){
        
    }

    public InetAddress getMainServer() {
        return mainServer;
    }

    public void setMainServer(InetAddress mainServer) {
        this.mainServer = mainServer;
    }

    public InetAddress getBackupServer() {
        return backupServer;
    }

    public void setBackupServer(InetAddress backupServer) {
        this.backupServer = backupServer;
    }

    public int getMainServerPort() {
        return mainServerPort;
    }

    public void setMainServerPort(int mainServerPort) {
        this.mainServerPort = mainServerPort;
    }

    public int getBackupServerPort() {
        return backupServerPort;
    }

    public void setBackupServerPort(int backupServerPort) {
        this.backupServerPort = backupServerPort;
    }
    
    
    
}
