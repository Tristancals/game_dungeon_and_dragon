package dungeon_and_dragon;

import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.interfaces.SufferedAnAttack;
import dungeon_and_dragon.rooms.enemies.Enemy;

import java.util.*;

/**
 * Class interface permet au joueur(s) de faire leur choix
 * debut / courant / fin de partie
 */
public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final ControllerBDD controllerBDD = ControllerBDD.getInstance();
    private Game game;

    public Menu(Game game) {
        this.game = game;
        game.setMenu(this);
        game.start();
    }


    /**
     * point d'entrer du jeu
     * envois vers la selection du nombre de joueurs
     */
    protected void launchGame() {
        displayChoice("Bienvenus dans DONJON & DRAGON",
                new String[]{"'entrée' pour lancer une partie",
                        "'0' pour quitter le jeu"});

        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("", this::selectNbrPlayers);
        functionChoiceMap.put("0", this::exitGame);
        functionChoiceMap.put("666", this::launchGame);
        listenerChoice(functionChoiceMap);
    }


    /**
     * une fois la partie lancer avec launchGame cette fonction
     * permet de sélectionner le nombre de joueurs pour la partie
     * et renvoi vers la fonction selectCharacter(game, nbrPlayer)
     */
    private void selectNbrPlayers() {
        displayChoice("Selection du nombre de joueur(s)",
                new String[]{"'1' à '4' joueur(s)",
                        "'0' pour quitter le jeu"});

        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> selectCharacter(1));
        functionChoiceMap.put("2", () -> selectCharacter(2));
        functionChoiceMap.put("3", () -> selectCharacter(3));
        functionChoiceMap.put("4", () -> selectCharacter(4));
        functionChoiceMap.put("0", this::exitGame);
        functionChoiceMap.put("666", this::selectNbrPlayers);
        listenerChoice(functionChoiceMap);
        game.setStates(GameState.PREPARATION);
    }

    /**
     * fonction qui va boucler en fonction du nombre de joueurs
     * sur la fonction selectTypeHero()
     *
     * @param nbrPlayers
     */
    private void selectCharacter(int nbrPlayers) {
        for (int i = 0; i < nbrPlayers; i++) {
            selectTypeHero();
        }
    }

    /**
     * permet de choisir la classe de son personnage et envois vers la
     * fonction pour choisir le nom de celui-ci playerName()
     */
    private void selectTypeHero() {
        //TODO si perso en bdd charger perso//
        displayChoice("Selection de la classe de votre personnage",
                new String[]{"'1' pour un Magicien",
                        "'2' pour un Guerrier",
                        "'3' charger un personnage",
                        "'0' pour quitter le jeu"});
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("2", () -> playerName("Warrior", "Guerrier"));
        functionChoiceMap.put("1", () -> playerName("Wizard", "Magicien"));
        functionChoiceMap.put("3", this::selectPlayerSave);
        functionChoiceMap.put("0", this::exitGame);
        functionChoiceMap.put("666", this::selectTypeHero);
        listenerChoice(functionChoiceMap);
    }

    private void selectPlayerSave() {
        List<Hero> playersSave = game.getPlayersSave(controllerBDD);
        List<String> choices = new ArrayList<>();
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        for (int i = 0; i < playersSave.size(); i++) {
            int index = i;
            choices.add("'" + (i + 1) + "' pour charger" + playersSave.get(i).getName());
            functionChoiceMap.put(String.valueOf((i + 1)), () -> game.addPlayers(playersSave.get(index)));
        }
        functionChoiceMap.put("666", this::selectPlayerSave);
        choices.add("'0' pour quitter le jeu");
        functionChoiceMap.put("0", this::exitGame);
        String[] choicesArray = new String[choices.size()];
        displayChoice("Choisir un hero", choices.toArray(choicesArray));
        listenerChoice(functionChoiceMap);
        display("# Joueur crée:\n");
    }

    /**
     * fonction pour capter la saisie du joueur pour donner un nom à son personnage
     * une fois celle-ci effectuer on crée le personnage avec createHero
     *
     * @param type   choix de la class du hero
     * @param typeFr choix de la class du hero en français
     */
    private void playerName(String type, String typeFr) {
        displayChoice("Donner un nom à votre " + typeFr,
                new String[]{"'0' pour quitter le jeu"});
        String name = scanner.nextLine();
        if (!(name.trim().length() > 0)) {
            playerName(type, typeFr);
        } else if (name.equals("0")) {
            exitGame();
        } else {
            game.createNewHero(type, name);
        }
    }

    /**
     * Permet de choisir la difficulté
     * change le nombre de pieces à visiter dans le donjon
     * et le niveau des monstres présent dans celui-ci
     */
    public void selectDifficultyDungeon() {
        displayChoice("Sélectionner la difficulté",
                new String[]{"'entrée' pour une petite balade..(facile)",
                        "'2' pour une petite aventure..(moyenne)",
                        "'3' pour un épopée..(difficile)",
                        "'4' pour un suicide..(mortelle)",
                        "'0' pour quitter le jeu"});
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("", () -> game.initDungeon(40, 1));
        functionChoiceMap.put("2", () -> game.initDungeon(45, 2));
        functionChoiceMap.put("3", () -> game.initDungeon(50, 3));
        functionChoiceMap.put("4", () -> game.initDungeon(55, 4));
        functionChoiceMap.put("0", this::exitGame);
        functionChoiceMap.put("666", this::selectDifficultyDungeon);
        listenerChoice(functionChoiceMap);
    }

    /**
     * au début du tour de chaque joueur nous lui proposons
     * d'avancer ==> playerMouve(player)
     * de se soigner ==> playerStays(player) / playerStaysAndTakeHeal(player)
     * (sauter un tour 1pv / ou si present consommer une potion)
     * ou quitter le jeu
     *
     * @param player
     */
    protected void whatDoesThePlayerDoInHisTurn(Hero player) {
        List<String> choices = new ArrayList<>();
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("", () -> game.playerMouve(player));
        choices.add("'entrée' pour lancer le dé");

        if (player.getLife() < player.getLifeMax()) {
            choices.add("'2' pour passer votre tour et regagner 1PV");
            functionChoiceMap.put("2", () -> game.playerStays(player));
            if (!player.displayInventory().equals("Inventaire: ")) {
                choices.add("'3' pour passer votre tour et consommer une potion");
                functionChoiceMap.put("3", () -> game.playerStaysAndTakeHeal(player));
            }
        }
        choices.add("'0' pour quitter le jeu");
        choices.add("'s' pour quitter le jeu et enregistrer partie");
        functionChoiceMap.put("666", () -> this.whatDoesThePlayerDoInHisTurn(player));
        functionChoiceMap.put("s", this::exitGameAndSaveTheGame);
        functionChoiceMap.put("q", this::exitGameAndSaveNotTheGame);
        choices.add("'q' quitter et enregistrer seulement les héros");
        functionChoiceMap.put("0", this::exitGame);
        String[] choicesArray = new String[choices.size()];
        displayChoice("Debut du tour de " + player.getName(), choices.toArray(choicesArray));
        listenerChoice(functionChoiceMap);
    }


    /**
     * fonction appeler apres avoir subi une attaque
     * permet de continuer le combat en attaquant ==> enemy.sufferedAnAttack(player, menu, this)
     * de fuir => tryRunAway(SufferedAnAttack player, SufferedAnAttack enemy)
     * ou quitter le jeu ==> menu.exitGame(this)
     *
     * @param player
     * @param enemy
     * @param menu
     */
    public void whatDoesThePlayerDoDuringTheFight(Hero player, Enemy enemy, Menu menu) {
        menu.displayChoice("Combat",
                new String[]{"'1' pour lancer une attaque",
                        "'2' pour tenter de fuir ...(" + (6 - game.getDungeonLevel()) + "/6 de réussit)",
                        "'0' pour quitter le jeu..",
                        "'q' pour quitter le jeu et save sans les position..",
                        "'s' pour quitter le jeu & enregistrer partie"});
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> enemy.sufferedAnAttack(player, menu, game));
        functionChoiceMap.put("2", () -> game.tryRunAway(player, enemy));
        functionChoiceMap.put("0", this::exitGame);
        functionChoiceMap.put("s", this::exitGameAndSaveTheGame);
        functionChoiceMap.put("q", this::exitGameAndSaveNotTheGame);
        functionChoiceMap.put("666", () -> whatDoesThePlayerDoDuringTheFight(player, enemy, menu));
        menu.listenerChoice(functionChoiceMap);
    }


    /**
     * termine la partie en cours sans enregistrer les personnages
     */
    public void exitGame() {
        game.setStates(GameState.END);
    }

    /**
     * termine la partie et enregistre les personnages & la partie en cours
     *
     */
    public void exitGameAndSaveTheGame() {
        controllerBDD.insertPlayers(game.getPlayers(), true);
        game.setStates(GameState.END);
    }

    /**
     * termine la partie et enregistre juste les personnages
     *
     */
    public void exitGameAndSaveNotTheGame() {
        controllerBDD.insertPlayers(game.getPlayers(), false);
        game.setStates(GameState.END);
    }

    /**
     * affiche un string pour le déroulement du jeu
     *
     * @param msg String à afficher
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
