package dungeon_and_dragon.gears;

public class Heal extends Gear{
    public Heal(String name, int heal) {
        super("Soin", name, heal);
    }

    @Override
    public String toString() {
        return super.toString() +" PV";
    }
}
