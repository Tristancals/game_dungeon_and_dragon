package dungeon_and_dragon.rooms.enemies;

public class Corpse extends Enemy{
    public Corpse(String type, String name) {
        super("Cadavre de "+type, name, 0, new int[]{0,0}, 0, 0);
    }

    @Override
    public String toString() {
        return  super.getType();
    }
}
