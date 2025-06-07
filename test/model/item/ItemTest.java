package model.item;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import model.item.Item;
import model.item.Food;
import model.item.Toy;

public class ItemTest {

    @Test
    public void testItemBasicProperties() {
        Food food = new Food("Kibble", "");
        assertEquals("Kibble", food.getName());
        assertEquals("Made of space plants.", food.getDescription());
        
        Toy toy = new Toy("Ball", "");
        assertEquals("Ball", toy.getName());
        assertEquals("Looks like the moon.", toy.getDescription());
    }
}