package ConquerSpace.game.universe;

/**
 *
 * @author zyunl
 */
public class GeographicPoint {
    private int X;
    private int Y;

    public GeographicPoint() {
        X = 0;
        Y = 0;
    }

    public GeographicPoint(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GeographicPoint) {
            GeographicPoint ither = (GeographicPoint) obj;
            return ((ither.X == X) && (ither.Y == Y));
        }
        return false;
    }  

    @Override
    public int hashCode() {
        return (X + "" + Y).hashCode();
    }
}
