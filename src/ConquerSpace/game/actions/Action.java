package ConquerSpace.game.actions;

import ConquerSpace.game.UniversePath;

/**
 * Action of a turn.
 * @author Zyun
 */
public class Action {
    /**
     * The selected object
     */
    private UniversePath selected;

    public Action(UniversePath selected) {
        this.selected = selected;
    }

    public UniversePath getSelected() {
        return selected;
    } 
}
