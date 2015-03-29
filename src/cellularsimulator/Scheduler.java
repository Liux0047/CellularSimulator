/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellularsimulator;

import java.util.ArrayList;

/**
 *
 * @author Allen
 */
public class Scheduler {

    private int droppedCallCount = 0;
    private int blockedCallCount = 0;
    private int totalCallCount = 0;
    private int successfulCallCount = 0;

    private double clock = 0.0;

    private ArrayList<CallEvent> eventList = new ArrayList<>();

    private Scheduler() {
    }

    public static Scheduler getInstance() {
        return SchedulerHolder.INSTANCE;
    }

    private static class SchedulerHolder {

        private static final Scheduler INSTANCE = new Scheduler();
    }

    public void scheduleEvent(CallEvent event) {

        int index = this.getInsertIndex(event.getTime(), 0, this.eventList.size());
        eventList.add(index, event);

    }

    public void advanceClock() {
        CallEvent currentEvent = this.eventList.remove(0);
        this.clock = currentEvent.getTime();

        if (currentEvent instanceof CallInitiation) {
            this.handleInitiation((CallInitiation) currentEvent);
            System.out.println(this.clock + ": Initiation from Station " + currentEvent.getStationId());
        } else if (currentEvent instanceof CallHandover) {
            this.handleHandover((CallHandover) currentEvent);
            System.out.println(this.clock + ": Handover from Station " + currentEvent.getStationId());
        } else if (currentEvent instanceof CallTermination) {
            this.handleTermination((CallTermination) currentEvent);
            System.out.println(this.clock + ": Termination in Station " + currentEvent.getStationId());
        }

    }
    
    public boolean isTerminated () {
        if(this.eventList.isEmpty()){
            System.out.println("Total Calls: " + this.totalCallCount);
            System.out.println("Blocked Calls: " + this.blockedCallCount);
            System.out.println("Dropped Calls: " + this.droppedCallCount);
            System.out.println("Successful Calls: " + this.successfulCallCount);
            return true;
        } else {
            return false;
        }
        
    }

    private void handleInitiation(CallInitiation initiationEvent) {
        this.totalCallCount++;

        if (initiationEvent.getStationId() == StationManager.STATION_COUNT - 1) {
            //if already in last station
            CallTermination terminationEvent = new CallTermination(
                    initiationEvent.getDurationInCell() + this.clock,
                    initiationEvent.getStationId()
            );
            this.scheduleEvent(terminationEvent);
            return;
        }

        BaseStation baseStation = StationManager.getInstance().getStation(initiationEvent.getStationId());
        if (!baseStation.allocateNewCall()) {    //if allocate new call fails
            this.blockedCallCount++;
        } else {
            if (initiationEvent.getDurationInCell() < initiationEvent.getDuration()) {
                //shedule handover
                CallHandover handoverEvent = new CallHandover(
                        initiationEvent.getDurationInCell() + this.clock,
                        initiationEvent.getStationId(),
                        initiationEvent.getSpeed(),
                        initiationEvent.getRemainingDuration()
                );
                this.scheduleEvent(handoverEvent);
            } else {
                //schedule termination event
                CallTermination terminationEvent = new CallTermination(
                        initiationEvent.getDurationInCell() + this.clock,
                        initiationEvent.getStationId()
                );
                this.scheduleEvent(terminationEvent);
            }
        }

    }

    private void handleHandover(CallHandover handoverEvent) {
        if (handoverEvent.getStationId() == StationManager.STATION_COUNT - 1) {
            //if already in last station
            CallTermination terminationEvent = new CallTermination(
                    handoverEvent.getDurationInCell() + this.clock,
                    handoverEvent.getStationId()
            );
            this.scheduleEvent(terminationEvent);
            return;
        }
        BaseStation preStation = StationManager.getInstance().getStation(handoverEvent.getStationId());
        preStation.releaseChannel();

        BaseStation nextStation = StationManager.getInstance().getStation(handoverEvent.getStationId() + 1);
        if (!nextStation.allocateHandover()) {
            this.droppedCallCount++;
        } else {
            if (handoverEvent.getDurationInCell() < handoverEvent.getDuration()) {
                //shedule handover
                CallHandover nextHandoverEvent = new CallHandover(
                        handoverEvent.getDurationInCell() + this.clock,
                        handoverEvent.getStationId() + 1,
                        handoverEvent.getSpeed(),
                        handoverEvent.getRemainingDuration()
                );
                this.scheduleEvent(nextHandoverEvent);
            } else {
                //schedule termination event
                CallTermination terminationEvent = new CallTermination(
                        handoverEvent.getDurationInCell() + this.clock,
                        handoverEvent.getStationId() + 1
                );
                this.scheduleEvent(terminationEvent);
            }
        }
    }

    private void handleTermination(CallTermination terminationEvent) {
        BaseStation baseStation = StationManager.getInstance().getStation(terminationEvent.getStationId());
        baseStation.releaseChannel();
        this.successfulCallCount++;
    }

    private int getInsertIndex(double time, int start, int end) {
        //binary search to find the index for insertion
        while (start < end) {
            int middle = (start + end) / 2;
            double middleTime = this.eventList.get(middle).getTime();

            if (time > middleTime) {
                return this.getInsertIndex(time, middle + 1, end);
            } else if (time < middleTime) {
                return this.getInsertIndex(time, start, middle);
            } else {
                return middle;
            }
        }
        return start;
    }

    public double getClock() {
        return clock;
    }

}
