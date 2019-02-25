package application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MissionTest {

    private Mission mission;
    private String name = "Design new suit";

    @Before
    public void setUp() {
        mission = new Mission(name);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getMissionNameTest() {
        assertEquals(name, mission.getMissionName());
    }

    @Test
    public void setMissionNameTest() {
        String rename = "Upgrade reactor";
        mission.setMissionName(rename);
        assertEquals(rename, mission.getMissionName());
    }

    @Test
    public void getDeletedTest() {
        assertEquals(false, mission.getDeleted());
    }

    @Test
    public void setDeletedTest() {
        mission.setDeleted(true);
        assertEquals(true, mission.getDeleted());
    }

    @Test
    public void getCompletedTest() {
        assertEquals(false, mission.getCompleted());
    }

    @Test
    public void setCompletedTest() {
        mission.setCompleted(true);
        assertEquals(true, mission.getCompleted());
    }

    @Test
    public void heroSizeTest() {
        assertEquals(0, mission.heroSize());
    }

    @Test
    public void addHeroTest() {
        mission.addHero(new SuperHero());
        assertEquals(1, mission.heroSize());
    }

    @Test
    public void hasHeroTest() {
        SuperHero hero = new SuperHero();
        mission.addHero(hero);
        assertTrue(mission.hasHero(hero));
    }

    @Test
    public void removeHeroTest() {
        SuperHero hero = new SuperHero();
        mission.addHero(hero);
        mission.removeHero(hero);
        assertEquals(0, mission.heroSize());
    }

    @Test
    public void toStringTest() {
        String expected = "Mission[id=null, MissionName='"+name+"', IsCompleted=false, IsDeleted=false]";
    }
}