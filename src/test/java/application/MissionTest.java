package application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MissionTest {

    Mission mission;
    String name = "Design new suit";

    @Before
    public void setUp() throws Exception {
        mission = new Mission(name);
    }

    @After
    public void tearDown() throws Exception {
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
    public void toStringTest() {
        String expected = "Mission[id=null, MissionName='"+name+"', IsCompleted=false, IsDeleted=false]";
    }
}