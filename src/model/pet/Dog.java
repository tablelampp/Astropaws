package model.pet;

import model.item.Food;
import model.item.Toy;

/**
 * This class represents a dog pet. It has its own unique values for health, hunger, happiness, and sleep.
 */
public class Dog extends Pet{

	/**
	 * Constructs a Dog object with the specified name.
	 * 
	 * @param name the name of the dog.
	 */
	public Dog(String name) {
		super(name);
	}
	
	/**
	 * Feeds the dog the specified food which increases its hunger and happiness.
	 * 
	 * @param food the food to feed to the dog.
	 */
	public void eat(Food food) {
		setHunger(getHunger() + food.getFullnessValue());
		setHappiness(getHappiness() + 5);
	}
	
	/**
	 * Gives the dog a gift which increases its happiness.
	 * 
	 * @param toy the gift to give to the dog.
	 */
	public void gift(Toy toy) {
		setHappiness(getHappiness() + toy.getFunValue());
	}
	
	/**
	 * Decreases the dog's health, hunger, happiness, and sleep after going on a space mission.
	 */
	public void applySpaceStatEffects() {
		setHealth(Math.max(1, getHealth() - 60));
		setHunger(getHunger() - 50);
		setHappiness(getHappiness() - 20);
		setSleep(getSleep() - 75);
	}
	
	/**
	 * Makes the dog play which increases its happiness abd decreases its sleep level.
	 */
	public void play() {
		setHappiness(getHappiness() + 10);
		setSleep(getSleep() - 10);
	}
	
	/**
	 * Exercises the dog which increases its health and decreases its hunger and sleep levels.
	 */
	public void exercise() {
		setHealth(getHealth() + 15);
		setHunger(getHunger() - 40);
		setSleep(getSleep() - 20);
	}
	
	/**
	 * Takes the dog to the vet which increases its health.
	 */
	public void vet() {
		setHealth(getHealth() + 50);
	}
}
