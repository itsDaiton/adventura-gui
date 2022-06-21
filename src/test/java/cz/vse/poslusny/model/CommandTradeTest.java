package cz.vse.poslusny.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy CommandTrade.
 * V této tříde se testuje metoda process, tudíž jestli příkaz funguje.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandTradeTest
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
     * Metoda testProcess testuje metodu "process" z příkazu "CommandTrade".
     *
     */
    @Test 
    public void testProcess()
    {
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Nevím co a s jakou postavou chceš obchodovat. Tento příkaz má dva parametry.", game.processCommand("vymen"));
        assertEquals("Nevím co a s jakou postavou chceš obchodovat. Tento příkaz má dva parametry.", game.processCommand("vymen něco"));
        
        game.getGamePlan().getCurrentArea().isCombatHappening(true);
        assertEquals("Právě bojuješ s nepřítelem, nemůžeš vyměňovat předměty.", game.processCommand("vymen Něco někomu"));
        game.getGamePlan().getCurrentArea().isCombatHappening(false);
        
        game.processCommand("seber Kuře");
        
        assertEquals("Postava 'Někomu' tady není.", game.processCommand("vymen Kuře Někomu"));
        
        game.processCommand("jdi Město");
        assertEquals("Město", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("seber Miska_s_kořením");
        game.processCommand("seber Dýka");
        
        assertEquals("Se zbraněmi nemůžeš obchodovat, nabízet můžeš pouze předměty z inventáře.", game.processCommand("vymen Dýka Starosta"));
        assertEquals("Předmět 'Koláč' nemáš v inventáři, obchodovat s ním nemůžeš.", game.processCommand("vymen Koláč Starosta"));
        
        game.processCommand("jdi Předměstí");
        assertEquals("Předměstí", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("Tohle přeci není koření, vrať se až si nějaké opatříš.", game.processCommand("vymen Kuře Obchodník"));
        assertEquals("Ano! to je přesně co potřebuji, tady máš svoji odměnu.", game.processCommand("vymen Miska_s_kořením Obchodník"));
        
        assertEquals("S touto postavou už nemůžeš znova obchodovat.", game.processCommand("vymen Kuře Obchodník"));
        
        game.processCommand("jdi Farma");
        assertEquals("Farma", game.getGamePlan().getCurrentArea().getName());
        
        game.processCommand("jdi Most_přes_řeku");
        assertEquals("Most_přes_řeku", game.getGamePlan().getCurrentArea().getName());
                
        game.processCommand("jdi Řeka");
        assertEquals("Řeka", game.getGamePlan().getCurrentArea().getName());
        
        assertEquals("S touto postavou se nedá obchodovat.", game.processCommand("vymen Kuře Lovec"));
    }
}
