package cz.vse.poslusny.model;
import java.util.*;

/**
 * Třída implementující příkaz pro výpiš předmětů z inventáře.
 * Vypíší se také životy a aktuální zbraň.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandInventory implements ICommand
{
    private static final String NAME = "inventar";
    
    private GamePlan plan;
        
    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandInventory(GamePlan plan) {
        this.plan = plan;
    }
        
    /**
    * Metoda process představuje logiky příkazu "inventar".
    * Metoda řeší jak se bude vypisovat inventář podle toho jestli máme zbraň, jestli máme předměty.
    * Upravuje se zde rětězec, který se vrací na konzoli.
    * 
    *
    * @param parameters parametry příkazu <i>(očekává se pole s žádným prvkem)</i>
    * @return informace pro hráče, které se vypíšou na konzoli
    */
    @Override
    public String process(String... parameters)
    {
        if(parameters.length > 0) {
            return "Příkaz '" + NAME + "' se používá bez parametrů a vypíše všechny předměty v inventáři a aktuální zbraň.";
        }
        
        Inventory inventory = plan.getInventory();
        Area area = plan.getCurrentArea();
        List<Item> items = new ArrayList(inventory.getItems());
        MyCharacter myChar = plan.getMyCharacter();
        Item currentWeapon = inventory.getCurrentWeapon();
        Item weapon = inventory.getCurrentWeapon();
        String inventoryList = "Tvůj inventář obsahuje tyto předměty:"; 
        String weaponSlot = "Tvoje aktuální zbraň je ";
        String health = "Tvoje životy: ";
        int healthPoints = myChar.getHealthPoints();
        health += healthPoints;
        
        if (area.isCombatHappening()){
            return "Právě bojuješ s nepřítelem, nemůžeš si prohlížet inventář.";
        }
        
        if(items.isEmpty() && currentWeapon == null) {
            return "Tvůj inventář je prázdný." + "\n" + "Nemáš nasazenou žádnou zbraň." + "\n" + health;
        }
        else if(items.isEmpty() && currentWeapon != null) {
            weaponSlot += "'" + weapon.getName() + "'" + "\n- Poškození zbraně: " + weapon.getDamage() + "\n- Hodnota odražení: " + weapon.getParryValue();
            return "Tvůj inventář je prázdný." + "\n" + weaponSlot + "\n" + health;
        }
        else if(!items.isEmpty()) {
            for(Item i : items) {
                if(items.contains(i)) {
                    inventoryList += "\n- " + i.getName();
                }

            }
            
            if (currentWeapon == null) {
                weaponSlot = "Nemáš nasazenou žádnou zbraň.";
            }
            else {
                weaponSlot += "'" + weapon.getName() + "'" + "\n- Poškození zbraně: " + weapon.getDamage() + "\n- Hodnota odražení: " + weapon.getParryValue();
            }            
        }
        
        return inventoryList + "\n" + weaponSlot +"\n" + health;
    }
        
    /**
     * Metoda vrací název příkazu tj. slovo podle kterého se příkaz volá.
     * V tomto případě "inventar".
     *
     * @return název příkazu
     */
    @Override
    public String getName()
    {
        return NAME;
    }
}

