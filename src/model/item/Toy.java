/**
 * Represents toy items that can be used to play with pets to increase their happiness.
 */

 package model.item;

 public class Toy extends Item {
	 public enum ToyType {
		 BALL,
		 ROCKETTOY,
		 FRISBEE,
		 ALIEN,
		 STARPLUSH
	 }
	 
	 private ToyType type;
	 private int funValue;
	 
	 /**
	  * Constructs a new toy item with the specified properties.
	  *
	  *	The fun value will be set based on the name of the toy.
	  *
	  * @param name the name of the toy to be created.
	  * @param description a description of what the toy is/does.
	  */
	 public Toy(String name, String description) {
		 super(name);
		 switch(name.toLowerCase()) {
			 case "ball":
				 type = ToyType.BALL;
				 funValue = 30;
				 setDescription("Looks like the moon.");
				 break;
			 case "rocket":
				 type = ToyType.ROCKETTOY;
				 funValue = 25;
				 setDescription("Makes space sounds.");
				 break;
			 case "frisbee":
				 type = ToyType.FRISBEE;
				 funValue = 10;
				 setDescription("Perfect for a game of catch.");
				 break;
			 case "alien":
				 type = ToyType.ALIEN;
				 funValue = 15;
				 setDescription("A friendly alien from a faraway planet.");
				 break;
			 case "starplush":
				 type = ToyType.STARPLUSH;
				 funValue = 20;
				 setDescription("Glows in the dark.");
				 break;
			 default:
				 type = null;	// do smth here idk
				 System.out.println("Failed to create toy instance: " + name); 
		 }
	 }
	 
	 /**
	  * Gets the fun value of the toy.
	  *
	  * @return the fun value of the toy.
	  */
	 public int getFunValue() {
		 return funValue;
	 }
	 
	 /**
	  * Gets the toy type.
	  *
	  * @return the toy type.
	  */
	 public ToyType getToyType() {
		 return type;
	 }
 }
 
 