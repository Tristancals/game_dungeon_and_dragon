package dungeon_and_dragon.rooms.enemies;

public class Goblin extends Enemy{
    public Goblin( String name, int level) {
        super("Goblin", name, level, new int[]{0,1}, 6, 1);
    }
}
