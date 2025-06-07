package model.pet;

import model.item.Food;
import model.item.Toy;

/**
 * This class represents a monkey pet. It has its own unique values for health, hunger, happiness, and sleep.
 */
public class Monkey extends Pet{

	/**
	 * Constructs a Monkey object with the specified name.
	 * 
	 * @param name the name of the monkey.
	 */
	public Monkey(String name) {
		super(name);
	}
	
	/**
	 * Feeds the monkey the specified food which increases its hunger and happiness.
	 * 
	 * @param food the food to feed to the monkey.
	 */
	public void eat(Food food) {
		setHunger(getHunger() + food.getFullnessValue());
		setHappiness(getHappiness() + 10);
	}
	
	/**
	 * Gives the monkey a gift which increases its happiness.
	 * 
	 * @param toy the gift to give to the monkey.
	 */
	public void gift(Toy toy) {
		setHappiness(getHappiness() + (toy.getFunValue()));
	}
	
	/**
	 * Decreases the monkey's health, hunger, happiness, and sleep after going on a space mission.
	 */
	public void applySpaceStatEffects() {
		setHealth(Math.max(1, getHealth() - 40));
		setHunger(getHunger() - 50);
		setHappiness(getHappiness() - 60);
		setSleep(getSleep() - 60);
	}
	
	/**
	 * Makes the monkey play which increases its happiness abd decreases its sleep level.
	 */
	public void play() {
		setHappiness(getHappiness() + 20);
		setSleep(getSleep() - 30);
	}
	
	/**
	 * Exercises the monkey which increases its health and decreases its hunger and sleep levels.
	 */
	public void exercise() {
		setHealth(getHealth() + 15);
		setHunger(getHunger() - 45);
		setSleep(getSleep() - 25);
	}
	
	/**
	 * Takes the monkey to the vet which increases its health.
	 */
	public void vet() {
		setHealth(getHealth() + 35);
	}
	
}
