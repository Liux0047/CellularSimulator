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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
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

        int size = 10000;
        StationManager stationMgr = StationManager.getInstance();
        Scheduler schedular = Scheduler.getInstance();
        schedular.setSize(size);

        int numRuns = 100;
        for (int i = 0; i < numRuns; i++) {
            generateData(size);
            
            readData();            
            stationMgr.initStations(BaseStation.NO_RESERVATION);
            while (!schedular.isTerminated()) {
                schedular.advanceClock();
            }
            stationMgr.reset();
            
            readData();
            stationMgr.initStations(BaseStation.RESERVATION_1);
            while (!schedular.isTerminated()) {
                schedular.advanceClock();
            }
            stationMgr.reset();
        }

        //outputWarmUp(schedular, size, numRuns);

    }

    public static void readData() {
        File file = new File("input-random.txt");
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
                double position = Double.parseDouble(lineVariables[5]);
                CallInitiation event = new CallInitiation(arrivalTime, baseStation, speed, duration, position);
                schedular.scheduleEvent(event);

                //System.out.println("Scheduled arrival No." + lineVariables[0]);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CellularSimulator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CellularSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void generateData(int size) {
        double randomCumulative, interval, duration, velocity, position;
        double arrivalTime = 0.0;
        Random random = new Random();

        try (PrintWriter writer = new PrintWriter("input-random.txt", "UTF-8")) {
            for (int i = 1; i <= size; i++) {
                writer.print(i + " ");
                randomCumulative = Math.random();
                interval = 1.3698 * Math.log(1 / (1 - randomCumulative));
                arrivalTime += interval;
                writer.print(arrivalTime + " ");
                writer.print((int) (Math.random() * 20) + 1 + " ");
                randomCumulative = Math.random();
                duration = 99.8319 * Math.log(1 / (1 - randomCumulative)) + 10.004;
                writer.print(duration + " ");
                velocity = random.nextGaussian() * 9.019057898 + 120.072;
                writer.print(velocity + " ");
                position = Math.random() * BaseStation.DIAMETER;
                writer.println(position);
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(CellularSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void outputWarmUp(Scheduler schedular, int size, int numRuns) {
        try (PrintWriter writer = new PrintWriter("warmup.txt", "UTF-8")) {
            for (int i = 0; i < size - 1; i++) {
                writer.println(schedular.getAvgBlocked()[i] / numRuns + " " + schedular.getAvgDropped()[i] / numRuns);
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(CellularSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
