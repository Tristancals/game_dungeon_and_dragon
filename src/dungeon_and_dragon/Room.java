package dungeon_and_dragon;

import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.interfaces.Interactable;

public class Room<T> implements Interactable{

    private T event;

    public T getEvent() {
        return event;
    }

    public void setEvent(T event) {
        this.event = event;
    }

    public Room(T event) {
        this.event = event;
    }


    @Override
    public void interact(Hero player, Menu menu, Game game) {
            ((Interactable) event).interact(player, menu, game);
    }
}
