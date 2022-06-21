package cz.vse.poslusny.model;

/**
 * Třída implementující příkaz pro vyhazování předmětů z inventáře.
 * Třída řeší za jakých podmínek a jak se jednotlivé předměty vyhazují.
 *
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandDrop implements ICommand
{
    private static final String NAME = "vyhod";
    
    private GamePlan plan;
       
    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */  
    public CommandDrop(GamePlan plan) {
        this.plan = plan;
    }
       
    /**
    * Metoda process představuje logiky příkazu "vyhod".
    * Metoda řeší za jakých podmínek mužeme předměty vyhazovat.
    * Po splnění podmínek se předmět vymaže z inventáře a vyhodí do lokace
    * Zbraně se vyhazovat nemůžou.
    *
    * @param parameters parametry příkazu <i>(očekává se pole s jedním prvkem)</i>
    * @return informace pro hráče, které se vypíšou na konzoli
    */
    @Override
    public String process(String... parameters) {
        if(parameters.length == 0) {
            return "Nevím, co mám upustit, musíš zadat název předmětu.";
        }
        
        if(parameters.length > 1) {
            return "Tomu nerozumím, neumím vyhodit více předmětů současně.";
        }
        
        String itemName = parameters[0];
        Area area = plan.getCurrentArea();
        Inventory inventory = plan.getInventory();
        Item item = inventory.getItemByName(itemName);
        Item weapon = inventory.getCurrentWeapon();
        String dropInfo = "";             
        
        if (area.isCombatHappening()) {
            return "Právě bojuješ s nepřítelem, nemůžeš vyhazovat předměty.";
        } 
        
        if(!inventory.containsItem(item)) {
            return "Předmět '" + itemName +"' nemáš v inventáři, nemůžeš ho vyhodit.";
        } 
        
        inventory.removeItem(item);
        dropInfo += "Vyhodil(a) jsi předmět '" + itemName + "' a můžeš ho znova sebrat v této lokaci.";       
        area.addItem(item);
        return dropInfo;
    }
       
    /**
     * Metoda vrací název příkazu tj. slovo podle kterého se příkaz volá.
     * V tomto případě "vyhod".
     *
     * @return název příkazu
     */    
    @Override
    public String getName() {
        return NAME;
    }
}
