package cz.vse.poslusny.model;

/**
 * Třída MyCharacter je pomocná třída pro boj s nepřáteli v příkazu "bojuj".
 * Obsahuje pouze metody pro "přepravu" dat. 
 * 
 * @author David Poslušný
 * @version LS 2020
 */
public class MyCharacter
{
    private int healthPoints; //životy hráče

    /**
     * Konstruktor třídy MyCharacter
     * Použe se nastavují defaultní životy na 250.
     */
    public MyCharacter() {
        healthPoints = 250;
    }

    /**
     * Metoda getHealthPoints vrací aktuální životy hráče při boji.
     *
     * @return healthPoints Aktuální hodnota životů
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Metoda setHealthPoints nastavuje života hráče na novou hodnotu zadanou v parametru.
     *
     * @param healthPoints Nová hodnota životů
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
    
    /**
     * Metoda changeHealthPoints zvětší životy hráče o hodnotu uvedenou v parametru.
     *
     * @param healthPoints Udává o kolik se hráči zvednou nebo sníží životy
     */
    public void changeHealthPoints(int healthPoints) {
        this.healthPoints += healthPoints;
    }
}
