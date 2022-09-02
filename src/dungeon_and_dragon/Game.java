package dungeon_and_dragon;

import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.interfaces.SufferedAnAttack;
import dungeon_and_dragon.rooms.Chest;
import dungeon_and_dragon.rooms.Empty;
import dungeon_and_dragon.rooms.Room;
import dungeon_and_dragon.rooms.enemies.Enemy;

import java.util.*;


public class Game {

    private final java.util.Random rand = new java.util.Random();
    private Dungeon dungeon;
    private List<Room> level = new ArrayList<>();
    private int nbrRoom;
    private List<Hero> players = new ArrayList<>();
    private int dungeonLevel;
    private GameState states = GameState.START;//TODO PREPARATION START
    private final Menu menu;
    private int round = 0;
    private int positionPlayerAtStart;
    private boolean gameInProgress = true;

    public Game(Menu menu) {
        this.menu = menu;
        start();
    }


    public void start() {
        while (gameInProgress) {
            switch (this.states) {
                case START:
                    System.out.println("---START---");
                    menu.launchGame(this);
                    break;
                case PREPARATION:
                    System.out.println("---PREPARATION---");
                    selectDifficultyDungeon();
                    break;
                case GAME:
                    System.out.println("---GAME---");
                    playRound();
                    //
                    break;
                case CONCLUSION:
                    System.out.println("---CONCLUSION---");
                    ////
                    break;
                case END:
                    System.out.println("---END---");
                    gameInProgress = false;
                    ///
                    break;
            }
        }
    }

