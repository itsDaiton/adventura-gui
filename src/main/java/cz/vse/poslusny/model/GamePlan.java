package cz.vse.poslusny.model;

/**
 * Třída představující aktuální stav hry. Veškeré informace o stavu hry
 * <i>(mapa lokací, inventář, vlastnosti hlavní postavy, informace o plnění
 * úkolů apod.)</i> by měly být uložené zde v podobě datových atributů.
 * <p>
 * Třída existuje především pro usnadnění potenciální implementace ukládání
 * a načítání hry. Pro uložení rozehrané hry do souboru by mělo stačit uložit
 * údaje z objektu této třídy <i>(např. pomocí serializace objektu)</i>. Pro
 * načtení uložené hry ze souboru by mělo stačit vytvořit objekt této třídy
 * a vhodným způsobem ho předat instanci třídy {@link Game}.
 *
 * @author Michael Kölling
 * @author Luboš Pavlíček
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author David Poslušný
 * @version LS 2020
 *
 * @see <a href="https://java.vse.cz/4it101/AdvSoubory">Postup pro implementaci ukládání a načítání hry na předmětové wiki</a>
 * @see java.io.Serializable
 */
public class GamePlan
{    
    private Area currentArea; // aktuální lokace
    private Inventory inventory; //inventář
    private Person character; //postava
    private MyCharacter myCharacter; //mojePostava - pomocná třída
    private boolean victorious; //skutečnost, jestli hráč vyhrál
    private Item hlavaBestie; //Hlava_bestie - předmět potřebný k výhře

    /**
     * Konstruktor třídy. Pomocí metody {@link #prepareWorldMap() prepareWorldMap}
     * vytvoří jednotlivé lokace a propojí je pomocí východů.
     */
    public GamePlan() {
        prepareWorldMap();
    }

