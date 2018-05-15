/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author xdovgan
 */
public class MissionAssignment {
    private Long id; //is a foreign key to mission
    private List <Agent> agents;

    public MissionAssignment(Long id, List<Agent> agents) {
        this.id = id;
        this.agents = agents;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public Long getId() {
        return id;
    }


    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    @Override
    public String toString() {
        return "MissionAssignment{" + "id=" + id + ", agents=" + agents + '}';
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MissionAssignment other = (MissionAssignment) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    
}
