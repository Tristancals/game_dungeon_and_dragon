package dungeon_and_dragon.rooms;

import dungeon_and_dragon.gears.*;

public class Chest extends Room {
    private Heal heal = null;
    private Offensive offensive = null;
    private Defensive defensive = null;

    private final java.util.Random rand = new java.util.Random();

    public Chest() {
        initLoot();
    }


    public void initLoot() {
        switch (rand.nextInt(4)) {
            case 0 -> initLootDefensive();
            case 1 -> initLootOffensive();
            default -> initLootPotion();
        }
    }

    private void initLootDefensive() {
        defensive = switch (rand.nextInt(6)) {
            case 0, 1 -> new Shield("Bouclier", 2);
            case 2 -> new Shield("Grand bouclier", 3);
            case 3, 4 -> new EnergyShield("Philter", 2);
            default -> new EnergyShield("Grand Philter", 3);
        };
    }

    private void initLootOffensive() {
        offensive = switch (rand.nextInt(6)) {
            case 0, 1 -> new Weapon("Massue", 3);
            case 2 -> new Weapon("Épée", 5);
            case 3, 4 -> new Spell("Éclair", 2);
            default -> new Spell("Boule de feu", 7);
        };
    }

    private void initLootPotion() {
        heal = switch (rand.nextInt(3)) {
            case 0, 1 -> new Heal("Petite Potion", 3);
            default -> new Heal("Potion", 5);
        };
    }


    public Heal getHeal() {
        return heal;
    }

    public Offensive getOffensive() {
        return offensive;
    }

    public Defensive getDefensive() {
        return defensive;
    }


    public String toString() {
        return "\n# Il y a un coffre...." +
                (heal != null ? heal.toString() : "") +
                (offensive != null ? offensive.toString() : "") +
                (defensive != null ? defensive.toString() : "");
    }

}
