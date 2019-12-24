package com.heroes.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import javafx.scene.Scene;
import com.heroes.main.Huluwa;
import com.heroes.view.BackgroundView;
import javafx.scene.layout.BorderPane;

//开始菜单
public class StartingMenu {
    private static final String STANDARD_BUTTON_STYLE = "-fx-effect: dropshadow(gaussian, #4682b4 , 10, 0.5, 1, 1 );";
    private static final String HOVERED_BUTTON_STYLE = "-fx-opacity: 0.8;";
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean iWantMenu = true;
    private BackgroundView startingMenu;
    private String pathToNewGame = new String("menu/buttons/heroes_new_game.png");
    private String pathToCredits = new String("menu/buttons/heroes_credits.png");
    private String pathToQuit = new String("menu/buttons/heroes_quit.png");

    public StartingMenu(BackgroundView startingMenu) {
        pcs.addPropertyChangeListener(this::propertyChange);
        this.startingMenu = startingMenu;
        createButtons();
    }


    public void propertyChange(PropertyChangeEvent evt) {
        try {
            Huluwa.startGame(Huluwa.getGameBackground());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean getiWantMenu() {
        return iWantMenu;
    }


    public void setiWantMenu(Boolean iDontWantMenu) {
        Boolean iWantMenu = this.iWantMenu;
        this.iWantMenu = iDontWantMenu;
        this.pcs.firePropertyChange("value", iWantMenu, iDontWantMenu);
    }
    private void exitStartingMenu() {
        removeButtons();
        setiWantMenu(false);
    }

    private void removeButtons() {
        this.startingMenu.getChildren().clear();
    }


    private void createButtons() {
        Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screensize.getWidth();
        int height = (int)screensize.getHeight();
        final ImageView startGameButton = new ImageView(new Image(pathToNewGame));
        startGameButton.setOnMouseClicked(event -> exitStartingMenu());

        final ImageView creditsButton = new ImageView(new Image(pathToCredits));
        creditsButton.setOnMouseClicked(event -> createCredits());

        final ImageView quitButton = new ImageView(new Image(pathToQuit));
        quitButton.setOnMouseClicked(event -> exitApp());

        ImageView[] images = {startGameButton, creditsButton, quitButton};

        VBox vbox = new VBox(height/6);
        vbox.setPadding(new Insets(50, 50, 50, 50));
        vbox.setAlignment(Pos.CENTER);

        //this.startingMenu.getChildren().add(vbox);
        BorderPane pane = new BorderPane();
        this.startingMenu.getChildren().add(pane);
        pane.setRight(vbox);
        for (ImageView img : images) {
            img.styleProperty().bind(
                    Bindings
                            .when(img.hoverProperty())
                            .then(
                                    new SimpleStringProperty(HOVERED_BUTTON_STYLE)
                            )
                            .otherwise(
                                    new SimpleStringProperty(STANDARD_BUTTON_STYLE)
                            )
            );
            vbox.getChildren().add(img);
        }
    }


    private void createCredits(){
        ImageView creditsFrame = new ImageView(new Image("menu/buttons/heroes_small_menu_plain.png"));
        creditsFrame.setLayoutX(450);
        creditsFrame.setLayoutY(250);
        creditsFrame.setFitWidth(500);
        creditsFrame.setPreserveRatio(true);
        this.startingMenu.getChildren().add(creditsFrame);

        Label credits = new Label("这里写游戏简介，自己可以加!");

        credits.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 14px; -fx-font-weight: 800;");
        credits.setWrapText(true);
        credits.setPrefWidth(creditsFrame.getLayoutX() - 10);
        credits.setTextAlignment(TextAlignment.JUSTIFY);
        credits.setLayoutX(creditsFrame.getLayoutX() + 30);
        credits.setLayoutY(creditsFrame.getLayoutY() + 30);
        this.startingMenu.getChildren().add(credits);

        ImageView creditsQuitButton = new ImageView(new Image("menu/buttons/heroes_refuse.png"));
        creditsQuitButton.setLayoutX(creditsFrame.getLayoutX() + 390);
        creditsQuitButton.setLayoutY(creditsFrame.getLayoutY() + 230);
        creditsQuitButton.setFitWidth(80);
        creditsQuitButton.setPreserveRatio(true);
        creditsQuitButton.setOnMouseClicked(event -> startingMenu.getChildren().removeAll(creditsFrame, credits, creditsQuitButton));
        this.startingMenu.getChildren().add(creditsQuitButton);
    }


        public static void exitApp(){
        System.exit(0);
    }

}
