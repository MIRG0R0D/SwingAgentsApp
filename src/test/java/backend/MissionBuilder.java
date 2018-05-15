/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.time.LocalDate;

/**
 *
 * @author Pavel
 */
public class MissionBuilder {

    private Long id;
    private String codeName;
    private LocalDate start;
    private LocalDate end;
    private String location;
    private String description;

    public MissionBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public MissionBuilder setCodeName(String codeName) {
        this.codeName = codeName;
        return this;
    }

    public MissionBuilder setStart(LocalDate start) {
        this.start = start;
        return this;
    }

    public MissionBuilder setEnd(LocalDate end) {
        this.end = end;
        return this;
    }

    public MissionBuilder setLocation(String location) {
        this.location = location;
        return this;
    }

    public MissionBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public Mission build(){
        return new Mission(id, codeName, start, end, location, description);
    }
    
}
