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
public class ServerConnect {
    
    private int workingPort;
    private InetAddress backupAddress;
    private int backupPort;
    private int clientPort;

    public ServerConnect(int workingPort, InetAddress backupAddress, int backupPort, int clientPort) {
        this.workingPort = workingPort;
        this.backupAddress = backupAddress;
        this.backupPort = backupPort;
        this.clientPort = clientPort;
    }    
    
    public ServerConnect(){ /* */ }

    public int getWorkingPort() {
        return workingPort;
    }

    public void setWorkingPort(int workingPort) {
        this.workingPort = workingPort;
    }

    public InetAddress getBackupAddress() {
        return backupAddress;
    }

    public void setBackupAddress(InetAddress backupAddress) {
        this.backupAddress = backupAddress;
    }

    public int getBackupPort() {
        return backupPort;
    }

    public void setBackupPort(int backupPort) {
        this.backupPort = backupPort;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }
    
    
    
    
}
