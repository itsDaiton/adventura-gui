package cz.vse.poslusny.model;

import java.util.*;

/**
 * Třída představuje lokaci <i>(místo, místnost, prostor)</i> ve scénáři hry.
 * Každá lokace má název, který ji jednoznačně identifikuje. Lokace může mít
 * sousední lokace, do kterých z ní lze odejít. Odkazy na všechny sousední
 * lokace jsou uložené v kolekci. Lokace taky může být zamčená a lze ji otevřít klíčem,
 * příkazem "pouzij Klíč", konkrétně se jedná o lokaci Opuštěná věž.
 *
 * @author Michael Kölling
 * @author Luboš Pavlíček
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author David Poslušný
 * @version LS 2020
 */
public class Area
{
    private String name; //jméno
    private String description; //popis lokace
    private Set<Area> exits;  // Obsahuje sousední lokace, do kterých lze z této odejít
    private Map<String, Item> items;  // Obsahuje předměty v lokaci
    private Map<String, Person> characters; //Obsahuje postavy v lokaci
    private boolean locked; //jestli je zavřená
    private boolean combatHappening; //jestli se v lokaci bojuje

    /**
     * Konstruktor třídy. Vytvoří lokaci se zadaným názvem a popisem.
     *
     * @param name název lokace <i>(jednoznačný identifikátor, musí se jednat o text bez mezer)</i>
     * @param description podrobnější popis lokace
     */
    public Area(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashSet<>();
        this.items = new HashMap<>();
        this.characters = new HashMap<>();
        this.locked = locked;
        this.combatHappening = combatHappening;
    }

    /**
     * Metoda vrací název lokace, který byl zadán při vytváření instance jako
     * parametr konstruktoru. Jedná se o jednoznačný identifikátor lokace <i>(ve
     * hře nemůže existovat více lokací se stejným názvem)</i>. Aby hra správně
     * fungovala, název lokace nesmí obsahovat mezery, v případě potřeby můžete
     * více slov oddělit pomlčkami, použít camel-case apod.
     *
     * @return název lokace
     */
    public String getName() {
        return name;
    }

    /**
     * Metoda přidá další východ z této lokace do lokace předané v parametru.
     * <p>
     * Vzhledem k tomu, že pro uložení sousedních lokací se používá {@linkplain Set},
     * může být přidán pouze jeden východ do každé lokace <i>(tzn. nelze mít dvoje
     * 'dveře' do stejné sousední lokace)</i>. Druhé volání metody se stejnou lokací
     * proto nebude mít žádný efekt.
     * <p>
     * Lze zadat též cestu do sebe sama.
     *
     * @param area lokace, do které bude vytvořen východ z aktuální lokace
     */
    public void addExit(Area area) {
        exits.add(area);
    }

    /**
     * Metoda vrací detailní informace o lokaci. Výsledek volání obsahuje název
     * lokace, podrobnější popis a seznam sousedních lokací, do kterých lze
     * odejít, předměty, které se v lokaci nachází a také postavy, které v lokaci jsou.
     *
     * @return detailní informace o lokaci
     */
    public String getFullDescription() {
        String exitNames = "Východy:";        
        for (Area exitArea : exits) {
            exitNames += " " + exitArea.getName();
            if(exitArea.isLocked()) {
                exitNames += "(zamknuto)";
            }
        }        
        
        String itemNames = "Předměty:";
        for (String itemName : items.keySet()) {
            itemNames += " " + itemName;
        }
                
        String charNames = "Postavy:";
        for(String charName : characters.keySet()) {
            charNames += " " + charName;
        }
        
        return "Jsi v lokaci " + name + ".\n"
                + description + "\n\n"
                + exitNames + "\n"
                + itemNames + "\n"
                + charNames;
    }

    /**
     * Metoda vrací lokaci, která sousedí s aktuální lokací a jejíž název
     * je předán v parametru. Pokud lokace s daným jménem nesousedí s aktuální
     * lokací, vrací se hodnota {@code null}.
     * <p>
     * Metoda je implementována pomocí tzv. 'lambda výrazu'. Pokud bychom chtěli
     * metodu implementovat klasickým způsobem, kód by mohl vypadat např. tímto
     * způsobem:
     * <pre> for (Area exitArea : exits) {
     *     if (exitArea.getName().equals(areaName)) {
     *          return exitArea;
     *     }
     * }
     *
     * return null;</pre>
     *
     * @param areaName jméno sousední lokace <i>(východu)</i>
     * @return lokace, která se nachází za příslušným východem; {@code null}, pokud aktuální lokace s touto nesousedí
     */
    public Area getExitArea(String areaName) {
        return exits.stream()
                    .filter(exit -> exit.getName().equals(areaName))
                    .findAny().orElse(null);
    }

