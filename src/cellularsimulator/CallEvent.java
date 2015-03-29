/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellularsimulator;

/**
 *
 * @author Allen
 */
public abstract class CallEvent {
    
    private final double time;
    private final int stationId;
    
    public CallEvent (double time, int stationId) {
        this.time = time;
        this.stationId = stationId;
    }

    public double getTime() {
        return time;
    }

    public int getStationId() {
        return stationId;
    }   
    
    
}
