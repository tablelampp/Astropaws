package model.item;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import model.item.Food;
import model.item.Food.FoodType;

public class FoodTest {

    @Test
    public void testFoodCreation() {
        Food kibble = new Food("Kibble", "");
        assertEquals(FoodType.KIBBLE, kibble.getFoodType());
        assertEquals(10, kibble.getFullnessValue());
        
        Food apple = new Food("Apple", "");
        assertEquals(FoodType.APPLE, apple.getFoodType());
        assertEquals(20, apple.getFullnessValue());
    }
    
    @Test
    public void testAllFoodTypes() {
        // Test all food types to ensure they're created correctly
        Food kibble = new Food("Kibble", "");
        Food apple = new Food("Apple", "");
        Food cheese = new Food("Cheese", "");
        Food bread = new Food("Bread", "");
        Food icecream = new Food("Icecream", "");
        Food chicken = new Food("Chicken", "");
        
        assertEquals(10, kibble.getFullnessValue());
        assertEquals(20, apple.getFullnessValue());
        assertEquals(15, cheese.getFullnessValue());
        assertEquals(30, bread.getFullnessValue());
        assertEquals(5, icecream.getFullnessValue());
        assertEquals(30, chicken.getFullnessValue());
    }
}