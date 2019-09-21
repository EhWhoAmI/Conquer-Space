package ConquerSpace.game.life;

/**
 *
 * @author zyunl
 */
public enum LifeTrait {
    Photosynthetic("Photosynthetic"),
    Delicious("Delicious"),
    HighAcidTolerance("High Acidity Tolerance"),
    HighAkalineTolerance("High Akaline Tolerance"),
    HighPHRange("High PH Range Tolerance"),
    Domesticated("Domesticated");
    
    private String name;
    
    private LifeTrait(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
