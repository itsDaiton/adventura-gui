package cz.vse.poslusny.model;

/**
 * Třída implementující příkaz pro prozkoumání předmětů v lokacích a v inventáři.
 * Kontroluje se jestli se s věcí dá hýbat, jestli je v inventáři nebo lokaci.
 * Pokud se jedná o nehybnou věc, tak se do lokace přidá nová věc a prozkoumat ji půjde poté normálně jako pohyblivou věc.
 * Pokud podmínky projdou, vypíše se informativní text o předmětu.
 *
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandInspect implements ICommand
{
    private static final String NAME = "prozkoumej";
    
    private GamePlan plan;
    
    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandInspect(GamePlan plan) {
        this.plan = plan;
    }
    
    /**
     * Metoda process představuje logiky příkazu "prozkoumej".
     * Po klasické kontrole vstupu z konzole se kontroluje jestli v lokaci neprobíhá boj.
     * Pokud se předmět nachází v lokaci nebo v inventáři, tak příkaz vypíše její popis.
     * Pokud se jedná o vybavenou zbraň nebo předmět není v lokaci, tak příkaz vypíše, že předmět se prozkoumat nedá.
     * Pokud je předmět nepohyblivý, tak se k výpisu přidá poznámka s totou skutečností.
     * Některé předměty se dají prozkoumat a přidají do lokace nový předmět, např. Truhla, Křoví.
     * Po opětovaném prozkoumání předmět začne vypisovat normální výpis jako pro pohyblivé předměty.
     * 
     * @param parameters parametry příkazu <i>(očekává se pole s jedním prvkem)</i>
     * @return informace pro hráče, které se vypíšou na konzoli
     */
    public String process(String... parameters) {
        if (parameters.length == 0) {
            return "Nevím, co mám prozkoumat, musíš zadat název předmětu.";
        }
        
        if (parameters.length > 1) {
            return "Tomu nerozumím, neumím prozkoumat více předmětů současně.";
        }
        
        String itemName = parameters[0];
        Area area = plan.getCurrentArea();
        Inventory inventory = plan.getInventory();               
        String note = "";
        String start = "Popis předmětu: ";
        
        if (area.isCombatHappening()) {
            return "Právě bojuješ s nepřítelem, nemůžeš zkoumat předměty.";
        }
        
        if(inventory.containsItem(inventory.getItemByName(itemName))) {
            return start + inventory.getItemByName(itemName).getDescription();
        }
        
        if(!area.containsItem(itemName)) {            
            return "Předmět '" + itemName + "' tady není nebo se jedná o aktuální vybavenou zbraň.";
        }
        
        Item item = area.getItem(itemName);
                                
        if (!item.isMoveable()) {  
            note += "\nPoznámka: S tímto předmětem hýbat nemůžeš.";
        }
        
        //úpravy pro vyřešení AvoidDuplicateLiterals v PMD
        String info = start + item.getDescription() + note;
        String stringBestie = "Mrtvola_bestie";
        String stringNasel = "a našel si v ní";
        String stringProhledal = "Prohledal jsi věc";
        String stringBandita = "Mrtvola_bandity";
        String stringTruhla = "Truhla";
        String stringKrovi = "Křoví";
             
        if (!item.isMoveable() && !item.isInspected()) {
            
            switch (itemName) {
                case "Mrtvola_bestie":
                        Item mrtvolaBestie = area.getItem(stringBestie);
                        Item hlavaBestie = plan.getHead();            
                        area.addItem(hlavaBestie);
                        mrtvolaBestie.inspected(true);
                        return stringProhledal + " '" + mrtvolaBestie.getName() + "' " + stringNasel + " '" + hlavaBestie.getName() + "'.";
                case "Mrtvola_bandity":
                        Item mrtvolaBandity = area.getItem(stringBandita);
                        Item bandituvMec = new Item("Meč_bandity", "Meč, který u sebe měl bandita.", true, true, 95, 40);             
                        Item kouzelnyLektvar = new Item("Kouzelný_lektvar", "Lektvar, který si našel v intventáři bandity.", true, false, 0, 0);
                        area.addItem(bandituvMec);
                        area.addItem(kouzelnyLektvar);
                        mrtvolaBandity.inspected(true);
                        return stringProhledal + " '" + mrtvolaBandity.getName() + "' " + stringNasel + " '" + bandituvMec.getName() + "' a '" + kouzelnyLektvar.getName() + "'."; 
                case "Truhla":
                        Item truhla = area.getItem(stringTruhla);
                        Item vojakuvMec = new Item("Meč_vojáka", "Meč, který ti věnoval umírající voják", true, true, 85, 40); 
                        area.addItem(vojakuvMec);
                        truhla.inspected(true);
                        return stringProhledal + " '" + truhla.getName() + "' " + stringNasel + " '" + vojakuvMec.getName() + "'.";
                case "Křoví":
                        Item krovi = area.getItem(stringKrovi);
                        Item boruvky = new Item("Borůvky", "Sladké borůvky natrhané v lese.", true, false, 0, 0);
                        area.addItem(boruvky);
                        krovi.inspected(true);
                        return stringProhledal + " '" + krovi.getName() + "' " + stringNasel + " '" + boruvky.getName() + "'.";
                default:
                        return "";
            }
        }
        
        return info;
    }
    
    /**
     * Metoda vrací název příkazu tj. slovo podle kterého se příkaz volá.
     * V tomto případě "prozkoumej".
     *
     * @return název příkazu
     */
    public String getName() {
        return NAME;
    }
}
