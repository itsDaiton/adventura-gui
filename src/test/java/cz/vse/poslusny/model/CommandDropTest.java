package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy CommandDrop.
 * V této tříde se testuje metoda process, tudíž jestli příkaz funguje.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandDropTest
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
     * Metoda testProcess testuje metodu "process" z příkazu "CommandDrop".
     *
     */
    @Test 
    public void testProcess()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Nevím, co mám upustit, musíš zadat název předmětu.", game.processCommand("vyhod"));
        assertEquals("Tomu nerozumím, neumím vyhodit více předmětů současně.", game.processCommand("vyhod něco něco"));
                      
        game.getGamePlan().getCurrentArea().isCombatHappening(true);
        assertEquals("Právě bojuješ s nepřítelem, nemůžeš vyhazovat předměty.", game.processCommand("vyhod Něco"));
        game.getGamePlan().getCurrentArea().isCombatHappening(false);
        
        assertEquals("Předmět 'Něco' nemáš v inventáři, nemůžeš ho vyhodit.", game.processCommand("vyhod Něco"));
        assertEquals("Předmět 'Koláč' nemáš v inventáři, nemůžeš ho vyhodit.", game.processCommand("vyhod Koláč"));
                        
        game.processCommand("seber Kuře");
        
        game.processCommand("jdi Město");
        assertEquals("Město", game.getGamePlan().getCurrentArea().getName());        
        
        assertEquals("Vyhodil(a) jsi předmět 'Kuře' a můžeš ho znova sebrat v této lokaci.", game.processCommand("vyhod Kuře"));
    }
}
