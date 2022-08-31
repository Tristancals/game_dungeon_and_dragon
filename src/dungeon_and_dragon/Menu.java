package dungeon_and_dragon;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);

    public Menu() {
    }

    public void launchGame(Game game) {
        System.out.println("launchGame");
        String choice;
        displayChoice("Bienvenus dans DONJON & DRAGON",
                new String[]{"'entrer' pour lancer la partie",
                        "'0' pour quitter le jeu"});
        choice = scanner.nextLine();
        switch (choice) {
            case "" -> selectNbrPlayers(game);
            case "0" -> exitGame(game);
            default -> launchGame(game);
        }
    }

   public void selectNbrPlayers(Game game){
        System.out.println("selectNbrPlayers");
        game.setStates(GameState.END);
    }
    public void exitGame(Game game){
        System.out.println("exitGame");
        game.setStates(GameState.END);
    }

    public void displayChoice(String request, String[] choices) {
        System.out.println("displayChoice");
        String msg = "######## - " + request + " - ########";
        for (String choice : choices) {
            msg += "\n#      - " + choice;
        }
        display(msg);
    }

    public void display(String msg) {
        System.out.println("display");
        System.out.println(msg);
    }

}
