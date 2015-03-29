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
public class StationManager {
    
    public static final int STATION_COUNT = 20;
    
    private ArrayList<BaseStation> stations = new ArrayList<>();
    
    private StationManager() {
    }
    
    public static StationManager getInstance() {
        return StationManagerHolder.INSTANCE;
    }
    
    private static class StationManagerHolder {

        private static final StationManager INSTANCE = new StationManager();
    }
    
    public void initStations (int FCA_SCHEME) {
        for (int i = 0; i< StationManager.STATION_COUNT; i++){
            stations.add(new BaseStation(FCA_SCHEME));
        }
    }
    
    public BaseStation getStation (int index){
        return stations.get(index);
    }
    
}
