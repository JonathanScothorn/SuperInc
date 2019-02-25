package application;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Where(clause="is_deleted=false")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String MissionName;
    private Boolean IsCompleted;

    @Column(name="is_deleted")
    private Boolean IsDeleted;

    @ManyToMany(mappedBy = "Missions")
    private List<SuperHero> Heroes;

    public Mission() {
        this("");
    }

    public Mission(String name) {
        MissionName = name;
        IsCompleted = false;
        IsDeleted = false;
        Heroes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getMissionName() {
        return MissionName;
    }

    public void setMissionName(String missionName) {
        MissionName = missionName;
    }

    public Boolean getDeleted() {
        return IsDeleted;
    }

    public void setDeleted(Boolean deleted) {
        IsDeleted = deleted;
    }

    public Boolean getCompleted() {
        return IsCompleted;
    }

    public void setCompleted(Boolean completed) {
        IsCompleted = completed;
    }

    public int heroSize() {
        return Heroes.size();
    }

    public boolean hasHero(SuperHero hero) {
        return Heroes.contains(hero);
    }

    public void addHero(SuperHero hero) {
        Heroes.add(hero);
    }

    public void removeHero(SuperHero hero) {
        Heroes.remove(hero);
    }

    @Override
    public String toString() {
        return String.format("Mission[id=%d, MissionName='%s', IsCompleted=%b, IsDeleted=%b]",
                id, MissionName, IsCompleted, IsDeleted);
    }

}
