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
public class CallInitiation extends CallEvent{
    
    private final double speed;
    private final double position;    
    private final double duration;

    public CallInitiation(double time, int stationId, double speed, double duration, double position) {
        super(time, stationId);
        this.speed = speed;
        this.duration = duration;        
        this.position = position;
        
    }
    
    public double getSpeed() {
        return speed;
    }

    public double getPosition() {
        return position;
    }

    public double getDuration() {
        return duration;
    }
    
    public double getDurationInCell () {
        return Math.min((BaseStation.DIAMETER - this.position) / this.speed, this.duration);
    }
    
    public double getRemainingDuration () {
        return this.duration - this.getDurationInCell();
    }
    
    
    
}
