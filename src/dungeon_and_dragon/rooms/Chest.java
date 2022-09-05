package dungeon_and_dragon.rooms;

import dungeon_and_dragon.Game;
import dungeon_and_dragon.Menu;
import dungeon_and_dragon.gears.*;
import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.interfaces.Interactable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chest implements Interactable {
    private Heal heal = null;
    private Offensive offensive = null;
    private Defensive defensive = null;

    private final java.util.Random rand = new java.util.Random();

    public Chest() {
        initLoot();
    }

    @Override
    public void interact(Hero player, Menu menu, Game game) {
        openChest( player,  game, menu);
    }


    /**
     * selon le contenu du coffre et du joueur
     * le joueur s'équipe avec l'équipement present
     * ou ajoute à son inventaire le soin
     *
     * @param player
     * @param game
     */
    private void openChest(Hero player, Game game,Menu menu) {
       List<Interactable> level=game.getLevel();
        if (this.getDefensive() != null) {
            if (Objects.equals(player.getType(), this.getDefensive().getType())) {
                if (player.getDefensive().getStats() < this.getDefensive().getStats()) {
                    player.setDefensive(this.getDefensive());
                    level.set(player.getPosition(), new Empty());////
                } else {
                    menu.display("# J'ai déjà mieux");
                }
            } else {
                menu.display("# Ce n'est pas pour moi");
            }
        } else if (this.getOffensive() != null) {
            if (Objects.equals(player.getType(), this.getOffensive().getType())) {
                if (player.getOffensive().getStats() < this.getOffensive().getStats()) {
                    player.setOffensive(this.getOffensive());
                    level.set(player.getPosition(), new Empty());////
                } else {
                    menu.display("# J'ai déjà mieux");
                }
            } else {
                menu.display("# Ce n'est pas pour moi");
            }
        } else {
            game.setLevel(player.addHealToInventory(level.get(player.getPosition()), menu),player.getPosition());////
        }
    }




    public void initLoot() {
        switch (rand.nextInt(4)) {
            case 0 -> initLootDefensive();
            case 1 -> initLootOffensive();
            default -> initLootPotion();
        }
    }

    private void initLootDefensive() {
        defensive = switch (rand.nextInt(6)) {
            case 0, 1 -> new Shield("Bouclier", 2);
            case 2 -> new Shield("Grand bouclier", 3);
            case 3, 4 -> new EnergyShield("Philter", 2);
            default -> new EnergyShield("Grand Philter", 3);
        };
    }

    private void initLootOffensive() {
        offensive = switch (rand.nextInt(6)) {
            case 0, 1 -> new Weapon("Massue", 3);
            case 2 -> new Weapon("Épée", 5);
            case 3, 4 -> new Spell("Éclair", 2);
            default -> new Spell("Boule de feu", 7);
        };
    }

    private void initLootPotion() {
        heal = switch (rand.nextInt(3)) {
            case 0, 1 -> new Heal("Petite Potion", 3);
            default -> new Heal("Potion", 5);
        };
    }


    public Heal getHeal() {
        return heal;
    }

    public Offensive getOffensive() {
        return offensive;
    }

    public Defensive getDefensive() {
        return defensive;
    }


    public String toString() {
        return "\n# Il y a un coffre...." +
                (heal != null ? heal.toString() : "") +
                (offensive != null ? offensive.toString() : "") +
                (defensive != null ? defensive.toString() : "");
    }

}
