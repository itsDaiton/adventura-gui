package cz.vse.poslusny.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testovací třída pro komplexní otestování herního příběhu.
 *
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author David Poslušný
 * @version LS 2020
 */
public class GameTest
{
    private Game game;

    /**
     * Metoda setUp připravuje instance, které bude potřebné k testování třídy.
     *
     */
    @Before
    public void setUp()
    {
        game = new Game();
    }

    /**
     * Metoda testPlayerQuit testuje funkčnost příkazu Terminate.
     */
    @Test
    public void testPlayerQuit()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());

        game.processCommand("jdi Farma");
        assertEquals("Farma", game.getGamePlan().getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("jdi Most_přes_řeku");
        assertEquals("Most_přes_řeku", game.getGamePlan().getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("konec");
        assertTrue(game.isGameOver());
    }

    /**
     * Metoda testPlayerLose testuje, jestli hráč prohraje při situacích, v kterých má prohrát.
     */
    @Test
    public void testPlayerLose()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Farma");
        assertEquals("Farma", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Most_přes_řeku");
        assertEquals("Most_přes_řeku", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Řeka");
        assertEquals("Řeka", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Polní_cesta");
        assertEquals("Polní_cesta", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("promluv Bandita");
        assertTrue(game.isGameOver());
    }

    /**
     * Metoda testPlayerWin testuje, jestli hráč vyhraje v situacích, v kterých má vyhrát.
     */
    @Test
    public void testPlayerWin()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Město");
        assertEquals("Město", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("seber Miska_s_kořením");
        
        game.processCommand("jdi Předměstí");
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("vymen Miska_s_kořením Obchodník");
        
        game.processCommand("jdi Farma");
        assertEquals("Farma", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Most_přes_řeku");
        assertEquals("Most_přes_řeku", game.getGamePlan().getCurrentArea().getName());
        game.processCommand("seber Džbán_s_vodou");
        
        game.processCommand("jdi Farma");
        assertEquals("Farma", game.getGamePlan().getCurrentArea().getName());
                
        game.processCommand("jdi Předměstí");
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("pouzij Klíč");        
        game.processCommand("jdi Opuštěná_věž");
        assertEquals("Opuštěná_věž", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("vymen Džbán_s_vodou Umírající_voják"); 
        game.processCommand("prozkoumej Truhla");
        game.processCommand("seber Meč_vojáka");
        
        game.processCommand("jdi Farma");
        assertEquals("Farma", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("pouzij Lektvar");
        
        game.processCommand("jdi Most_přes_řeku");
        assertEquals("Most_přes_řeku", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Řeka");
        assertEquals("Řeka", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Polní_cesta");
        assertEquals("Polní_cesta", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Les");
        assertEquals("Les", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Temný_Háj");
        assertEquals("Temný_Háj", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("bojuj Bestie");

        Person bestie = game.getGamePlan().getCurrentArea().getCharacter("Bestie");
        bestie.setHealthPoints(0);
        game.processCommand("bojuj Bestie");        
        
        game.processCommand("prozkoumej Mrtvola_bestie");
        game.processCommand("seber Hlava_bestie");
        
        game.processCommand("jdi Les");
        assertEquals("Les", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Polní_cesta");
        assertEquals("Polní_cesta", game.getGamePlan().getCurrentArea().getName());       
        
        game.processCommand("jdi Předměstí");
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Město");
        assertEquals("Město", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("vymen Hlava_bestie Starosta");
        assertTrue("Odměna", game.getGamePlan().getInventory().containsItem(game.getGamePlan().getInventory().getItemByName("Odměna")));
        
        game.processCommand("pouzij Odměna");
        game.getGamePlan().setVictorious(true);
        assertEquals(true, game.getGamePlan().isVictorious());
        assertTrue(game.isGameOver());
        
    }

}
