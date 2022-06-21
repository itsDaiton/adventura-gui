package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy CommandInventory.
 * V této tříde se testuje metoda process, tudíž jestli příkaz funguje.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandInventoryTest
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
     * Metoda testProcess testuje metodu "process" z příkazu "CommandInventory".
     *
     */
    @Test 
    public void testProcess()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Příkaz 'inventar' se používá bez parametrů a vypíše všechny předměty v inventáři a aktuální zbraň.", game.processCommand("inventar něco"));
        assertEquals("Příkaz 'inventar' se používá bez parametrů a vypíše všechny předměty v inventáři a aktuální zbraň.", game.processCommand("inventar něco něco")); 
        
        game.getGamePlan().getCurrentArea().isCombatHappening(true);
        assertEquals("Právě bojuješ s nepřítelem, nemůžeš si prohlížet inventář.", game.processCommand("inventar"));
        game.getGamePlan().getCurrentArea().isCombatHappening(false);
        
        assertEquals("Tvůj inventář je prázdný.\nNemáš nasazenou žádnou zbraň.\nTvoje životy: 250", game.processCommand("inventar"));
        
        game.processCommand("jdi Město");
        assertEquals("Město", game.getGamePlan().getCurrentArea().getName()); 
       
        game.processCommand("seber Dýka");
        assertEquals("Tvůj inventář je prázdný.\nTvoje aktuální zbraň je 'Dýka'\n- Poškození zbraně: 15\n- Hodnota odražení: 5\nTvoje životy: 250", game.processCommand("inventar")); 
        
        game.processCommand("seber Miska_s_kořením");
        assertEquals("Tvůj inventář obsahuje tyto předměty:\n- Miska_s_kořením\nTvoje aktuální zbraň je 'Dýka'\n- Poškození zbraně: 15\n- Hodnota odražení: 5\nTvoje životy: 250", game.processCommand("inventar"));
    }
}
