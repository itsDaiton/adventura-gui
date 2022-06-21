package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy CommandTalk.
 * V této tříde se testuje metoda process, tudíž jestli příkaz funguje.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandTalkTest
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
     * Metoda testProcess testuje metodu "process" z příkazu "CommandTalk".
     *
     */
    @Test 
    public void testProcess()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Nevím, s kým si mám promluvit, musíš zadat s kým si chceš promluvit.", game.processCommand("promluv"));
        assertEquals("Tomu nerozumím, neumím mluvit s více postavami současně.", game.processCommand("promluv něco něco")); 
        
        game.getGamePlan().getCurrentArea().isCombatHappening(true);
        assertEquals("Právě bojuješ s nepřítelem, nemůžeš mluvit s postavami.", game.processCommand("promluv Obchodník"));
        game.getGamePlan().getCurrentArea().isCombatHappening(false);
        
        assertEquals("Postava 'Starosta' tady není.", game.processCommand("promluv Starosta"));
        assertEquals("Zdravím tě, poutníku. Dneska mi měla přijet zásilka koření 'Miska_s_kořením', ale kurýr se asi někde zdržel." + "\nPokud mi nějaké koření přineseš, vymění ho s tebou za 'Klíč'.", game.processCommand("promluv Obchodník"));
        
        game.processCommand("jdi Farma");
        assertEquals("Farma", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Most_přes_řeku");
        assertEquals("Most_přes_řeku", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Řeka");
        assertEquals("Řeka", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Polní_cesta");
        assertEquals("Polní_cesta", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Haha! Dej mi všechny svoje peníze nebo tě podříznu!" + "\n\nNeměl si u sebe žádnou zbraň. 'Bandita' tě zabil.", game.processCommand("promluv Bandita"));
        assertTrue(game.isGameOver());
    }
}