    /**
     * Metoda porovnává dvě lokace <i>(objekty)</i>. Lokace jsou shodné,
     * pokud mají stejný název <i>(atribut {@link #name})</i>. Tato metoda
     * je důležitá pro správné fungování seznamu východů do sousedních
     * lokací.
     * <p>
     * Podrobnější popis metody najdete v dokumentaci třídy {@linkplain Object}.
     *
     * @param o objekt, který bude porovnán s aktuálním
     * @return {@code true}, pokud mají obě lokace stejný název; jinak {@code false}
     *
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        // Ověříme, že parametr není null
        if (o == null) {
            return false;
        }

        // Ověříme, že se nejedná o stejnou instanci (objekt)
        if (this == o) {
            return true;
        }

        // Ověříme, že parametr je typu (objekt třídy) Area
        if (!(o instanceof Area)) {
            return false;
        }

        // Provedeme 'tvrdé' přetypování
        Area area = (Area) o;

        // Provedeme porovnání názvů, statická metoda equals z pomocné třídy
        // java.util.Objects porovná hodnoty obou parametrů a vrátí true pro
        // stejné názvy a i v případě, že jsou oba názvy null; jinak vrátí
        // false
        return Objects.equals(this.name, area.name);
    }

    /**
     * Metoda vrací číselný identifikátor instance, který se používá
     * pro optimalizaci ukládání v dynamických datových strukturách
     * <i>(např.&nbsp;{@linkplain HashSet})</i>. Při překrytí metody
     * {@link #equals(Object) equals} je vždy nutné překrýt i tuto
     * metodu.
     * <p>
     * Podrobnější popis pravidel pro implementaci metody najdete
     * v dokumentaci třídy {@linkplain Object}.
     *
     * @return číselný identifikátor instance
     *
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Metoda přidá předmět do lokace.
     * 
     * @param item Předmět, který bude do lokace přidán
     */
    public void addItem(Item item){
        items.put(item.getName(), item);
    }

    /**
     * Metoda removeItem odstraňí předmět z lokace.
     *
     * @param itemName Název předmětu
     * @return Předmět, který se z lokace odstraňil
     */
    public Item removeItem(String itemName) {
        return items.remove(itemName);
    }

    /**
     * Metoda getItem vrací předmět z lokace podle jména zadaného v parametru.
     *
     * @param itemName Jméno předměta
     * @return Předmět, který má stejné jmené jako je rětězec v parametru
     */
    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    /**
     * Metoda containsItem vrací informaci, jestli je daný předmět v lokaci, podle jeho jména.
     *
     * @param itemName Jméno předmětu
     * @return Skutečnost, jestli je předmět v lokaci
     */
    public boolean containsItem(String itemName) {
        return items.containsKey(itemName);
    }  
    
    /**
     * Metoda addCharacter přidá postavu do lokace.
     *
     * @param character Postava, která se má do lokace přidat
     */
    public void addCharacter(Person character) {
        characters.put(character.getName(), character);
    }
    
    /**
     * Metoda removeCharacter vymaže postavu z lokace podle jména v parametru.
     *
     * @param charName Jméno postavy
     * @return Postava, která se z lokace vymazala
     */
    public Person removeCharacter(String charName) {
        return characters.remove(charName);
    }
    
    /**
     * Metoda getCharacter vrátí postavu v lokaci podle jejího jména.
     *
     * @param charName Jméno postavy
     * @return Postava, která má stejné jméno jako parametr
     */
    public Person getCharacter(String charName) {
        return characters.get(charName);
    }
    
    /**
     * Metoda containsCharacter vrací informace, jestli se postava s jménem v parametru nachází v lokaci.
     *
     * @param charName Jméno postavy
     * @return Skutečnost, jestli se postava v lokaci nachází
     */
    public boolean containsCharacter(String charName) {
        return characters.containsKey(charName);
    }
    
    /**
     * Metoda isLocked vrací informaci, jestli je lokace zamčená.
     *
     * @return locked Skutečnost, jestli je lokace zamčená
     */
    public boolean isLocked() {
        return locked;
    }
    
    /**
     * Methoda lock zamyká a odemyká místnost.
     *
     * @param locked Skutečnost, jestli se lokace zamkne, nebo odemkne
     */
    public void lock(boolean locked) {
        this.locked = locked;
    }
    
    /**
     * Metoda isCombatHappening vrací informace jestli se v lokaci bojuje.
     *
     * @return combatHappening Skutečnost, jestli se v lokaci bojuje
     */
    public boolean isCombatHappening() {
        return combatHappening;
    }
    
    /**
     * Metoda isCombatHappening zapíná a vypíná boj v lokaci.
     *
     * @param combatHappening Zapnutí nebo vypnutí boje
     */
    public void isCombatHappening(boolean combatHappening) {
        this.combatHappening = combatHappening;
    }

    /**
     * Metoda getDescription vrací popis lokace.
     *
     * @return description Popis dané lokace
     */
    public String getDescription() {
        return description;
    }

    /**
     * Metoda getAreaExits vrací kolekci východů z dané lokace
     *
     * @return description Kolekce jednotlivých východů
     */
    public Collection<Area> getAreaExits() {
        return Collections.unmodifiableCollection(exits);
    }

    /**
     * Metoda getCharacters vrací kolekci postav v dané lokaci
     *
     * @return characters Mapa, která obsahuje jméno postavy a objekt třídy Person
     */
    public Map<String, Person> getCharacters() { return characters; }

    /**
     * Metoda getItems vrací kolekci věcí v dané lokaci
     *
     * @return items Mapa, která obsahuje jméno předmětu a objekt třídy Item
     */
    public Map<String, Item> getItems() { return items; }
}
