package application;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SuperHero {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Firstname;
    private String Lastname;
    private String Superheroname;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable
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
