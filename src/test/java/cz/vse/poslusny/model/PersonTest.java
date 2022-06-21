package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy Person.
 *
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author David Poslušný
 * @version LS 2020
 */
public class PersonTest
{
    Item coChce;
    Item coNechce;
    Item coMiDa;
    Person trader;
    Inventory inventory;

    /**
     * Metoda setUp vrací aktuální stav hry pro testování.
     *
     */
    @Before
    public void setUp()
    {
       coChce = new Item("coChce", "popis1", true, false, 0, 0);
       coNechce = new Item("coNechce", "popis2", true, false, 0, 0);
       coMiDa = new Item("coMiDa", "popis3", true, false, 0, 0);
       trader = new Person("trader", true, false, "test");
       trader.setTradingParameters(coMiDa, coChce, "potom", "predtim", "nechci", "chci", false);
       inventory = new Inventory();
    }
    /**
     * Metoda testTrade testuje funčknost obchodování s postavou.
     *
     */
    @Test
    public void testTrade()
    {
        assertNull(trader.trade(coNechce));
        assertEquals(coMiDa, trader.trade(coChce));
    }
}
