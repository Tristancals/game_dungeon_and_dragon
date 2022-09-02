package dungeon_and_dragon.heros;

import dungeon_and_dragon.Game;
import dungeon_and_dragon.Menu;
import dungeon_and_dragon.gears.Defensive;
import dungeon_and_dragon.gears.Heal;
import dungeon_and_dragon.gears.Offensive;
import dungeon_and_dragon.interfaces.SufferedAnAttack;
import dungeon_and_dragon.rooms.Chest;
import dungeon_and_dragon.rooms.Empty;
import dungeon_and_dragon.rooms.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Hero implements SufferedAnAttack {
    private final String type;
    private final String name;
    private final int level;
    private int life;
    private final int lifeMax;
    private int position;
    private final int[] attack;
    private int defense;
    private Offensive offensive;
    private Defensive defensive;
    private ArrayList<Heal> inventory;
    private final java.util.Random rand = new java.util.Random();

    ////////////////////////////////////////////////////////////////

    public Hero(String type, String name, int level,
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
        if (this.inventory == null) {
            this.inventory = new ArrayList<>();
        }
    }

    ////////////////////////////////////////////////////////////////
    public void selectHeal(Menu menu) {
        int index = 0;
        String[] choices = new String[inventory.size()];
        Map<String, Runnable> functionChoiceMap = new HashMap<>();

        for (Heal potion : inventory) {
            index++;
            choices[index - 1] = "'" + index + "' pour une " + potion.getName() + " restaure " + potion.getStats() + " PV";
            functionChoiceMap.put(String.valueOf(index),
                    () -> {
                        setLife((Math.min(getLife() + potion.getStats(), getLifeMax())));
                        this.inventory.remove(potion);
                    });

        }
        menu.displayChoice("Choisir une potion:", choices);
        menu.listenerChoice(functionChoiceMap);
    }

    public void sufferedAnAttack(SufferedAnAttack enemy, Menu menu, Game game) {
        int damage = enemy.getDamages();
        if (rand.nextInt(6 - enemy.getDefense()) > 0) {
            setLife(getLife() - damage);
            menu.display("# -" + enemy.getName() + " le " + enemy.getType() +
                    " vous inflige " + damage + " de dégâts à votre héro");
        } else {
            menu.display("# -Le " + enemy.getType() + " à raté sont attaque!!!");
        }
        if (getLife() > 0) {
            game.whatDoesThePlayerDoDuringTheFight(this, enemy, menu);
        } else {
            menu.display("# \n~~~~~~~~~ - YOU LOSE - ~~~~~~~~~" +
                    "\n# -Votre héro à mordu la poussière..." +
                    "\n#            ||||||" +
                    "\n#  ||||||||||||||||||||||||||" +
                    "\n#  |||||| -" + this.getName() + "- ||||||" +
                    "\n#  ||||||||||||||||||||||||||" +
                    "\n#            ||||||" +
                    "\n#            ||||||" +
                    "\n#            ||||||" +
                    "\n#            ||||||" +
                    "\n#            ||||||");
        }
    }

    public boolean isALife() {
        return getLife() > 0;
    }

    public int getDamages() {
        int min = getAttack()[0];
        int max = (getAttack()[1] - getAttack()[0]) + (1 + getOffensive().getStats());
        return min + rand.nextInt(max);
    }

    public String displayInventory() {
        String inventory = "Inventaire: ";
        for (Heal potion : this.inventory) {
            inventory += "\n# -" + potion.toString();
        }
        return inventory;
    }

    private boolean playerHasAlreadyThisHeal(Heal potion) {
        for (Heal p : this.inventory) {
            if ( p.equals(potion)){
                return true;
            }
        }
        return false;
    }

    public Room addHealToInventory(Room room,Menu menu) {
        Heal potion = ((Chest) room).getHeal();
        if (!playerHasAlreadyThisHeal(potion)) {
            inventory.add(potion);
            menu.display("# Je Stock cette potion!");
            return new Empty();
        } else {
            menu.display("# Je n'ai plus de place pour cette potion...");
            return room;
        }
    }

    ///////////////////////////////////////////////////////////////
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    protected int[] getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public Offensive getOffensive() {
        return offensive;
    }

    public void setOffensive(Offensive offensive) {
        this.offensive = offensive;
    }

    public Defensive getDefensive() {
        return defensive;
    }

    public void setDefensive(Defensive defensive) {
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
                "\n#######################################\n";
    }
}
