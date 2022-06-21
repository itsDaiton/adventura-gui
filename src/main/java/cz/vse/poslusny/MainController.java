package cz.vse.poslusny;

import cz.vse.poslusny.model.*;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Collection;

/**
 * Třída MainController nám umožňuje pracovat s grafickým rozhraním aplikace.
 * Po spojení souboru "scene.fxml", kde vytváríme scénu pro náš stage je možné v této třídy jednotlivé prvky upravovat a přidávat jim události.
 * Třída se zejména stará o správné zobrazení všechn informací v grafickém rozhraní pomocí updatovacích metod.
 *
 * @author David Poslušný
 * @version ZS 2020
 */
public class MainController {

    public Label areaName;
    public Label areaDescription;
    public Label enemyHealth;
    public TextArea textOutput;
    public TextField textInput;
    public BorderPane pane;
    public VBox areas;
    public VBox items;
    public VBox characters;
    public VBox inventory;
    public VBox weapon;
    public VBox health;
    public VBox tradeWindow;
    public HBox middleBox;
    public VBox fightWindow;
    public VBox menu;
    public VBox textIO;
    public VBox rightBox;
    public VBox leftBox;
    public VBox infoBox;
    public Button btnTrade;
    public Button btnFight;
    public ComboBox offers;
    public ComboBox traders;
    public ComboBox enemies;

    public MenuItem itemNewGame;
    public MenuItem itemHelp;
    public MenuItem itemTerminate;

    private IGame game;

    /**
     * Metoda init inicializuje aplikaci, tj. spustí program - načte všechny prvky jako např. tlačítka nebo comboBoxy.
     * Po načtení se proveda metoda update, zobrazují se úvodní príběh hry v textové oblasti a prvky aplikace se odemknou uživateli k používání.
     *
     * @param game nová instance třídy Game, která se vytvoří při spuštění aplikace
     */
    public void init(IGame game) {
        this.game = game;
        disableLayout(false);
        showPrologue();
        update();
    }

    /**
     * Metoda newGame přiřazuje položce "Nová hra" v menu aplikace událost, po které se spustí nová hra.
     * Do metody init, která spouští program se pošle nová instance třídy Game, tudíž se restartuje.
     *
     */
    private void newGame() {
        itemNewGame.setOnAction(event -> {
            Game game = new Game();
            init(game);
        });
    }

