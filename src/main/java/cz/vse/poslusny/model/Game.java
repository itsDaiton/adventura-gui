package cz.vse.poslusny.model;

/**
 * Hlavní třída logiky aplikace. Třída vytváří instanci třídy {@link GamePlan},
 * která inicializuje lokace hry, a vytváří seznam platných příkazů a instance
 * tříd provádějících jednotlivé příkazy.
 *
 * Během hry třída vypisuje uvítací a ukončovací texty a vyhodnocuje jednotlivé
 * příkazy zadané uživatelem.
 *
 * @author Michael Kölling
 * @author Luboš Pavlíček
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author David Poslušný
 * @version LS 2020
 */
public class Game implements IGame
{
    private ListOfCommands listOfCommands;
    private GamePlan gamePlan;
    private boolean gameOver;

    /**
     * Konstruktor třídy. Vytvoří hru, inicializuje herní plán udržující
     * aktuální stav hry a seznam platných příkazů ve hře.
     */
    public Game() {
        gameOver = false;
        gamePlan = new GamePlan();
        listOfCommands = new ListOfCommands();

        listOfCommands.addCommand(new CommandHelp(listOfCommands));
        listOfCommands.addCommand(new CommandTerminate(this));
        listOfCommands.addCommand(new CommandMove(gamePlan));
        listOfCommands.addCommand(new CommandPick(gamePlan));
        listOfCommands.addCommand(new CommandInspect(gamePlan));
        listOfCommands.addCommand(new CommandInventory(gamePlan));
        listOfCommands.addCommand(new CommandDrop(gamePlan));
        listOfCommands.addCommand(new CommandTalk(gamePlan, this));
        listOfCommands.addCommand(new CommandTrade(gamePlan));
        listOfCommands.addCommand(new CommandUse(gamePlan));
        listOfCommands.addCommand(new CommandFight(gamePlan, this));
    }

    /**
     * Metoda getPrologue vrací úvodní text, který hráče seznámí s příběh a cílem hry.
     *
     * @return Aktuální lokace, ve které se hráč ocitne, když zapne hru.
     */   
    @Override
    public String getPrologue() {
        return "Vítejte v fantasy textové hře!\n"
                + "Příběh hry:\nHlavní postavou je zaklínač jménem Edward, který byl jako malý kluk odvlečen k cechu zaklínkačů a po náročné proceduře se z něj stal jeden z nich.\n"
                + "Edward cestuje po zemi Tamriel a vyhledává monstra, která ohrožují města a vesnice a za peníze tyto monstra zabíjí.\n"
                + "Príběh hry začíná, když Edward přijde do města Riften. Zde se dozví, že toto město sužuje bestie, která loví místní dobytek a zabíjí občany města.\n"
                + "Jediné informace, které Edward zjistí jsou, že bestie loví pouze v noci a že nikdo neví jak vypadá, je pouze slyšet její hruzostrašné kvílení.\n"
                + "Detaily a také výši odměny se Edward může dozvědět od starosty města, který bydlí ve městě.\n"
                + "Hra začíná když Edward přijde k předměstí města Riften, po tom co si vyslech historky poutníka.\n"
                + "\n\n"
                + "Cíl hry:\n"
                + "Cílem hry je zabít bestii, která sužuje město Riften. Po zabití je nutno odebrat její hlavu a přinést jí starostovi města.\n"
                + "Hra končí výhrou pokud hráč úspěšně odnese hlavu besti starostovi a proběhne výměna pomocí příkazu 'vymen'.\n"
                + "Hra končí prohrou pokud hráč zemře při boji s nepřátelskými postavami.\n"
                + "V průběhu hry může hráč interaktovat s postavami, sbírat a vyhazovat předměty, zkoumat předměty, chodit mezi lokacemi,\n"
                + "obchodovat s postavami, používat předměty," 
                + "vypisovat aktuální obsah invetáře, zjistit zbývající životy"
                + "a bojovat s nepřáteli."
                + "\n\n"
                + gamePlan.getCurrentArea().getFullDescription();
    }    

    /**
     * Metoda getEpilogue vrací text po ukončení konzole a vypnutí hry.
     * Text se upravuje podle toho jestli hráč vyhrál nebo prohrál.
     *
     * @return Text při konci konzole a vyhodnocení hry (výhra/prohra)
     */
    @Override
    public String getEpilogue() {
        String epilogue = "Díky, že sis zahrál(a).";

        if (gamePlan.isVictorious()) 
        {
            epilogue = "Zvítězil(a) jsi !\n\n" + epilogue;
        }
        return epilogue;
    }

    /**
     * Metoda isGameOver vrací informaci, jestli hra skončila.
     *
     * @return gameOver Skutečnost, jestli hra skončila
     */
    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Metoda processCommand přečte input z konzole a zjistí o jaký příkaz se jedná.
     * V případě, že žádný příkaz nerozpozná, tak vrátí rětězec, že příkaz nezná.
     *
     * @param line Text který mi napíšeme na konzoli (user-input)
     * @return Skutečnost, jestli příkaz rozpoznal nebo ne
     */
    @Override
    public String processCommand(String line) {
        String[] words = line.split("[ \t]+");

        String cmdName = words[0];
        String[] cmdParameters = new String[words.length - 1];

        for (int i = 0; i < cmdParameters.length; i++) {
            cmdParameters[i] = words[i + 1];
        }

        String result = null;
        if (listOfCommands.checkCommand(cmdName)) {
            ICommand command = listOfCommands.getCommand(cmdName);
            result = command.process(cmdParameters);
        } else {
            result = "Nechápu, co po mně chceš. Tento příkaz neznám.";
        }

        if (gamePlan.isVictorious()) {
            gameOver = true;
        }

        return result;
    }

    /**
     * Metoda getGamePlan vrátí GamePlan, který obsahuje kostru hry (postavy, předměty, lokace...)
     *
     * @return gamePlan Plán hry
     */
    @Override
    public GamePlan getGamePlan() {
        return gamePlan;
    }

    /**
     * Metoda nastaví příznak indikující, že nastal konec hry. Metodu
     * využívá třída {@link CommandTerminate}, mohou ji ale použít
     * i další implementace rozhraní {@link ICommand}.
     *
     * @param gameOver příznak indikující, zda hra již skončila
     */
    void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

}
