package dungeon_and_dragon;

import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.interfaces.Interactable;

public class Case<T> implements Interactable{

    private T event;

    public T getEvent() {
        return event;
    }

    public void setEvent(T event) {
        this.event = event;
    }

    public Case(T event) {
        this.event = event;
    }


    @Override
    public void interact(Hero player, Menu menu, Game game) {
        if(event instanceof Interactable){
            ((Interactable) event).interact(player, menu, game);
        }
    }
}
