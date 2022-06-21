package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy Inventory.
 * V této tříde se testuje kapacita inventáře, přidávání a odebírání předmětů.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class InventoryTest
{
    private Game game;
    private Inventory inventory;
    private Item i1, i2, i3, i4, i5, i6, nonMoveable, weapon;
    
    /**
     * Metoda setUp připravuje instance, které bude potřebné k testování třídy.
     *
     */
    @Before
    public void setUp()
    {
        game = new Game();
        i1 = new Item("item1", "popis1", true, false, 0, 0);
        i2 = new Item("item2", "popis2",  true, false, 0, 0);
        i3 = new Item("item3", "popis3",true, false, 0, 0);
        i4 = new Item("item4", "popis4", true, false, 0, 0);
        i5 = new Item("item5", "popis5", true, false, 0, 0);
        i6 = new Item("item6", "popis6", true, false, 0, 0);
        weapon = new Item("weapon", "zbraň", true, true, 30, 30);
        nonMoveable = new Item("nepřenositelné", "věc", false, false, 0, 0);
        inventory = new Inventory();
    }
    
    /**
     * Metoda testIsFull testuje funkčnost metody "isFull".
     *
     */
    @Test
    public void testIsFull()
    {
        inventory.addItem(i1);
        inventory.addItem(i2);
        inventory.addItem(i3);
        inventory.addItem(i4);
        
        assertEquals(false, inventory.isFull());
        
        inventory.addItem(i5);
        
        assertEquals(true, inventory.isFull());
    }
    
    /**
     * Metoda testAddItem testuje funkčnost metody "addItem".
     *
     */
    @Test
    public void testAddItem()
    {
        assertTrue(inventory.addItem(i1));
        assertTrue(inventory.addItem(i2));
        assertTrue(inventory.addItem(i3));
        
        assertFalse(inventory.addItem(nonMoveable));
        
        assertTrue(inventory.addItem(i4));
        assertTrue(inventory.addItem(i5));
        
        assertFalse(inventory.addItem(i6));
        assertFalse(inventory.addItem(i1));
        assertFalse(inventory.addItem(i2));
    }
    
    /**
     * Metoda testRemoveItem testuje funkčnost metody "removeItem".
     *
     */
    @Test
    public void testRemoveItem()
    {
        assertTrue(inventory.addItem(i1));
        assertTrue(inventory.addItem(i2));
        assertTrue(inventory.addItem(i3));
        
        assertFalse(inventory.addItem(nonMoveable));
        
        assertTrue(inventory.addItem(i4));
        assertTrue(inventory.addItem(i5));
        
        assertFalse(inventory.addItem(i6));
        assertFalse(inventory.addItem(i1));
        assertFalse(inventory.addItem(i2));
        
        assertTrue(inventory.removeItem(i1));
        assertFalse(inventory.removeItem(i1));
        
        assertTrue(inventory.removeItem(i2));
        assertTrue(inventory.removeItem(i3));
        
        assertFalse(inventory.removeItem(i2));
        
        assertFalse(inventory.removeItem(null));    
    }
}
