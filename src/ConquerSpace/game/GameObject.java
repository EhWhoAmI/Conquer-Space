package ConquerSpace.game;

/**
 * This is a game object, used to ID all the in game objects.
 *
 * @deprecated
 * @author Zyun
 */
public class GameObject {

    /**
     * The counter of the ID.
     */
    private static long IDCounter = 0;

    /**
     * The object ID.
     */
    protected long ObjectID;

    /**
     * Constructor that will be inherited by all the game objects. Will init the
     * ID.
     */
    public GameObject() {
        this.ObjectID = IDCounter;
        IDCounter++;
    }

    /**
     * Get the ID of the game object.
     * @return the game object ID
     */
    final public long getGameObjectID() {
        return (ObjectID);
    }
}
