package backend;


import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SecretManagerImpl implements SecretManager {
    private final AgentManagerImpl agentManager;
    private final MissionManager missionManager;

    private final DataSource ds;

    public SecretManagerImpl(DataSource ds) {
        missionManager = new MissionManagerImpl(ds);
        agentManager = new AgentManagerImpl(ds);
        this.ds=ds;

    }

    private void checkMission(Mission mis){
        if(mis == null){
            throw new IllegalArgumentException();
        }

        if(mis.getCodeName() == null || mis.getCodeName().isEmpty()){
            throw new IllegalArgumentException();
        }

        if(mis.getStart() == null){
            throw new IllegalArgumentException();
        }
    }

    private void checkAgent(Agent agent){
        if(agent == null){
            throw new IllegalArgumentException();
        }

        if(agent.getId() == null){
            throw new IllegalArgumentException();
        }
    }


    /**
     * finding some mission with the certain agent
     * @param agent agent on mission
     * @return mission with the agent
     */
    
    // can 1 agent be on several missions?????
    @Override
    public Mission findMissionWithAgent(Agent agent) {

        checkAgent(agent);

        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select * from APP.MISSION_ASSIGNMENT WHERE AGENT_ID = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, agent.getId());
            ResultSet rs = ps.executeQuery();

            if  (rs.next()) {
                long missionId = rs.getLong(2);
                long agentId = rs.getLong(3);

                return missionManager.getMission(missionId);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * finding agents on the certain mission
     * @param mission mission with agents
     * @return list of agents on the mission
     */
    @Override
    public List<Agent> findAgentsWithMission(Mission mission) {

        checkMission(mission);

        List <Agent> agents = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select * from APP.MISSION_ASSIGNMENT WHERE MISSION_ID = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, mission.getId());
            ResultSet rs = ps.executeQuery();

            while  (rs.next()) {
                long missionId = rs.getLong(2);
                long agentId = rs.getLong(3);

                Agent agent = agentManager.findAgentById(agentId);
                agents.add(agent);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return agents;
    }

    /**
     * adding new agent for the mission
     * @param agent new agent for the mission
     * @param mission the mission itself
     */
    @Override
    public void attachAgentToMission(Agent agent, Mission mission) {

        checkMission(mission);
        checkAgent(agent);

        Connection con = null;
        try {
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement("insert into APP.MISSION_ASSIGNMENT(MISSION_ID, AGENT_ID) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, mission.getId());
            ps.setLong(2, agent.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SecretManagerImpl.class.getName()).log(Level.SEVERE, "Error executing insert: ", ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SecretManagerImpl.class.getName()).log(Level.SEVERE, "Error closing connection: ", ex);
                }
            }
        }

        
    }

    /**
     * finishing the mission
     * @param mission mission to be finished
     */
    @Override
    public void finishTheMission(Mission mission) {
        checkMission(mission);

        mission.setEnd(LocalDate.now());
        missionManager.updateMission(mission.getId(), mission);
        
    }

    @Override
    public void detachAgentFromMission(Agent agent, Mission mission) {
        checkMission(mission);
        checkAgent(agent);

        Connection con = null;
        try {
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement("delete from APP.MISSION_ASSIGNMENT where MISSION_ID  = ? and AGENT_ID = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, mission.getId());
            ps.setLong(2, agent.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SecretManagerImpl.class.getName()).log(Level.SEVERE, "Error executing insert: ", ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SecretManagerImpl.class.getName()).log(Level.SEVERE, "Error closing connection: ", ex);
                }
            }
        }

    }


}
