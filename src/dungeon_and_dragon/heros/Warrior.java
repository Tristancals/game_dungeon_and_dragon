package dungeon_and_dragon.heros;

import dungeon_and_dragon.gears.*;
import java.util.ArrayList;

public class Warrior extends Hero {
    public Warrior(int id,String name, int level, Offensive offensive,
                   Defensive defensive, ArrayList<Heal> inventory) {

        super(id,"Warrior",
                name, level,
                10,
                new int[]{5, 10},
                offensive,
                defensive,
                inventory);
    }
}
