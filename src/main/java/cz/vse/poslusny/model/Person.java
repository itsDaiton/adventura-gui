package cz.vse.poslusny.model;

/**
 * Třída představuje základní logiky postav v této hře. S postavami se dá mluvit, obchodovat a také bojovat.
 * Postavy s kterými se nedá obchodovat mají jen dialog pro mluvení.
 * Obchodníci mají dialogy na základě toho jak obchod probíhá. Také nějakou věc chtějí a nějakou věc nabízí.
 * Nepřátelé mají životy a hráč je musí zabít.
 * Třída kromě getterů a setterů  obsahuje metodu trade, která obstarává výměnu hráče a postavy.
 * Konstruktor obsahuje základní atributy, dále, podle toho co je postava zač (obchodník, nepřítel), se metodami setCombatParameters a setTradingParameters
 * nastavují specifické parametry pro jejich role.
 * 
 *  UPDATE 8/11/2020 - Třídu "Character" jsem přejmenoval na "Person", protože v Javě již tato třída existuje a tudíž mi kompilace házela errory.
 *  V prostředí BlueJ jsem s tímto neměl problémy, ale v programu IntelliJ jsem nemohol provést mavenizaci projektu.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class Person
{ 
    private String name; //jméno
    private boolean tradeable; //jestli s postavou jde obchodovat
    private boolean hostile; //jestli je postava nepřítel
    private String dialog; // dialog postavy
    
    private Item itemOffered; //več, kterou postava nabízí
    private Item itemDemanded; //věc, kterou postava chce
    private String dialogBefore; //dialog před výměnou
    private String dialogAfter; //dialog po výměně
    private String dialogRefusal; //dialog, když předmět nechce
    private String dialogAcceptance; //dialog, když předmět chce
    private boolean tradeHappened; //jestli proběhla výměna
    
    private int healthPoints; //životy protivníka
    
    /**
     * Konstruktor třídy Person. Vytvoří postavu a nastaví jí základní vlastnosti.
     *
     * @param name Název postavy, musí být bez mezer, aby ho konzole přečetla
     * @param tradeable Skutečnost, jestli se dá s postavou obchodovat
     * @param hostile Skutečnost, jestli je postava nepřátelská
     * @param dialog To co nám postava řekne po tom co si s ní promluvíme
     */
    public Person(String name, boolean tradeable, boolean hostile, String dialog) {
        this.name = name;
        this.tradeable = tradeable;
        this.hostile = hostile;
        this.dialog = dialog;
    }
    
    /**
     * Metoda setTradingParameters nastavuje parametry nutné pro obchod s postavou, tudíž pro postavy, co jsou obchodníci.
     *
     * @param itemOffered Předmět, kterou postava nabízí
     * @param itemDemanded Předmět, kterou postava chce
     * @param dialogAfer Dialog po výměně
     * @param dialogBefore Dialog před výměnou
     * @param dialogRefusal Dialog, když nabízenou věc nechce
     * @param dialogAcceptance Dialog, kddyž nabízenou věc chce
     * @param tradeHappened Skutečnost, jestli obchod proběhl
     */
    public void setTradingParameters(Item itemOffered, Item itemDemanded, String dialogAfer, String dialogBefore, String dialogRefusal, String dialogAcceptance, boolean tradeHappened) {
        this.itemOffered = itemOffered;
        this.itemDemanded = itemDemanded;
        this.dialogAfter = dialogAfer;
        this.dialogBefore = dialogBefore;
        this.dialogRefusal = dialogRefusal;
        this.dialogAcceptance = dialogAcceptance;
        this.tradeHappened = tradeHappened;
    }
    
    /**
     * Metoda setCombatParameters nastaví životy nepřátelům.
     *
     * @param healthPoints Životy nepřátelské postavy
     */
    public void setCombatParameters(int healthPoints) {
        this.healthPoints = healthPoints;
    }
    
    /**
     * Metoda getDialog vrací dialog postavy podle toho jestli je to obchodník nebo ne.
     * Pokud výměna již proběhla vrací dialog po výměně.
     *
     * @return dialog, dialogBefore, dialogAfter Možnosti dialogu podle podmínek
     */
    public String getDialog() {
        if (isTradeable()) {
            return dialogBefore;
        }
        else if (didTradeHappened()) {
            return dialogAfter;
        }
        else {
            return dialog;
        }
    }
    
    /**
     * Meoda getTradeDialog vrací dialog obchodník podle toho co mu nabízíme a jak na to on reaguje.
     *
     * @return dialogAcceptance, dialogRefusal Možnosti dialogu podle podmínek
     */
    public String getTradeDialog() {
        if(didTradeHappened()) {
            return dialogAcceptance;
        }
        else {
            return dialogRefusal;
        }
    }
    
    /**
     * Metoda getName vrací jméno postavy.
     *
     * @return name Jméno postavy
     */
    public String getName() {
        return name;
    }
    
    /**
     * Methoda getHealthPoints vrací počet životů postavy.
     *
     * @return healthPoints Životy postavy
     */
    public int getHealthPoints() {
        return healthPoints;
    }
    
    /**
     * Metoda setHealthPoints nastaví životy postavy na hodnotu uvedenou v parametru.
     *
     * @param healthPoints Nově nastavené životy postavy
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
    
    /**
     * Metoda isTradeable vrací informaci jestli jde s postavou obchodovat.
     *
     * @return tradeable Skutečnost, jestli jde s postavou obchodovat.
     */
    public boolean isTradeable() {
        return tradeable;
    }
    
    /**
     * Metoda isHostile vrací informaci, jestli je postava nepřátelská.
     *
     * @return hostile Skutečnost jestli je postava nepřátelská
     */
    public boolean isHostile() {
        return hostile;
    }
    
    /**
     * Metoda didTradeHappened vrací informace jestli obchod proběhl.
     *
     * @return tradeHappened Skutečnost, že obchod proběhl.
     */
    public boolean didTradeHappened() {
        return tradeHappened;
    }
    
    /**
     * Metoda trade zajištuě výměnu s postavou. Pokud se věc kterou nabízíme rovná věci, kterou obchodník chce tak obchod proběhne.
     * Poté se nastaví skutečnost, že obchod proběhl na pravda a s postavou už nepůjde znova obchodovat.
     * V případě že se věci nerovnají, tak metoda vrátí null.
     *
     * @param item Věc, kterou my nabízíme obchodníkovy
     * @return itemOffered Pokud obchod proběhne, obchodník nám věc dá a ta se pošle do návratové hodnoty.
     */
    public Item trade(Item item) {
        if(item.getName() == itemDemanded.getName()) {
            tradeHappened = true;
            tradeable = false;
            return itemOffered;
        }
        else {
            return null;
        }
    }
}