    /**
     * Metoda vytváří jednotlivé postavy, předměty a dále lokace, které propojuje pomocí východů. 
     * Jako výchozí aktuální lokaci následně nastaví Předměstí.
     */
    private void prepareWorldMap() {
        inventory = new Inventory();
        myCharacter = new MyCharacter();
             
        Area mesto = new Area("Město", "Vešel si do města Riften. Kvůli bestii je tu zvýšený počet stráží. Před sebou vidíš, jak se Starosta baví s občany.");
        Area predmesti = new Area("Předměstí", "Předměstí města Riften. Velké množství trhů s všemožnými obchodníky. Co asi tak nabízejí za předměty?");
        Area farma = new Area("Farma", "Místní Farmář zde pěstuje různe plodiny a pak je prodává na místních trzích. Říká se o něm, že je to agresivní opilec.\nDej si na něj pozor!");
        Area reka = new Area("Řeka", "Řeka bludička lemuje celou krajinu. Vypráví se o ní, že kdo se z ní napije, ten na týden svoji žízen zažene.\nPodél řeky roste pěkné kvítí.");
        Area mostPresReku = new Area("Most_přes_řeku", "Most, který používají povozy a obchodníci v případě, že se potřebují dostat do Města Riften.\nMost je vyroben s kamení, pěkná práce.");
        Area opustenaVez = new Area("Opuštěná_věž", "Věž, která v případech nouze funguje jako strážní post. Kolem sebe vidíš staré, rozbité zdi a mnoho pavučin.");
        Area polniCesta = new Area("Polní_cesta", "Dlouhá polní cesta, která vede přes celou zemi. Často se na ní potulují banditi nebo cestují obchodníci.");
        Area les = new Area("Les","Kolem tebe jsou jak listnaté tak jehličnaté stromy a i nějaké keře a skála.\nPodíval si se na sever a vidíš vstup do Temného háje.\nKdyž se otočís uvidíš hradby města Riften.");
        Area temnyHaj = new Area("Temný_Háj", "Vešel jsi do temného háje. Cítíš jak se kolem tebe les zavírá.\nMáš pocit jakoby bylo těžší dýchat, cítíš menší bolest hlavy.\nPodivné zvuky vycházejí ze všech koutů háje.");
      
        mesto.addExit(predmesti);                
        predmesti.addExit(mesto);
        predmesti.addExit(farma);
        predmesti.addExit(opustenaVez);
        predmesti.addExit(mostPresReku);        
        farma.addExit(predmesti);
        farma.addExit(mostPresReku);
        reka.addExit(opustenaVez);
        reka.addExit(polniCesta);
        reka.addExit(mostPresReku);
        mostPresReku.addExit(farma);
        mostPresReku.addExit(predmesti);
        mostPresReku.addExit(reka);        
        opustenaVez.addExit(predmesti);
        opustenaVez.addExit(farma);       
        polniCesta.addExit(predmesti);
        polniCesta.addExit(reka);
        polniCesta.addExit(mostPresReku);
        polniCesta.addExit(les);        
        les.addExit(polniCesta);
        les.addExit(temnyHaj);
        temnyHaj.addExit(les);
        opustenaVez.lock(true);

        currentArea = predmesti;
        
        Item pivo = new Item("Pivo","Čerstvě uvařené pivečko. Tomu nikdo neodolá!", true, false, 0, 0);
        Item kolac = new Item("Koláč", "Borůvkový koláč upečený od místního pekaře.", true, false, 0, 0);
        Item kure = new Item("Kuře", "Propečené, výborně okořeněné kuře.", true, false, 0, 0);
        Item lektvar = new Item("Lektvar", "Červený lektvar v malé lahvičce. Co se asi stane když ho vypiješ?", true, false, 0, 0);       
        Item truhla = new Item("Truhla", "Truhla se zlatým zdobením. Co v ní asi bude?", false, false, 0, 0);
        Item krovi = new Item("Křoví", "Křoví na kterém rostou borůvky.", false, false, 0, 0);
        Item odmena = new Item("Odměna", "Odměna za zabití bestie a záchranu Riftenu. Přijmi prosím náš dar a nekonečné díky!", true, false, 0, 0);
        Item miskaSKorenim = new Item("Miska_s_kořením", "Miska s kořením z dalekých zemí.", true, false, 0, 0);
        Item kukurice = new Item("Kukuřice", "Kukuřice, která roste na poli na Farmě.", true, false, 0, 0);
        Item dzbanSVodou = new Item("Džbán_s_vodou", "Natočená voda ve džbánu z místní řeky.", true, false, 0, 0);
        Item klic = new Item("Klíč", "Klíč k opuštěné věži.", true, false, 0, 0);
        hlavaBestie = new Item("Hlava_bestie", "Hlava, kterou si sťal z bestie, uložená v pytli.", true, false, 0, 0);
             
        Item dyka = new Item("Dýka", "Želzná dýka, vypadá docela tupě.", true, true, 15, 5);
        Item mec = new Item("Meč", "Jednoruční meč z oceli. Jen tu tak leží, asi ho nikdo nechce.", true, true, 30, 10);
        Item sekera = new Item("Sekera", "Ocelová sekera ukovaná a nabroušená místním kovářem.", true, true, 45, 15);                 
                       
        Person bestie = new Person("Bestie", false, true, "Vrrrr Bestie tě vycítila a okamžitě na tebe zaútočila.");
        Person obchodnik = new Person("Obchodník", true, false, "");
        Person umirajiciVojak = new Person("Umírající_Voják", true, false,  "");
        Person starosta = new Person("Starosta", true, false, "");
        Person lovec = new Person("Lovec", false, false, "Zdravím tě zaklínači, co děláš v naši krajinách ? Ale ano, ó můj bože! Ty jsi tu abys zabil tu bestii.\n" + "Já jsem lovec Henry, lovím v místních lesích a zrovna včera jsem zpratřil onu besti jak šla zpátky do svého doupěte někdy v lokaci 'Les'\n." + "Měl jsem strach, že mě viděla, tak jsem hned utekl, snad ti tato informace pomůže.");
        Person bandita = new Person("Bandita", false, true, "Haha! Dej mi všechny svoje peníze nebo tě podříznu!");
        Person farmar = new Person("Farmář", true, false, "");
        Person pocestny = new Person("Pocestný", true, false, "");        
        
        bestie.setCombatParameters(400);
        bandita.setCombatParameters(250);
        obchodnik.setTradingParameters(klic, miskaSKorenim, "Děkuji ti za to, že si mi donesl to koření, to ti nezapomenu!", "Zdravím tě, poutníku. Dneska mi měla přijet zásilka koření '" + miskaSKorenim.getName() + "', ale kurýr se asi někde zdržel." + "\nPokud mi nějaké koření přineseš, vymění ho s tebou za '" + klic.getName() + "'.", "Tohle přeci není koření, vrať se až si nějaké opatříš.", "Ano! to je přesně co potřebuji, tady máš svoji odměnu.", false);
        umirajiciVojak.setTradingParameters(lektvar, dzbanSVodou, "Děkuji ti cizinče, díky tvojí zásluze se mi začínají vracet smysly.\nPočkám na večerní výměnu stráží, kolegové mě ošetří.\nPokud bys chtěl můžeš si vzít můj osobní meč, mám ho tady v předmětu '" + truhla.getName() + "'.", "Pojď blíže cizinče, včera večer jsem tu byl na stráži a zaútočila na mě bestii.\nPřinesl bys mi prosím '" + dzbanSVodou.getName() + "'? Mám obrovskou žízeň.\nZa tvojí snahu se ti odměním předmětem '" + lektvar.getName() + "'.", "Potřebuji vodu prosím, tvoje věci opravdu nechci.", "Konečně, nekonečná žízeň je pryč, tady máš odměnu '" + lektvar.getName() + "'.", false);
        starosta.setTradingParameters(odmena, hlavaBestie, "Tento čin ti nikdy nezapomeneme!\nBez tebe bychom byli v koncích!", "Dobrý den zaklínači, mám pro tebe práci, jedná se o práci na kterou je vaše řemeslo nejlepší.\nV okolí našeho města pobíhá a vraží bestie, nevím co je zač, ale působí nám značné problémy.\nBylo zde hodně odvážlivců, kteří chtěli bestii zabít ale nikdo neuspěl, našli jsem jejich těla v přilehlém lese.\nJestli tuto bestii dokážeš zabít a přineš mi předmět '" + hlavaBestie.getName() + "', tak se ti velmi dobře odměním.\nPokud si na tento úkol věříš, tak věž, že bestie je nemilosrdná a loví v noci.\nHodně štestní na lovu.", "To co mi nabízíš není důkaz, že si bestii zabil, vrať se z důkazem a odměna tě nemine.", "Tomu nevěřím, ty jsi hrdina, ať žije zaklínač Edward.\nTady máš svojí odměnu - předmět '" + odmena.getName() + "', jsi zachráncem města Riften!", false);
        farmar.setTradingParameters(mec, pivo, "Díky za pivko, jsi dobrý chlap!", "Nazdar frajere, co bys řekl na to, že bys mi přinesl '" + pivo.getName() + "', dám ti za to '" + mec.getName() + "'.", "Tohle není '" + pivo.getName() + "', nechtěj abych se rozčílil.", "Ahhhh tomu říkat pivečko. Díky a tady máš odměnu '" + mec.getName() + "'.\nZkus se s ním nepořezat, hahaha.", false);
        pocestny.setTradingParameters(sekera, kolac, "Je radost s tebou obchodovat.", "Zdravím tě, jsem pocestný a rád bych vyměnil svůj předmět '" + sekera.getName() + "'.\nDám ti ji, když mi přineseš předmět '" + kolac.getName() + "'.", "Pokud si pamatuju tak takhle koláče nevypadají, zkus to znovu.", "Ano to bude stačit, tady, vem si předmět '" + sekera.getName() + "'.", false);
        
        mesto.addItem(miskaSKorenim);        
        mesto.addItem(pivo);
        mesto.addItem(dyka);
        mesto.addCharacter(starosta);        
        predmesti.addItem(kure);
        predmesti.addItem(kolac);
        predmesti.addCharacter(obchodnik);        
        farma.addItem(kukurice);
        farma.addCharacter(farmar);        
        mostPresReku.addItem(dzbanSVodou);
        mostPresReku.addCharacter(pocestny);       
        reka.addCharacter(lovec);        
        opustenaVez.addItem(truhla);
        opustenaVez.addCharacter(umirajiciVojak);        
        polniCesta.addCharacter(bandita);        
        les.addItem(krovi);         
        temnyHaj.addCharacter(bestie);
    }

