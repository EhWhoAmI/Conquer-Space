package ConquerSpace.game.universe.ships.components;

import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;

/**
 *
 * @author Zyun
 */
public class EngineComponent extends ShipComponent{

    private EngineTechnology engineTech;
    
    public EngineComponent() {
        super(0, 0, null);
    }

    @Override
    public String getRatingType() {
        return "Thrust (kn)";
    }

    @Override
    public String toString() {
        return super.toString() + " " + engineTech.getName() + " engine";
    }

    public void setEngineTech(EngineTechnology engineTech) {
        this.engineTech = engineTech;
    }

    public EngineTechnology getEngineTech() {
        return engineTech;
    }
}
