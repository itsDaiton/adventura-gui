package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy CommandFight.
 * V této tříde se testuje metoda process, tudíž jestli příkaz funguje.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandFightTest
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
     * Metoda testProcess testuje metodu "process" z příkazu "CommandFight".
     *
     */
    @Test 
    public void testProcess()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Nevím, na koho mám zaútočit, zadej svůj cíl.", game.processCommand("bojuj"));
        assertEquals("Tomu nerozumím, neumím bojovat s více nepřáteli naráz.", game.processCommand("bojuj Něco Něco"));
        
        assertEquals("Nemůžeš s nikým bojovat, nemáš nasazenou žádnou zbraň.", game.processCommand("bojuj Někdo"));
        
        game.processCommand("jdi Město");
        assertEquals("Město", game.getGamePlan().getCurrentArea().getName()); 
        
        game.processCommand("seber Dýka");
        
        assertEquals("Postava 'Bandita' tady není, nemůžeš s ní bojovat.", game.processCommand("bojuj Bandita"));
        
        assertEquals("Postava 'Starosta' je přátelská, nemůžeš s ní bojovat.", game.processCommand("bojuj Starosta"));
        
        game.processCommand("jdi Předměstí");
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Farma");
        assertEquals("Farma", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Most_přes_řeku");
        assertEquals("Most_přes_řeku", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Řeka");
        assertEquals("Řeka", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Polní_cesta");
        assertEquals("Polní_cesta", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("bojuj Bandita");
        game.getGamePlan().getMyCharacter().setHealthPoints(1);   
        game.processCommand("bojuj Bandita");
        assertTrue(game.isGameOver());
    }
}
