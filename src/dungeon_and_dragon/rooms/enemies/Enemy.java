package dungeon_and_dragon.rooms.enemies;

import dungeon_and_dragon.rooms.Room;

import java.util.Arrays;
import java.util.Random;

public abstract class Enemy extends Room {
    private String type;
    private String name;
    private int level;
    private int[] attack;
    private int life;
    private int lifeMax;
    private int defense;
    private final java.util.Random rand = new java.util.Random();
    ////////////////////////////////////////////////////////////////

    public Enemy(String type, String name, int level, int[] attack, int life,  int defense) {
        this.type = type;
        this.name = name;
        this.level = level;
        this.attack = attack;
        this.life = life;
        this.lifeMax = life;
        this.defense = defense;
    }

    ////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int[] getAttack() {
        return attack;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLifeMax() {
        return lifeMax;
    }

    public int getDefense() {
        return defense;
    }

    ////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        return "\n######### - FICHE ENNEMI - #########" +
                "\n# Le " + type + " nommer " + name + ", niveau " + level +
                "\n# " + life + "/"+getLifeMax()+"PV, attaque " + attack[0] + "-" +attack[1] +
                "}\n#######################################\n";
    }

}
