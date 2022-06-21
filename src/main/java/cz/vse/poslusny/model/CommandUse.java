package cz.vse.poslusny.model;

/**
 * Třída implementující příkaz pro používání předmětů z invetáře.
 * Třída řeší, co jednotlivé předměty po použití udělají a jaké jsou podmínky jejich použití.
 *
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandUse implements ICommand
{
   private static final String NAME = "pouzij";
    
   private GamePlan plan;  
       
   /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */ 
   public CommandUse(GamePlan plan){
       this.plan = plan;
   }
   
   /**
    * Metoda process představuje logiky příkazu "pouzij".
    * Metoda kontroluje, jestli se nebojuje s nepřítelem.
    * Dále kontroluje jestli je předmět v inventáři a poté co který předmět po použití dělá.
    *
    * @param parameters parametry příkazu <i>(očekává se pole s jedním prvkem)</i>
    * @return informace pro hráče, které se vypíšou na konzoli
    */
   @Override
   public String process(String... parameters){
       if (parameters.length == 0) {
           return "Nevím co mám použít, zadej název předmětu.";
       }
       
       if (parameters.length > 1) {
           return "Tomu nerozumím, neumím použít více předmětů současně.";
       }
       
       Area area = plan.getCurrentArea();
       Inventory inventory = plan.getInventory();
       String itemName = parameters[0];
       Item item = inventory.getItemByName(itemName);
       Item weapon = inventory.getCurrentWeapon();
       MyCharacter myChar = plan.getMyCharacter();
       
       if (area.isCombatHappening()) {
           return "Právě bojuješ s nepřítelem, nemůžeš pužívat předměty.";
       }
                     
       if(area.containsItem(itemName)) {
           return "Předmět '" + itemName + "' musíš nejdříve sebrat do inventáře.";
       }
       
       if(!inventory.containsItem(item)) {
           return "Předmět '" + itemName + "' nemáš v inventáři, nemůžeš ho použít.";
       }
       
       String vypil = "Vypl si ";
       String snedl = "Snědl si ";
       
       switch (itemName) {
           case "Pivo":
                if(weapon == null) {
                    myChar.changeHealthPoints(5);
                    inventory.removeItem(item);
                    return vypil + "'" + itemName + "', životy se ti zvýšili o 5.";
                }
                else {
                    weapon.setDamage(-10);
                    inventory.removeItem(item);
                    return vypil + "'" + itemName + "', poškození na tvé aktuální zbraňi se ti snížilo o 10."; 
                }
           case "Koláč":
                myChar.changeHealthPoints(15);
                inventory.removeItem(item);
                return snedl + "'" + itemName + "', životy se ti zvýšili o 15.";
           case "Kuře":
                myChar.changeHealthPoints(30);
                inventory.removeItem(item);
                return snedl + "'" + itemName + "', životy se ti zvýšili o 30.";
           case "Lektvar":
                myChar.changeHealthPoints(60);
                inventory.removeItem(item);
                return vypil + "'" + itemName + "', životy se ti zvýšili o 60.";
           case "Borůvky":
                myChar.changeHealthPoints(10);
                inventory.removeItem(item);
                return snedl + "'" + itemName + "', životy se ti zvýšili o 10.";
           case "Miska_s_kořením":
                myChar.changeHealthPoints(5);
                if(weapon == null) {
                    inventory.removeItem(item);
                    return snedl + "'" + itemName + "', životy se ti zvýšili o 5.";
                }
                else {
                    weapon.setDamage(5);
                    inventory.removeItem(item);
                    return snedl + "'" + itemName + "', životy se ti zvýšili o 5 a poškození na tvé aktuální zbraňi se ti zvýšilo o 5.";
                }
           case "Kukuřice":
                myChar.changeHealthPoints(10);
                inventory.removeItem(item);
                return snedl + "'" + itemName + "', životy se ti zvýšili o 10.";
           case "Džbán_s_vodou":
                myChar.changeHealthPoints(25);
                inventory.removeItem(item);
                return vypil + "'" + itemName + "', životy se ti zvýšili o 25.";
           case "Klíč":
                if(area.getName().equals("Předměstí")) {
                    Area vez = area.getExitArea("Opuštěná_věž");
                    vez.lock(false);
                    inventory.removeItem(item);
                    return "Odemkl jsi lokaci '" + vez.getName() + "' klíčem '" + itemName + "'.";
                }
                else {
                    return "Klíč nebylo možné použít, v této lokaci neexistuje zamčený východ, který by tento klíč otevíral.";
                }
           case "Hlava_bestie":
                return "Předmět '" + itemName + "' nemůžeš použít.";
           case "Kouzelný_lektvar":
                if(weapon == null) {
                    myChar.changeHealthPoints(75);
                    inventory.removeItem(item);
                    return vypil + "'" + itemName + "', životy se ti zvýšili o 75.";
                }
                else {
                    weapon.setDamage(25);
                    inventory.removeItem(item);
                    return vypil + "'" + itemName + "', poškození na tvé aktuální zbraňi se ti zvýšilo o 25."; 
                }
           case "Odměna":
                inventory.removeItem(item);
                plan.setVictorious(true);
                return "Poukázka na 1000 zlatých zaklínači Edwardovi za mimořádnou pomoc městu a jeho obyvatelům.\nPodepsáno starostou města Riften.\nTento dokument lze uplatnit ve všech bankách v zemi Tamriel.";
           default:
                return "Předmět nemůžeš takhle použít, přečti si co dělá a zkus to znovu.";
       }
   }
     
   /**
     * Metoda vrací název příkazu tj. slovo podle kterého se příkaz volá.
     * V tomto případě "pouzij".
     *
     * @return název příkazu
     */
   @Override
   public String getName() {
       return NAME;
   }
}
