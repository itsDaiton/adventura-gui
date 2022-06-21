package cz.vse.poslusny.model;

/**
 * Třída implementující příkaz pro sbírání předmětů do inventáře a sbírání zbraní.
 * Třída řeší za jakých podmínek a jak se jednotlivé předměty a zbraně sbírají do inventáře.
 *
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandPick implements ICommand
{
    private static final String NAME = "seber";
    
    private GamePlan plan;
        
    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandPick(GamePlan plan) {
        this.plan = plan;
    }
        
    /**
    * Metoda process představuje logiky příkazu "seber".
    * Metoda řeší za jakých podmínek se předměty mohou sbírat do inventáře.
    * Kontroluje se jestli je předmět zbraň či jen věc a po splnění podmínek se přidá do inventáře
    * Také se kontroluje kapacita inventáře.
    *
    * @param parameters parametry příkazu <i>(očekává se pole s jedním prvkem)</i>
    * @return informace pro hráče, které se vypíšou na konzoli
    */
    @Override
    public String process(String... parameters)
    {
        if (parameters.length == 0) {
            return "Nevím, co mám sebrat, musíš zadat název předmětu.";
        }
        
        if (parameters.length > 1) {
            return "Nevím, co mám sebrat, musíš zadat jen jeden předmět.";
        }
        
        String itemName = parameters[0];
        Area area = plan.getCurrentArea();
        Inventory inventory = plan.getInventory();
        
        if (area.isCombatHappening()) {
            return "Právě bojuješ s nepřítelem, nemůžeš sbírat předměty.";
        }
        
        if (!area.containsItem(itemName)) {
            return "Předmět '" + itemName + "' tady není.";
        }
        
        Item item = area.getItem(itemName);
        
        if (!item.isMoveable()) {
            return "Předmět '" + itemName + "' fakt neuneseš.";
        }
                
        if(item.isWeapon()) {
            Item currentWeapon = inventory.getCurrentWeapon();
            inventory.replaceCurrentWeapon(item);
            area.removeItem(itemName);
            if(currentWeapon == null) {
                return "Sebral si zbraň '" + itemName + "', nyní je to tvoje aktuální zbraň.";
            }
            else {
                area.addItem(currentWeapon);
                return "Sebral si zbraň '" + itemName + "' a vyměnil jí za svojí zbraň '" + currentWeapon.getName() + "'";
            }
        }
        else {                    
            if(inventory.isFull()) {       
                return "Tvůj inventář je plný, předmět '" + itemName + "' nebylo možné sebrat,";
            }                    
        
            inventory.addItem(item);
            area.removeItem(itemName);
        
            return "Sebral(a) jsi předmět '" + itemName + "' a uložil jsi ho do inventáře.";
        }

    }
    
    /**
     * Metoda vrací název příkazu tj. slovo podle kterého se příkaz volá.
     * V tomto případě "seber".
     *
     * @return název příkazu
     */
    @Override
    public String getName()
    {
        return NAME;
    }
    
}
