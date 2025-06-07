/**
 * Represents food items that can be fed to pets to increase their fullness.
 * Different food items have different effects on the pet's fullness level.
 */

 package model.item;

 public class Food extends Item {
	 private int fullnessValue;
	 private FoodType type;
	 
	 public enum FoodType {
		 KIBBLE,
		 APPLE,
		 CHEESE,
		 BREAD,
		 ICECREAM,
		 CHICKEN
	 }
		  
	 /**
	  * Constructs a new food item with the specified properties.
	  *
	  * @param name the name of the food.
	  * @param description a description of what the food is.
	  */
	 public Food(String name, String description) {
		 super(name);
		 
		 switch(name.toLowerCase()) {
			 case "kibble":
				 type = FoodType.KIBBLE;
				 fullnessValue = 10;
				 setDescription("Made of space plants.");
				 break;
			 case "apple":
				 type = FoodType.APPLE;
				 fullnessValue = 20;
				 setDescription("Fresh and tasty!");
				 break;
			 case "cheese":
				 type = FoodType.CHEESE;
				 fullnessValue = 15;
				 setDescription("A slice of cheese for a quick snack.");
				 break;
			 case "bread":
				 type = FoodType.BREAD;
				 fullnessValue = 30;
				 setDescription("Good for a quick energy boost.");
				 break;
			 case "icecream":
				 type = FoodType.ICECREAM;
				 fullnessValue = 5;
				 setDescription("The best sweet treat for a hot day.");
				 break;
			 case "chicken":
				 type = FoodType.CHICKEN;
				 fullnessValue = 30;
				 setDescription("A great source of protein.");
				 break;
			 default:
				 type = null;
				 System.out.println("Failed to create food instance: " + name); 
		 }
		 
	 }
	 
	 
	 /**
	  * Gets the fullness value of this food
	  *
	  * @return the fullness value of the food.
	  */
	 public int getFullnessValue() {
		 return fullnessValue;
	 }
	 
	 /**
	  * Gets the type of food.
	  * @return the type of food.
	  */
	 public FoodType getFoodType() {
		 return type;
	 }
 }
 