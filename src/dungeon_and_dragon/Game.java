package dungeon_and_dragon;

import dungeon_and_dragon.exceptions.PlayerOutOfBoardException;
import dungeon_and_dragon.gears.Gear;
import dungeon_and_dragon.gears.Heal;
import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.interfaces.SufferedAnAttack;
import dungeon_and_dragon.interfaces.Interactable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class Game {

    private final java.util.Random rand = new java.util.Random();
    private List<Interactable> level = new ArrayList<>();
    private int nbrRoom;
    private final List<Hero> players = new ArrayList<>();
    private int dungeonLevel;
    private GameState states = GameState.START;
    private final Menu menu;
    private int round = 0;
    private int positionPlayerAtStart;
    private boolean gameInProgress = true;

    public Game(Menu menu) {
        this.menu = menu;
        start();
    }


    /**
     * Fonction qui boucle sur les ENUMS et permet l'avancement du jeu
     * START => création de l'équipe de hero
     * PREPARATION => mise en place du donjon
     * GAME => déroulement de la partie
     * END => fin de partie.. soit par la sortie d'un hero du donjon..
     * Soit par la mort de toute l'équipe
     */
    private void start() {
        while (gameInProgress) {
            System.out.println("switch");
            switch (this.states) {
                case START:
                    menu.launchGame(this);
                    break;
                case PREPARATION:
                    selectDifficultyDungeon();
                    break;
                case GAME:
                    menu.display("""

                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                            ~~~~~~~~~~ - DEBUT DE LA PARTIE - ~~~~~~~~~~
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                            """);
                    playRoundTeam();
                    break;
                case END:
                    System.out.println("---END---");
                    savePlayers();
                    ///
                    break;
            }
        }
    }

    public void savePlayers() {
        Controller controller = new Controller();

        Connection connection = controller.getConnection();
        try {

//            Class.forName("com.mysql.cj.jdbc.Driver");                          // lien avec la dependence .jar

            Statement statement = connection.createStatement();


            for (Hero player : players) {
                String heroName = player.getName();
                String heroType = player.getType();
                int heroLevel = player.getLevel();


                Gear gearOf = player.getOffensive();
                String gearOfName = gearOf.getName();
                String gearOfType = gearOf.getType();
                int gearOfStats = gearOf.getStats();
                System.out.println(gearOfStats+" "+gearOfName+" "+gearOfType);
                String sql = "INSERT INTO gear(gear_name,gear_type,gear_stats) VALUES ('" +
                        gearOfName + "','" + gearOfType + "'," + gearOfStats + ");";
                statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);

                ResultSet generatedKeys = statement.getGeneratedKeys();
                int id_gearOf=0;
                if (generatedKeys.next()) {
                     id_gearOf = generatedKeys.getInt(1);
                    System.out.println(sql +" "+id_gearOf);
                }

                Gear gearDef = player.getDefensive();
                String gearDefName = gearDef.getName();
                String gearDefType = gearDef.getType();
                int gearDefStats = gearDef.getStats();
                sql = "INSERT INTO gear(gear_name,gear_type,gear_stats) VALUES ('" +
                        gearDefName + "','" + gearDefType + "'," + gearDefStats + ");";
                statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
                generatedKeys = statement.getGeneratedKeys();
                int id_gearDef=0;
                if (generatedKeys.next()) {
                     id_gearDef = generatedKeys.getInt(1);
                    System.out.println(sql + " " + id_gearDef);
                }

                sql = "INSERT INTO hero(hero_name,hero_type,hero_level,id_gear_def,id_gear_of) VALUES ('" +
                        heroName + "','" + heroType + "'," + heroLevel + "," + id_gearDef + "," + id_gearOf + ");";
                statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
                generatedKeys = statement.getGeneratedKeys();
                int id_hero=0;
                if (generatedKeys.next()) {
                     id_hero = generatedKeys.getInt(1);
                    System.out.println(sql + " " + id_hero);
                }

                ArrayList<Heal> inventory = player.getInventory();
                for (Heal potion : inventory) {
                    int gearStats = potion.getStats();
                    String gearName = potion.getName();
                    String gearType = potion.getType();
                    sql = "INSERT INTO gear(gear_name,gear_type,gear_stats) VALUES ('" +
                            gearName + "','" + gearType + "'," + gearStats + ");";
                    statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
                    generatedKeys = statement.getGeneratedKeys();
                    int id_potion=0;
                    if (generatedKeys.next()) {
                         id_potion = generatedKeys.getInt(1);
                        System.out.println(sql + " " + id_potion);
                    }

                    sql = "INSERT INTO inventory(id_hero,id_gear_heal) VALUES (" +
                            id_hero + "," + id_potion + ");";
                    statement.executeUpdate(sql);
                    System.out.println(sql);

                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

//        controller.closeConnection();
    }

    /**
     * Permet de choisir la difficulté
     * change le nombre de pieces à visiter dans le donjon
     * et le niveau des monstres présent dans celui-ci
     */
    private void selectDifficultyDungeon() {
        menu.displayChoice("Sélectionner la difficulté",
                new String[]{"'1' pour une petite balade..(facile)",
                        "'2' pour une petite aventure..(moyenne)",
                        "'3' pour un épopée..(difficile)",
                        "'4' pour un suicide..(mortelle)",
                        "'0' pour quitter le jeu"});
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> initDungeon(40, 1));
        functionChoiceMap.put("2", () -> initDungeon(45, 2));
        functionChoiceMap.put("3", () -> initDungeon(50, 3));
        functionChoiceMap.put("4", () -> initDungeon(55, 4));
        functionChoiceMap.put("0", () -> menu.exitGame(this));
        functionChoiceMap.put("666", this::selectDifficultyDungeon);
        menu.listenerChoice(functionChoiceMap);
    }

    /**
     * Fonction pour créer une instance d'un Dungeon
     * qui va créer une collection de Room avec comme valeur Chest/Empty/Enemy
     * et une fois réaliser il lance la partie
     *
     * @param nbrRoom
     * @param dungeonLevel
     */
    private void initDungeon(int nbrRoom, int dungeonLevel) {
        this.nbrRoom = nbrRoom;
        this.dungeonLevel = dungeonLevel;
        Dungeon dungeon = new Dungeon(nbrRoom, dungeonLevel);
        level = dungeon.getLevel();
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
                    whatDoesThePlayerDoInHisTurn(player);
                    positionPlayerAtStart = player.getPosition();
                    whatIsInTheCase(player);
                }
            }
        } else {
            gameInProgress = false;
        }
        if (gameInProgress) {
            if (round > 1) {
                menu.display("""



                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        ~~~~~~~~~~ - TOUR SUIVANT - ~~~~~~~~~~
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                                
                                                
                        """);
            }
            playRoundTeam();
        } else {
            menu.display("""



                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    ~~~~~~~~~~~~~~~~~~~~ - GAGNER - ~~~~~~~~~~~~~~~~~~~~
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                            
                                            
                    """);
            setStates(GameState.END);
            savePlayers();
            menu.display(players.toString());
        }
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
    public void whatDoesThePlayerDoDuringTheFight(SufferedAnAttack player, SufferedAnAttack enemy, Menu menu) {
        menu.displayChoice("Combat",
                new String[]{"'1' pour lancer une attaque",
                        "'2' pour tenter de fuir ...(" + (6 - dungeonLevel) + "/6 de réussit)",
                        "'0' pour quitter le jeu.."});
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> enemy.sufferedAnAttack(player, menu, this));
        functionChoiceMap.put("2", () -> tryRunAway(player, enemy));
        functionChoiceMap.put("0", () -> menu.exitGame(this));
        functionChoiceMap.put("666", () -> whatDoesThePlayerDoDuringTheFight(player, enemy, menu));
        menu.listenerChoice(functionChoiceMap);
    }

    /**
     * Fonction qui permet la fuite d'un joueur lors d'un combat mal engager..
     * avec un peu de chance...
     *
     * @param player
     * @param enemy
     */
    private void tryRunAway(SufferedAnAttack player, SufferedAnAttack enemy) {
        if (rand.nextInt(6) > (dungeonLevel - 1)) {
            menu.display("Vous parvenez à fuir en position: " + positionPlayerAtStart);
            player.setPosition(positionPlayerAtStart);
        } else {
            menu.display("Vous rater votre fuite....");
            player.sufferedAnAttack(enemy, menu, this);
        }
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
    private void whatDoesThePlayerDoInHisTurn(Hero player) {
        List<String> choices = new ArrayList<>();
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> playerMouve(player));
        choices.add("'1' pour lancer le dé");

        if (player.getLife() < player.getLifeMax()) {
            choices.add("'2' pour passer votre tour et regagner 1PV");
            functionChoiceMap.put("2", () -> playerStays(player));
            if (!player.displayInventory().equals("Inventaire: ")) {
                choices.add("'3' pour passer votre tour et consommer une potion");
                functionChoiceMap.put("3", () -> playerStaysAndTakeHeal(player));
            }
        }
        choices.add("'0' pour quitter le jeu");
        functionChoiceMap.put("666", () -> whatDoesThePlayerDoInHisTurn(player));
        functionChoiceMap.put("0", () -> menu.exitGame(this));
        String[] choicesArray = new String[choices.size()];
        menu.displayChoice("Debut du tour de " + player.getName(), choices.toArray(choicesArray));
        menu.listenerChoice(functionChoiceMap);
    }

    /**
     * le joueur récupère 1 PV
     *
     * @param player
     */
    private void playerStays(Hero player) {
        player.setLife(player.getLife() + 1);
    }

    /**
     * envois vers le menu de selection d'une potion
     *
     * @param player
     */
    private void playerStaysAndTakeHeal(Hero player) {
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

    private void playerMouve(Hero player) {
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

        if (positionPlayer == this.nbrRoom && player.isALife()) {
            // TODO partie fini à la fin du tour proposer une nouvelle partie
            setStates(GameState.END);
            gameInProgress = false;
        }
    }


    /**
     * Fonction qui permet de vérifier si toute l'équipe est mort..
     *
     * @return
     */
    private boolean allPlayerDead() {
        int count = 0;
        for (Hero player :
                players) {
            if (!player.isALife()) {
                count++;
            }
        }
        return count == players.size();
    }


    /**
     * fonction qui simule un dé à 6 faces
     *
     * @return
     */
    private int dieRoll() {
        return 1 + rand.nextInt(6);
//        return 40;
    }

    /**
     * Fonction qui selon le contenu de la pieces appel une fonction pour interagir avec
     * et changer l'état de celle-ci selon l'interaction
     *
     * @param player
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
}
