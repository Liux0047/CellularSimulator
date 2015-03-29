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
public class BaseStation {

    public static final int NO_RESERVATION = 0;
    public static final int RESERVATION_1 = 1;

    public static final int DIAMETER = 2;

    private final int FCA_SCHEME;

    private int availableChannel;
    private int reservedHandoverChannel;

    public BaseStation(int FCA_SCHEME) {
        this.FCA_SCHEME = FCA_SCHEME;

        if (FCA_SCHEME == BaseStation.NO_RESERVATION) {
            this.availableChannel = 10;
            this.reservedHandoverChannel = 0;
        } else if (FCA_SCHEME == BaseStation.RESERVATION_1) {
            this.availableChannel = 9;
            this.reservedHandoverChannel = 1;
        }
    }

    public boolean allocateNewCall() {
        if (this.availableChannel <= 0) {
            return false;
        } else {
            this.availableChannel--;
            return true;
        }
    }

    public boolean allocateHandover() {
        if (this.availableChannel == 0) {   //no available general channel
            if (this.FCA_SCHEME == BaseStation.RESERVATION_1 && this.reservedHandoverChannel > 0) {
                this.reservedHandoverChannel--;
                return true;
            } else {
                return false;
            }
        } else {
            this.availableChannel--;
            return true;
        }
    }

    public void releaseChannel() {
        if (this.FCA_SCHEME == BaseStation.RESERVATION_1 && this.reservedHandoverChannel == 0) {
            this.reservedHandoverChannel++;
        } else {
            this.availableChannel++;
        }
    }

}
