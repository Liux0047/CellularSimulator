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
        CallEvent currentEvent = eventList.remove(0);
        this.clock = currentEvent.getTime();

        if (currentEvent instanceof CallInitiation) {

            this.handleInitiation((CallInitiation) currentEvent);

        } else if (currentEvent instanceof CallHandover) {

        } else if (currentEvent instanceof CallTermination) {

        }
    }

    private void handleInitiation(CallInitiation initiationEvent) {
        this.totalCallCount++;
        BaseStation baseStation = StationManager.getInstance().getStation(initiationEvent.getStationId());
        if (!baseStation.allocateNewCall()) {    //if allocate new call fails
            this.blockedCallCount++;
        } else {
            if (initiationEvent.getDurationInCell() < initiationEvent.getDuration()) {
                CallTermination terminationEvent = new CallTermination(
                        initiationEvent.getDurationInCell(),
                        initiationEvent.getStationId()
                );
                this.scheduleEvent(terminationEvent);
            } else {
                CallHandover handoverEvent = new CallHandover(
                        initiationEvent.getDurationInCell(),
                        initiationEvent.getStationId(),
                        initiationEvent.getSpeed(),
                        initiationEvent.getRemainingDuration()
                );
                this.scheduleEvent(handoverEvent);
            }
        }

    }

    private int getInsertIndex(double time, int start, int end) {
        //binary search to find the index for insertion
        while (start < end) {
            int middle = (start + end) / 2;
            double middleTime = this.eventList.get(middle).getTime();

            if (time > middleTime) {
                this.getInsertIndex(time, middle, end);
            } else if (time < middleTime) {
                this.getInsertIndex(time, start, middle);
            } else {
                return middle;
            }
        }
        return start;
    }

}
