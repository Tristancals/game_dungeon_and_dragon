package dungeon_and_dragon.rooms.enemies;

import dungeon_and_dragon.Game;
import dungeon_and_dragon.Menu;
import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.interfaces.SufferedAnAttack;
import dungeon_and_dragon.interfaces.Interactable;

public abstract class Enemy implements SufferedAnAttack, Interactable {
    private final String type;
    private final String name;
    private final int level;
    private final int[] attack;
    private int life;
    private int lifeMax;
    private final int defense;
    private final java.util.Random rand = new java.util.Random();

    private int position;
    ////////////////////////////////////////////////////////////////

    public Enemy(String type, String name, int level, int[] attack, int life, int defense) {
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


    @Override
    public void interact(Hero player, Menu menu, Game game) {
        if (getLife() > 0) {
            sufferedAnAttack(player, menu, game);
        }
    }

    public void sufferedAnAttack(SufferedAnAttack player, Menu menu, Game game) {
        int damage = player.getDamages();
        if (rand.nextInt(6 - player.getDefense()) > 0) {
            setLife(getLife() - damage);
            menu.display("# -" + player.getName() + " le " + player.getType() +
                    " inflige " + damage + " de dégâts au " + this.getType());
        } else {
            menu.display("# -Votre attaque à raté!!!");
        }
        if (getLife() > 0) {
            player.sufferedAnAttack(this, menu, game);
        } else {
            menu.display(" \n#     #~#~#~#~#~# - VICTOIRE! - #~#~#~#~#~#" +
                    "\n# -Votre personnage a terrasser le " + this.getType() +
                    " " + this.getName() + "...");

            game.setLevel(new Corpse(this.getType(), this.getName()), player.getPosition());

        }
    }

    public int getDamages() {
        int min = getAttack()[0];
        int max = (getAttack()[1] - getAttack()[0]) + 1;
        return min + rand.nextInt(max);
    }

    public void buffEnemy(int level) {
        int buff = level - 1;
        attack[1] = attack[1] + buff;
        life = life + (buff * 2);
        lifeMax = lifeMax + (buff * 2);
    }
    ////////////////////////////////////////////////////////////////

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int[] getAttack() {
        return attack;
    }

    public int getLife() {
        return life;
    }

    private void setLife(int life) {
        this.life = life;
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
                "\n# " + life + "/" + lifeMax + "PV, attaque " + attack[0] + "-" + attack[1] +
                "}\n####################################";
    }

}
