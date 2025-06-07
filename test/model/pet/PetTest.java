package model.pet;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.pet.Pet;
import model.pet.Pet.PetState;
import model.pet.Dog;
import model.pet.Fish;
import model.pet.Monkey;
import model.pet.Mouse;
import model.item.Food;
import model.item.Toy;

public class PetTest {
    
    private Dog dog;
    private Fish fish;
    private Monkey monkey;
    private Mouse mouse;
    
    @BeforeEach
    public void setUp() {
        dog = new Dog("Rex");
        fish = new Fish("Bubbles");
        monkey = new Monkey("George");
        mouse = new Mouse("Mickey");
    }
    
    @Test
    public void testPetInitialization() {
        assertEquals("Rex", dog.getName());
        assertEquals(100, dog.getHealth());
        assertEquals(100, dog.getSleep());
        assertEquals(100, dog.getHunger());
        assertEquals(100, dog.getHappiness());
        assertEquals(0, dog.getSpaceReadiness());
        assertEquals(PetState.NORMAL, dog.getCurrentState());
    }
    
    @Test
    public void testPetStateChanges() {
        // Test health state change
        dog.setHealth(0);
        assertEquals(PetState.DEAD, dog.getCurrentState());
        
        // Reset for next test
        dog = new Dog("Rex");
        
        // Test sleep state change
        dog.setSleep(0);
        assertEquals(PetState.SLEEPING, dog.getCurrentState());
        
        // Test that full sleep returns to normal
        dog.setSleep(100);
        dog.updateState();
        assertEquals(PetState.NORMAL, dog.getCurrentState());
        
        // Test happiness state change
        dog.setHappiness(0);
        assertEquals(PetState.ANGRY, dog.getCurrentState());
        
        // Test hunger state change
        dog = new Dog("Rex"); // Reset
        dog.setHunger(0);
        assertEquals(PetState.HUNGRY, dog.getCurrentState());
    }
    
    @Test
    public void testPetActions() {
        // Test eat
        Food kibble = new Food("Kibble", "");
        int initialHunger = dog.getHunger();
        dog.setHunger(50); // Set to middle value
        dog.eat(kibble);
        assertTrue(dog.getHunger() > 50); // Should increase
        
        // Test play
        dog.setHappiness(50);
        dog.play();
        assertTrue(dog.getHappiness() > 50);
        
        // Test gift
        Toy ball = new Toy("Ball", "");
        dog.setHappiness(50);
        dog.gift(ball);
        assertTrue(dog.getHappiness() > 50);
        
        // Test exercise
        dog.setHealth(50);
        dog.exercise();
        assertTrue(dog.getHealth() > 50);
        
        // Test vet
        dog.setHealth(20);
        dog.vet();
        assertTrue(dog.getHealth() > 20);
    }
    
    @Test
    public void testProgressBarUpdate() {
        dog.setHunger(100);
        dog.setHappiness(100);
        dog.setSleep(100);
        dog.updateProgressBars();
        
        // Values should decrease
        assertTrue(dog.getHunger() < 100);
        assertTrue(dog.getHappiness() < 100);
        assertTrue(dog.getSleep() < 100);
    }
    
    @Test
    public void testSpaceMission() {
        // Pet must be space ready
        dog.setSpaceReadiness(100);
        
        // Record initial stats
        int initialHealth = dog.getHealth();
        int initialHunger = dog.getHunger();
        int initialHappiness = dog.getHappiness();
        int initialSleep = dog.getSleep();
        
        boolean result = dog.goOnSpaceMission();
        assertTrue(result);
        
        // Space readiness should reset
        assertEquals(0, dog.getSpaceReadiness());
        
        // Stats should be reduced
        assertTrue(dog.getHealth() < initialHealth);
        assertTrue(dog.getHunger() < initialHunger);
        assertTrue(dog.getHappiness() < initialHappiness);
        assertTrue(dog.getSleep() < initialSleep);
        
        // Test mission count
        assertEquals(1, dog.getTotalMission());
    }
}