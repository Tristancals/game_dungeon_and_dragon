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

    /**
     * point d'entrer du jeu
     * envois vers la selection du nombre de joueurs
     * @param game permet la transmission de l'instance de game pour
     *             enregistrer le paramètre de la partie
     */
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


    /**
     * une fois la partie lancer avec launchGame cette fonction
     * permet de sélectionner le nombre de joueurs pour la partie
     * et renvoi vers la fonction selectCharacter(game, nbrPlayer)
     * @param game
     */
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

    /**
     * fonction qui va boucler en fonction du nombre de joueurs
     * sur la fonction selectTypeHero()
     * @param game
     * @param nbrPlayers
     */
    private void selectCharacter(Game game, int nbrPlayers) {
        for (int i = 0; i < nbrPlayers; i++) {
            selectTypeHero(game);
        }
    }

    /**
     * permet de choisir la classe de son personnage et envois vers la
     * fonction pour choisir le nom de celui-ci playerName()
     * @param game
     */
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

    /**
     * fonction pour capter la saisie du joueur pour donner un nom à son personnage
     * une fois celle-ci effectuer on crée le personnage avec createHero
     * @param game
     * @param type
     * @param typeFr
     */
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

    /**
     * récupère le choix de chaque joueur et crée leur personage
     * et le donner à l'instance de game
     * @param game
     * @param type
     * @param name
     */
    private void createHero(Game game, String type, String name) {
        Hero player = switch (type) {
            case "Warrior" -> new Warrior(name, 1, new Weapon("Glaive", 1), new Shield("Petit bouclier", 1), null);
            default -> new Wizard(name, 1, new Spell("Foudre", 1), new EnergyShield("Petit Philtre", 1), null);
        };
        game.addPlayers(player);
        display("# Joueur crée:\n" + player.toString());
    }

    /**
     * termine la partie en cours
     * @param game
     */
    public void exitGame(Game game) {
        game.setStates(GameState.END);
    }


    /**
     * affiche un string pour le déroulement du jeu
     *
     * @param msg
     */
    public void display(String msg) {
        System.out.println(msg);
    }

    /**
     * afficher à l'utilisateur une question avec une liste de choix possible
     *
     * @param request la question
     * @param choices la liste de choix possible
     */
    public void displayChoice(String request, String[] choices) {
        String msg = "######## - " + request + " - ########";
        for (String choice : choices) {
            msg += "\n#      -Taper: " + choice;
        }
        display(msg);
    }

    /**
     * fonction permet d'écouter les choix de l'utilisateur
     *
     * @param functionChoiceMap récupère une map avec comme key les choix disponible
     *                          et en value des fonctions à exécuter
     */
    public void listenerChoice(Map<String, Runnable> functionChoiceMap) {
        String choice = scanner.nextLine();
        if (functionChoiceMap.containsKey(choice) && !choice.equals("666")) {
            functionChoiceMap.get(choice).run();
        } else {
            functionChoiceMap.get("666").run();
        }
    }
}
