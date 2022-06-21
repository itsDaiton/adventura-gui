package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy CommandUse.
 * V této tříde se testuje metoda process, tudíž jestli příkaz funguje.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandUseTest
{
    private Game game;
        
    /**
     * Metoda setUp vrací aktuální stav hry pro testování.
     *
     */
    @Before
    public void setUp()
    {
        game = new Game();
    }
            
    /**
     * Metoda testProcess testuje metodu "process" z příkazu "CommandUse".
     *
     */
    @Test 
    public void testProcess()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Nevím co mám použít, zadej název předmětu.", game.processCommand("pouzij"));
        assertEquals("Tomu nerozumím, neumím použít více předmětů současně.", game.processCommand("pouzij něco něco"));
        
        game.getGamePlan().getCurrentArea().isCombatHappening(true);
        assertEquals("Právě bojuješ s nepřítelem, nemůžeš pužívat předměty.", game.processCommand("pouzij Kuře"));
        game.getGamePlan().getCurrentArea().isCombatHappening(false);
        
        assertFalse(game.getGamePlan().getCurrentArea().containsItem("Kukuřice"));
        assertEquals("Předmět 'Kukuřice' nemáš v inventáři, nemůžeš ho použít.", game.processCommand("pouzij Kukuřice"));
        
        assertTrue(game.getGamePlan().getCurrentArea().containsItem("Koláč"));
        assertEquals("Předmět 'Koláč' musíš nejdříve sebrat do inventáře.", game.processCommand("pouzij Koláč"));

        game.processCommand("seber Kuře");        
        assertEquals("Snědl si 'Kuře', životy se ti zvýšili o 30.", game.processCommand("pouzij Kuře"));  
    }
}
