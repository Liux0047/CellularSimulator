/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellularsimulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Allen
 */
public class CellularSimulator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        readData();
        StationManager.getInstance().initStations(BaseStation.NO_RESERVATION);
        //StationManager.getInstance().initStations(BaseStation.RESERVATION_1);
        Scheduler schedular = Scheduler.getInstance();

        while (!schedular.isTerminated()){
            schedular.advanceClock();            
        }

    }

    public static void readData() {
        File file = new File("input.txt");
        Scheduler schedular = Scheduler.getInstance();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String[] lineVariables;
            while ((line = br.readLine()) != null) {
                lineVariables = line.split("\\s+");
                double arrivalTime = Double.parseDouble(lineVariables[1]);
                int baseStation = Integer.parseInt(lineVariables[2]) - 1;
                double duration = Double.parseDouble(lineVariables[3]);
                double speed = Double.parseDouble(lineVariables[4]) / 3600;
                CallInitiation event = new CallInitiation(arrivalTime, baseStation, speed, duration);
                schedular.scheduleEvent(event);

                //System.out.println("Scheduled arrival No." + lineVariables[0]);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CellularSimulator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CellularSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
