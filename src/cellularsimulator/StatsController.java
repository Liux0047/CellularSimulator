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
public class StatsController {
    
    private int droppedCallCount = 0;
    private int blockedCallCount = 0;
    
    private double clock = 0.0;
    
    private StatsController() {
    }
    
    public static StatsController getInstance() {
        return StatsControllerHolder.INSTANCE;
    }
    
    private static class StatsControllerHolder {

        private static final StatsController INSTANCE = new StatsController();
    }
}
