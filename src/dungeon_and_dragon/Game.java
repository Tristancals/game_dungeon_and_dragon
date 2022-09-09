package dungeon_and_dragon;

import dungeon_and_dragon.exceptions.PlayerOutOfBoardException;
import dungeon_and_dragon.gears.EnergyShield;
import dungeon_and_dragon.gears.Shield;
import dungeon_and_dragon.gears.Spell;
import dungeon_and_dragon.gears.Weapon;
import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.heros.Warrior;
import dungeon_and_dragon.heros.Wizard;
import dungeon_and_dragon.interfaces.SufferedAnAttack;
import dungeon_and_dragon.interfaces.Interactable;
import dungeon_and_dragon.rooms.Chest;

import java.util.*;


public class Game {

    private final java.util.Random rand = new java.util.Random();
    private List<Interactable> level = new ArrayList<>();
    private int nbrRoom;
    private final List<Hero> players = new ArrayList<>();
    private List<Hero> playersSave = new ArrayList<>();
    private int dungeonLevel;
    private GameState states = GameState.START;
    private Menu menu;
    private int round = 0;
    private int positionPlayerAtStart;
    private boolean gameInProgress = true;

    public Game() {

    }


    /**
     * Fonction qui boucle sur les ENUMS et permet l'avancement du jeu
     * START => création de l'équipe de hero
     * PREPARATION => mise en place du donjon
     * GAME => déroulement de la partie
     * END => fin de partie.. soit par la sortie d'un hero du donjon..
     * Soit par la mort de toute l'équipe
     */
    protected void start() {
        while (gameInProgress) {
            switch (this.states) {
                case START:
                    menu.launchGame();
                    break;
                case PREPARATION:
                    menu.selectDifficultyDungeon();
                    break;
                case GAME:
                    if(round==0) {
                        menu.display("""

                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                ~~~~~~~~~~ - DEBUT DE LA PARTIE - ~~~~~~~~~~
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                """);
                    }
                    playRoundTeam();
                    break;
                case END:
                    menu.display("""

                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                            ~~~~~~~~~~~ - FIN DE LA PARTIE - ~~~~~~~~~~~
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                            """);
                    gameInProgress = false;
                    break;
            }
        }
    }

    /**
     * Récupère le choix de chaque joueur et crée leur personage
     * et le donner à l'attribut List<players> à l'instance de game
     *
     * @param type class du hero choisie
     * @param name nom du hero
     */
    public void createNewHero(String type, String name) {
        Hero player = switch (type) {
            case "Warrior" -> new Warrior(0, name, 1, new Weapon("Glaive", 1),
                    new Shield("Petit bouclier", 1), null);
            default -> new Wizard(0, name, 1, new Spell("Foudre", 1),
                    new EnergyShield("Petit Philtre", 1), null);
        };
        addPlayers(player);
        menu.display("# Joueur crée:\n" + player);
    }


    /**
     * Fonction pour créer une instance d'un Dungeon
     * qui va créer une collection de Room avec comme valeur Chest/Empty/Enemy
     * et une fois réaliser il lance la partie
     *
     * @param nbrRoom
     * @param dungeonLevel
     */
    protected void initDungeon(int nbrRoom, int dungeonLevel) {
        this.nbrRoom = nbrRoom;
        this.dungeonLevel = dungeonLevel;
        Dungeon dungeon = new Dungeon(nbrRoom, dungeonLevel);
        level = dungeon.getLevel();

//        for (Interactable room : level) {
//            if (room.getClass() == Chest.class) {
//                System.out.println(room);
//                if (((Chest) room).getOffensive() != null) {
//                    System.out.println(((Chest) room).getOffensive().getType());
//                }
//                if (((Chest) room).getDefensive() != null) {
//                    System.out.println(((Chest) room).getDefensive().getType());
//                }
//                if (((Chest) room).getHeal() != null) {
//                    System.out.println(((Chest) room).getHeal().getType());
//                }
//            }
//        } TODO
        setStates(GameState.GAME);
    }

    ///////////////////////////////////////////////

