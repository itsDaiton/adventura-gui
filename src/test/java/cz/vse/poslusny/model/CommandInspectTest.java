package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy CommandInspect.
 * V této tříde se testuje metoda process, tudíž jestli příkaz funguje.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandInspectTest
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
     * Metoda testProcess testuje metodu "process" z příkazu "CommandInspect".
     *
     */
    @Test 
    public void testProcess()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Nevím, co mám prozkoumat, musíš zadat název předmětu.", game.processCommand("prozkoumej"));
        assertEquals("Tomu nerozumím, neumím prozkoumat více předmětů současně.", game.processCommand("prozkoumej něco něco"));
        
        game.getGamePlan().getCurrentArea().isCombatHappening(true);
        assertEquals("Právě bojuješ s nepřítelem, nemůžeš zkoumat předměty.", game.processCommand("prozkoumej něco"));
        game.getGamePlan().getCurrentArea().isCombatHappening(false);
        
        assertEquals("Předmět 'Něco' tady není nebo se jedná o aktuální vybavenou zbraň.", game.processCommand("prozkoumej Něco"));        
               
        game.processCommand("seber Kuře");
        assertEquals("Popis předmětu: " + game.getGamePlan().getInventory().getItemByName("Kuře").getDescription(), game.processCommand("prozkoumej Kuře"));        
        
        game.processCommand("jdi Farma");
        assertEquals("Farma", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Most_přes_řeku");
        assertEquals("Most_přes_řeku", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Řeka");
        assertEquals("Řeka", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Polní_cesta");
        assertEquals("Polní_cesta", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Les");
        assertEquals("Les", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Prohledal jsi věc 'Křoví' a našel si v ní 'Borůvky'.", game.processCommand("prozkoumej Křoví"));
        game.getGamePlan().getCurrentArea().getItem("Křoví").inspected(true);
        assertEquals("Popis předmětu: " + game.getGamePlan().getCurrentArea().getItem("Křoví").getDescription() + "\nPoznámka: S tímto předmětem hýbat nemůžeš.", game.processCommand("prozkoumej Křoví"));
        
    }
}
