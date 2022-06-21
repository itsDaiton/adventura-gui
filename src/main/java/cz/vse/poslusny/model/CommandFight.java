package cz.vse.poslusny.model;

import java.util.*;
/**
 * Třída implementující příkaz pro boj s postavami.
 * Rozhoduje s kterými postavami se dá bojovat a s kterými ne a za jakých podmínek.
 * Dále se rozhoduje co se stane po boji.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandFight implements ICommand
{
    private static final String NAME = "bojuj";
    
    private GamePlan plan; 
    private Game game;
    private Random rnd = new Random();
        
    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     * @param game odkaz na hru
     */
    public CommandFight(GamePlan plan, Game game)
    {
        this.plan = plan;
        this.game = game;
    }
        
    /**
    * Metoda process představuje logiky příkazu "bojuj".
    * V metodě jsou implementovány základní podmínky pro boj.
    * Samotný boj se počítá přes vzorce.
    * Je šance na kritický útok a na odražení.
    * Po souboji se rozhodne co dál s hrou.
    * Mrtvoly se dají prozkoumat
    *
    * @param parameters parametry příkazu <i>(očekává se pole s jedním prvkem)</i>
    * @return informace pro hráče, které se vypíšou na konzoli
    */
    @Override
    public String process(String... parameters)
    {
        if (parameters.length == 0) {
            return "Nevím, na koho mám zaútočit, zadej svůj cíl.";
        }
        
        if (parameters.length > 1) {
            return "Tomu nerozumím, neumím bojovat s více nepřáteli naráz.";
        }
              
        Area area = plan.getCurrentArea();
        MyCharacter myChar = plan.getMyCharacter();
        Inventory inventory = plan.getInventory();       
        Item weapon = inventory.getCurrentWeapon();
        String enemyName = parameters[0];
        String myCrit = "";
        String enemyCrit = "";
        
        int enemyHealth = 0;
        int enemyRawDamage = 0;
        int enemyRawParryValue = 0;
        
        if(weapon == null) {
            return "Nemůžeš s nikým bojovat, nemáš nasazenou žádnou zbraň.";
        }
        
        int myHealth = myChar.getHealthPoints();
        int myRawDamage = inventory.getCurrentWeapon().getDamage();
        int myRawParryValue = inventory.getCurrentWeapon().getParryValue();
        
        if (!area.containsCharacter(enemyName)) {
            return "Postava '" + enemyName + "' tady není, nemůžeš s ní bojovat.";
        }
        
        Person enemy = area.getCharacter(enemyName);
        
        if(!enemy.isHostile()) {
            return "Postava '" + enemyName + "' je přátelská, nemůžeš s ní bojovat.";
        }
        
        area.isCombatHappening(true);
                
        if(enemy.getName().contentEquals("Bandita")) {                                
            enemyHealth = enemy.getHealthPoints();
            enemyRawDamage = 40;
            enemyRawParryValue = 20;                  
        }
        else if(enemy.getName().equals("Bestie")) {            
            enemyHealth = enemy.getHealthPoints();
            enemyRawDamage = 60;
            enemyRawParryValue = 40; 
        }
        
        int critChance = 50;
        int criticalStrike = 150;
        int min = 1;
        int max = 100;
        
        int myCritChance = rnd.nextInt(max-min) + min;
        int enemyCritChance = rnd.nextInt(max-min) + min;
        int myParryChance = rnd.nextInt(max-min) + min;
        int enemyParryChance = rnd.nextInt(max-min) + min;
        
        int myCriticalDamage = (myRawDamage * criticalStrike) / 100;
        int enemyCriticalDamage = (enemyRawDamage * criticalStrike) / 100;
        
        String myParry = "";
        String enemyParry = "";
        
        if (myCritChance <= critChance) {
            myRawDamage = myCriticalDamage;
            myCrit += "kritickým zásahem";
        }
        else {
            myRawDamage = myRawDamage * 1;
        }
        
        if (enemyCritChance <= critChance) {
            enemyRawDamage = enemyCriticalDamage;
            enemyCrit += "kritickým zásahem";
        }
        else {  
            enemyRawDamage = enemyRawDamage * 1;
        }
        
        if(enemyParryChance <= 50) {
            if((myRawDamage - enemyRawParryValue) < 0) {
                myRawDamage = 0;
            }
            else {
                myRawDamage = myRawDamage - enemyRawParryValue;
            }
            enemyParry = " Protivníkovy se podařilo odrazit část rány a snížit poškození úderu o " + enemyRawParryValue + ".";            
        }        
        else {
            myRawDamage = myRawDamage * 1;
            enemyParry = "";
        }
        
        if(myParryChance <= 50) {
            if((enemyRawDamage - myRawParryValue) < 0) {
                enemyRawDamage = 0;
            }
            else {
                enemyRawDamage = enemyRawDamage - myRawParryValue;
            }
            myParry += " Podařilo se ti odrazit část rány a snížit poškození úderu o " + myRawParryValue + "."; 
        }
        else {
            enemyRawDamage = enemyRawDamage * 1;
            myParry = "";
        }
        
        int myHit = myRawDamage;
        int enemyHit = enemyRawDamage;
        
        myHealth = myHealth - enemyHit;
        enemyHealth = enemyHealth - myHit;
        
        myChar.setHealthPoints(myHealth);
        enemy.setHealthPoints(enemyHealth);
        
        String combatInfo = enemy.getName() + " tě uděřil "+ enemyCrit + " za " + enemyHit + " životů." + myParry + " Nyní máš " + myHealth + " životů. " + 
        " \nUdeřil si protivníka ' " + enemy.getName() + "'" + myCrit + " za " + myHit + "." + enemyParry + " Jeho životy jsou nyní " + enemyHealth + ".";
                
        if (myHealth <= 0 && enemyHealth <= 0) {
            game.setGameOver(true);
            return combatInfo + 
            "\n\nTy a '" + enemy.getName() + "' jste sobě udělili smrtelné rány, oba umíráte. Nepodařilo se ti zachránit obyvatele města Riften. Prohrál(a) jsi.";
        }
        
        if (enemyHealth <= 0) {
            Item mrtvola;
            area.isCombatHappening(false);
            area.removeCharacter(enemy.getName());
            if(enemy.getName().equals("Bandita")) {
                mrtvola = new Item("Mrtvola_bandity","Toto je mrtvola bandity, kterého si zabil.", false, false, 0, 0);
                area.addItem(mrtvola);
            } 
            else {
                mrtvola = new Item("Mrtvola_bestie", "Před tebou leží mrtvola bestie, kterou si zabil.", false, false, 0, 0);
                area.addItem(mrtvola);
            }
            
            return combatInfo +
            "\n\nPodařilo se ti ubránit proti nepříteli " + enemy.getName() + " a zabít ho! Po souboji ti zbylo " + myHealth + " životů. Před sebou vidíš '" + mrtvola.getName() +"' můžeš ji prozkoumat."; 
        }
        
        if (myHealth <= 0) {
            game.setGameOver(true);
            return combatInfo +
            "\n\nZemřel si, nepodařilo se ti zachránit obyvatele města Riften. Prohrál(a) jsi.";
        }
        
        return combatInfo;      
        
    }
    
    /**
     * Metoda vrací název příkazu tj. slovo podle kterého se příkaz volá.
     * V tomto případě "bojuj".
     *
     * @return název příkazu
     */
    public String getName() {
        return NAME;     
    }
}
