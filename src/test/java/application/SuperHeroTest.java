package application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SuperHeroTest {

    SuperHero hero;
    String firstName = "Tony";
    String lastName = "Stark";
    String heroName = "Iron Man";

    @Before
    public void setUp() throws Exception {
        hero = new SuperHero(firstName, lastName, heroName);
    }

    @After
    public void tearDown() throws Exception {
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
    public void toStringTest() {
        String expected = "SuperHero[id=null, Firstname='"+firstName+"', Lastname='"+lastName+"', Superheroname='"+heroName+"']";
        assertEquals(expected, hero.toString());
    }
}