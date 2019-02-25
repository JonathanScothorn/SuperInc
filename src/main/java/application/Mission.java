package application;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Where(clause="is_deleted=false")
@ApiModel(description = "Mission data")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "Automatically generated Mission ID")
    private Long id;

    @ApiModelProperty(notes = "Mission's Name")
    private String MissionName;

    @ApiModelProperty(notes = "Boolean determining if the mission is active")
    private Boolean IsCompleted;

    @Column(name="is_deleted")
    @ApiModelProperty(notes = "Boolean determining if the mission has been soft deleted")
    private Boolean IsDeleted;

    @ManyToMany(mappedBy = "Missions")
    @ApiModelProperty(notes = "List of heroes who are on the mission")
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
