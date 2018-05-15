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

//import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

/**
 * @author Dima
 */
public class AgentManagerImplTest {

    private AgentManagerImpl manager;
    private Agent jamesBond;
    private Agent vonStierlitz;
    private Agent badAgent;
    private ClientDataSource ds;

    @Before
    public void setUp() {

        ds = new ClientDataSource();
        ds.setUser("aa");
        ds.setServerName("localhost");
        ds.setDatabaseName("myDB");
        ds.setPortNumber(1527);


        manager = new AgentManagerImpl(ds);


        jamesBond = new AgentBuilder()
                .name("Bond, James")
                .born(LocalDate.of(1964, Month.JANUARY, 1))
                .level("00")
                .id(null)
                .build();
        badAgent = new Agent(null, null, null, null);

        vonStierlitz = new AgentBuilder()
                .name("Max Otto vonStierlitz")
                .born(LocalDate.of(1900, Month.OCTOBER, 8))
                .level("00")
                .id(null)
                .build();

        badAgent = new AgentBuilder()
                .name("")
                .born(LocalDate.of(2050, Month.MARCH, 5))
                .level("")
                .id(null)
                .build();

    }

    @After
    public void tearDown() {
        try {
            ds.getConnection().prepareStatement("DELETE FROM APP.AGENT").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test of create method, of class AgentManagerImpl.
     */
    @Test
    public void testCreate() {
        Long bondId = manager.create(jamesBond);
        Long stierId = manager.create(vonStierlitz);

        assertNotNull(bondId);
        assertNotNull(stierId);

        assertNotEquals(bondId, stierId);

    }
/*
    @Test
    public void createAgentWithNullName() {
        Agent bad = new AgentBuilder().name(null).build();
        assertThatThrownBy(() -> manager.create(bad))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createAgentWithNullLevel() {
        Agent bad = new AgentBuilder().level(null).build();
        assertThatThrownBy(() -> manager.create(bad))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // Test exception using AssertJ assertThatThrownBy() method
    // this requires Java 8 due to using lambda expression
    @Test
    public void createBadAgent() {
        Agent bad = badAgent;

        assertThatThrownBy(() -> manager.create(bad))
                .isInstanceOf(IllegalArgumentException.class);
    }
*/
    /**
     * Test of findAgentById method, of class AgentManagerImpl.
     */
    @Test
    public void testFindAgentById() {
        Agent bond = jamesBond;
        Long id = manager.create(jamesBond);
        assertEquals(manager.findAgentById(id), jamesBond);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAgentByBadID() {
        long bad = manager.create(badAgent);

    }

    /**
     * Test of update method, of class AgentManagerImpl.
     */
    @Test
    public void testUpdate() {
        Long id = manager.create(vonStierlitz);
        String newName = "Vsevolod";
        vonStierlitz.setName(newName);
        manager.update(id, vonStierlitz);

        //change here
        assertEquals(manager.findAgentById(id).getName(), newName);
        assertEquals(manager.findAgentById(id), vonStierlitz);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFailure() {
        Agent stierlitz = vonStierlitz;
        Long id = manager.create(vonStierlitz);
        String newName = "";
        stierlitz.setName(newName);
        manager.update(id, stierlitz);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullBody() {
        Long id = manager.create(vonStierlitz);
        manager.update(id, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullId() {

        Long id = manager.create(vonStierlitz);
        manager.update(null, vonStierlitz);
    }
/*
    @Test
    public void testBadUpdates() {
        Agent stierlitz = vonStierlitz;
        Long id = manager.create(stierlitz);

        stierlitz.setName("");
        stierlitz.setLevel(null);

        assertThatThrownBy(() -> manager.update(id, stierlitz))
                .isInstanceOf(IllegalArgumentException.class);
    }
*/
    /**
     * Test of findAllAgents method, of class AgentManagerImpl.
     */
    @Test
    public void testFindAllAgents() {
        Agent bond = jamesBond;
        Agent stierlitz = vonStierlitz;

        Long bondID = manager.create(bond);
        Long stierId = manager.create(stierlitz);

        List<Agent> result = manager.findAllAgents();

        assertEquals(2, result.size());
        assertTrue(result.contains(stierlitz));
        assertTrue(result.contains(bond));
        assertNotEquals(bondID, stierId);
    }

}
