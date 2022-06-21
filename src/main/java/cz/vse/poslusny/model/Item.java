package cz.vse.poslusny.model;

/**
 * Třída představuje základní logiky předmětů ve hře. Předmět může být pouze jen předmět, který se dá uložit do inventáře,
 * nebo to může být zbraň, která se používá ve slotu na zbraň. Pro případné zbraně jsou tu datové atributy pro nastavení poškození a hodnoty odražení.
 * Lze také vytvořit nehybnou věc a nastavit, že když se prozkoumá, tak se do místnosti přídá nějaký předmět. K tomuto slouží datové atributy inspected a moveable.
 * Ve třídě se nacházejí převážně jen "gettery" a pár "setterů", které jsou určené pro práci v příkazech a jiných třídách.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class Item
{
    private String name; //jméno předmětu
    private String description; //popis předmětu
    private boolean moveable; //jestli se s ním dá hýbat
    private boolean weapon; //jestli je to zbraň
    private int damage; //poškození
    private int parryValue; //hodnota odražení   
    private boolean inspected = false; //jestli byl prozkoumán

    /**
     * Konstruktor třídy Item. Vytvoří předmět se zadaným názvem, popisem a ostaními vlastnostmi.
     *
     * @param name Název předmětu, nesmí obsahovat mezery, aby bylo možné ho příkazem přečíst z konzole
     * @param description Popis předmětu, který se vypíše na konzoli
     * @param moveable Skutečnost, jestli lze předmět uložit do inventáře
     * @param weapon Skutečnost, jestli je předmět zbraň, nebo pouze předmět, který se dá uložit do inventáře
     * @param damage V případě zbraně se lze nastavit poškození dané zbraně (využivá se v příkazu "bojuj")
     * @param parryValue Hodnota odražení dané zbraně, také použitelné při souboji s nepřítelem
     */
    public Item(String name, String description, boolean moveable, boolean weapon, int damage, int parryValue) {
        this.name = name;
        this.description = description;
        this.moveable = moveable;
        this.weapon = weapon;
        this.damage = damage;
        this.parryValue = parryValue;       
    }

    /**
     * Metoda getName vrací Jméno daného předmětu.
     *
     * @return name Název předmětu
     */
    public String getName() {
        return name;
    }

    /**
     * Metoda getDescription vrací Popis daného předmětu.
     *
     * @return description Popis předmětu
     */
    public String getDescription() {
        return description;
    }

    /**
     * Metoda setDescription mění popis daného předmětu na řetězec zadaný v parametru.
     *
     * @param description Nový popis předmětu
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Metoda isMoveable vrací informaci, jestli je daný předmět přenositelný.
     *
     * @return moveable Skutečnost, jestli jde předmět přenášet
     */
    public boolean isMoveable() {
        return moveable;
    }

    /**
     * Metoda setMoveable nastavuje danému předmětu možnost být přenositelný, nebo v opačném případě tuto možnost vypne.
     *
     * @param moveable Skutečnost, jestli jde předmět přenášet
     */
    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }
    
    /**
     * Meetoda isWeapon vrací informaci, jestli je daný předmět zbraň.
     *
     * @return weapon Skutečnost, jestli je předmět zbraň
     */
    public boolean isWeapon() {
        return weapon;
    }
    
    /**
     * Metoda getDamage vrací poškození dané zbraně.
     *
     * @return damage Poškození předmětu - zbraně.
     */
    public int getDamage() {
        return damage;
    }
    
    /**
     * Metoda setDamage přičtne nebo odečte poškození na dané zbrani o hodnotu zadanou v parametru.
     *
     * @param damage Nové poškození na zbraňi po odečtení/přičtení
     */
    public void setDamage(int damage) {
        this.damage += damage;
    }
    
    /**
     * Metoda getParryValue vrací hodnotu odražení dané zbraně, která byla zvolena při inicializaci v konstruktoru.
     *
     * @return parryValue Hodnota odrazení předmětu
     */
    public int getParryValue() {
        return parryValue;
    }
    
    /**
     * Metoda setParryValue přičte nebo odečte hodnotu odražení na dané zbrani o hodnotu zadanou v parametru.
     *
     * @param parryValue Nová hodnota odražení na zbrani po odečtení/přičtení
     */
    public void setParryValue(int parryValue) {
        this.parryValue += parryValue;
    }
    
    /**
     * Metoda isInspected vrací informace jestli byl předmět prozkoumán příkazem "prozkoumej".
     *
     * @return inspected Skutečnost, jestli byl předmět prozkoumán
     */
    public boolean isInspected() {
        return inspected;
    }
    
    /**
     * Metoda inspected nastaví předmětu, že byl prozkoumán, nebo opačně.
     *
     * @param inspected Skutečnost, jestli byl předmět prozkoumán
     */
    public void inspected(boolean inspected) {
        this.inspected = inspected;
    }
}
