package dungeon_and_dragon.heros;

import dungeon_and_dragon.gears.*;
import java.util.ArrayList;

public class Warrior extends Hero {
    public Warrior(String name, int level, Offensive offensive,
                   Defensive defensive, ArrayList<Heal> inventory) {

        super("Guerrier",
                name, level,
                10,
                new int[]{5, 10},
                offensive,
                defensive,
                inventory);
    }
}
