package cz.vse.poslusny.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Testovací třída pro komplexní otestování třídy {@link ListOfCommands}.
 *
 * @author Luboš Pavlíček
 * @author Jan Říha
 * @author David Poslušný
 * @version LS 2020
 */
public class ListOfCommandsTest
{
    private Game game;
    private ICommand cmdTerminate;
    private ICommand cmdMove;

    /**
     * Metoda setUp připravuje instance, které bude potřebné k testování třídy.
     *
     */
    @Before
    public void setUp()
    {
        game = new Game();
        cmdTerminate = new CommandTerminate(game);
        cmdMove = new CommandMove(game.getGamePlan());
    }

    /**
     * Metoda testAddAndGetCommand testuje přidávání a získávání příkazů.
     *
     */
    @Test
    public void testAddAndGetCommand()
    {
        ListOfCommands listOfCommands = new ListOfCommands();
        listOfCommands.addCommand(cmdTerminate);
        listOfCommands.addCommand(cmdMove);

        assertEquals(cmdTerminate, listOfCommands.getCommand(cmdTerminate.getName()));
        assertEquals(cmdMove, listOfCommands.getCommand(cmdMove.getName()));
        assertNull(listOfCommands.getCommand("napoveda"));
    }

    /**
     * Metoda testCheckCommand testuje kontrolu příkazu.
     *
     */
    @Test
    public void testCheckCommand()
    {
        ListOfCommands listOfCommands = new ListOfCommands();
        listOfCommands.addCommand(cmdTerminate);
        listOfCommands.addCommand(cmdMove);

        assertTrue(listOfCommands.checkCommand(cmdTerminate.getName()));
        assertTrue(listOfCommands.checkCommand(cmdMove.getName()));

        if (cmdTerminate.getName().substring(0, 1).equals(cmdTerminate.getName().substring(0, 1).toLowerCase())) {
            assertFalse(listOfCommands.checkCommand(cmdTerminate.getName().toUpperCase()));
        } else {
            assertFalse(listOfCommands.checkCommand(cmdTerminate.getName().toLowerCase()));
        }

        assertFalse(listOfCommands.checkCommand("napoveda"));
    }

    /**
     * Metoda testGetNames testuje získávání jmén příkazů.
     *
     */
    @Test
    public void testGetNames()
    {
        ListOfCommands listOfCommands = new ListOfCommands();
        listOfCommands.addCommand(cmdTerminate);
        listOfCommands.addCommand(cmdMove);

        String names = listOfCommands.getNames();
        assertTrue(names.contains(cmdTerminate.getName()));
        assertTrue(names.contains(cmdMove.getName()));

        if (cmdTerminate.getName().substring(0, 1).equals(cmdTerminate.getName().substring(0, 1).toLowerCase())) {
            assertFalse(names.contains(cmdTerminate.getName().toUpperCase()));
        } else {
            assertFalse(names.contains(cmdTerminate.getName().toLowerCase()));
        }

        assertFalse(names.contains("napoveda"));

        assertEquals(2, names.split(" ").length);
    }

}
