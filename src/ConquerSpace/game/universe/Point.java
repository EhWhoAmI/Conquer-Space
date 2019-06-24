package ConquerSpace.game.universe;

/**
 *
 * @author zyunl
 */
public class Point {
    private long x;
    private long y;

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return (int) (Long.parseLong("" + x + y) % Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }  
}
