package application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SuperHeroTest {

    private SuperHero hero;
    private String firstName = "Tony";
    private String lastName = "Stark";
    private String heroName = "Iron Man";

    @Before
    public void setUp() {
        hero = new SuperHero(firstName, lastName, heroName);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getSuperheronameTest() {
        assertEquals(heroName, hero.getSuperheroname());
    }

    @Test
    public void setSuperheronameTest() {
        String rename = "Iron Knight";
        hero.setSuperheroname(rename);
        assertEquals(rename, hero.getSuperheroname());
    }

    @Test
    public void getLastnameTest() {
        assertEquals(lastName, hero.getLastname());
    }

    @Test
    public void setLastnameTest() {
        String rename = "Rhodes";
        hero.setLastname(rename);
        assertEquals(rename, hero.getLastname());
    }

    @Test
    public void getFirstnameTest() {
        assertEquals(firstName, hero.getFirstname());
    }

    @Test
    public void setFirstnameTest() {
        String rename = "James";
        hero.setFirstname(rename);
        assertEquals(rename, hero.getFirstname());
    }

    @Test
    public void missionSizeTest() {
        assertEquals(0, hero.missionSize());
    }

    @Test
    public void addMissionTest() {
        hero.addMission(new Mission());
        assertEquals(1, hero.missionSize());
    }

    @Test
    public void hasMissionTest() {
        Mission mission = new Mission();
        hero.addMission(mission);
        assertTrue(hero.hasMission(mission));
    }

    @Test
    public void removeMissionTest() {
        Mission mission = new Mission();
        hero.addMission(mission);
        hero.removeMission(mission);
        assertEquals(0, hero.missionSize());
    }

    @Test
    public void getMissionsTest() {
        Mission m1 = new Mission();
        Mission m2 = new Mission();
        hero.addMission(m1);
        hero.addMission(m2);
        assertTrue(hero.getMissions().contains(m1) && hero.getMissions().contains(m2));
    }

    @Test
    public void toStringTest() {
        String expected = "SuperHero[id=null, Firstname='"+firstName+"', Lastname='"+lastName+"', Superheroname='"+heroName+"']";
        assertEquals(expected, hero.toString());
    }
}