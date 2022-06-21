package cz.vse.poslusny.model;

import java.util.*;

/**
 * Třída představuje implementace inventáře do hry. Předměty se v inventáři uskladňují v množině.
 * Do inventáře není možné neustále přidávat předměty, nýbrž má omezenou kapacitu danou konstantou.
 * Z inventáře se dají odebírat a přidávat předměty. Příkazy "seber" a "vyhod" tyto metody využívají při práci s předměty.
 * Z inventáře se dají zjistit všechny jeho položky, nebo jen jeden předmět dle zadaného parametru.
 * Zbraně se v mé hře neskladují v inventáři, ale je na ně určený jeden slot. Proto pro ně existuje jen instance třídy Item.
 * Následně lze uvedenými metodami měnit zbraň a získat informaci o tom, kterou zbraň hráč drží.
 * Zbraně nelze vyhazovat, pouze nahrazovat, protože jsou nezbytnou součástí pro dosažení výhry, takže by bylo nesmyslné nechat hráče je vyhazovat.
 * Pouze když hra začně tak hráč nemá zbraň, aby to neměl tak lehké.
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class Inventory
{
    Set<Item> items; //množina pro ukládání předmětů 
    private static final int MAXIMUM_CAPACITY = 5; // maximální kapacita inventáře
    private int currentCapacity; // aktuální kapacita inventáře
    private Item currentWeapon; // aktuální zbraň
    
    /**
     * Konstruktor třídy Item.
     * Inicializuje se v množina předmětů, s kterou se pracuje v této třídě.
     * Následně se nastaví aktuální kapacita na délku množiny a inicializuje se aktuální zbraň.
     */ 
    public Inventory() {
        items = new HashSet<>();
        currentCapacity = items.size();
        this.currentWeapon = currentWeapon;
    }
    
    /**
     * Metoda isFull kontroluje zda je inventář plný.
     *
     * @return Skutečnost, jestli je inventář plný nebo ne
     */
    public boolean isFull() {
        if(currentCapacity < MAXIMUM_CAPACITY)
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * Metoda addItem přidá předmět do inventáře, resp. do množiny items.
     * Kontroluje se jestli je předmět hybatelný, aby se mohl dát do inventáře a jestli není null.
     * Poté se zkontroluje jestli inventář není plný a jestli je volný, tak se zvedne kapacita o jednotku a předmět se přidá do množiny.
     * 
     * @param item Předmět, který chceme přidat do inventáře
     * @return Skutečnost, jestli se předmět přidal do inventáře
     */
    public boolean addItem(Item item) {
        if(!item.isMoveable() || item == null) {
            return false;
        }
        else {
            if(isFull()) {
                return false;
            }
            else {
                currentCapacity++;
                return items.add(item);                
            }
        }
    }

    /**
     * Metoda removeItem odstraní prvek z inventáře.
     * Stejně jako u přidání se kontroluje, jestli není parametr metody null a také se kontroluje jestli předmět v inventáři je, aby ho bylo moct odstranit.
     * Pokud budou splňěny podmínky, tak se předmět z inventáře odstraní a zmenší se kapacita o jednotku.
     * 
     * @param item Předmět, který chceme odstranit
     * @return Skutečnost, jestli byl předmět odstraněn
     */
    public boolean removeItem(Item item) {
        if(!items.contains(item) || item == null) {
            return false;
        }
        else {
            currentCapacity--;
            return items.remove(item);
        }
    }
    
    /**
     * Metoda getItems vrací všechny itemy v inventáři jako množinu.
     *
     * @return Množina všech předmětů v inventáři.
     */
    public Collection<Item> getItems() {
        return new HashSet<>(items);
    }
    
    /**
     * Metoda containsItem vráci informaci jestli předmět zadaný v parametru nachází v inventáři.
     *
     * @param item Předmět, o kterém chceme zjistit, jestli se nachází v inventáři
     * @return Skutečnost, jestli se předmět zadaný v parametru nachází v inventáři
     */
    public boolean containsItem(Item item) {
        return items.contains(item);
    }
        
    /**
     * Metoda getItemByName vrací Předmět podle jeho jména.
     * Pokud parametr není null, tak se projedu množina a vybere se ten předmět, 
     * který vyhovuje podmínce a to že mám stejné jméno jako to zadané v parametru
     * 
     * @param itemName Název předmětu
     * @return Předmět, který podle jeho jména získáme
     */
    public Item getItemByName(String itemName) {
        if(itemName == null) {
            return null;
        }
        
        for(Item i : items) {
            if(i.getName().equals(itemName)) {
                return i;
            }
        }
        
        return null;
    }    
    
    /**
     * Metoda getCurrentWeapon vrací aktuální zbraň.
     *
     * @return Aktuální zbraň hráče
     */
    public Item getCurrentWeapon() {
        return currentWeapon;
    }

    /**
     * Metoda replaceCurrentWeapon vymění aktuální zbraň za novou zbraň zadanou v parametru.
     *
     * @param item Nová zbraň
     */
    public void replaceCurrentWeapon(Item item) {
        if (item != null && item.isWeapon()) {
            currentWeapon = item;
        }
    }     
}
