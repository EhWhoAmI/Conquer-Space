package ConquerSpace.game;

/**
 * Action of a turn.
 * @author Zyun
 */
public class Action {
    /**
     * The selected object
     */
    private UniversePath selected;
    
    /**
     * ID of the action
     */
    private int actionDone;

    public Action(UniversePath selected, int actionDone) {
        this.selected = selected;
        this.actionDone = actionDone;
    }
}
