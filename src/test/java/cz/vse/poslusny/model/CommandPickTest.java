package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy CommandPick.
 * V této tříde se testuje metoda process, tudíž jestli příkaz funguje.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandPickTest
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
     * Metoda testProcess testuje metodu "process" z příkazu "CommandPick".
     *
     */
    @Test 
    public void testProcess()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Nevím, co mám sebrat, musíš zadat název předmětu.", game.processCommand("seber"));
        assertEquals("Nevím, co mám sebrat, musíš zadat jen jeden předmět.", game.processCommand("seber něco něco"));
        
        game.getGamePlan().getCurrentArea().isCombatHappening(true);
        assertEquals("Právě bojuješ s nepřítelem, nemůžeš sbírat předměty.", game.processCommand("seber Něco"));
        game.getGamePlan().getCurrentArea().isCombatHappening(false);
        
        assertEquals("Předmět 'Kukuřice' tady není.", game.processCommand("seber Kukuřice"));
        
        game.processCommand("jdi Město");
        assertEquals("Město", game.getGamePlan().getCurrentArea().getName()); 
        
        assertEquals("Sebral si zbraň 'Dýka', nyní je to tvoje aktuální zbraň.", game.processCommand("seber Dýka"));
        assertEquals("Sebral(a) jsi předmět 'Miska_s_kořením' a uložil jsi ho do inventáře.", game.processCommand("seber Miska_s_kořením"));
        
        game.processCommand("jdi Předměstí");
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName()); 
        
        game.processCommand("vymen Miska_s_kořením Obchodník");
        game.processCommand("pouzij Klíč"); 
        
        game.processCommand("jdi Opuštěná_věž");
        assertEquals("Opuštěná_věž", game.getGamePlan().getCurrentArea().getName()); 
        
        game.processCommand("prozkoumej Truhla"); 
        assertEquals("Sebral si zbraň 'Meč_vojáka' a vyměnil jí za svojí zbraň 'Dýka'", game.processCommand("seber Meč_vojáka"));
        
        assertEquals("Předmět 'Truhla' fakt neuneseš.", game.processCommand("seber Truhla"));
    }
}
