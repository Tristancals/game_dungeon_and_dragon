package dungeon_and_dragon;

import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.rooms.Room;

import java.util.ArrayList;
import java.util.List;


public class Game {
    private final java.util.Random rand = new java.util.Random();
    private final Room[] dungeon = new Room[65];
    private List<Hero> players=new ArrayList<>();
    private int dungeonLevel;
    private GameState states=GameState.START;
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
                    System.out.println("start");
                    menu.launchGame(this);
                    break;
                case PREPARATION:
                    System.out.println("preparation");
                    ///
                    break;
                case GAME:
                    System.out.println("game");
                    //
                    break;
                case CONCLUSION:
                    System.out.println("conclusion");
                    ////
                    break;
                case END:
                    System.out.println("end");
                    playing=false;
                    ///
                    break;
            }
        }
    }

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
