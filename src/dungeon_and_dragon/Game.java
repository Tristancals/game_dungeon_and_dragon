package dungeon_and_dragon;

import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.rooms.Room;

import java.util.*;


public class Game {

    private final java.util.Random rand = new java.util.Random();
    private Dungeon dungeon;
    private List<Room> level=new ArrayList<>();
    private List<Hero> players=new ArrayList<>();
    private int dungeonLevel;
    private GameState states=GameState.PREPARATION;//TODO PREPARATION START
    private final Menu menu;


    public Game(Menu menu) {
        this.menu = menu;
        start();
    }


    public void start() {
        boolean playing = true;
        while (playing) {
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
                    //
                    break;
                case CONCLUSION:
                    System.out.println("---CONCLUSION---");
                    ////
                    break;
                case END:
                    System.out.println("---END---");
                    playing=false;
                    ///
                    break;
            }
        }
    }

    public void selectDifficultyDungeon(){
        System.out.println("f-selectDifficultyDungeon");

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
    public void initDungeon(int nbrRoom,int dungeonLevel){
        dungeon=new Dungeon(nbrRoom,dungeonLevel);
        level=dungeon.getLevel();
        System.out.println(level);
        setStates(GameState.END);
    }

    ///////////////////////////////////////////////
//    public void whatIsInTheCase(Character player) {
//
//    }

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



//    public int dieRoll() {
//        return 1 + rand.nextInt(6);
//    }





    ///////////////////////////////////////////////

    public List<Hero> getPlayers() {
        return players;
    }

    public void setPlayers( List<Hero> players) {
        this.players = players;
    }
    public void addPlayers( Hero player) {
        this.players.add(player);
    }
    public void setStates(GameState states) {
        this.states = states;
    }
}
