package ConquerSpace.game.events;

/**
 *
 * @author zyunl
 */
public class Event {

    private int id;
    private String text;

    public Event(String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return text;
    }
}
