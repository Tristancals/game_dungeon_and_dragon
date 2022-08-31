package dungeon_and_dragon.characters;

import dungeon_and_dragon.gears.*;

import java.util.ArrayList;

public class Wizard extends Character{
    public Wizard(String name, int level, Offensive offensive,
                   Defensive defensive, ArrayList<Heal> inventory) {

        super("Magicien",
                name, level,
                8,
                new int[]{8, 15},
                offensive,
                defensive,
                inventory);
    }
}
