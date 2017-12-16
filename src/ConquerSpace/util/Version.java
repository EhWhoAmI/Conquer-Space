package ConquerSpace.util;

/**
 *
 * @author Zyun
 */
public class Version {

    private int major;
    private int minor;
    private int patch;
    private String extras;

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
    
    static boolean isGreater(Version v1, Version v2) {
        if (v1.major > v2.major && v1.minor > v2.minor && v1.patch > v2.patch)
            return true;
        return false;
    }
    
    //Ultra lazy with this one
    static boolean isLess(Version v1, Version v2) {
        return (!isGreater(v1, v2));
    }
}
