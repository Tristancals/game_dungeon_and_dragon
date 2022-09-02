package dungeon_and_dragon;

import dungeon_and_dragon.rooms.Chest;
import dungeon_and_dragon.rooms.Empty;
import dungeon_and_dragon.rooms.Room;
import dungeon_and_dragon.rooms.enemies.Dragon;
import dungeon_and_dragon.rooms.enemies.Goblin;
import dungeon_and_dragon.rooms.enemies.Sorcerer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dungeon {

    private int nbrRoom;

    private List<Room> level = new ArrayList<>();

    private int dungeonLevel;
    private final List<String> listNames = Arrays.asList("Lynn", "Vaughan", "Dunlap", "Carmella", "Gloria", "Krista", "Haynes", "Pitts", "Norma", "Andrews", "Bernadine", "Miranda", "Tyler", "Rowland", "Jeanne", "Bridgett", "Elvira", "Emily", "Carr", "Adrian", "Ann", "Sargent", "Morrison", "Grant", "Ethel", "Hodge", "Jenifer", "Cunningham", "Pace", "Juliana", "Mcdowell", "Mcdowell", "Zimmerman", "Aline");
    private final java.util.Random rand = new java.util.Random();

    public Dungeon(int nbrRoom, int dungeonLevel) {
        this.nbrRoom = nbrRoom;
        this.dungeonLevel = dungeonLevel;
        initDungeon();
    }

    public void initDungeon() {
        for (int i = 0; i < nbrRoom; i++) {
            if (i == 0) {
                level.add(new Empty());
            } else {
                level.add(switch (rand.nextInt(3)) {
                    case 0 -> new Empty();
                    case 1 -> new Chest();
                    default -> selectMonster();
                });
            }
        }
        level.add(new Dragon("Le grand "+setMonsterName(), dungeonLevel+3));
    }

    public Room selectMonster() {
        String name = setMonsterName();
        return switch (rand.nextInt(6)) {
            case 0, 1, 2 -> new Goblin(name, dungeonLevel);
            case 3, 4 -> new Sorcerer(name, dungeonLevel);
            default -> new Dragon(name, dungeonLevel);
        };
    }

    public String setMonsterName() {
        return this.listNames.get(rand.nextInt(this.listNames.size()));
    }

    public List<Room> getLevel() {
        return level;
    }
}
