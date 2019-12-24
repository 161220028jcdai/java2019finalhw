package com.heroes.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.heroes.view.BackgroundView;
import com.heroes.view.UnitView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


//��ɫ
public class Player extends Pane {

    public int[] P1seeding = {0, 31, 45, 76, 105, 121, 150};
  
    private String name;
    private String town;
    private Boolean canMove;
    private List<Unit> unitList = new ArrayList<Unit>();
    private int leftUnits;
    private boolean startsOnLeftSide;
    private int[] P2seeding = {14, 43, 59, 88, 119, 133, 164};


    Player(String name, boolean leftSide, String town) {
        this.town = town;
        this.name = name;
        this.leftUnits = 7;
        this.startsOnLeftSide = leftSide;
    }

    public String getName() {
        return name;
    }

    public boolean getIfCanMove() {
        return canMove;
    }

    public void setIfCanMove(boolean yORn) {
        this.canMove = yORn;
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> listOfUnits) {
        this.unitList = listOfUnits;
    }

    public int getLeftUnits() {
        return leftUnits;
    }

    public void setLeftUnits(int numb) {
        this.leftUnits = numb;
    }

    public boolean getIfStartsOnLeftSide() {
        return startsOnLeftSide;
    }


    public void createUnitsObjects(Player player) throws IOException {
        Utils utils = new Utils();
        List<HashMap> ListOfUnitsProperties = utils.fileRead(utils.createPath());
        for (HashMap<String, String> unitProperties : ListOfUnitsProperties)
            if (unitProperties.get("town").equals(this.town)) {
                if (unitProperties.get("shooter").equals("true")) {
                    Unit unit = new Shooter(unitProperties, player);
                    unitList.add(unit);
                    UnitView unitView = new UnitView(unit);
                    unit.setUnitView(unitView);
                }
                if (unitProperties.get("shooter").equals("false")) {
                    Unit unit = new NonShooter(unitProperties, player);
                    unitList.add(unit);
                    UnitView unitView = new UnitView(unit);
                    unit.setUnitView(unitView);
                }
            }
    }

    


    public void setUnitSeeding(List<Square> squaresList) {
        if (this.startsOnLeftSide) {
            for (int i = 1; i <= this.unitList.size(); i++) {
                unitList.get(i - 1).setPosition(squaresList.get(P1seeding[i - 1]));

                System.out.println(unitList.get(i-1).getName());                            //checking and debuging
                System.out.println("from txt file position X = " + unitList.get(i-1).getX());      //checking and debuging
                System.out.println("from txt file position Y = " + unitList.get(i-1).getY());      //checking and debuging

                unitList.get(i-1).setX(((P1seeding[i-1])%15)+1);                            //updating unit X based on the square number
                unitList.get(i-1).setY(((P1seeding[i-1])/15)+1);                            //updating unit Y based on the square number

                System.out.println("after updating X = " + unitList.get(i-1).getX());                //checking and debuging
                System.out.println("after updating Y = " + unitList.get(i-1).getY());                //checking and debuging

                squaresList.get(P1seeding[i - 1]).setStandable(false);
            }
        } else {
            for (int i = 1; i <= this.unitList.size(); i++) {
                unitList.get(i - 1).setPosition(squaresList.get(P2seeding[i - 1]));

                System.out.println(unitList.get(i-1).getName());
                unitList.get(i-1).setX(((P2seeding[i-1])%15)+1);                            //updating unit X based on the square number
                unitList.get(i-1).setY(((P2seeding[i-1])/15)+1);
                System.out.println("after updating X = " + unitList.get(i-1).getX());                //checking and debuging
                System.out.println("after updating Y = " + unitList.get(i-1).getY());

                squaresList.get(P2seeding[i - 1]).setStandable(false);
            }
        }
    }

    /**
     * Creates two vBox that contains labels with quantity of lefts units
     * */

    public void createVboxWithUnitCounters(Player player, BackgroundView gamebackground){
        VBox vbox = new VBox(40);
        vbox.setPadding(new Insets(200, 10, 10, 10));
        vbox.setAlignment(Pos.BASELINE_CENTER);
        gamebackground.getChildren().add(vbox);

        for(Unit unit : player.getUnitList()){
        	String str="";
        	switch(unit.getName()) {
        	case "red":
        		str="����";
        		break;
    		case "orange":
    			str="����";
    			break;
    		case "yellow":
    			str="����";
    			break;
    		case "green":
    			str="����";
    			break;
    		case "blue":
    			str="����";
    			break;
    		case "cyran":
    			str="����";
    			break;
    		case "purple":
    			str="����";
    			break;
    		case "Demon":
    			str="����";
    			break;
    		case "Devil":
    			str="���";
    			break;
    		case "Efreet":
    			str="����";
    			break;
    		case "Gog":
    			str="����";
    			break;
    		case "HellHound":
    			str="��Ȯ";
    			break;
    		case "Imp":
    			str="�ɹ�";
    			break;
    		case "PitFiend":
    			str="��Ԩ��ħ";
    			break;
    		default:
    			break;
        	}
        	System.out.println(unit.getName()+" "+str);         
            Label counter = new Label();
            counter.setText(str+" "+ String.valueOf(unit.getQuantity())+"Ѫ"+unit.getAttackPower()+(unit.isShooter()?"Զ��":"����")+unit.getDefencePower()+"��");
            String namestring=str;
            unit.pcs.addPropertyChangeListener(evt -> {
                counter.setText(namestring +" "+ String.valueOf(evt.getNewValue())+"Ѫ"+unit.getAttackPower()+(unit.isShooter()?"Զ��":"����")+unit.getDefencePower()+"��");
            });
            counter.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 14px; -fx-font-weight: 800;");
            vbox.getChildren().add(counter);
            vbox.setAlignment(Pos.CENTER_RIGHT);
        }

        if (!player.startsOnLeftSide){
            vbox.setLayoutX(1600);
        }
    }
}