    /**
     * Metoda terminate přiřazuje položce "Konec" v menu aplikace událost, po které se vypne aplikace.
     */
    private void terminate() {
        itemTerminate.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Metoda help přiřazuje položce "Nápověda" v menu aplikace událost, po které se vytvoří a zobrazí nové okno.
     * V tomto oknu se zobrazí soubor "Nápověda.html" pomocí prvku WebView.
     */
    private void help() {
        itemHelp.setOnAction(event -> {
            Stage primaryStage = new Stage();
            primaryStage.setMaximized(true);
            primaryStage.setTitle("Nápověda");

            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(getClass().getResource("/Nápověda.html").toString());

            Scene scene = new Scene(webView);
            primaryStage.setScene(scene);

            InputStream icon = getClass().getClassLoader().getResourceAsStream("icon_help.png");
            Image img = new Image(icon);
            primaryStage.getIcons().add(img);

            primaryStage.show();
        });
    }

    /**
     * Metoda executeCommand slouží jako obecná pomůcka, která vykoná příkaz podle zadaného parametru.
     *
     * @param command příkaz, který se má provést
     */
    private void executeCommand(String command) {
        String result = game.processCommand(command);
        textOutput.appendText(result + "\n\n");
        update();
    }

    /**
     * Metoda update aktualizuje jednotlivé prvky v grafickém rozhraní.
     * Ve metodě se volají aktualizační metody jednotlivých prvků, např. updateAreas a také se zde kontroluje jestli hra nebyla ukončena.
     * Provádí se zde také aktualizace informačního boxu v horní sekci programu a pozadí aplikace podle aktuální lokace.
     *
     */
    private void update() {
        String name = game.getGamePlan().getCurrentArea().getName();
        areaName.setText(name);

        String description = game.getGamePlan().getCurrentArea().getDescription();
        areaDescription.setText(description);

        String currentArea = game.getGamePlan().getCurrentArea().getName();
        String command = "-fx-background-image: url" + "('areas/" + currentArea + ".jpg')";
        pane.setStyle(command);


        updateAreas();
        updateCharacters();
        updateItems();
        updateInventory();
        updateHealth();
        updateTradingWindow();
        updateFightingWindow();

        getGameState();
        terminate();
        newGame();
        help();

    }

    /**
     * Metoda updateAreas aktualizuje jednotlivé grafické prvky při přechodu mezi lokacemi, aby vypisovali pouze východy z aktuální lokace.
     * Při kliknutí na lokaci se provede příkaz jdi.
     */
    private void updateAreas() {
        Collection<Area> exitAreas = game.getGamePlan().getCurrentArea().getAreaExits();
        areas.getChildren().clear();

        for (Area area : exitAreas) {
            String areaName = area.getName();
            Label areaLabel = new Label(areaName);
            areaLabel.setCursor(Cursor.HAND);

            areaLabel.setOnMouseClicked(event -> {
                executeCommand("jdi " + areaName);
            });

            areas.getChildren().add(areaLabel);

        }
    }

    /**
     * Metoda updateCharacters aktualizuje jednotlivé grafické prvky při přechodu mezi lokacemi, aby vypisovali pouze postavy, které se v lokaci nacházejí.
     * Aktualizace se také provede, pokud nějakou postavu zabije a už se v lokaci nenachází.
     * Při kliknutí na postavu se provede příkaz promluv.
     * V případě promluvení s nepřítelem začne boj a nelze dělat nic jiného než bojovat.+
     */
    private void updateCharacters() {
        Collection<Person> listOfCharacters = game.getGamePlan().getCurrentArea().getCharacters().values();
        characters.getChildren().clear();

        for (Person person : listOfCharacters) {
            String charName = person.getName();
            Label charLabel = new Label(charName);

            charLabel.setCursor(Cursor.HAND);
            charLabel.setOnMouseClicked(event -> {
                executeCommand("promluv " + charName);
            });

            characters.getChildren().add(charLabel);
        }
    }

    /**
     * Metoda updateItems aktualizuje jednotlivé grafické prvky při přechodu mezi lokacemi, při sebrání předmětu, nebo při prozkoumání nepohyblivého předmětu.
     * Kliknutím na přenositelný předmět se provede příkaz seber.
     * Kliknutím na nepřenositelné předmět se provede příkaz prozkoumej a do lokace se přidá předmět, který byl "vevnitř" předmětu nepohyblivého.
     * Pokud by chtěl hráč sbírat nepohyblivý předmět, vypíše se mu odpovídající popisek.
     */
    private void updateItems() {
        Collection<Item> listOfItems = game.getGamePlan().getCurrentArea().getItems().values();
        items.getChildren().clear();

        for(Item item : listOfItems) {
            String itemName = item.getName();
            Label itemLabel = new Label(itemName);

            if(item.isMoveable()) {
                itemLabel.setCursor(Cursor.HAND);
                itemLabel.setOnMouseClicked(event -> {
                    executeCommand("seber " + itemName);
                });
            }
            else {
                if (!item.isInspected()) {
                    itemLabel.setTooltip(new Tooltip("Předmět " + itemName + " nelze přenášet, lze ho ale prozkoumat."));
                }
                else {
                    itemLabel.setTooltip(new Tooltip(item.getDescription()));
                }
                itemLabel.setOnMouseClicked(event -> {
                    executeCommand("prozkoumej " + itemName);
                });
            }

            items.getChildren().add(itemLabel);
        }
    }

    /**
     * Metoda updateInventory aktualizuje jednotlivé grafické prvky při vyhození předmětu z inventáře nebo použití předmětu v inventáři.
     * Kliknutím levým tlačítkem myši na předmět v inventáři se provede příkaz vyhod.
     * Kliknutím pravým tlačítkem myši na předmět v inventáři se provede příkaz pouzij.
     * V obou případech je potřeba zavolat update, protože předměty se již nenacházejí v inventáři.
     * Položky v inventáři se zobrazují pomocí obrázků, které se nastaví jednotlivým labelům.
     * Najetím myši se zobrazí popisek jednotlivého předmětu.
     * Tato metoda také řeší aktualizaci grafických prvků spojených s boxem Zbraň - toto se děje v případě že si hráč vymění zbraň za novou.
     * Vypisuje se poškození zbraně a hodnota odražení.
     */
    private void updateInventory() {
        Collection<Item> inventoryList = game.getGamePlan().getInventory().getItems();
        inventory.getChildren().clear();

        Item currentWeapon = game.getGamePlan().getInventory().getCurrentWeapon();
        weapon.getChildren().clear();

        for (Item item : inventoryList) {
            String itemName = item.getName();
            Label itemLabel = new Label();
            itemLabel.setTooltip(new Tooltip(item.getDescription()));
            InputStream inventoryStream = getClass().getClassLoader().getResourceAsStream("items/" + itemName + ".jpg");
            setDisplayProperties(inventoryStream, itemLabel);

            itemLabel.setCursor(Cursor.HAND);
            itemLabel.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    executeCommand("vyhod " + itemName);
                }
                else if(event.getButton() == MouseButton.SECONDARY) {
                    executeCommand("pouzij " + itemName);
                }
            });

