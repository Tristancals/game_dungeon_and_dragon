package dungeon_and_dragon.interfaces;

import dungeon_and_dragon.Menu;

public interface SufferedAnAttack {
    void sufferedAnAttack(SufferedAnAttack enemy, Menu menu);
    int getDamages();

    int getDefense();

    String getName();

    String getType();
}
