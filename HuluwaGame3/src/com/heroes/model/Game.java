package com.heroes.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.heroes.view.BackgroundView;
import com.heroes.view.UnitView;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;


//��Ϸ��̨
public class Game extends Pane {

    private static final String STANDARD_BUTTON_STYLE = "-fx-effect: dropshadow(gaussian,  #8b4513 , 10, 0.5, 1, 1 );";
    private static final String HOVERED_BUTTON_STYLE = "-fx-opacity: 0.8;";
    private List<Square> squaresList = FXCollections.observableArrayList();
    private Player P1;
    private Player P2;
    private Player[] arrayOfPlayers = new Player[2];
    private ArrayList<Unit> unitsInTheGame;
    private int iterUnit;
    private BackgroundView gameBackground;
    private static boolean isMoving = false;
    private static boolean wasMove = false;
    private static boolean canClickSkipTurn = true;
    private List<Square> squaresToMove;
    List<Unit> possibleUnitsToAttack;
 
    public Game(BackgroundView gameBackground) throws IOException {
        this.gameBackground = gameBackground;
        createSquares();
        createPlayersAndTheirsUnits();
        createArrayListOfAllUnitsInTheGame();
      
        addQuitButton();
        checkWhereCanMove();
        checkPossibleUnitsToAttack(unitsInTheGame.get(iterUnit));
        makeSquareShadows();
        UnitView.higlightUnit(unitsInTheGame.get(iterUnit).getUnitView());
    }


    public static void setMoving(boolean moving) {
        isMoving = moving;
    }

    public static void setWasMove(boolean wasMove) {
        Game.wasMove = wasMove;
    }



    public void addQuitButton(){
        ImageView quitButton = new ImageView(new Image("menu/buttons/heroes_quit.png"));
        quitButton.setLayoutY(620);
        quitButton.setLayoutX(1220);
        quitButton.setOnMouseClicked(event ->  StartingMenu.exitApp());
        quitButton.setFitWidth(140);
        quitButton.setPreserveRatio(true);
        quitButton.styleProperty().bind(
                Bindings
                        .when(quitButton.hoverProperty())
                        .then(
                                new SimpleStringProperty(HOVERED_BUTTON_STYLE)
                        )
                        .otherwise(
                                new SimpleStringProperty(STANDARD_BUTTON_STYLE)
                        )
        );
        this.gameBackground.getChildren().add(quitButton);
    }


    private EventHandler<MouseEvent> endMoveButtonClicked = e -> {
        if (!isMoving) {
            endTurn();
        }
    };

    private void endTurn(){
        deleteSquareShadows();
        UnitView.removeHighlightFromUnit(unitsInTheGame.get(iterUnit).getUnitView());
        UnitView.removeHighlightFromGroupOfUnits(this.possibleUnitsToAttack);
        changeIterUnitToNextUnit();
        UnitView.higlightUnit(unitsInTheGame.get(iterUnit).getUnitView());
        checkWhereCanMove();
        checkPossibleUnitsToAttack(unitsInTheGame.get(iterUnit));
        UnitView.highlightGroupOfUnits(this.possibleUnitsToAttack);
        makeSquareShadows();
    }


 
    private EventHandler<MouseEvent> onMouseClickedHandler = e -> {

        if (!isMoving) {
            if (e.getSource().getClass().getName().equals("com.heroes.model.Square")) {
                if (!wasMove) {
                    Square square = (Square) e.getSource();
                    for (Square squareToMove : this.squaresToMove) {
                        if (squareToMove.getName() == square.getName()) {
                            setMoving(true);
                            setWasMove(true);
                            deleteSquareShadows();
                            move(square);
                            UnitView.removeHighlightFromGroupOfUnits(this.possibleUnitsToAttack);
                            checkPossibleUnitsToAttack(unitsInTheGame.get(iterUnit));

 
                            UnitView.highlightGroupOfUnits(this.possibleUnitsToAttack);
                            new Thread(() -> {
                                try {
                                    Thread.sleep(MouseUtils.moveTime);
                                    if (this.possibleUnitsToAttack.size() == 0){
                                        System.out.println("there is no possible attack for "+ this.unitsInTheGame.get(iterUnit).getName());
                                        endTurn();
                                    }
                                } catch (Exception error) {
                                    System.err.println(error);
                                }
                            }).start();
                        }
                    }
                }
            }
            if (e.getSource().getClass().getName().equals("javafx.scene.image.ImageView")) {
                mainLoop:for (Unit unit : unitsInTheGame) {
                    if (e.getTarget().toString().equals(unit.getUnitView().getDefaultPhoto().toString())) {
                        if (!wasMove){
                            if (this.unitsInTheGame.get(iterUnit).getName().equals(unit.getName())) {
                                this.unitsInTheGame.get(iterUnit).setDefending(true);
                                System.out.println(unit.getName() + " is defending");
                                endTurn();
                                break;
                            }
                        }
                        for (Unit unitToAttact : this.possibleUnitsToAttack) {
                            if (unitToAttact.getName().equals(unit.getName())) {
                                unitToAttact.attack(this.unitsInTheGame.get(iterUnit),unit);
                                if(this.unitsInTheGame.get(iterUnit).isShooter() && ((Math.abs(this.unitsInTheGame.get(iterUnit).getX() - unitToAttact.getX()) > 1 || Math.abs(this.unitsInTheGame.get(iterUnit).getY() - unitToAttact.getY()) > 1))){
                                    MouseUtils.universalAnimation(this.unitsInTheGame.get(iterUnit), this.unitsInTheGame.get(iterUnit).getUnitView().getShootAnimation());
                                }else {
                                    MouseUtils.universalAnimation(this.unitsInTheGame.get(iterUnit), this.unitsInTheGame.get(iterUnit).getUnitView().getAttackAnimation());
                                }

                                if (unit.isDead() == false) {
                                    MouseUtils.universalAnimation(unit, unit.getUnitView().getHitAnimation());
                                } else {
                                    MouseUtils.universalAnimation(unit, unit.getUnitView().getDeathAnimation());
                                }
                                deleteSquareShadows();
                                new Thread(() -> {
                                    try {
                                    Thread.sleep((long) (MouseUtils.attackTime*1.2));

                                        if (P1.getLeftUnits() == 0 || P2.getLeftUnits() == 0) {
//                                    END GAME
                                            System.out.println("GAME OVER");
                                        }
                                        endTurn();
                                    } catch (Exception error) {
                                        System.err.println(error);
                                    }
                                }).start();
                                break mainLoop;
                            }
                        }
                        if (unit.getTown().equals(this.unitsInTheGame.get(iterUnit).getTown())) {
                            System.out.println(unit.getName() + "its your teammate");
                        } else {
                            System.out.println("you are too far away from " + unit.getName());
                        }

                        break;
                    }
                }
            }
        }
    };


