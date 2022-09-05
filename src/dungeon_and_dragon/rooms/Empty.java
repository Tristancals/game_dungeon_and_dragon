package dungeon_and_dragon.rooms;

import dungeon_and_dragon.Game;
import dungeon_and_dragon.Menu;
import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.interfaces.Interactable;

public class Empty implements Interactable {
    @Override
    public String toString() {
        return "\n# Une piece vide.......";
    }

    @Override
    public void interact(Hero player, Menu menu, Game game) {

    }
}
