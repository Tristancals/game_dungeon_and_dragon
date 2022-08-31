package dungeon_and_dragon.characters;

import dungeon_and_dragon.Menu;
import dungeon_and_dragon.gears.Defensive;
import dungeon_and_dragon.gears.Heal;
import dungeon_and_dragon.gears.Offensive;
import dungeon_and_dragon.interfaces.SufferedAnAttack;

import java.util.ArrayList;

public abstract class Character implements SufferedAnAttack {
    private String type;
    private String name;
    private int level;
    private int life;
    private int lifeMax;
    private int position;
    private int[] attack;
    private int defense;
    private Offensive offensive;
    private Defensive defensive;
    private ArrayList<Heal> inventory = new ArrayList<Heal>();
    private final java.util.Random rand = new java.util.Random();

    ////////////////////////////////////////////////////////////////

    public Character(String type, String name, int level,
                        int life, int[] attack,
                        Offensive offensive, Defensive defensive, ArrayList<Heal> inventory) {
        this.type = type;
        this.name = name;
        this.level = level;
        this.life = life;
        this.lifeMax = life;
        this.attack = attack;
        this.offensive = offensive;
        this.defensive = defensive;
        this.inventory = inventory;
    }

    ////////////////////////////////////////////////////////////////
    public void useHeal(){

    }

    public void sufferedAnAttack(SufferedAnAttack enemy, Menu menu) {
    }
    public boolean isALife() {
        return getLife() > 0;
    }
    public int getDamages() {
       return 0;
    }
    public String displayInventory(){
       return "";
    }

    public Heal playerHasAlreadyThisHeal(Menu menu) {
        return new Heal("",0);
    }

    ////////////////////////////////////////////////////////////////
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    public int getLife() {
        return life;
    }

    private void setLife(int life) {
        this.life = life;
    }

    public int getLifeMax() {
        return lifeMax;
    }

    private void setLifeMax(int lifeMax) {
        this.lifeMax = lifeMax;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int[] getAttack() {
        return attack;
    }

    private void setAttack(int[] attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    private void setDefense(int defense) {
        this.defense = defense;
    }

    public Offensive getOffensive() {
        return offensive;
    }

    private void setOffensive(Offensive offensive) {
        this.offensive = offensive;
    }

    public Defensive getDefensive() {
        return defensive;
    }

    private void setDefensive(Defensive defensive) {
        this.defensive = defensive;
    }


    ////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "\n######### - FICHE PERSONNAGE - #########" +
                "\n# Le " + type + " nommer " + name + ", niveau " + level +
                "\n# " + life + "/" + getLifeMax() + "PV, attaque " + attack[0] + "-" + attack[1] +
                "\n# " + offensive +
                "\n# " + defensive +
                "\n# " + displayInventory() +
                "\n# Avec pour position " + position +
                "}\n##########################################\n";
    }
}
