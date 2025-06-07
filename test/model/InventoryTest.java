package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Inventory;
import model.item.Food;
import model.item.Toy;
import model.item.Item;
import java.util.ArrayList;

public class InventoryTest {
    
    private Inventory inventory;
    
    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
    }
    
    @Test
    public void testAddItem() {
        Food kibble = new Food("Kibble", "");
        boolean result = inventory.addItem(kibble);
        assertTrue(result);
        
        ArrayList<Item> items = inventory.getInventoryItems();
        assertEquals(1, items.size());
        assertEquals("Kibble", items.get(0).getName());
    }
    
    @Test
    public void testRemoveItem() {
        Food kibble = new Food("Kibble", "");
        inventory.addItem(kibble);
        
        boolean result = inventory.removeItem(kibble);
        assertTrue(result);
        
        ArrayList<Item> items = inventory.getInventoryItems();
        assertEquals(0, items.size());
    }
    
    @Test
    public void testInventoryLimit() {
        // The inventory has a limit of 20 items
        for (int i = 0; i < 20; i++) {
            Food food = new Food("Kibble", "");
            boolean result = inventory.addItem(food);
            assertTrue(result);
        }
        
        // Try to add one more item over the limit
        Food extraFood = new Food("Kibble", "");
        boolean result = inventory.addItem(extraFood);
        assertFalse(result);
        
        ArrayList<Item> items = inventory.getInventoryItems();
        assertEquals(20, items.size());
    }
    
    @Test
    public void testContains() {
        Food kibble = new Food("Kibble", "");
        inventory.addItem(kibble);
        
        Food anotherKibble = new Food("Kibble", "");
        assertTrue(inventory.contains(anotherKibble));
        
        Food apple = new Food("Apple", "");
        assertFalse(inventory.contains(apple));
    }
}