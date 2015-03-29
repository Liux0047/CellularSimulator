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

    public CallInitiation(double time, int stationId, double speed, double duration) {
        super(time, stationId);
        this.speed = speed;
        this.duration = duration;
        
        this.position = this.generatePosition();
        
    }
    
    private double generatePosition () {
        //call initiation position inside a cell is uniformly distributed
        return Math.random() * BaseStation.DIAMETER;
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
