package com.heroes.main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;

import com.heroes.audio.Sountracks;
import com.heroes.model.Game;
import com.heroes.model.StartingMenu;
import com.heroes.view.BackgroundView;

//启动类
public class Huluwa extends Application{

    private static final double WINDOW_WIDTH = 1920;
    private static final double WINDOW_HEIGHT = 1080;
    private static BackgroundView gameBackground;
    private static Thread activeThread;

    public static void main(String[] args) {
        launch(args);
    }

    public static Thread getActiveThread() {
        return activeThread;
    }

    public static BackgroundView getGameBackground() {
        return gameBackground;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        BackgroundView gameBackground = new BackgroundView(primaryStage);
        this.gameBackground = gameBackground;

        gameBackground.changeBackground(gameBackground, gameBackground.getPathToStartingMenuBackground());

        Sountracks sountrack = new Sountracks();

        Sountracks.chooseSoundtrack(Sountracks.Themes.MAIN);
        Sountracks.getMainTheme().setAutoPlay(true); //设置自动播放
        Sountracks.getMainTheme().setCycleCount(20); //设置循环播放次数
        Sountracks.getMainTheme().play();

		

        StartingMenu startingMenu = new StartingMenu(gameBackground);

        primaryStage.setTitle("葫芦娃");
        primaryStage.setScene(new Scene(gameBackground, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static void startGame(BackgroundView gameBackground ) throws IOException {
        Sountracks.chooseSoundtrackToStop(Sountracks.Themes.MAIN);
        Sountracks.chooseSoundtrack(Sountracks.Themes.BATTLE);
        Sountracks.getBattleTheme().setAutoPlay(true);
        Sountracks.getBattleTheme().setCycleCount(20); //设置循环播放次数
        Sountracks.getBattleTheme().play();
        Game game = new Game(gameBackground);
        gameBackground.changeBackground(gameBackground, gameBackground.getPathToFieldMap());
    }



}
