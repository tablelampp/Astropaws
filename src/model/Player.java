package model;
import model.pet.Pet;
import model.item.*;

/**
 * This class represents a player of the game. It provides functionality for using items and changing the player's score.
 */
public class Player {
    private Pet activePet;
    private Inventory inventory;

    /**
     * Constructor for the Player class. Creates a Player with the given attributes. 
     * 
     * @param pet the player's pet
     */
    public Player(Pet pet) {
        this.inventory = new Inventory();
        this.activePet = pet;
    }
    
    /**
     * Gets the player's pet.
     * 
     * @return the player's pet.
     */
    public Pet getPet() {
        return activePet;
    }

    /**
     * Sets the player's pet to the specified pet.
     * 
     * @param pet the player's new pet.
     */
    public void setPet(Pet pet) {
        this.activePet = pet;
    }

    /**
     * Gets the player's inventory.
     * 
     * @return the player's inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Sets the player's inventory to the specified value.
     * 
     * @param inventory the value to be assigned to the player's inventory.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Give the pet a gift.
     * 
     * <p>Give the toy to the active pet, remove the toy from the player's inventory,
     * and update the state of the active pet.</p>
     * 
     * @param toy the toy to be given to the pet.
     */
    public void giftToPet(Toy toy) {
    	activePet.gift(toy);
    	inventory.removeItem(toy);
        activePet.updateState();
    }
    
    /**
     * Send the pet to bed.
     * 
     * <p>Make the active pet sleep and update its state.</p>
     */
    public void sendToBed() {
    	activePet.sleep();
    }
    
    /**
     * Feed the pet the specified food item.
     * 
     * <p>Feed the active pet, remove the food item from the player's inventory,
     * and update the pet's state.</p>
     * 
     * @param food the food to be given to the pet.
     */
    public void feedPet(Food food) {
    	activePet.eat(food);
    	inventory.removeItem(food);
        activePet.updateState();
    }
    
    /**
     * Send the pet to space and update its state.
     */
    public void sendToSpace() {
    	activePet.goOnSpaceMission();
        activePet.updateState();
    }
    
    /**
     * Play with the pet and update its state.
     */
    public void playWithPet() {
    	activePet.play();
        activePet.updateState();
    }
    
    /**
     * Take the pet to the vet and update its state.
     */
    public void takeToVet() {
    	activePet.vet();
        activePet.updateState();
    }
    
    /**
     * Exercise the pet and update its state.
     */
    public void petExercise() {
    	activePet.exercise();
        activePet.updateState();
    }
}
