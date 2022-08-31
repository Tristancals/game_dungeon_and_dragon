package dungeon_and_dragon.gears;

public abstract class Offensive extends Gear{
    public Offensive(String type, String name, int attack) {
        super(type, name, attack);
    }
    public String toString() {
        return super.toString()+" DEG";
    }
}
