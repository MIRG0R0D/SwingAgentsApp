package backend;

import java.util.List;


public interface SecretManager {
    
public Mission findMissionWithAgent(Agent agent) ;
public List <Agent> findAgentsWithMission(Mission mission);
public void attachAgentToMission(Agent agent, Mission mission);
public void finishTheMission(Mission mission);
}
