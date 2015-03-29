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
public class CallHandover extends CallEvent{
    
    private final double speed;
    private final double duration;

    public CallHandover(double time, int stationId, double speed, double duration) {
        super(time, stationId);
        this.speed = speed;
        this.duration = duration;
    }
    
    public double getDurationInCell () {
        return Math.min(BaseStation.DIAMETER / this.speed, this.duration);
    }
    
    public double getRemainingDuration () {
        return this.duration - this.getDurationInCell();
    }

    public double getSpeed() {
        return speed;
    }

    public double getDuration() {
        return duration;
    }
    
      
    
}
