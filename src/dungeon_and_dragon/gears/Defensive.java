package dungeon_and_dragon.gears;

public abstract class Defensive extends Gear{
    public Defensive(String type, String name, int defense) {
        super(type, name, defense);
    }
    @Override
    public String toString() {
        return super.toString()+" DEF";
    }
}
