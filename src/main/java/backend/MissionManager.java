package backend;

import java.util.List;

public interface MissionManager {
    

    Long createMission(Mission mis);
    
        
    void updateMission(Long id, Mission mis);
    
    List <Mission> getMissions();

    List <Mission> getUncompletedMissions();
    
    Mission getMission(Long id);

    void deleteMission(Long longId);
}
