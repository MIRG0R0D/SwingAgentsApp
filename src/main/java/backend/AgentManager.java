package backend;

import java.util.List;


public interface AgentManager {
    
    public Long create(Agent agent) ;

    public Agent findAgentById(Long id);
    
    public void update(Long id, Agent agent);
    
    public List<Agent> findAllAgents();
    
    public void deleteAgentById(Long id);
    
    public void deleteAllAgents();
    

}
