package model.pet;

import model.item.Food;
import model.item.Toy;

/**
 * This class represents a fish pet. It has its own unique values for health, hunger, happiness, and sleep.
 */
public class Fish extends Pet{

	/**
	 * Constructs a Fish object with the specified name.
	 * 
	 * @param name the name of the fish.
	 */
	public Fish(String name) {
		super(name);
	}
	
	/**
	 * Feeds the fish the specified food which increases its hunger and happiness.
	 * 
	 * @param food the food to feed to the fish.
	 */
	public void eat(Food food) {
		setHunger(getHunger() + food.getFullnessValue());
		setHappiness(getHappiness() + 5);
	}
	
	/**
	 * Gives the fish a gift which increases its happiness.
	 * 
	 * @param toy the gift to give to the fish.
	 */
	public void gift(Toy toy) {
		setHappiness(getHappiness() + (toy.getFunValue()));
	}
	
	/**
	 * Decreases the fish's health, hunger, happiness, and sleep after going on a space mission.
	 */
	public void applySpaceStatEffects() {
		setHealth(Math.max(1, getHealth() - 80));
		setHunger(getHunger() - 70);
		setHappiness(getHappiness() - 40);
		setSleep(getSleep() - 30);
	}
	
	/**
	 * Makes the fish play which increases its happiness abd decreases its sleep level.
	 */
	public void play() {
		setHappiness(getHappiness() + 10);
		setSleep(getSleep() - 15);
	}
	
	/**
	 * Exercises the fish which increases its health and decreases its hunger and sleep levels.
	 */
	public void exercise() {
		setHealth(getHealth() + 5);
		setHunger(getHunger() - 40);
		setSleep(getSleep() - 25);
	}
	
	/**
	 * Takes the fish to the vet which increases its health.
	 */
	public void vet() {
		setHealth(getHealth() + 75);
	}
}
