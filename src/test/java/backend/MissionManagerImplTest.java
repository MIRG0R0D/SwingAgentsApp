/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import org.apache.derby.jdbc.ClientDataSource;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Dima
 */
public class MissionManagerImplTest {

    private MissionManagerImpl manager;
    private Mission killTerrorists;
    private Mission infiltrateSPD;
    private Mission badMission;
    private ClientDataSource ds;

    public MissionManagerImplTest() {
    }


    @Before
    public void setUp() {

        ds = new ClientDataSource();
        ds.setUser("aa");
        ds.setServerName("localhost");
        ds.setDatabaseName("myDB");
        ds.setPortNumber(1527);


        manager = new MissionManagerImpl(ds);

        badMission = new MissionBuilder()
                .setCodeName(null)
                .setDescription("")
                .setStart(LocalDate.of(2000, Month.NOVEMBER, 15))
                .setEnd(LocalDate.of(2000, Month.NOVEMBER, 10))
                .setId(null)
                .setLocation("")
                .build();
        killTerrorists = new MissionBuilder()
                .setCodeName("killingspree")
                .setDescription("Kill them all")
                .setStart(LocalDate.of(2000, Month.NOVEMBER, 8))
                .setEnd(LocalDate.of(2000, Month.NOVEMBER, 10))
                .setId(null)
                .setLocation("BRNO")
                .build();

        infiltrateSPD = new MissionBuilder()
                .setCodeName("spdfree")
                .setDescription("Get info about spd organization")
                .setStart(LocalDate.of(2015, Month.MARCH, 15))
                .setEnd(null)
                .setId(null)
                .setLocation("PRAHA")
                .build();

    }


    @After
    public void tearDown() {
        try {
            ds.getConnection().prepareStatement("DELETE FROM APP.MISSION").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test of create method, of class MissionManagerImpl.
     */
    @Test
    public void testCreate() {
        Long terroristId = manager.createMission(killTerrorists);
        Long spdId = manager.createMission(infiltrateSPD);
        assertNotNull(terroristId);
        assertNotNull(spdId);
        assertNotEquals(terroristId, spdId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBadMission() {

        long bad = manager.createMission(badMission);

    }

    /**
     * Test of findMissionById method, of class MissionManagerImpl.
     */
    @Test
    public void testFindMissionById() {

        Long spdId = manager.createMission(infiltrateSPD);
        assertEquals(manager.getMission(spdId), infiltrateSPD);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMissionByNullID() {
        Long spdId = manager.createMission(infiltrateSPD);
        manager.getMission(null);
    }


    /**
     * Test of update method, of class MissionManagerImpl.
     */
    @Test
    public void testUpdate() {
        Long terroristId = manager.createMission(killTerrorists);
        Mission mission = manager.getMission(terroristId);
        String newCodeName = "NewName";
        mission.setCodeName(newCodeName);
        manager.updateMission(terroristId, mission);
        assertEquals(manager.getMission(terroristId).getCodeName(), newCodeName);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure() {
        Long terroristId = manager.createMission(killTerrorists);
        Mission mission = manager.getMission(terroristId);
        String newCodeName = "";
        mission.setCodeName(newCodeName);
        manager.updateMission(terroristId, mission);
    }

    /**
     * Test of getUncompletedMissions method, of class MissionManagerImpl.
     */
    @Test
    public void testFindAllUncompletedMissions() {
        manager.createMission(killTerrorists);
        manager.createMission(infiltrateSPD);

        List<Mission> result = manager.getUncompletedMissions();

        assertTrue(result.size() == 1);
        assertFalse(result.contains(killTerrorists));
        assertTrue(result.contains(infiltrateSPD));
    }

    /**
     * Test of getMissions method, of class MissionManagerImpl.
     */
    @Test
    public void testFindAllMission() {
        Long terroristId = manager.createMission(killTerrorists);
        Long spdId = manager.createMission(infiltrateSPD);

        List<Mission> result = manager.getMissions();

        assertThat(result, Matchers.hasSize(2));
        assertTrue(result.contains(killTerrorists));
        assertTrue(result.contains(infiltrateSPD));
        assertTrue(terroristId != spdId);
    }

}
