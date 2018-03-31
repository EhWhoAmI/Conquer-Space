package ConquerSpace.game;

/**
 * Action of a turn.
 * @author Zyun
 */
public class Action {
    private UniversePath selected;
    private int actionDone;

    public Action(UniversePath selected, int actionDone) {
        this.selected = selected;
        this.actionDone = actionDone;
    }
}
