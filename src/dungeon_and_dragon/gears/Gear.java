package dungeon_and_dragon.gears;

public abstract class Gear {
    private String type;
    private String name;
    private int stats;

    public Gear(String type, String name, int stats) {
        this.type = type;
        this.name = name;
        this.stats = stats;
    }

    public String toString() {
        return "      " + name + ", " + stats ;
    }
}
