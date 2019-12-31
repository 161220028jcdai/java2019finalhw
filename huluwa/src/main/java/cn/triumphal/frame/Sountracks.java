package cn.triumphal.frame;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

//添加燃向背景音乐
public class Sountracks {

    private static MediaPlayer mainTheme;
    private static MediaPlayer battleTheme;

String mainThemeFile = "";
String battleThemeFile = "";
 

    public Sountracks() {
    	mainThemeFile = getClass().getResource("/soundtracks/huluwa.mp3").toString();
    	System.out.println(mainThemeFile);
    	//battleThemeFile =getClass().getResource("/soundtracks/huluwa.mp3").toString(); //相对路径
    	battleThemeFile =  getClass().getResource("/soundtracks/huluwa.mp3").toString();//测试绝对路径
    	System.out.println(battleThemeFile);
    	this.loadSounds();
       
    }

    public static MediaPlayer getMainTheme() {
        return mainTheme;
    }

    public static MediaPlayer getBattleTheme() {
        return battleTheme;
    }

    private void loadSounds() {
        Media mainTheme = new Media( mainThemeFile);
        Sountracks.mainTheme = new MediaPlayer(mainTheme);

        Media battleTheme = new Media( battleThemeFile);
        Sountracks.battleTheme = new MediaPlayer(battleTheme);
        }


    public static void chooseSoundtrack(Themes theme){
        if (theme == Themes.MAIN){
            playMainTheme();
        } else if (theme == Themes.BATTLE){
            playBattleTheme();
        }
    }

    public static void chooseSoundtrackToStop(Themes theme){
        if (theme == Themes.MAIN){
            stopMainTheme();
        } else if (theme == Themes.BATTLE){
            stopBattleTheme();
        }
    }


    private static void stopMainTheme(){
        getMainTheme().stop();
    }

    private static void stopBattleTheme(){
        getBattleTheme().stop();
    }

    private static void playMainTheme(){
    	getMainTheme().setVolume(100);
        getMainTheme().play();
        getMainTheme().setOnError(() -> System.out.println("Error : " + getMainTheme().getError().toString()));


    }

    private static void playBattleTheme(){
        getBattleTheme().play();
    }
 

    public enum Themes {
        MAIN, BATTLE
    }



 
    }



