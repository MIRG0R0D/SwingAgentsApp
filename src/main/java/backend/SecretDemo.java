package backend;


import org.apache.derby.jdbc.ClientDataSource;

import java.time.LocalDate;

public class SecretDemo {

    private static ClientDataSource ds;
    private static SecretManager secretManager;
    private static MissionManager missionManager;
    private static AgentManager agentManager;

    public static void main(String[] args) {

        SecretDemo demo = new SecretDemo(getDataSource());
        //agent_demo();
        //test1();
        //mis_demo();
        //add_some_agents();
        mis_demo();
    
        
    }

    public static ClientDataSource getDataSource(){
        if(ds == null) {
            ds = new ClientDataSource();
            ds.setServerName("localhost");
            ds.setPortNumber(1527);
            ds.setDatabaseName("AgentsDB");
        }
        return ds;
    }
    
    private static void test1(){
        Agent agent = new Agent(null, LocalDate.now(), "00", "James Bond");
        System.out.println(agent.toString());
        agentManager.create(agent);
        System.out.println(agent.toString());
    }
    
    private static void mis_demo(){
        System.out.println("Mission demo \n");
        missionManager.createMission(new Mission(null,"Cold ice",LocalDate.now(),null,"Egypt","Agents, u shulde find some ice for my Coca-cola"));
        missionManager.createMission(new Mission(null,"Hot desert",LocalDate.now(),null,"Syberia","Does anyone have a heat-pack?"));
        missionManager.createMission(new Mission(null,"Exams",LocalDate.now(),null,"University","U really need to work on it"));
        missionManager.createMission(new Mission(null,"Flying potato",LocalDate.now(),null,"Belarus","Get me some potato"));
        missionManager.createMission(new Mission(null,"Dying world",LocalDate.now(),null,"Global","Doom's day soon"));
        
    }
    private static void clear_agents(){
        System.out.println("Delete all agents");
        try{
            
            agentManager.deleteAllAgents();
        }catch (Exception e){ e.printStackTrace(); }
        
    }
    private static void add_some_agents(){
        System.out.println("adding agents");
        try{
            
            agentManager.create(new Agent(Long.MIN_VALUE, LocalDate.now(), "05", "John Freak"));
            agentManager.create(new Agent(Long.MIN_VALUE, LocalDate.now(), "03", "Devis Horse"));
            agentManager.create(new Agent(Long.MIN_VALUE, LocalDate.now(), "25", "Jonny English"));
            agentManager.create(new Agent(Long.MIN_VALUE, LocalDate.now(), "00", "James Bond"));
            agentManager.create(new Agent(Long.MIN_VALUE, LocalDate.now(), "15", "Tom Ford"));
            agentManager.create(new Agent(Long.MIN_VALUE, LocalDate.now(), "13", "Chuck Norris"));
        }catch (Exception e){ e.printStackTrace(); }
        
    }
    private static void agent_demo(){
        System.out.println("Agent demo \n");
        System.out.println("adding agents");
        Long jamesID=null, tomID = null, chuckID=null;
        try{
            jamesID=agentManager.create(new Agent(Long.MIN_VALUE, LocalDate.now(), "00", "James Bond"));
            tomID=agentManager.create(new Agent(Long.MIN_VALUE, LocalDate.now(), "15", "Tom Ford"));
            chuckID=agentManager.create(new Agent(Long.MIN_VALUE, LocalDate.now(), "15", "Chuck Norris"));
        }catch (Exception e){ e.printStackTrace(); }
        
        System.out.println("Find all agents");
        try{
            for (Agent ag : agentManager.findAllAgents())
                System.out.println(ag.toString());
        }catch (Exception e){ e.printStackTrace(); }
        
        System.out.println("Finding  agents by id");
        try{
            System.out.println(agentManager.findAgentById(tomID).toString());
        }catch (Exception e){ e.printStackTrace(); }
        
        System.out.println("Updating");
        try{
            System.out.println(agentManager.findAgentById(jamesID).toString());
            agentManager.update(jamesID, new Agent(Long.MIN_VALUE, LocalDate.now(), "00", "Bond, James Bond"));
            System.out.println(agentManager.findAgentById(jamesID).toString());
        }catch (Exception e){ e.printStackTrace(); }
        
        System.out.println("Delete agents");
        try{
            
            agentManager.deleteAgentById(jamesID);
            System.out.println("delete James - OK");
        }catch (Exception e){ e.printStackTrace(); }
        
        System.out.println("Find all agents");
        try{
            for (Agent ag : agentManager.findAllAgents())
                System.out.println(ag.toString());
        }catch (Exception e){ e.printStackTrace(); }
        
        System.out.println("Delete all agents");
        try{
            
            agentManager.deleteAllAgents();
        }catch (Exception e){ e.printStackTrace(); }
        
        System.out.println("Find all agents");
        try{
            for (Agent ag : agentManager.findAllAgents())
                System.out.println(ag.toString());
        }catch (Exception e){ e.printStackTrace(); }
    }

    private SecretDemo(ClientDataSource ds) {
        this.ds = ds;
        secretManager = new SecretManagerImpl(ds);
        agentManager = new AgentManagerImpl(ds);
        missionManager = new MissionManagerImpl(ds);
    }






}
