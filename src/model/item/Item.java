/**
 * Abstract class representing a generic item in the virtual pet game.
 * Items can be used by players to interact with their pets.
 */

package model.item;

public abstract class Item {
    private String name;
    private String description;
    
    /**
     * Constructs a new item with the specified name and description.
     * 
     * @param name the name of the item.
     * @param description a description of what the item is/does.
     */
    public Item(String name) {
        this.name = name;
        this.description = "";
    }
    
    /**
     * Gets the name of the item.
     * 
     * @return the name of the item.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the description of the item.
     * 
     * @return the description of the item.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description of the item to the specified value.
     * 
     * @param description the item's description.
     */
    protected void setDescription(String description) {
        this.description = description;
    }
    
    // // Gets the current quantity of this item.
    // public int getQuantity() {
    //     return quantity;
    // }
    
//    // Increases the quantity of this item by the specified amount.
//    public void increaseQuantity(int amount) {
//        if (amount > 0) {
//            this.quantity += amount;
//        }
//    }
    
//    // Decreases the quantity of this item by one when used.
//    public boolean decreaseQuantity() {
//        if (quantity > 0) {
//            quantity--;
//            return true;
//        }
//        return false;
//    }
    
//    // Sets the quantity to a specific value.
//    public void setQuantity(int quantity) {
//        if (quantity >= 0) {
//            this.quantity = quantity;
//        }
//    }
    
    // Abstract method to use the item on a pet.
    // Each subclass must implement this method to define the item's effect.
//    public abstract boolean use(Pet pet);

}
