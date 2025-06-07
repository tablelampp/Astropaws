package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import model.Game;
import model.Player;
import model.pet.Pet;
import model.pet.Dog;
import model.pet.Pet.PetState;
import model.item.Food;
import model.item.Toy;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.List;

public class GameTest {
    
    private Game game;
    
    @TempDir
    static Path tempDir;
    
    @BeforeEach
    public void setUp() {
        // For tests, we'll create a temporary directory for save files
        File dataDir = new File(tempDir.toString(), "data");
        File petsDir = new File(dataDir, "pets");
        dataDir.mkdir();
        petsDir.mkdir();
        
        // Set the path to our temp directory for testing
        System.setProperty("user.dir", tempDir.toString());
        
        game = new Game();
    }
    
    @AfterEach
    public void tearDown() {
        // Stop the game timer
        game.stopGame();
    }
    
    @Test
    public void testCreateNewPet() {
        boolean result = game.createNewPet("Rex", "dog");
        assertTrue(result);
        
        Player player = game.getPlayer();
        assertNotNull(player.getPet());
        assertEquals("Rex", player.getPet().getName());
        assertTrue(player.getPet() instanceof Dog);
    }
    
    @Test
    public void testStartAndStopGame() {
        game.createNewPet("Rex", "dog");
        
        // Start game
        game.startGame();
        
        // Let it run for a short time
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }
        
        // Stop game
        game.stopGame();
        
        // Game state should be saved
        List<Integer> petIDs = game.getExistingPetIDs();
        assertFalse(petIDs.isEmpty());
    }
    
    @Test
    public void testSaveAndLoadPet() {
        // Create and save a pet
        game.createNewPet("Rex", "dog");
        int petID = game.getPlayer().getPet().getID();
        
        // Modify the pet's stats to verify loading works
        game.getPlayer().getPet().setHealth(75);
        game.getPlayer().getPet().setHunger(80);
        game.savePet();

        // Turn off parental controls
        game.updateParentalControls(false, 0, 0, null);
        
        // Clear current pet
        game.clearCurrentPet();
        assertNull(game.getPlayer().getPet());
        
        // Load the pet back
        boolean loadResult = game.loadPet(petID);
        assertTrue(loadResult);
        
        Pet loadedPet = game.getPlayer().getPet();
        assertNotNull(loadedPet);
        assertEquals("Rex", loadedPet.getName());
        assertEquals(75, loadedPet.getHealth());
        assertEquals(80, loadedPet.getHunger());
    }
    
    @Test
    public void testGetAvailablePets() {
        // Create multiple pets
        game.createNewPet("Rex", "dog");
        int dog1ID = game.getPlayer().getPet().getID();
        
        game.createNewPet("Bubbles", "fish");
        int fish1ID = game.getPlayer().getPet().getID();
        
        // Get available pets
        Map<Integer, String> availablePets = game.getAvailablePets();
        
        assertTrue(availablePets.containsKey(dog1ID));
        assertTrue(availablePets.containsKey(fish1ID));
        assertEquals("Rex", availablePets.get(dog1ID));
        assertEquals("Bubbles", availablePets.get(fish1ID));
    }
    
    @Test
    public void testGetAlivePets() {
        // Create multiple pets
        game.createNewPet("Rex", "dog");
        int dog1ID = game.getPlayer().getPet().getID();
        
        game.createNewPet("Bubbles", "fish");
        int fish1ID = game.getPlayer().getPet().getID();
        
        // Kill one pet
        game.getPlayer().getPet().setHealth(0);
        game.getPlayer().getPet().updateState();
        assertEquals(PetState.DEAD, game.getPlayer().getPet().getCurrentState());
        game.savePet();
        
        // Get alive pets
        Map<Integer, String> alivePets = game.getAlivePets();
        
        // Only the dog should be alive
        assertTrue(alivePets.containsKey(dog1ID));
        assertFalse(alivePets.containsKey(fish1ID));
    }
    
@Test
public void testRevivePet() {
    // Create a pet first
    game.createNewPet("Rex", "dog");
    int petID = game.getPlayer().getPet().getID();

    // Disable parental controls
    game.updateParentalControls(false, 0, 0, null);
    
    // Kill the pet
    game.getPlayer().getPet().setHealth(0);
    game.getPlayer().getPet().updateState();
    assertEquals(PetState.DEAD, game.getPlayer().getPet().getCurrentState());
    game.savePet();
    
    // Revive the pet
    boolean result = game.revivePet(petID);
    // assertTrue(result);
    
    // Check the pet's stats
    Pet pet = game.getPlayer().getPet();
    assertEquals(100, pet.getHealth());
    assertEquals(100, pet.getHappiness());
    assertEquals(100, pet.getHunger());
    assertEquals(100, pet.getSleep());
    assertEquals(PetState.NORMAL, pet.getCurrentState());
}
    
    @Test
    public void testParentalControls() {
        // Test setting parental controls
        game.updateParentalControls(true, 8, 20, "testpass");
        
        assertTrue(game.isTimeRestrictionEnabled());
        assertEquals(8, game.getStartHour());
        assertEquals(20, game.getEndHour());
        
        // Test password verification
        assertTrue(game.verifyParentPassword("testpass"));
        assertFalse(game.verifyParentPassword("wrongpass"));
        
        // Reset play time stats
        game.resetPlayTimeStats();
        assertEquals(0, game.getTotalPlayTime());
        assertEquals(0, game.getLastSessionTime());
        assertEquals(0, game.getAverageSessionTime());
    }
    
    @Test
    public void testActionCooldowns() {
        game.setActionCooldown("vet");
        assertTrue(game.isActionOnCooldown("vet"));
        assertTrue(game.getRemainingCooldown("vet") > 0);
        
        // Play action cooldown
        game.setActionCooldown("play");
        assertTrue(game.isActionOnCooldown("play"));
        assertTrue(game.getRemainingCooldown("play") > 0);
    }
}