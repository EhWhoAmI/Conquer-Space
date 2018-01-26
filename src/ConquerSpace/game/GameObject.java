package ConquerSpace.game;

public class GameObject {
	private static long IDCounter = 0;
	protected long ObjectID;
	
	public GameObject() {
		this.ObjectID = IDCounter;
		IDCounter ++;
	}
	
	final public long getGameObjectID() {
		return (ObjectID);
	}
}