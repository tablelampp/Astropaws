package model.pet;

import model.item.Food;
import model.item.Toy;

/**
 * This is an abstract pet class which represents a generic pet in the game.
 * Pets have stats representing their health, happiness, sleep, hunger, and space 
 * readiness. A pet will be in one of the following states at all times: normal,
 * sleeping, hungry, angry, dead.
 */
public abstract class Pet {

	private String name;
	
	// Core stats (score from 0-100)
	private int health;
	private int sleep;
	private int hunger;
	private int happiness;
	private int spaceReadiness;
	private int petID;
	
	// All pet states
	public enum PetState {
		NORMAL,
		SLEEPING,
		HUNGRY,
		ANGRY,
		DEAD
	}
	
	public enum PetType {
		DOG,
		FISH,
		MONKEY,
		MOUSE
	}
	
	private PetState currentState;
	private int totalMissions;
	
	/**
	 * Constructs a generic Pet with a normal state, full stats, and a space
	 * readiness of 0.
	 * 
	 * @param name the name of the pet to be created.
	 */
	public Pet(String name) {
		this.name = name;
		
		this.health = 100;
		this.sleep = 100;
		this.hunger = 100;
		this.happiness = 100;
		this.spaceReadiness = 0;
		
		this.currentState = PetState.NORMAL;
		this.totalMissions = 0;
	}
	
	/**
	 * Updates the pet's stats over time
	 */
	public void updateState() {
		if (health <= 0) {
			currentState = PetState.DEAD;
			return;
		}
		
		if (currentState == PetState.SLEEPING && sleep >= 100) {
			currentState = PetState.NORMAL;
			return;
		}
		
		if (sleep <= 0) {
			currentState = PetState.SLEEPING;
			health -= 5;
			return;
		}
		
		if (happiness <= 0) {
			currentState = PetState.ANGRY;
			return;
		}
		
		if (hunger <= 0) {
			currentState = PetState.HUNGRY;
			health -= 5;
			return;
		}
		
		currentState = PetState.NORMAL;
	}

	/**
	 * Feeds the pet the specified food which increases its hunger and happiness.
	 * 
	 * @param food the food to feed to the pet.
	 */
	public abstract void eat(Food food);

	/**
	 * Makes the pet play which increases its happiness abd decreases its sleep level.
	 */
	public abstract void play();

	/**
	 * Gives the pet a gift which increases its happiness.
	 * 
	 * @param toy the gift to give to the pet.
	 */
	public abstract void gift(Toy toy);

	/**
	 * Exercises the pet which increases its health and decreases its hunger and sleep levels.
	 */
	public abstract void exercise();

	/**
	 * Takes the pet to the vet which increases its health.
	 */
	public abstract void vet();

	/**
	 * Decreases the dog's health, hunger, happiness, and sleep after going on a space mission.
	 */
	public abstract void applySpaceStatEffects();	// Each pet's stats are affected differently by a space mission

	/**
	 * Make the pet go to sleep by changing its state.
	 */
	public void sleep() {
		currentState = PetState.SLEEPING;
	}
	
	/**
	 * Update the progress bars based on what state they are in.
	 */
	public void updateProgressBars() {
            if (currentState == PetState.DEAD) return;

            if (currentState != PetState.SLEEPING) {
                    hunger = Math.max(0,  hunger - 5);
                    happiness = Math.max(0, happiness - 10);
                    sleep = Math.max(0, sleep - 2);
            } else {
                    sleep = Math.min(100, sleep + 10);
					return;		// Other stats are not affected while sleeping
            }

            if (hunger <= 50 && hunger > 10) health = Math.max(0, health - 20);
			else if (hunger <= 10) health = Math.max(0, health - 30);
			

            if(health >= 90 && hunger >= 90 && sleep >= 90 && happiness >= 90) {
                    spaceReadiness = Math.min(100,  spaceReadiness + 10);
            }


            updateState();

			if (PetState.DEAD == currentState) {
				health = 0;
				sleep = 0;
				hunger = 0;
				happiness = 0;
				spaceReadiness = 0;
			}
			
			System.out.println("-----------PROGRESS BARS UPDATED-----------");
			System.out.println("Health: " + health);
			System.out.println("Sleep: " + sleep);
			System.out.println("Hunger: " + hunger);
			System.out.println("Happiness: " + happiness);
			System.out.println("Space Readiness: " + spaceReadiness);
			System.out.println();
			System.out.println("Current state: " + currentState);
			System.out.println("--------------------------------------------");
	}

	/**
	 * Makes the pet go on a space mission as long as the pet is
	 * ready for a space mission. 
	 * 
	 * <p>After the mission, the pet's space
	 * readiness will be reset, their stats will be effected, and their
	 * number of missions will be incremented.</p>
	 * 
	 * @return true if the pet successfull went to space and false otherwise.
	 */
	public boolean goOnSpaceMission() {
		if (!isSpaceReady()) return false;
		
		spaceReadiness = 0;
		applySpaceStatEffects();
		updateState();
		
		totalMissions++;
		
		return true;
	}
	
