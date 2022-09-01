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

    public void launchGame(Game game) {
        System.out.println("launchGame");

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
        System.out.println("listenerChoice");
        String choice = scanner.nextLine();
        if (functionChoiceMap.containsKey(choice) && !choice.equals("666")) {
            functionChoiceMap.get(choice).run();
        } else {
            functionChoiceMap.get("666").run();
        }
    }

    public void selectNbrPlayers(Game game) {
        System.out.println("selectNbrPlayers");

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

    }

    public void selectCharacter(Game game, int nbrPlayers) {
        System.out.println("selectCharacter");
        for (int i = 0; i < nbrPlayers; i++) {
            selectTypeHero(game);
        }
    }

    public void selectTypeHero(Game game) {
        System.out.println("selectTypeHero");
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

    public void playerName(Game game, String type, String typeFr) {
        System.out.println("playerName");
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

    public void createHero(Game game, String type, String name) {
        Hero player = switch (type) {
            case "Guerrier" -> new Warrior(name, 1, new Weapon("Glaive", 1), new Shield("Petit bouclier", 1), null);
            default -> new Wizard(name, 1, new Spell("Foudre", 1), new EnergyShield("Petit Philtre", 1), null);
        };
        game.addPlayers(player);
        System.out.println(game.getPlayers());
    }

    public void exitGame(Game game) {
        System.out.println("exitGame");
        game.setStates(GameState.END);
    }

    public void displayChoice(String request, String[] choices) {
        System.out.println("displayChoice");
        String msg = "######## - " + request + " - ########";
        for (String choice : choices) {
            msg += "\n#      -Taper: " + choice;
        }
        display(msg);
    }

    public void display(String msg) {
        System.out.println("display");
        System.out.println(msg);
    }

}