    private void changeIterUnitToNextUnit(){
        if (iterUnit < 13 ) {
            do {
                iterUnit++;
                if (iterUnit > 13){
                    iterUnit = 0;
                }
            }
            while(this.unitsInTheGame.get(iterUnit).isDead );


        } else iterUnit = 0;
        setWasMove(false);
    }
    private void makeSquareShadows(){
        Square.highlightStandableSquares(squaresToMove, Square.getSquareOpacityValues().get("Highlight"));
    }
    private void deleteSquareShadows(){
        Square.highlightStandableSquares(this.squaresToMove, Square.getSquareOpacityValues().get("Normal"));
    }
    private void checkWhereCanMove(){
        this.squaresToMove = Validation.createArrayOfSquareToMove(this.unitsInTheGame.get(iterUnit), this.squaresList);
    }
    private void checkPossibleUnitsToAttack(Unit unit){
        this.possibleUnitsToAttack = unit.createArrayOfUnitsToAttack(this.unitsInTheGame.get(iterUnit), this.unitsInTheGame);
    }
    private void move(Square square){
        this.unitsInTheGame.get(iterUnit).setDefending(false);
        MouseUtils.moveToSquare(this.unitsInTheGame.get(iterUnit), square);
    }

 
    private void createPlayersAndTheirsUnits() throws IOException {
        this.P1 = new Player("P1", true, "Castle");
        this.P2 = new Player("P2", false, "Inferno");
        arrayOfPlayers[0] = P1;
        arrayOfPlayers[1] = P2;
        for (Player player : arrayOfPlayers) {
            player.createUnitsObjects(player);
            player.setUnitSeeding(squaresList);
            UnitView.attachPhoto(player, this.gameBackground);
            UnitView.refineStartingCoords(player, this);
            player.createVboxWithUnitCounters(player, this.gameBackground);
        }
    }


 
    private void addMouseEventHandlers(Square gamesquare) {
        gamesquare.setOnMouseClicked(onMouseClickedHandler);
    }

 
    private void createSquares() {
        for (int listHeigth = 1; listHeigth <= 11; listHeigth++) {
            for (int listWidth = 1; listWidth <= 15;listWidth++) {
                Square gameSquare = new Square(listWidth, listHeigth);
                gameSquare.setBlurredBackground();
                gameSquare.setLayoutX(((listWidth - 1) * 80) + 300);
                gameSquare.setLayoutY(((listHeigth - 1) * 80) + 140);
                gameSquare.setId(gameSquare.getName());
                addMouseEventHandlers(gameSquare);
                squaresList.add(gameSquare);
                this.gameBackground.getChildren().add(gameSquare);
            }
        }
    }

 
    private void createArrayListOfAllUnitsInTheGame() {
        ArrayList<Unit> unitsInTheGame = new ArrayList<>();
        for (Unit unit : P1.getUnitList()) {
            unit.getUnitView().getDefaultPhoto().setOnMouseClicked(onMouseClickedHandler);
            unitsInTheGame.add(unit);      
        }
        for (Unit unit : P2.getUnitList()) {
            unit.getUnitView().getDefaultPhoto().setOnMouseClicked(onMouseClickedHandler);
            unitsInTheGame.add(unit);     
        }

        Collections.sort(unitsInTheGame, new Comparator<Unit>() {    
            public int compare(Unit u1, Unit u2) {
                return Integer.valueOf(u2.initiative).compareTo(u1.initiative);  
            }
        });
        for (int i = 0; i < unitsInTheGame.size(); i++) {
            System.out.println(unitsInTheGame.get(i).name + " posiada inicjatywe = " + unitsInTheGame.get(i).initiative);
        }
        this.unitsInTheGame = unitsInTheGame;
    }

}




