package dungeon_and_dragon.exceptions;

public class PlayerOutOfBoardException extends Exception{
    public PlayerOutOfBoardException(String message) {
        super(message);
    }
}
