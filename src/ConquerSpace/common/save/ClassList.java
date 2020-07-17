/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.common.save;

import ConquerSpace.common.game.characters.*;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.*;
import ConquerSpace.common.game.life.*;
import ConquerSpace.common.game.organizations.*;
import ConquerSpace.common.game.organizations.civilization.*;
import ConquerSpace.common.game.organizations.corp.*;
import ConquerSpace.common.game.population.*;
import ConquerSpace.common.game.resources.*;
import ConquerSpace.common.game.ships.*;
import ConquerSpace.common.game.ships.components.engine.*;
import ConquerSpace.common.game.ships.components.templates.*;
import ConquerSpace.common.game.ships.hull.*;
import ConquerSpace.common.game.ships.launch.*;
import ConquerSpace.common.game.ships.satellites.*;
import ConquerSpace.common.game.universe.bodies.*;

/**
 *
 * @author EhWhoAmI
 */
public class ClassList {

    public static Class[] classes = {
        Stratum.class,
        Species.class,
        Administrator.class,
        ConstructingArea.class,
        HullMaterial.class,
        FarmFieldArea.class,
        SpaceTelescope.class,
        ResourceStockpileArea.class,
        ConsumerArea.class,
        LaunchSystem.class,
        LaunchVehicle.class,
        Galaxy.class,
        ShipComponentTemplate.class,
        Scientist.class,
        EngineTemplate.class,
        CapitolArea.class,
        ResearchArea.class,
        ObservatoryArea.class,
        Civilization.class,
        Company.class,
        Person.class,
        ShipClass.class,
        ManufacturerArea.class,
        MineArea.class,
        Star.class,
        SpaceShip.class,
        Hull.class,
        ResidentialArea.class,
        TimedManufacturerArea.class,
        CommercialArea.class,
        Race.class,
        Ship.class,
        Satellite.class,
        NoneSatellite.class,
        StarSystemBody.class,
        PowerPlantArea.class,
        InfrastructureArea.class,
        SpacePortArea.class,
        PopulationSegment.class,
        Organization.class,
        Body.class,
        Culture.class,
        EngineTechnology.class,
        StarSystem.class,
        City.class,
        Area.class,
        Population.class,
        Planet.class
    };
}
