package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Player;
import model.Inventory;
import model.pet.Dog;
import model.item.Food;
import model.item.Toy;

public class PlayerTest {
    
    private Player player;
    private Dog dog;
    
    @BeforeEach
    public void setUp() {
        dog = new Dog("Rex");
        player = new Player(dog);
    }
    
    @Test
    public void testPlayerInitialization() {
        assertEquals(dog, player.getPet());
        assertNotNull(player.getInventory());
    }
    
    @Test
    public void testFeedPet() {
        Food kibble = new Food("Kibble", "");
        player.setInventory(new Inventory());
        player.getInventory().addItem(kibble);
        dog.setHunger(80);

        int initialHunger = 80;
        player.feedPet(kibble);
        
        // Check hunger increased
        assertTrue(dog.getHunger() > initialHunger);
        
        //Check item was removed from inventory
        assertFalse(player.getInventory().contains(kibble));
    }
    
    @Test
    public void testGiftToPet() {
        Toy ball = new Toy("Ball", "");
        player.setInventory(new Inventory());
        player.getInventory().addItem(ball);
        dog.setHappiness(80);
        
        int initialHappiness = 80;
        player.giftToPet(ball);
        
        // Check happiness increased
        assertTrue(dog.getHappiness() > initialHappiness);
        
        // Check item was removed from inventory
        assertFalse(player.getInventory().contains(ball));
    }
    
    @Test
    public void testSendToBed() {
        player.sendToBed();
        assertEquals(model.pet.Pet.PetState.SLEEPING, dog.getCurrentState());
    }
    
    @Test
    public void testPlayWithPet() {
        dog.setHappiness(80);
        int initialHappiness = 80;

        player.playWithPet();
        assertTrue(dog.getHappiness() > initialHappiness);
    }
    
    @Test
    public void testTakeToVet() {
        dog.setHealth(50);
        player.takeToVet();
        assertTrue(dog.getHealth() > 50);
    }
    
    @Test
    public void testPetExercise() {
        dog.setHealth(50);
        player.petExercise();
        assertTrue(dog.getHealth() > 50);
    }
}