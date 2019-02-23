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

    public SuperHero(String firstName, String lastName, String superHeroName) {
        Firstname = firstName;
        Lastname = lastName;
        Superheroname = superHeroName;
        Missions = new ArrayList<Mission>();

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

    @Override
    public String toString() {
        return String.format("SuperHero[id=%d, Firstname='%s', Lastname='%s', Superheroname='%s']",
                id, Firstname, Lastname, Superheroname);
    }

}
