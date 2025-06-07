package model;

import java.util.ArrayList;
import model.item.*;
/**
 * This class represents the player's inventory which holds their items.
 */
public class Inventory {
    private static final int ITEM_LIMIT = 20;
    private ArrayList<Item> items;

    /**
     * Constructor for the Inventory class. Creates an array list to hold the items.
     */
    public Inventory() {
        items = new ArrayList<Item>();
    }

    /**
     * Adds an item to the player's inventory if their inventory is not full.
     * 
     * @param item the item to be added.
     */
    public boolean addItem(Item item) {
        if (items.size() < ITEM_LIMIT) {
            items.add(item);
            return true;
        }
        return false;
    }

    /**
     * Removes an item from the player's inventory.
     * 
     * @param item the item to be removed.
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Returns the items a player has in their inventory.
     * 
     * @return the array list of items.
     */
    public ArrayList<Item> getInventoryItems() {
        return items;
    }
    
    /**
     * Checks if the inventory contains the specified item.
     * 
     * @param searchedItem item to search for.
     * @return true if searchedItem is in the inventory and false otherwise.
     */
    public boolean contains(Item searchedItem) {
    	for(Item currItem : items) {
    		if (currItem.getName().equals(searchedItem.getName())) return true;
    	}
    	
    	return false;
    }
}
