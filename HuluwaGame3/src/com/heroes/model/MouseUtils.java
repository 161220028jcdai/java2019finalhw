package com.heroes.model;

import java.util.Collection;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

//响应鼠标事件
public class MouseUtils extends Pane {
    public static int moveTime;
    public static int attackTime;


    public static void universalAnimation(Unit unit, List<Image> animationList){
        Timeline timeLine = new Timeline();
        Collection<KeyFrame> frames = timeLine.getKeyFrames();
        Duration frameGap = Duration.millis(106);
        Duration frameTime = Duration.ZERO;
        for (Image img : animationList) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> unit.getUnitView().getDefaultPhoto().setImage(img)));
        }
        timeLine.setCycleCount(0);
        timeLine.play();

        new Thread(() -> {
            try {
                Thread.sleep(106*(animationList.size()+1));
                if (animationList.equals(unit.getUnitView().getDeathAnimation())){
                    unit.getUnitView().getDefaultPhoto().setImage(unit.getUnitView().getDeathphoto());
                } else {
                    unit.getUnitView().getDefaultPhoto().setImage(unit.getUnitView().getStandPhoto());
                }
                Game.setMoving(false);
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }


    public static void moveToSquare(Unit unit, Square square) {
        if (unit.getUnitView().getDefaultPhoto() == null)
            return;

        double targetX = 0;
        double targetY = square.getLayoutY() - 215;

        if (unit.getOwner().getName().equals("P2")) {
            targetX = square.getLayoutX() - 225;
        }
        if (unit.getOwner().getName().equals("P1")) {
            targetX = square.getLayoutX() - 165;
        }

        double sourceX = unit.getUnitView().getDefaultPhoto().getLayoutX() + unit.getUnitView().getDefaultPhoto().getTranslateX();
        double sourceY = unit.getUnitView().getDefaultPhoto().getLayoutY() + unit.getUnitView().getDefaultPhoto().getTranslateY();

        Timeline timeLine = new Timeline();

        Collection<KeyFrame> frames = timeLine.getKeyFrames();
        Duration frameGap = Duration.millis(146);
        Duration frameTime = Duration.ZERO;
        for (Image img : unit.getUnitView().getMoveAnimation()) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> unit.getUnitView().getDefaultPhoto().setImage(img)));
        }
        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();

        new Thread(() -> {
            try {
                Thread.sleep((long) (Math.abs(moveTime)));
                timeLine.stop();
                unit.getUnitView().getDefaultPhoto().setImage(unit.getUnitView().getStandPhoto());
                Game.setMoving(false);
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();

        moveTime = (int) ((Math.sqrt(Math.pow((targetX - sourceX), 2) + Math.pow((sourceY - targetY), 2))) * 6);

        animateSpriteMovement(unit, sourceX, sourceY, targetX, targetY, Duration.millis(moveTime));

        unit.setX(square.getLocationX());        
        unit.setY(square.getLocationY());        
        unit.getPosition().setStandable(true);
        unit.setPosition(square); 
        square.setStandable(false);
    }


    private static void animateSpriteMovement(
            Unit unit, double sourceX, double sourceY,
            double targetX, double targetY, Duration duration) {


        Path path = new Path();
        path.getElements().add(new MoveToAbs(unit.getUnitView().getDefaultPhoto(), sourceX, sourceY));
        path.getElements().add(new LineToAbs(unit.getUnitView().getDefaultPhoto(), targetX, targetY));

        PathTransition pathTransition = new PathTransition(duration, path, unit.getUnitView().getDefaultPhoto());
        pathTransition.setInterpolator(Interpolator.EASE_IN);


        ParallelTransition pt = new ParallelTransition(unit.getUnitView().getDefaultPhoto(), pathTransition);
        pt.play();
    }


    private static class MoveToAbs extends MoveTo {
        MoveToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2,
                    y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }


    private static class LineToAbs extends LineTo {
        LineToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2,
                    y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }

}
