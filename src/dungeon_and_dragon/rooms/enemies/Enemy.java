package dungeon_and_dragon.rooms.enemies;

import dungeon_and_dragon.Game;
import dungeon_and_dragon.Menu;
import dungeon_and_dragon.interfaces.SufferedAnAttack;
import dungeon_and_dragon.rooms.Room;

import java.util.Arrays;
import java.util.Random;

public abstract class Enemy extends Room implements SufferedAnAttack {
    private String type;
    private String name;
    private int level;
    private int[] attack;
    private int life;
    private int lifeMax;
    private int defense;
    private final java.util.Random rand = new java.util.Random();

    private int position;
    ////////////////////////////////////////////////////////////////

    public Enemy(String type, String name, int level, int[] attack, int life,  int defense) {
        this.type = type;
        this.name = name;
        this.level = level;
        this.attack = attack;
        this.life = life;
        this.lifeMax = life;
        this.defense = defense;

        buffEnemy(level);
    }

    ////////////////////////////////////////////////////////////////

    public void sufferedAnAttack(SufferedAnAttack player, Menu menu, Game game) {
        int damage =player.getDamages() ;
        if (rand.nextInt(6 - player.getDefense()) > 0) {
            setLife(getLife() - damage);
            menu.display("# -" + player.getName() + " le " + player.getType() +
                    " inflige " + damage + " de dégâts au " + this.getType());
        } else {
            menu.display("# -Votre attaque à raté!!!");
        }
        if (getLife() > 0) {
            player.sufferedAnAttack(this,menu,game);
        } else {
            menu.display(" \n#     #~#~#~#~#~# - VICTOIRE! - #~#~#~#~#~#" +
                    "\n# -Votre personnage a terrasser le " + this.getType() +
                    " " + this.getName() + "...");
        }
    }

    public int getDamages() {
        int min = getAttack()[0];
        int max = (getAttack()[1]- getAttack()[0]) + 1;
        return min + rand.nextInt(max);
    }

    public void buffEnemy(int level){
        int buff=level-1;
        attack[1]=attack[1]+buff;
        life=life+(buff*2);
        lifeMax=lifeMax+(buff*2);
    }
    ////////////////////////////////////////////////////////////////

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int[] getAttack() {
        return attack;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLifeMax() {
        return lifeMax;
    }

    public int getDefense() {
        return defense;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    ////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        return "\n######### - FICHE ENNEMI - #########" +
                "\n# Le " + type + " nommer " + name + ", niveau " + level +
                "\n# " + life + "/"+getLifeMax()+"PV, attaque " + attack[0] + "-" +attack[1] +
                "}\n####################################\n";
    }

}
