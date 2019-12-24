package com.heroes.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.heroes.view.UnitView;

import javafx.scene.Node;

//游戏单元，数据在txt中定义
public abstract class Unit extends Node {

    protected String name;
    protected String town;
    protected boolean shooter;
    protected Player owner;
    protected int attackPower;
    protected int defencePower;
    protected int minAttackDamage;
    protected int maxAttackDamage;
    protected int healthPoints;
    protected int healthPointsLeft;
    protected int moveRange;
    protected int initiative;
    protected int quantity;
    protected int x;
    protected int y;
    protected boolean isDefending;
    protected boolean isDead;
    protected UnitView unitView;

    protected Square position;


    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void setHealthPointsLeft(int healthPointsLeft) {
        this.healthPointsLeft = healthPointsLeft;
    }

    public void setQuantity(int newValue) {
        int oldValue = this.quantity;
        this.quantity = newValue;
        this.pcs.firePropertyChange("value", oldValue, newValue);
    }

    public void setUnitView(UnitView arg) {
        unitView = arg;
    }

    public boolean isShooter() {
        return shooter;
    }

    public Player getOwner() {
        return owner;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefencePower() {
        return defencePower;
    }

    public int getMinAttackDamage() {
        return minAttackDamage;
    }

    public int getMaxAttackDamage() {
        return maxAttackDamage;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getHealthPointsLeft() {
        return healthPointsLeft;
    }

    public int getMoveRange() {
        return moveRange;
    }

    public int getInitiative() {
        return initiative;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDefending() {
        return isDefending;
    }

    public boolean isDead() {
        return isDead;
    }

    public String getName() {
    	return name;
    }

    public String getTown() {
        return town;
    }

    public UnitView getUnitView() {
        return unitView;
    }

    public Square getPosition() {
        return position;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public void setDefending(boolean defending) {
        isDefending = defending;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void move() {
    }

    public int calculateAttackBonus() {
        return 69;
    }

    public void defend() {
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }


    public List<Unit> createArrayOfUnitsToAttack(Unit movingUnit, List<Unit> unitsInTheGame){
        List<Unit> unitsToAttack = new ArrayList<>();
        for (Unit unit : unitsInTheGame){
            if (Math.abs(unit.getX() - movingUnit.getX()) <= 1 && Math.abs(unit.getY() - movingUnit.getY()) <= 1 && !(unit.getTown().equals(movingUnit.getTown())) && !(unit.isDead)) {
                unitsToAttack.add(unit);
            }
        }

        return unitsToAttack;

    }

    public void attack(Unit attackingUnit, Unit attackedUnit){

        int totalAttackDamage = 0;
        double attackBonus;
        double defenceBonus;
        int attackFinalPower;
        int attackedUnitsTotalHealthBeforeAttack;
        int attackedUnitsTotalHealthAfterAttack;

        attackingUnit.setDefending(false);

        // for testing
        System.out.printf("attacker is %s and defender is %s\n" ,attackingUnit.getName(),attackedUnit.getName());
        System.out.printf("defender: quantity of units before attack = %d , health left before attack  = %d\n",attackedUnit.getQuantity(), attackedUnit.getHealthPointsLeft());

        for (int i = 1; i <= attackedUnit.getQuantity(); i++){
            int randomNum = ThreadLocalRandom.current().nextInt(attackingUnit.getMinAttackDamage(), attackingUnit.getMaxAttackDamage() + 1);
            totalAttackDamage += randomNum;
        }
        int attackPowerVsDefencePower = attackingUnit.getAttackPower() - attackedUnit.getDefencePower();
        if (attackPowerVsDefencePower > 0) {
            attackBonus = (attackPowerVsDefencePower * 0.05);
            if (attackBonus > 3){attackBonus = 3;} //attack bonus max 300% which is 60points difference
            attackFinalPower = (int) (totalAttackDamage + (totalAttackDamage * attackBonus));

        } else {
            defenceBonus = (Math.abs(attackPowerVsDefencePower * 0.025));
            if (defenceBonus > 0.3) {defenceBonus = 0.3;}  
            attackFinalPower = (int) (totalAttackDamage - (totalAttackDamage * defenceBonus));
        }
        if (attackedUnit.isDefending()){    
            attackFinalPower *= 0.7;
        }
        attackedUnitsTotalHealthBeforeAttack = ((attackedUnit.getQuantity() - 1) * attackedUnit.getHealthPoints()) + attackedUnit.getHealthPointsLeft();
        attackedUnitsTotalHealthAfterAttack = attackedUnitsTotalHealthBeforeAttack - attackFinalPower;
        if (attackedUnitsTotalHealthAfterAttack > 0){
            attackedUnit.setQuantity((attackedUnitsTotalHealthAfterAttack / attackedUnit.getHealthPoints()) + 1 );
            attackedUnit.setHealthPointsLeft(attackedUnitsTotalHealthAfterAttack % attackedUnit.getHealthPoints());
            attackedUnit.setDefending(false);
        } else {
            attackedUnit.setQuantity(0);
            attackedUnit.setHealthPointsLeft(0);
            attackedUnit.setDead(true);
            attackedUnit.getOwner().setLeftUnits(attackedUnit.getOwner().getLeftUnits()-1);
        }

        // for testing
        System.out.printf("defender: quantity of units after attack = %d , health left after attack  = %d\n",attackedUnit.getQuantity(), attackedUnit.getHealthPointsLeft());
    }
}