    /**
     * Fonction qui va se répéter jusqu'à la fin de la partie
     * permet d'effectuer le tour de jeu des joueurs un à un
     * round ==> compter le nombre de tours de la partie
     * allPlayerDead() ==> savoir si il reste un joueur en vie dans l'équipe
     * playerNbr ==> savoir le numéro du joueur qui joue son tour
     * player.isALife() ==> savoir si le joueur sélectionner est vivant..
     * whatDoesThePlayerDoInHisTurn(player) ==> laisser le choix au joueur en début de tour
     * avancer ou de soigner (ou quitter le jeu)
     * positionPlayerAtStart ==> enregistrer la position du joueur au début de son tour
     * whatIsInTheCase(player) ==> fonction qui vas proposer une action en fonction de la Room
     * gameInProgress ==> boolean qui permet de mettre fin à la partie
     */
    private void playRoundTeam() {
        round++;
        if (!allPlayerDead()) {
            int playerNbr = 0;
            for (Hero player : players) {
                playerNbr++;
                if (players.size() > 1 && playerNbr > 1) {
                    menu.display("""

                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                            ~~~~~~~~~~ - JOUEUR SUIVANT - ~~~~~~~~~~
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                            
                            """);
                }
                if (player.isALife()) {
                    menu.display("######## - Tour " + round +
                            " de " + player.getName() +
                            " " + playerNbr + "/" + players.size() +
                            " - ########\n" + player);

                    positionPlayerAtStart = player.getPosition() ;
                    menu.whatDoesThePlayerDoInHisTurn(player);
                    whatIsInTheCase(player);
                    if (player.getPosition() == this.nbrRoom && player.isALife()) {
                        menu.display("""



                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    ~~~~~~~~~ - LE JOUEUR À TROUVER LA SORTIE - ~~~~~~~~~
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                            
                     - TOUS LES JOUEURS VONT SE TÉLÉPORTER EN LIEU SÛR - 
                     ~~~~~~~~~~~~~~~ - A LA FIN DU TOUR - ~~~~~~~~~~~~~~
                    """);
                        setStates(GameState.END);
                    }
                }
            }
        }
        if (gameInProgress && states == GameState.GAME) {
            if (round > 1) {
                menu.display("""



                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        ~~~~~~~~~~ - TOUR SUIVANT - ~~~~~~~~~~
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                                
                                                
                        """);
            }
        } else {
            if(!allPlayerDead()) {
                menu.display("""



                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        ~~~~~~~~~~~~~~~~~~~~ - GAGNER - ~~~~~~~~~~~~~~~~~~~~
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                                
                                                
                        """);
            }else {

                menu.display("""



                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        ~~~~~~~~~~~~~~~~~~ - GAME OVER - ~~~~~~~~~~~~~~~~~~
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                                
                                                
                        """);
            }
            setStates(GameState.END);
            menu.exitGameAndSaveNotTheGame();
            menu.display(players.toString());
        }
    }


    /**
     * Fonction qui permet la fuite d'un joueur lors d'un combat mal engager..
     * avec un peu de chance...
     *
     * @param player
     * @param enemy
     */
    protected void tryRunAway(SufferedAnAttack player, SufferedAnAttack enemy) {
        if (rand.nextInt(6) > (dungeonLevel - 1)) {
            menu.display("Vous parvenez à fuir en position: " + positionPlayerAtStart);
            player.setPosition(positionPlayerAtStart);
        } else {
            menu.display("Vous rater votre fuite....");
            player.sufferedAnAttack(enemy, menu, this);
        }
    }


    /**
     * le joueur récupère 1 PV
     *
     * @param player
     */
    protected void playerStays(Hero player) {
        player.setLife(player.getLife() + 1);
    }

    /**
     * envois vers le menu de selection d'une potion
     *
     * @param player
     */
    protected void playerStaysAndTakeHeal(Hero player) {
        player.selectHeal(menu);
    }

    private void playerWrongWay(Hero player, int positionPlayer, int dieRoll) throws PlayerOutOfBoardException {
        if (positionPlayer > this.nbrRoom) {
            positionPlayer -= (dieRoll * rand.nextInt(dieRoll / 2) + dieRoll);
            player.setPosition(positionPlayer);
            System.out.println(positionPlayer);
            throw new PlayerOutOfBoardException("Un vortex la téléporter en position: " + positionPlayer);
        }
    }

    protected void playerMouve(Hero player) {
        int dieRoll = this.dieRoll();
        int positionPlayer = player.getPosition();
        positionPlayer += dieRoll;
        menu.display("\n# Il lance le dé ..." +
                "\n# Il obtient: " + dieRoll +
                "\n# " + (positionPlayer <= this.nbrRoom ?
                "Se qui l'amène dans la pieces: " + positionPlayer :
                ""));
        try {
            playerWrongWay(player, positionPlayer, dieRoll);
            player.setPosition(positionPlayer);
        } catch (PlayerOutOfBoardException e) {
            menu.display(e.getMessage());
        }
    }

    /**
     * Fonction qui permet de vérifier si toute l'équipe est mort..
     * Si c'est le cas passe à false gameInProgress
     *
     * @return boolean true si toute l'équipe est morte
     */
    private boolean allPlayerDead() {
        int count = 0;
        for (Hero player : players) {
            if (!player.isALife()) {
                count++;
            }
        }
        if (count == players.size()) {
            gameInProgress = false;
        }
        return count == players.size();
    }

    /**
     * fonction qui simule un dé à 6 faces
     *
     * @return un int de 1 à 6
     */
    private int dieRoll() {
        return 1 + rand.nextInt(6);
    }

    /**
     * Fonction qui selon le contenu de la pieces appel une fonction pour interagir avec
     * et changer l'état de celle-ci selon l'interaction
     *
     * @param player transmet le joueur qui joue son tour à la fonction interact()
     */
    private void whatIsInTheCase(Hero player) {
        Interactable room = level.get(player.getPosition());
        menu.display("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                room.toString() +
                "\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        room.interact(player, menu, this);
    }

    /**
     * lors du lancement de la partie cette fonction ajoute un joueur
     * à l'attribut players
     *
     * @param player
     */
    protected void addPlayers(Hero player) {
        this.players.add(player);
    }

    /**
     * permet selon l'avancement de la partie de switch les ENUMS
     *
     * @param states
     */
    protected void setStates(GameState states) {
        this.states = states;
    }

    public void setLevel(Interactable room, int index) {
        this.level.set(index, room);
    }

    public List<Interactable> getLevel() {
        return level;
    }

    public void setPlayersSave(List<Hero> playersSave) {
        this.playersSave = playersSave;
    }

    public List<Hero> getPlayersSave(ControllerBDD controllerBDD) {

       controllerBDD.selectAllPlayers(this);

        return playersSave;
    }

    public List<Hero> getPlayers() {
        return players;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getDungeonLevel() {
        return dungeonLevel;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }
}