            inventory.getChildren().add(itemLabel);
        }

        if (currentWeapon != null) {
            String weaponName = currentWeapon.getName();
            Label weaponLabel = new Label();
            weaponLabel.setTooltip((new Tooltip((currentWeapon.getDescription()))));
            InputStream weaponStream = getClass().getClassLoader().getResourceAsStream("items/" + weaponName + ".jpg");
            setDisplayProperties(weaponStream, weaponLabel);

            weaponLabel.setTooltip(new Tooltip("Svoji zbraň nemůžeš vyhodit, lze ji pouze vyměnit za jinou."));

            Label weaponDamage = new Label("Poškození: " + currentWeapon.getDamage());
            Label weaponParryValue = new Label("Odražení: " + currentWeapon.getParryValue());

            weapon.getChildren().addAll(weaponLabel,weaponDamage,weaponParryValue);
        }
    }

    /**
     * Metoda updateHealth aktualizuje v grafickém rozhraní Label s ID = "health", který vypisuje aktuální životy hráče.
     */
    private void updateHealth() {
        health.getChildren().clear();
        int healthPoints = game.getGamePlan().getMyCharacter().getHealthPoints();
        Label healthLabel = new Label(String.valueOf(healthPoints));
        healthLabel.setStyle("-fx-font-weight: bold");
        health.getChildren().add(healthLabel);
    }

    /**
     * Metoda updateTradingWindow aktualizuje grafické prvky v boxu Obchodování.
     * Aktualizace se provede po pokusu hráče vyměnit předmět s postavou - stisknutím tlačítka.
     * ComboBoxy mají nastavený počáteční text, aby hráč věděl, co zvolit a jak tato část rozhraní funguje.
     * Po aktualizace se vybraná hodnota v comboBoxu nastaví opět na null a hráč tedy musí opět vybrat nějakou položku.
     *
     */
    private void updateTradingWindow() {
        Collection<Item> inventoryList = game.getGamePlan().getInventory().getItems();
        Collection<Person> listOfCharacters = game.getGamePlan().getCurrentArea().getCharacters().values();

        traders.getItems().clear();
        offers.getItems().clear();

        traders.setPromptText("Postava");
        offers.setPromptText("Předmět");

        for (Item item : inventoryList) {
            offers.getItems().add(item.getName());
        }
        for (Person person : listOfCharacters) {
            traders.getItems().add(person.getName());
        }

        btnTrade.setOnMouseClicked(event -> {
            executeCommand("vymen " + offers.getValue() + " " + traders.getValue());
        });

    }

    /**
     * Metoda updateFightingWindow aktualizuje grafické prvky v boxu Bojování.
     * Stejně jako u obchodování se aktualizace provede po stisknutí tlačítka.
     * Label s ID = "enemyHealth" vypisuje aktuální počet životý postavy s kterou hráč bojuje.
     * V případě že není postava vybraná, nebo je vybraná přátelská postava a tlačítko je stisknuto, tak se vypíše odpovídající text, který hráče upozorní.
     * Po kliknutí na tlačítko se v comboBoxu, kde se vybírá postava zachová předchozí hodnota, aby hráč mohl jen klikat na tlačítko a nemusel při každém updatu
     * vybírá postavu znova.
     */
    private void updateFightingWindow() {
        Collection<Person> listOfCharacters = game.getGamePlan().getCurrentArea().getCharacters().values();
        Person enemy = game.getGamePlan().getCurrentArea().getCharacter(String.valueOf(enemies.getValue()));

        if (enemy != null) {
            if (enemy.isHostile()) {
                enemyHealth.setText("Životy: " + enemy.getHealthPoints());
            }
            else {
                enemyHealth.setText("Tato postava nemá životy.");
            }
        }
        else {
            enemyHealth.setText("Není vybraná žádná postava.");
        }

        enemies.getItems().clear();
        enemies.setPromptText("Postava");

        for (Person person : listOfCharacters) {
            enemies.getItems().add(person.getName());
        }

        btnFight.setOnMouseClicked(event -> {
            Object selectedPerson = enemies.getValue();
            executeCommand("bojuj " + enemies.getValue());
            enemies.getSelectionModel().select(selectedPerson);
        });

    }

    /**
     * Metoda setDisplayProperties je pomocná metoda, která přidá labelům místo textu obrázku pomocí metody setGraphic
     * Tato metoda se používá pro zobrazení inventáře a zbraně jako obrázků.
     *
     * @param label labely, kterým se přiřadí obrázek
     * @param stream odkaz na obrázek, který se nastaví jednotlivým labelům
     */
    private void setDisplayProperties(InputStream stream, Label label) {
        Image img = new Image(stream);
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(80);
        imageView.setFitHeight(60);
        imageView.setPreserveRatio(true);
        label.setGraphic(imageView);
        label.setStyle("-fx-padding: 7");
    }

    /**
     * Metoda onInputKeypressed nám vytváří možnost psát do textového pole a hrát hru pouze "textově".
     * Při stisknutí tlačítka Enter se zkusí provést příkaz a textové pole se vyčistí.
     *
     * @param keyEvent o jakou událost se jedná
     */
    public void onInputKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            executeCommand(textInput.getText());
            textInput.setText("");
        }
    }

    /**
     * Metoda showPrologue vyčistí textovou oblast a vypíše úvodní text hry.
     * Využívá se při spuštění nové hry.
     *
     */
    private void showPrologue() {
        textOutput.clear();
        textOutput.setText(game.getPrologue() + "\n\n");
    }

    /**
     * Metoda showEpilogue vypíše konečný text hry.
     * Využívá se při skončení hry, tj. pokud hráč vyhraje nebo prohraje.
     *
     */
    private void showEpilogue() {
        textOutput.appendText(textOutput.getText() + game.getEpilogue());
    }

    /**
     * Metoda getGameState kontroluje jestli nebyla hra ukončena a pokud ano, tak vypíše epilog a vypne většinu ovládacích prvků grafického rohgzaní.
     *
     */
    private void getGameState() {
        if (game.isGameOver()) {
            disableLayout(true);
            showEpilogue();
        }
    }

    /**
     * Metoda disableLayout vypíná nebo zapína většinu ovládacích prvků aplikace podle stav, který je poslán v parametru.
     *
     * @param state stav, který indikuje jestli se mají ovládací prvky zapnout nebo vypnout
     */
    private void disableLayout(boolean state) {
        middleBox.setDisable(state);
        rightBox.setDisable(state);
        leftBox.setDisable(state);
        textInput.setDisable(state);
        infoBox.setDisable(state);
    }
}
