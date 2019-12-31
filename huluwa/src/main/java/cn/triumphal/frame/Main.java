package cn.triumphal.frame;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(this.getClass().getResource("/sample.fxml"));
        Parent root = fxmlLoader.load();
        Sountracks sountrack = new Sountracks();

        Sountracks.chooseSoundtrack(Sountracks.Themes.MAIN);
        Sountracks.getMainTheme().setAutoPlay(true); //设置自动播放
        Sountracks.getMainTheme().setCycleCount(20); //设置循环播放次数
        Sountracks.getMainTheme().play();
        primaryStage.setTitle("Huluwa VS Monster");
        Scene scene = new Scene(root,1000,600);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon.png")));
        primaryStage.setResizable(false);
        Controller controller = fxmlLoader.getController();
        controller.InitController();
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}