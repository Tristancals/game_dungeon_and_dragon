package dungeon_and_dragon;

import dungeon_and_dragon.gears.EnergyShield;
import dungeon_and_dragon.gears.Shield;
import dungeon_and_dragon.gears.Spell;
import dungeon_and_dragon.gears.Weapon;
import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.heros.Warrior;
import dungeon_and_dragon.heros.Wizard;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);

    public Menu() {
    }

    protected void launchGame(Game game) {

        displayChoice("Bienvenus dans DONJON & DRAGON",
                new String[]{"'1' pour lancer une partie",
                        "'0' pour quitter le jeu"});

        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> selectNbrPlayers(game));
        functionChoiceMap.put("0", () -> exitGame(game));
        functionChoiceMap.put("666", () -> launchGame(game));
        listenerChoice(functionChoiceMap);
    }

    public void listenerChoice(Map<String, Runnable> functionChoiceMap) {
        String choice = scanner.nextLine();
        if (functionChoiceMap.containsKey(choice) && !choice.equals("666")) {
            functionChoiceMap.get(choice).run();
        } else {
            functionChoiceMap.get("666").run();
        }
    }

    private void selectNbrPlayers(Game game) {

        displayChoice("Selection du nombre de joueur(s)",
                new String[]{"'1' à '4' joueur(s)",
                        "'0' pour quitter le jeu"});

        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> selectCharacter(game, 1));
        functionChoiceMap.put("2", () -> selectCharacter(game, 2));
        functionChoiceMap.put("3", () -> selectCharacter(game, 3));
        functionChoiceMap.put("4", () -> selectCharacter(game, 4));
        functionChoiceMap.put("0", () -> exitGame(game));
        functionChoiceMap.put("666", () -> selectNbrPlayers(game));
        listenerChoice(functionChoiceMap);
        game.setStates(GameState.PREPARATION);
    }

    private void selectCharacter(Game game, int nbrPlayers) {
        for (int i = 0; i < nbrPlayers; i++) {
            selectTypeHero(game);
        }
    }

    private void selectTypeHero(Game game) {
        displayChoice("Selection de la classe de votre personnage",
                new String[]{"'1' pour un Magicien",
                        "'2' pour un Guerrier",
                        "'0' pour quitter le jeu"});

        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("2", () -> playerName(game, "Warrior", "Guerrier"));
        functionChoiceMap.put("1", () -> playerName(game, "Wizard", "Magicien"));
        functionChoiceMap.put("0", () -> exitGame(game));
        functionChoiceMap.put("666", () -> selectTypeHero(game));
        listenerChoice(functionChoiceMap);
    }

    private void playerName(Game game, String type, String typeFr) {
        displayChoice("Donner un nom à votre " + typeFr,
                new String[]{"'0' pour quitter le jeu"});
        String name = scanner.nextLine();
        if (!(name.trim().length() > 0)) {
            playerName(game, type, typeFr);
        } else if (name.equals("0")) {
            exitGame(game);
        } else {
            createHero(game, type, name);
        }
    }

    private void createHero(Game game, String type, String name) {
        System.out.println(type);
        Hero player = switch (type) {
            case "Warrior" -> new Warrior(name, 1, new Weapon("Glaive", 1), new Shield("Petit bouclier", 1), null);
            default -> new Wizard(name, 1, new Spell("Foudre", 1), new EnergyShield("Petit Philtre", 1), null);
        };
        game.addPlayers(player);
        display("# Joueur crée:\n"+player.toString());
        }

    public void exitGame(Game game) {
        game.setStates(GameState.END);
    }

    public void displayChoice(String request, String[] choices) {
        String msg = "######## - " + request + " - ########";
        for (String choice : choices) {
            msg += "\n#      -Taper: " + choice;
        }
        display(msg);
    }

    public void display(String msg) {
        System.out.println(msg);
    }

}
