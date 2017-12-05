package ConquerSpace.util;

/**
 *
 * @author Zyun
 */
public class Version {

    int major;
    int minor;
    int patch;
    String extras;

    public Version(int major, int minor, int patch, String extras) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.extras = extras;
    }

    @Override
    public String toString() {
        return ("" + major + "." + minor + "." + patch + "-" + extras);
    }
}
