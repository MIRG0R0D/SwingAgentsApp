package backend;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AgentManagerImpl implements AgentManager {

    private final DataSource ds;

    public AgentManagerImpl(DataSource ds) {
        this.ds = ds;
    }

    private void check(Agent agent){
        if(agent == null){
            throw new IllegalArgumentException();
        }

        if(agent.getName() == null || agent.getName().isEmpty() ||
                agent.getLevel() == null || agent.getLevel().isEmpty() ||
                agent.getBorn() == null ){
            throw new IllegalArgumentException();
        }



    }

    /**
     * creating new agent
     *
     * @param agent parameters of new agent
     * @return id of the created object
     */
    @Override
    public Long create(Agent agent) {

        check(agent);

        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("insert into APP.AGENT(BORN, LEVEL, NAME) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(agent.getBorn()));
            ps.setString(2, agent.getLevel());
            ps.setString(3, agent.getName());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                long key = keys.getLong(1);
                agent.setId(key);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, "Error executing insert: ", ex);
        } 
        return agent.getId();
    }

    /**
     * finding some certain agent by it's id
     *
     * @param id ID of the agent
     * @return Agent.class if found, null if not
     */
    @Override
    public Agent findAgentById(Long id) {

        if(id == null || id < 1){
            throw new IllegalArgumentException();
        }

        List<Agent> agents = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select * from APP.AGENT WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate bord = rs.getDate(2).toLocalDate();
                String level = rs.getString(3);
                String name = rs.getString(4);
                Agent agent = new Agent(id, bord, level, name);
                return agent;
            }

        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * updating agent info
     * ID and born date will not be changed
     *
     * @param id    id of the agent
     * @param agent new Agent info
     */
    @Override
    public void update(Long id, Agent agent) {

        if(id == null || id < 1){
            throw new IllegalArgumentException();
        }

        check(agent);

        try (Connection con = ds.getConnection()){
            
            PreparedStatement ps = con.prepareStatement("UPDATE APP.AGENT " +
                    "SET LEVEL = ?, NAME = ? " +
                    "WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, agent.getLevel());
            ps.setString(2, agent.getName());
            ps.setLong(3, id);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                long key = keys.getLong(1);
                agent.setId(key);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, "Error executing update: ", ex);
        } 

    }



    /**
     * finding all existing agents
     *
     * @return full list of agents
     */
    @Override
    public List<Agent> findAllAgents() {
        List<Agent> agents = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM APP.AGENT");
            while (rs.next()) {
                Long id = rs.getLong(1);
                LocalDate bord = rs.getDate(2).toLocalDate();
                String level = rs.getString(3);
                String name = rs.getString(4);
                Agent agent = new Agent(id, bord, level, name);
                agents.add(agent);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return agents;
    }

    @Override
    public void deleteAgentById(Long id) {
        if(id == null || id < 1){
            throw new IllegalArgumentException();
        }

        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE from APP.AGENT WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, id);
            ps.executeUpdate();


        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteAllAgents() {
       
                try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE from APP.AGENT", Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();


        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
