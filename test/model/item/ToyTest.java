package model.item;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import model.item.Toy;
import model.item.Toy.ToyType;

public class ToyTest {

    @Test
    public void testToyCreation() {
        Toy ball = new Toy("Ball", "");
        assertEquals(ToyType.BALL, ball.getToyType());
        assertEquals(30, ball.getFunValue());
        
        Toy rocket = new Toy("Rocket", "");
        assertEquals(ToyType.ROCKETTOY, rocket.getToyType());
        assertEquals(25, rocket.getFunValue());
    }
    
    @Test
    public void testAllToyTypes() {
        // Test all toy types to ensure they're created correctly
        Toy ball = new Toy("Ball", "");
        Toy rocket = new Toy("Rocket", "");
        Toy frisbee = new Toy("Frisbee", "");
        Toy alien = new Toy("Alien", "");
        Toy starPlush = new Toy("StarPlush", "");
        
        assertEquals(30, ball.getFunValue());
        assertEquals(25, rocket.getFunValue());
        assertEquals(10, frisbee.getFunValue());
        assertEquals(15, alien.getFunValue());
        assertEquals(20, starPlush.getFunValue());
    }
}