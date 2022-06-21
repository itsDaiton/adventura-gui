package cz.vse.poslusny.main;

import cz.vse.poslusny.MainController;
import cz.vse.poslusny.model.Game;
import cz.vse.poslusny.model.IGame;
import cz.vse.poslusny.textUI.TextUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Hlavní třída určená pro spuštění hry. Obsahuje pouze statickou metodu
 * {@linkplain #main(String[]) main}, která vytvoří instance logiky hry
 * a uživatelského rozhraní, propojí je a zahájí hru.
 *
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author David Poslušný
 * @version ZS 2020
 */
public final class Start extends Application
{
    /**
     * Metoda pro spuštění celé aplikace, která určuje jestli se spustí textová verze nebo grafická verz hry podle zadaných parametrů.
     * Pokud je v parametrech programu napsát řetězec "text", tak se spustí textová verze.
     *
     * @param args parametry aplikace z příkazového řádku
     */
    public static void main(String[] args)
    {
        List<String> input = Arrays.asList(args);

        if(input.contains("text")) {
            IGame game = new Game();
            TextUI textUI = new TextUI(game);
            textUI.play();
        }
        else {
            launch();
        }
    }

    /**
     * Metoda start spustí aplikaci v grafickém prostředí pomocí platformy JavaFX
     *
     * @param primaryStage stage, neboli okno, které se zobrazí při spuštění aplikace (dále s v metodě do stage přidává scéna a např. se ještě přidává ikona aplikace.)
     * @throws Exception Skutečnost, že metoda může vyhodit výjimku
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Zaklínač Edward: Příběh o bestii");

        FXMLLoader loader = new FXMLLoader();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("scene.fxml");
        Parent root = loader.load(stream);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        InputStream icon = getClass().getClassLoader().getResourceAsStream("icon.png");
        Image img = new Image(icon);
        primaryStage.getIcons().add(img);

        MainController controller = loader.getController();
        IGame game = new Game();
        controller.init(game);
        primaryStage.show();
    }

}