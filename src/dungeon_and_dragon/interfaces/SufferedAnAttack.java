package dungeon_and_dragon.interfaces;

import dungeon_and_dragon.Game;
import dungeon_and_dragon.Menu;

public interface SufferedAnAttack {
    void sufferedAnAttack(SufferedAnAttack enemy, Menu menu, Game game);
    int getDamages();

    int getDefense();

    int getPosition();

    void setPosition(int position);

    String getName();

    String getType();
}