	/**
	 * Gets the name of the pet.
	 * 
	 * @return the name of the pet.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the pet.
	 * 
	 * @param name the name to be assigned to the pet.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the pet's health.
	 * 
	 * @return the pet's health.
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Sets the health to the specified value. 
	 * 
	 * @param health the value to be assigned to health.
	 */
	public void setHealth(int health) {
		this.health = Math.max(0, Math.min(100,  health));
		updateState();
	}
	
	/**
	 * Gets the sleep value.
	 * 
	 * @return the sleep value.
	 */
	public int getSleep() {
		return sleep;
	}
	
	/**
	 * Sets the pet's sleep level, ensuring it is between 0 and 100. Then 
	 * update the pet's state.
	 * 
	 * @param sleep the value to be assigned to sleep.
	 */
	public void setSleep(int sleep) {
		this.sleep = Math.max(0, Math.min(100, sleep));
		updateState();
	}
	
	/**
	 * Gets the pet's hunger level.
	 * 
	 * @return the pet's hunger level.
	 */
	public int getHunger() {
		return hunger;
	}
	
	/**
	 * Sets the pet's hunger level, ensuring it it between 0 and 100. Then update
	 * the pet's state.
	 * 
	 * @param hunger the value to be assigned to hunger.
	 */
	public void setHunger(int hunger) {
		this.hunger = Math.max(0, Math.min(100, hunger));
		updateState();
	}
	
	/**
	 * Get the pet's happiness level.
	 * 
	 * @return the pet's happiness level.
	 */
	public int getHappiness() {
		return happiness;
	}
	
	/**
	 * Set the pet's happiness level, ensuring the value is between 0 and 100. 
	 * Then update the pet's state.
	 * 
	 * @param happiness the value to be assigned to happiness.
	 */
	public void setHappiness(int happiness) {
		this.happiness = Math.max(0, Math.min(100, happiness));
		updateState();
	}
	
	/**
	 * Sets the pet's space readiness, ensuring the value is between 0 and 100.
	 * Then update the pet's state.
	 * 
	 * @param spaceReadiness the value to be assigned to space readiness.
	 */
	public void setSpaceReadiness(int spaceReadiness) {
		this.spaceReadiness = Math.max(0, Math.min(100, spaceReadiness));
		updateState();
	}

	/**
	 * Gets the pet's space readiness.
	 * 
	 * @return the pet's space readiness.
	 */
	public int getSpaceReadiness() {
		return spaceReadiness;
	}
	
	// set space readiness not needed since it is not a function to manually set this stat
	
	/**
	 * Gets the pet's current state.
	 * 
	 * @return the pet's current state.
	 */
	public PetState getCurrentState() {
		return currentState;
	}
	
	/**
	 * Get the pet's total number of missions.
	 * 
	 * @return the pet's total number of missons.
	 */
	public int getTotalMission() {
		return totalMissions;
	}
	
	/**
	 * Gets the pet's ID.
	 * 
	 * @return the pet's ID.
	 */
	public int getID() {
		return petID;
	}
	
	/**
	 * Sets the pet's ID to the specified value.
	 * 
	 * @param ID the value to assign to the ID.
	 */
	public void setID(int ID) {
		petID = ID;
	}
	
	/**
	 * Checks if the pet is space ready. 
	 * 
	 * <p>A pet is ready for space if their space readiness is greater than or 
	 * equal to 100 and their current state is normal.</p>
	 * 
	 * @return true if the pet is space ready and false otherwise.
	 */
	public boolean isSpaceReady() {
		return spaceReadiness >= 100 && currentState == PetState.NORMAL;
	}
	
	/**
	 * Check if the pet is in the specified state.
	 * 
	 * @param state the state to check against the pet's state.
	 * @return true if the specified state is the pet's current state
	 * and false otherwise.
	 */
	public boolean isInState(PetState state) {
		return currentState == state;
	}
	
	/**
	 * Checks if the pet is alive. 
	 * 
	 * @return true if the pet is alive and false otherwise.
	 */
	public boolean isAlive() {
		return currentState != PetState.DEAD;
	}

	/**
	 * Sets the pet's state to the specified state.
	 * 
	 * @param state the state to be assigned to the pet's state.
	 */
	public void setCurrentState(PetState state) {
		this.currentState = state;
	}
	
	/**
	 * Sets the total number of missions.
	 * 
	 * @param missions the value to be assigned to total missions.
	 */
	public void setTotalMissions(int missions) {
		this.totalMissions = missions;
	}
	
}
