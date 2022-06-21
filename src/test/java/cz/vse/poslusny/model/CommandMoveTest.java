package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy CommandMove.
 * V této tříde se testuje metoda process, tudíž jestli příkaz funguje.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandMoveTest
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
     * Metoda testProcess testuje metodu "process" z příkazu "CommandMove".
     *
     */
    @Test 
    public void testProcess()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Nechápu, kam mám jít. Musíš mi zadat nějaký cíl.", game.processCommand("jdi"));
        assertEquals("Nechápu, co po mě chceš. Neumím se 'rozdvojit' a jít na více míst současně.", game.processCommand("jdi něco něco"));
        
        game.getGamePlan().getCurrentArea().isCombatHappening(true);
        assertEquals("Právě bojuješ s nepřítelem, nemůžeš z lokace odejít.", game.processCommand("jdi Město"));
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        game.getGamePlan().getCurrentArea().isCombatHappening(false);
        
        assertEquals("Tam se ale odsud jít nedá!", game.processCommand("jdi něco"));
        
        assertEquals("Místnost 'Opuštěná_věž' je zavřená. Musíš najít způsob jak ji otevřít.", game.processCommand("jdi Opuštěná_věž"));
    }
    
}
