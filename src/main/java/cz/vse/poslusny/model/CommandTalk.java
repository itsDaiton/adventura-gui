package cz.vse.poslusny.model;

/**
 * Třída implementující příkaz pro mluvení s postavami.
 * Rozhoduje s kterými postavami se dá mluvit a s kterými ne.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandTalk implements ICommand
{
    private static final String NAME = "promluv";
    
    private GamePlan plan;
    private Game game;
        
    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     * @param game odkaz na hru
     */
    public CommandTalk(GamePlan plan, Game game) {
        this.plan = plan;
        this.game = game;
    }
        
    /**
    * Metoda process představuje logiky příkazu "promluv".
    * Metoda řeší jak a za jakých podmínek se bude moct mluvit s postavami.
    * Pokud se splní základní podmínky mluvení, tak se ještě kontroluje, jestli se mluví
    * s nepřítelem a hráč nemá zbraň, tak že zemře a hra končí.
    *
    * @param parameters parametry příkazu <i>(očekává se pole s jedním prvkem)</i>
    * @return informace pro hráče, které se vypíšou na konzoli
    */
    @Override
    public String process(String... parameters)
    {        
        if (parameters.length == 0) {
            return "Nevím, s kým si mám promluvit, musíš zadat s kým si chceš promluvit.";
        }
        
        if (parameters.length > 1) {
            return "Tomu nerozumím, neumím mluvit s více postavami současně.";
        }
        
        Area area = plan.getCurrentArea();
        String charName = parameters[0];
        Person character = area.getCharacter(charName);
        Inventory inventory = plan.getInventory();
        Item weapon = inventory.getCurrentWeapon();
        
        if (area.isCombatHappening()) {
            return "Právě bojuješ s nepřítelem, nemůžeš mluvit s postavami.";
        }
                
        if (!area.containsCharacter(charName))  {
            return "Postava '" + charName + "' tady není.";
        }
        
        if (character.isHostile()) {
            area.isCombatHappening(true);
            
            if (weapon == null) {
                game.setGameOver(true);
                return character.getDialog() + "\n\nNeměl si u sebe žádnou zbraň. '" + character.getName() + "' tě zabil.";
            }
            
            return character.getDialog() + "\n" + "Začal souboj s '" + charName + "'! Musíš se ubránit nebo zemřeš!";
        }
                       
        return character.getDialog();
    }
        
    /**
     * Metoda vrací název příkazu tj. slovo podle kterého se příkaz volá.
     * V tomto případě "promluv".
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return NAME;
    }

}
