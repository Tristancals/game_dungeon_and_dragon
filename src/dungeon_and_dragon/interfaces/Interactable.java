package dungeon_and_dragon.interfaces;

import dungeon_and_dragon.Game;
import dungeon_and_dragon.Menu;
import dungeon_and_dragon.heros.Hero;

public interface Interactable {

    void interact (Hero player, Menu menu, Game game);
}
