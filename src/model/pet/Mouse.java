package model.pet;

import model.item.Food;
import model.item.Toy;

/**
 * This class represents a mouse pet. It has its own unique values for health, hunger, happiness, and sleep.
 */
public class Mouse extends Pet{

	/**
	 * Constructs a mouse object with the specified name.
	 * 
	 * @param name the name of the mouse.
	 */
	public Mouse(String name) {
		super(name);
	}
	
	/**
	 * Feeds the mouse the specified food which increases its hunger and happiness.
	 * 
	 * @param food the food to feed to the mouse.
	 */
	public void eat(Food food) {
		setHunger(getHunger() + food.getFullnessValue());
		setHappiness(getHappiness() + 5);
	}

	/**
	 * Gives the mouse a gift which increases its happiness.
	 * 
	 * @param toy the gift to give to the mouse.
	 */
	public void gift(Toy toy) {
		setHappiness(getHappiness() + (toy.getFunValue()));
	}
	
	/**
	 * Decreases the mouse's health, hunger, happiness, and sleep after going on a space mission.
	 */
	public void applySpaceStatEffects() {
		setHealth(Math.max(1, getHealth() - 80));
		setHunger(getHunger() - 70);
		setHappiness(getHappiness() - 25);
		setSleep(getSleep() - 20);
	}
	
	/**
	 * Makes the mouse play which increases its happiness abd decreases its sleep level.
	 */
	public void play() {
		setHappiness(getHappiness() + 10);
		setSleep(getSleep() - 15);
	}
	
	/**
	 * Exercises the mouse which increases its health and decreases its hunger and sleep levels.
	 */
	public void exercise() {
		setHealth(getHealth() + 15);
		setHunger(getHunger() - 35);
		setSleep(getSleep() - 30);
	}
	
	/**
	 * Takes the mouse to the vet which increases its health.
	 */
	public void vet() {
		setHealth(getHealth() + 70);
	}
}
