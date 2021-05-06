/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.models;

/**
 *
 * @author pekochu
 */
public class ServerConnect {
    
    private int workingPort;

    public ServerConnect(int workingPort) {
        this.workingPort = workingPort;
    }   
    
    public ServerConnect(){
        
    }

    public int getWorkingPort() {
        return workingPort;
    }

    public void setWorkingPort(int workingPort) {
        this.workingPort = workingPort;
    }
    
    
}
