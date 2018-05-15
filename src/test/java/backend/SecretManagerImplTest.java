/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import org.apache.derby.jdbc.ClientDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * @author xdovgan
 */
public class SecretManagerImplTest {
    private SecretManager manager;
    private MissionManager missionManager;
    private AgentManager agentManager;
    private Agent jamesBond;
    private Agent vonStierlitz;
    private Agent badAgent;
    private Mission infiltrateSPD;
    private ClientDataSource ds;

    @Before
    public void setUp() {
        ds = new ClientDataSource();
        ds.setUser("aa");
        ds.setServerName("localhost");
        ds.setDatabaseName("myDB");
        ds.setPortNumber(1527);


        manager = new SecretManagerImpl(ds);
        missionManager = new MissionManagerImpl(ds);
        agentManager = new AgentManagerImpl(ds);
        
        badAgent = new Agent(null,null, null, null);
        jamesBond = new AgentBuilder()
                .name("Bond, James")
                .born(LocalDate.of(1964, Month.JANUARY, 1))
                .level("00")
                .id(null)
                .build();

        vonStierlitz = new AgentBuilder()
                .name("Max Otto vonStierlitz")
                .born(LocalDate.of(1900, Month.OCTOBER, 8))
                .level("00")
                .id(null)
                .build();
        
        infiltrateSPD = new MissionBuilder()
                .setCodeName("spdfree")
                .setDescription("Get info about spd organization")
                .setStart(LocalDate.of(2017, Month.MARCH, 20))
                .setEnd(null)
                .setId(null)
                .setLocation("PRAHA")
                .build();

    }

    @After
    public void tearDown() {
        try {
            ds.getConnection().prepareStatement("DELETE FROM APP.MISSION").execute();
            ds.getConnection().prepareStatement("DELETE FROM APP.AGENT").execute();
            ds.getConnection().prepareStatement("DELETE FROM APP.MISSION_ASSIGNMENT").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
      
    /**
     * Test of findMissionWithAgent method, of class SecretManagerImpl.
     */
    @Test
    public void testFindMissionWithAgent() {
        
        Agent agent = jamesBond;
        Mission mission = infiltrateSPD;
        missionManager.createMission(mission);
        agentManager.create(agent);
        manager.attachAgentToMission(agent, mission);
        
        assertEquals(manager.findMissionWithAgent(agent), mission);
        
    }
    
      /**
     * Test of attachAgentToMission method, of class SecretManagerImpl.
     */
    @Test
    public void testAttachAgentToMission() {
        Agent agent = jamesBond;
        Mission mission = infiltrateSPD;
        missionManager.createMission(mission);
        agentManager.create(agent);
        manager.attachAgentToMission(agent, mission);
        
    }  
    @Test(expected = IllegalArgumentException.class)
    public void testAttachBadAgent() {
        
        Agent agent = badAgent;
        Mission mission = infiltrateSPD;
        manager.attachAgentToMission(agent, mission);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttachNullAgent() {
        
        Mission mission = infiltrateSPD;
        missionManager.createMission(mission);
        manager.attachAgentToMission(null, mission);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttachNullMission() {
        
        Agent agent = vonStierlitz;
        manager.attachAgentToMission(agent, null);
    }
    
    @Test
    public void testFindAgentsWithMission() {
        
        Agent stier = vonStierlitz;
        Agent bond = jamesBond;
        Mission mission = infiltrateSPD;

        agentManager.create(stier);
        agentManager.create(bond);
        missionManager.createMission(mission);

        manager.attachAgentToMission(stier, mission);
        manager.attachAgentToMission(bond, mission);
        
        List <Agent> result = manager.findAgentsWithMission(mission);
        
        assertTrue(result.size()==2);
        assertTrue(result.contains(stier));
        assertTrue(result.contains(bond));
        
    }



    /**
     * Test of finishTheMission method, of class SecretManagerImpl.
     */
    @Test
    public void testFinishTheMission() {

        Agent agent = jamesBond;
        Mission mission = infiltrateSPD;

        long agentId = agentManager.create(agent);
        long missionId = missionManager.createMission(mission);

        agent.setId(agentId);
        mission.setId(missionId);

        manager.attachAgentToMission(agent, mission);
        manager.finishTheMission(mission);

        mission = missionManager.getMission(missionId);
        
        assertNotNull(mission.getEnd());
    }
    
}
