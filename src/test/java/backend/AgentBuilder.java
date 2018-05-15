/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.time.LocalDate;

/**
 *
 * @author Dima
 */
public class AgentBuilder {
    private Long id;
    private LocalDate born;
    private String level;
    private String name;
    
    public AgentBuilder id(Long id) {
        this.id = id;
        return this;
    }
    
    public AgentBuilder born(LocalDate born) {
        this.born = born;
        return this;
    }
    
    public AgentBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    public AgentBuilder level(String level) {
        this.level = level;
        return this;
    }
    
    public Agent build(){
        return new Agent(id, born, level, name);
    }
    
}
