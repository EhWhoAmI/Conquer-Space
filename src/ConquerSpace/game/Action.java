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
    
    private String[] args;

    public Action(UniversePath selected, int actionDone, String[] args) {
        this.selected = selected;
        this.actionDone = actionDone;
        this.args = args;
    }

    public int getActionDone() {
        return actionDone;
    }

    public String[] getArgs() {
        return args;
    }

    public UniversePath getSelected() {
        return selected;
    }
    
    
}
