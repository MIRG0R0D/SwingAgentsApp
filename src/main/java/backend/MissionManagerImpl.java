package backend;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MissionManagerImpl implements MissionManager {


    private DataSource ds = null;

    public MissionManagerImpl(DataSource ds) {

        this.ds = ds;
    }

    private void check(Mission mis) {
        if (mis == null) {
            throw new IllegalArgumentException();
        }

        if (mis.getCodeName() == null || mis.getCodeName().isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (mis.getStart() == null) {
            throw new IllegalArgumentException();
        }


    }

    /**
     * create new mission
     *
     * @param mis params of new mission
     * @return id of new mission
     */
    @Override
    public Long createMission(Mission mis) {

        check(mis);

        Connection con = null;
        try {
            con = ds.getConnection();
            if(mis.getId() == null) {
                PreparedStatement ps = con.prepareStatement("insert into APP.MISSION(CODENAME, START, \"END\", DESCRIPTION, LOCATION) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, mis.getCodeName());
                ps.setDate(2, Date.valueOf(mis.getStart()));
                ps.setDate(3, (mis.getEnd() == null) ? null : Date.valueOf(mis.getEnd()));
                ps.setString(4, mis.getDescription());
                ps.setString(5, mis.getLocation());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    long key = keys.getLong(1);
                    mis.setId(key);
                }
            }else{
                PreparedStatement ps = con.prepareStatement("insert into APP.MISSION(ID ,CODENAME, START, \"END\", DESCRIPTION, LOCATION) values (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, mis.getId());
                ps.setString(2, mis.getCodeName());
                ps.setDate(3, Date.valueOf(mis.getStart()));
                ps.setDate(4, (mis.getEnd() == null) ? null : Date.valueOf(mis.getEnd()));
                ps.setString(5, mis.getDescription());
                ps.setString(6, mis.getLocation());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    long key = keys.getLong(1);
                    mis.setId(key);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MissionManagerImpl.class.getName()).log(Level.SEVERE, "Error executing insert: ", ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, "Error closing connection: ", ex);
                }
            }
        }


        return mis.getId();
    }


    /**
     * update existing mission
     *
     * @param id  id of the mission
     * @param mis new params
     */
    @Override
    public void updateMission(Long id, Mission mis) {

        if (id == null || id < 1) {
            throw new IllegalArgumentException();
        }

        check(mis);

        Connection con = null;
        try {
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE APP.MISSION\n" +
                    "SET CODENAME=?, START=?, \"END\"=?, DESCRIPTION=?, LOCATION=?\n" +
                    "WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, mis.getCodeName());
            ps.setDate(2, Date.valueOf(mis.getStart()));
            ps.setDate(3, Date.valueOf(mis.getEnd()));
            ps.setString(4, mis.getDescription());
            ps.setString(5, mis.getLocation());
            ps.setLong(6, id);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                long key = keys.getLong(1);
                mis.setId(key);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MissionManagerImpl.class.getName()).log(Level.SEVERE, "Error executing update: ", ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MissionManagerImpl.class.getName()).log(Level.SEVERE, "Error closing connection: ", ex);
                }
            }
        }


    }

    /**
     * get all missions
     *
     * @return list of missions
     */
    @Override
    public List<Mission> getMissions() {
        List<Mission> mis = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from APP.MISSION");
            while (rs.next()) {
                Long id = rs.getLong(1);
                String codeName = rs.getString(5);
                LocalDate start = rs.getDate(2).toLocalDate();
                LocalDate end = null;
                if (rs.getDate(3) != null)
                    end = rs.getDate(3).toLocalDate();
                String description = rs.getString(4);
                String location = rs.getString(6);
                Mission current = new Mission(id, codeName, start, end, location, description);
                mis.add(current);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mis;
    }

    /**
     * get all uncompleted missions
     *
     * @return list of missions
     */
    @Override
    public List<Mission> getUncompletedMissions() {
        List<Mission> mis = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from APP.MISSION WHERE \"END\" IS NULL");
            while (rs.next()) {
                Long id = rs.getLong(1);
                String codeName = rs.getString(5);
                LocalDate start = rs.getDate(2).toLocalDate();
                LocalDate end = null;
                if (rs.getDate(3) != null)
                    end = rs.getDate(3).toLocalDate();
                String description = rs.getString(4);
                String location = rs.getString(6);
                Mission current = new Mission(id, codeName, start, end, location, description);
                mis.add(current);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mis;
    }

    /**
     * get certain mission by it's id
     *
     * @param id id of the mission
     * @return mission with the same id
     */
    @Override
    public Mission getMission(Long id) {

        if (id == null || id < 1) {
            throw new IllegalArgumentException();
        }


        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select * from APP.MISSION WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                id = rs.getLong(1);
                String codeName = rs.getString(5);
                LocalDate start = rs.getDate(2).toLocalDate();
                LocalDate end = null;
                if (rs.getDate(3) != null)
                    end = rs.getDate(3).toLocalDate();
                String description = rs.getString(4);
                String location = rs.getString(6);
                Mission mis = new Mission(id, codeName, start, end, location, description);
                return mis;
            }

        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void deleteMission(Long id) {

        if (id == null || id < 1) {
            throw new IllegalArgumentException();
        }

        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE from APP.MISSION WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, id);
            ps.executeUpdate();


        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteAllMissions() {
        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE from APP.MISSION", Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();


        } catch (SQLException ex) {
            Logger.getLogger(AgentManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