    /**
     * Metoda vrací odkaz na aktuální lokaci, ve které se hráč právě nachází.
     *
     * @return aktuální lokace
     */
    public Area getCurrentArea() {
        return currentArea;
    }

    /**
     * Metoda nastaví aktuální lokaci, používá ji příkaz {@link CommandMove}
     * při přechodu mezi lokacemi.
     *
     * @param area lokace, která bude nastavena jako aktuální
     */
    public void setCurrentArea(Area area) {
        currentArea = area;
    }
    
    /**
     * Metoda isVictorious vrací informaci jestli hráč vyhrál.
     *
     * @return victorious Skutečnost, jestli hráč vyhrál
     */
    public boolean isVictorious() {
        return victorious;
    }
    
    /**
     * Metoda setVictorious nastavuje jestli hráč vyhrál nebo ne.
     *
     * @param victorious Skutečnost, jestli hráč vyhrál nebo ne.
     */
    public void setVictorious(boolean victorious) { 
        this.victorious = victorious;
    }
    
    /**
     * Metoda getInventory vrací inventář.
     *
     * @return inventory Instance inventáře
     */
    public Inventory getInventory() {
        return inventory;
    }
    
    /**
     * Metoda getCharacter vrací postavu.
     *
     * @return character Instance třídy Person
     */
    public Person getCharacter() {
        return character;
    }
    
    /**
     * Metoda getMyCharacter vrací instanci třídy MyCharacter.
     *
     * @return myCharacter Instance pomocné třídy MyCharacter
     */
    public MyCharacter getMyCharacter() {
        return myCharacter;
    }
    
    /**
     * Metoda getHead vrací hlavu bestie.
     *
     * @return hlavaBestie Předmět potřebný pro výhru
     */
    public Item getHead() {
        return hlavaBestie;
    }
}
