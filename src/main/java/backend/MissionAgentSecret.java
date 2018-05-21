package backend;

import java.util.List;

public class MissionAgentSecret {
    public Mission mission;
    public List<Agent> agentsWithMission;
    public List<Agent> agentsWithoutMission;

    public MissionAgentSecret(Mission mission, List<Agent> agentsWithMission, List<Agent> agentsWithoutMission){
        this.mission = mission;
        this.agentsWithMission = agentsWithMission;
        this.agentsWithoutMission = agentsWithoutMission;
    }
}
