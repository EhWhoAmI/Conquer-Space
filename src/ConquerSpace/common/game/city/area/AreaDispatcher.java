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

package ConquerSpace.common.game.city.area;

/**
 *
 * @author EhWhoAmI
 */
public interface AreaDispatcher {

    void dispatch(CommercialArea area);

    void dispatch(ConstructingArea area);

    void dispatch(ConsumerArea area);

    void dispatch(CustomComponentFactoryManufacturerArea area);

    void dispatch(TimedManufacturerArea area);

    void dispatch(ManufacturerArea area);

    void dispatch(MineArea area);

    void dispatch(SpacePortArea area);

    void dispatch(LogisticsHubArea area);

    void dispatch(ObservatoryArea area);

    void dispatch(ResourceStockpileArea area);

    void dispatch(FarmFieldArea area);

    void dispatch(PowerPlantArea area);

    void dispatch(ResearchArea area);

    void dispatch(ResidentialArea area);

    void dispatch(InfrastructureArea area);

    void dispatch(CapitolArea area);
}