    public void selectDifficultyDungeon() {

        menu.displayChoice("Sélectionner la difficulté",
                new String[]{"'1' pour une petite balade..(facile)",
                        "'2' pour une petite aventure..(moyenne)",
                        "'3' pour un épopée..(difficile)",
                        "'4' pour un suicide..(mortelle)",
                        "'0' pour quitter le jeu"});
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> initDungeon(400, 1));
        functionChoiceMap.put("2", () -> initDungeon(45, 2));
        functionChoiceMap.put("3", () -> initDungeon(50, 3));
        functionChoiceMap.put("4", () -> initDungeon(55, 4));
        functionChoiceMap.put("0", () -> menu.exitGame(this));
        functionChoiceMap.put("666", this::selectDifficultyDungeon);
        menu.listenerChoice(functionChoiceMap);
    }

    public void initDungeon(int nbrRoom, int dungeonLevel) {
        this.nbrRoom = nbrRoom;
        this.dungeonLevel=dungeonLevel;
        dungeon = new Dungeon(nbrRoom, dungeonLevel);
        level = dungeon.getLevel();
        System.out.println(level);
        setStates(GameState.GAME);
    }

    ///////////////////////////////////////////////


    public void playRound() {
        round++;
        if (!allPlayerDead()) {
            int playerNbr = 0;
            for (Hero player : players) {
                playerNbr++;
                if (player.isALife()) {
                    menu.display("######## - Tour " + round +
                            " de " + player.getName() +
                            " " + playerNbr + "/" + players.size() +
                            " - ########\n" + player);
                    whatDoesThePlayerDoInHisTurn(player);
                    positionPlayerAtStart=player.getPosition();
                    playerMouve(player);

                    whatIsInTheCase(player);

                }
            }
        } else {
            gameInProgress = false;
        }
        if (gameInProgress) {
            //TODO demander nouveaux tour
            playRound();
        } else {
            //TODO afficher stats de la partie
            //TODO proposer une nouvelle partie avec les mm personnage ou pas..;
            menu.display(players.toString());
        }
    }
    public void tryRunAway(SufferedAnAttack player,SufferedAnAttack enemy ){
        if(rand.nextInt(6)>(dungeonLevel-1)) {
            menu.display("Vous parvenez à fuir en position: "+positionPlayerAtStart);
            player.setPosition(positionPlayerAtStart);
        }else {
            menu.display("Vous rater votre fuite....");
            player.sufferedAnAttack(enemy, menu, this);
        }
    }
    public void whatDoesThePlayerDoDuringTheFight(SufferedAnAttack player,SufferedAnAttack enemy, Menu menu){
        menu.displayChoice("Combat",
                new String[]{"'1' pour lancer une attaque",
                "'2' pour tenter de fuir ...("+(6-dungeonLevel)+"/6 de réussit)",
                "'0' pour quitter le jeu.."});
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> enemy.sufferedAnAttack(player, menu,this));
        functionChoiceMap.put("2", () -> tryRunAway(player,enemy));
        functionChoiceMap.put("0", () -> menu.exitGame(this));
        functionChoiceMap.put("666", () -> whatDoesThePlayerDoDuringTheFight(player,enemy,menu));
        menu.listenerChoice(functionChoiceMap);
    }
    public void whatDoesThePlayerDoInHisTurn(Hero player) {
        List<String> choices = new ArrayList<>();
        Map<String, Runnable> functionChoiceMap = new HashMap<>();
        functionChoiceMap.put("1", () -> playerMouve(player));
        choices.add("'1' pour lancer le dé");

        if (player.getLife() < player.getLifeMax()) {
            choices.add("'2' pour passer votre tour et regagner 1PV");
            functionChoiceMap.put("2", () -> playerStays(player));
            if (!player.displayInventory().equals("Inventaire:")) {
                choices.add("'3' pour passer votre tour et consommer une potion");
                functionChoiceMap.put("3", () -> playerStaysAndTakeHeal(player));
            }
        }
        choices.add("'0' pour quitter le jeu");
        functionChoiceMap.put("666", () -> whatDoesThePlayerDoInHisTurn(player));
        functionChoiceMap.put("0", () -> menu.exitGame(this));
        String[] choicesArray = new String[choices.size()];
        menu.displayChoice("Debut de votre tour", choices.toArray(choicesArray));
        menu.listenerChoice(functionChoiceMap);
    }

    public void playerStays(Hero player){
        player.setLife(player.getLife()+1);
    }

    public void playerStaysAndTakeHeal(Hero player){
        player.selectHeal(menu);
    }

    public void playerMouve(Hero player) {

        int dieRoll = this.dieRoll();
        int positionPlayer = player.getPosition();
        positionPlayer += dieRoll;
        menu.display("\n# Il lance le dé ..." +
                "\n# Il obtient: " + dieRoll +
                "\n# " + (positionPlayer <= this.nbrRoom ?
                "Se qui l'amène dans la pieces: " +positionPlayer:
                ""));
        if (positionPlayer > this.nbrRoom) {
            // TODO exception personnage hors limite
            positionPlayer -= (dieRoll * rand.nextInt(dieRoll / 2) + dieRoll);
            menu.display("Un vortex la téléporter en position: " + positionPlayer);
        } else if (positionPlayer == this.nbrRoom) {
            // TODO partie fini à la fin du tour proposer une nouvelle partie
//            displayPlayerGetOut(player);
            gameInProgress = false;
        }
        player.setPosition(positionPlayer);
    }


    public boolean allPlayerDead() {
        int count = 0;
        for (Hero player :
                players) {
            if (!player.isALife()) {
                count++;
            }
        }
        return count == players.size();
    }


    public int dieRoll() {
        return 1 + rand.nextInt(6);
    }
    public void whatIsInTheCase(Hero player) {
        Room room=level.get(player.getPosition());
        menu.display("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"+room.toString()+"\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        if (Enemy.class.equals(room.getClass().getSuperclass())) {
            ((Enemy) room).sufferedAnAttack(player, menu,this);
        } else if (Chest.class.equals(room.getClass())) {
            openChest(player,room);
        }
    }

    public void openChest(Hero player, Room room){

        Chest chest=(Chest) room;
        if(chest.getDefensive()!=null){
            if(Objects.equals(player.getType(), chest.getDefensive().getType())){
                if( player.getDefensive().getStats() < chest.getDefensive().getStats()) {
                    player.setDefensive(chest.getDefensive());
                  level.add(player.getPosition(),new Empty());////
                }else{
                    menu.display("# J'ai déjà mieux");
                }
            }else {
                menu.display("# Ce n'est pas pour moi");
            }
        } else if (chest.getOffensive()!=null) {
            if(Objects.equals(player.getType(), chest.getOffensive().getType())){
                if( player.getOffensive().getStats() < chest.getOffensive().getStats()) {
                    player.setOffensive(chest.getOffensive());
                    level.add(player.getPosition(), new Empty() );////
                }else{
                    menu.display("# J'ai déjà mieux");
                }
            }else {
                menu.display("# Ce n'est pas pour moi");
            }
        }else {
            level.add(player.getPosition(), player.addHealToInventory(room,menu));////
        }
    }

//    public Room takeLoot(Room chestRoom, Character player) {
//        ChestRoom chest = ((ChestRoom) chestRoom);
//
//        return chestRoom;
//    }

//    public void takeLootPotion(ChestRoom chest, Character player) {
//        player.addInInventory(chest.getPotion());
//    }

//    public Room takeLootDefensive(Room chestRoom, Character player) {
//        ChestRoom chest = ((ChestRoom) chestRoom);
//
//        return chestRoom;
//    }

//    public Room takeLootOffensive(Room chestRoom, Character player) {
//        ChestRoom chest = ((ChestRoom) chestRoom);
//
//        return chestRoom;
//    }


    ///////////////////////////////////////////////

    public List<Hero> getPlayers() {
        return players;
    }

    public void setPlayers(List<Hero> players) {
        this.players = players;
    }

    public void addPlayers(Hero player) {
        this.players.add(player);
    }

    public void setStates(GameState states) {
        this.states = states;
    }
}
