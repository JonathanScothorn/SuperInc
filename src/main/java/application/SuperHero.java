package application;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@ApiModel(description = "Superhero data")
public class SuperHero {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "Automatically generated Superhero ID")
    private Long id;

    @ApiModelProperty(notes = "Hero's first name")
    private String Firstname;

    @ApiModelProperty(notes = "Hero's last name")
    private String Lastname;

    @ApiModelProperty(notes = "Hero's secret (or not so secret) identity")
    private String Superheroname;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable
    @ApiModelProperty(notes = "List of missions the hero is on")
    private List<Mission> Missions;

    public SuperHero() {
        this("", "", "");
    }

    public SuperHero(String firstName, String lastName, String superHeroName) {
        Firstname = firstName;
        Lastname = lastName;
        Superheroname = superHeroName;
        Missions = new ArrayList<>();

    }

    public Long getId() {
        return id;
    }

    public String getSuperheroname() {
        return Superheroname;
    }

    public void setSuperheroname(String superheroname) {
        Superheroname = superheroname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public int missionSize() {
        return Missions.size();
    }

    public boolean hasMission(Mission mission) {
        return Missions.contains(mission);
    }

    public void addMission(Mission mission) {
        Missions.add(mission);
    }

    public void removeMission(Mission mission) {
        Missions.remove(mission);
    }

    public List<Mission> getMissions() {
        return Missions;
    }

    @Override
    public String toString() {
        return String.format("SuperHero[id=%d, Firstname='%s', Lastname='%s', Superheroname='%s']",
                id, Firstname, Lastname, Superheroname);
    }

}
