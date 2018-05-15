package backend;

import java.time.LocalDate;
import java.util.Objects;


public class Mission {

    public static final int COLUMNS_COUNT = 6;
    private Long id;
    private String codeName;
    private LocalDate start;
    private LocalDate end;
    private String location;
    private String description;

    public Mission(Long id, String codeName, LocalDate start, LocalDate end, String location, String description) {
        this.id = id;
        this.codeName = codeName;
        this.start = start;
        this.end = end;
        this.location = location;
        this.description = description;
    }

    public Mission() {
        this.id=0l;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Mission{" + "id=" + id + ", codeName=" + codeName + ", start=" 
                + start + ", end=" + end + ", location=" + location 
                + ", description=" + description + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
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
        final Mission other = (Mission) obj;
        if (!Objects.equals(this.codeName, other.codeName)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        if (!Objects.equals(this.end, other.end)) {
            return false;
        }
        return true;
    }

    
    
               
}
