package cz.vse.poslusny.model;

/**
 * Třída implementující příkaz pro pohyb mezi herními lokacemi.
 * Kontroluje jestli jsou lokace zavřené a nebo jestli se v lokaci vede souboj.
 *
 * @author Jarmila Pavlíčková
 * @author Luboš Pavlíček
 * @author Jan Říha
 * @author David Poslušný
 * @version LS 2020
 */
public class CommandMove implements ICommand
{
    private static final String NAME = "jdi";

    private GamePlan plan;

    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandMove(GamePlan plan) {
        this.plan = plan;
    }

    /**
     * Metoda se pokusí přesunout hráče do jiné lokace. Nejprve zkontroluje počet
     * parametrů. Pokud nebyl zadán žádný parametr <i>(tj. neznáme cíl cesty)</i>,
     * nebo bylo zadáno dva a více parametrů <i>(tj. hráč chce jít na více míst
     * současně)</i>, vrátí chybové hlášení. Pokud byl zadán právě jeden parametr,
     * zkontroluje, zda s aktuální lokací sousedí jiná lokace s daným názvem <i>(tj.
     * zda z aktuální lokace lze jít do požadovaného cíle)</i>. Pokud ne, vrátí
     * chybové hlášení. Také se kontroluje, jestli v lokaci neprobíhá boj a jestli není lokace zamčená.
     * Pokud všechny kontroly proběhnou v pořádku, provede přesun
     * hráče do cílové lokace a vrátí její popis.
     *
     * @param parameters parametry příkazu <i>(očekává se pole s jedním prvkem)</i>
     * @return informace pro hráče, které se vypíšou na konzoli
     */
    @Override
    public String process(String... parameters) {
        if (parameters.length == 0) {
            return "Nechápu, kam mám jít. Musíš mi zadat nějaký cíl.";  // Pokud hráč nezadá žádný parametr (cíl cesty)
        } 
        else if (parameters.length > 1) {
            return "Nechápu, co po mě chceš. Neumím se 'rozdvojit' a jít na více míst současně.";  // Pokud hráč zadá více parametrů
        }

        // Název cílové lokace si uložíme do pomocné proměnné
        String exitName = parameters[0];

        // Zkusíme získat odkaz na cílovou lokaci
        Area exitArea = plan.getCurrentArea().getExitArea(exitName);
        Area currentArea = plan.getCurrentArea();
        
        if (currentArea.isCombatHappening()) {
            return "Právě bojuješ s nepřítelem, nemůžeš z lokace odejít.";
        }

        if (exitArea == null) {
            return "Tam se ale odsud jít nedá!";  // Pokud hráč zadal název lokace, která nesousedí s aktuální
        }
        else {
            if(exitArea.isLocked()) {
                return "Místnost '" + exitArea.getName() + "' je zavřená. Musíš najít způsob jak ji otevřít.";
            }
        }

        // Změníme aktuální lokaci (přesuneme hráče) a vrátíme popis nové lokace
        plan.setCurrentArea(exitArea);
        return exitArea.getFullDescription();
    }

    /**
     * Metoda vrací název příkazu tj. slovo podle kterého se příkaz volá.
     * V tomto případě "jdi".
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return NAME;
    }

}
