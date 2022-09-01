package dungeon_and_dragon.rooms.enemies;

public class Dragon extends Enemy{
    public Dragon(String name, int level) {
        super("Dragon", name, level,new int[]{0,4},15,3);
    }
}
