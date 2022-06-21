package cz.vse.poslusny.model;

/**
 * Třída implementující příkaz pro výměnu s postavami.
 * Třída řeší, co s kým se dá obchodovat a za jakých podmínek.
 *
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandTrade implements ICommand
{
   private static final String NAME = "vymen";
    
   private GamePlan plan;  
         
   /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */  
   public CommandTrade(GamePlan plan) {
       this.plan = plan;
   }
         
   /**
    * Metoda process představuje logiky příkazu "vymen".
    * Metoda řeší za jakých podmínek můžeme s postavou obchodovat, jestli náhodou nejsme v boji.
    * Při splnění podmínek určí jak výměna bude vypada a jaká předměty se budou vyměnovat.
    *
    * @param parameters parametry příkazu <i>(očekává se pole s dvěma prvky)</i>
    * @return informace pro hráče, které se vypíšou na konzoli
    */
   @Override
   public String process(String... parameters) {   
       if(parameters.length < 2) {
           return "Nevím co a s jakou postavou chceš obchodovat. Tento příkaz má dva parametry.";
       }
       
       Area area = plan.getCurrentArea();
       Inventory inventory = plan.getInventory();       
       String itemOffered = parameters[0];
       String charName = parameters[1];
       Item item = inventory.getItemByName(itemOffered);
       Person character = area.getCharacter(charName);
       Item weapon = inventory.getCurrentWeapon();
       
       if (area.isCombatHappening()) {
           return "Právě bojuješ s nepřítelem, nemůžeš vyměňovat předměty.";
       }
       
       if (!area.containsCharacter(charName)) {
            return "Postava '" + charName + "' tady není.";
       }
       
       if (!character.isTradeable() || character.isHostile()) {
           if(character.didTradeHappened()) {
               return "S touto postavou už nemůžeš znova obchodovat."; 
           }
           else {
               return "S touto postavou se nedá obchodovat.";
           }
       }
       
       if(weapon != null && itemOffered.equals(weapon.getName())) {
           return "Se zbraněmi nemůžeš obchodovat, nabízet můžeš pouze předměty z inventáře."; 
       }
       
       if (!inventory.containsItem(item)) {
           return "Předmět '" + itemOffered + "' nemáš v inventáři, obchodovat s ním nemůžeš.";
       }
      
       Item itemTraded = character.trade(item);
       
       if(character.didTradeHappened()) {
           inventory.removeItem(item);
           if(itemTraded.isWeapon()) {
               if(weapon == null) {
                   inventory.replaceCurrentWeapon(itemTraded);                 
               }
               else {
                   inventory.replaceCurrentWeapon(itemTraded);
                   area.addItem(weapon);
               }
           }
           else {
               inventory.addItem(itemTraded);              
           }
       }  
       
       return character.getTradeDialog();
   }
      
   /**
     * Metoda vrací název příkazu tj. slovo podle kterého se příkaz volá.
     * V tomto případě "vymen".
     *
     * @return název příkazu
     */
   @Override
   public String getName(){
       return NAME;
   }
}
