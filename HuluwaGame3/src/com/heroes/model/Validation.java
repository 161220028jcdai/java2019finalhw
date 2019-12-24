package com.heroes.model;

import java.util.ArrayList;
import java.util.List;


/**
 * —È÷§ 
 */
public class Validation {
 
    public static List<Square> createArrayOfSquareToMove(Unit unit, List<Square> squaresList){
        List<Square>squaresToMove = new ArrayList<>();
        for (Square square : squaresList){
            if ((Math.abs(square.getLocationX() - unit.getX()) + Math.abs(square.getLocationY()-unit.getY()) <= unit.getMoveRange()) && square.getIsStandable()){
                squaresToMove.add(square);
            }
        }
 
        return squaresToMove;
    }
 
}
