package swing;

import backend.AgentManager;
import backend.AgentManagerImpl;
import backend.MissionManager;
import backend.MissionManagerImpl;
import backend.SecretManager;
import backend.SecretManagerImpl;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.derby.jdbc.ClientDataSource;

/**
 * Created by Dima on 14.05.2018.
 */
public class Index {
    private static Properties props;
    private static Index instance; //singleton
    
    private static ClientDataSource ds;
    private static SecretManager secretManager;
    private static MissionManager missionManager;
    private static AgentManager agentManager;

    public static final String ENG_PROPERTIES = "src/main/resources/Eng_lang.properties";

    private Index(){}
    
    public static synchronized Index getInstance(){
        if (instance == null) {
            instance = new Index();
            
            try{
                ds = getDataSource();
            }catch (Exception e){
                System.out.println("DB problems");
                e.printStackTrace();
                return null;
            }
 
            secretManager = new SecretManagerImpl(ds);
            agentManager = new AgentManagerImpl(ds);
            missionManager = new MissionManagerImpl(ds);
           
            try (FileInputStream fis = new FileInputStream(ENG_PROPERTIES)) {
                props = new Properties();
                props.load(fis);
            } catch (IOException e) {
                System.out.println("props problems");
                e.printStackTrace();
                return null;
            }
        }
        return instance;
        
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
    

    public static Properties getProps() {
        return props;
    }

    public static SecretManager getSecretManager() {
        return secretManager;
    }

    public static MissionManager getMissionManager() {
        return missionManager;
    }

    public static AgentManager getAgentManager() {
        return agentManager;
    }
    
    
    public static void main(String[] args) {


        JFrame grid = new MainPage(getInstance());
        //JFrame mis = new MissionEdit(props,0);

    }
}
