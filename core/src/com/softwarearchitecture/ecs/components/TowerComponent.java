package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

public class TowerComponent implements Serializable {

    private int damage;
    private int range;
    private int attackCooldown;
    private float timeSinceLastAttack;

    public TowerComponent(int damage, int range, int attackCooldown) {
        if (damage >= 1 && range >= 1 && attackCooldown >= 1) {
            this.damage = damage;
            this.range = range;
            this.attackCooldown = attackCooldown;
        }
        else {
            this.damage = 1;
            this.range = 1;
            this.attackCooldown = 1;
        }
    }

    public int getDamage() {
        return this.damage;
    }

    public int getRange() {
        return this.range;
    }

    public void setDamage(int damage) {
        if (damage >= 1) {
            this.damage = damage;
        }
        else {
            this.damage = 1;
        }
    }

    public void setRange(int range) {
        if (range >= 1) {
            this.range = range;
        }
        else {
            this.range = 1;
        }
    }

    public void updateTimeSinceLastAttack(float deltaTime) {
        if (timeSinceLastAttack < attackCooldown) {
            timeSinceLastAttack += deltaTime;
        }
    }

    public boolean canAttack() {
        return timeSinceLastAttack >= attackCooldown;
    }

    public void resetAttackTimer() {
        timeSinceLastAttack = 0;
    }
}
