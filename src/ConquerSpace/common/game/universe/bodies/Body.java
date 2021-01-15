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
package ConquerSpace.common.game.universe.bodies;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.universe.Orbit;
import ConquerSpace.common.game.universe.SpacePoint;
import ConquerSpace.common.game.universe.UniversePath;

/**
 *
 * @author EhWhoAmI
 */
public class Body extends ConquerSpaceGameObject{
    /**
     * Describes the orbit around the reference body.
     */
    public Orbit orbit = new Orbit(0, 0, 0, 0);

    //Not serialized because it's based on orbit
    public SpacePoint point = new SpacePoint(0, 0);
    
    //Body it's orbiting
    public Body referenceBody;
    
    public Body(GameState gameState) {
        super(gameState);
    }

    public Orbit getOrbit() {
        return orbit;
    }

    public SpacePoint getPoint() {
        return point;
    }

    public void setOrbit(Orbit orbit) {
        this.orbit = orbit;
    }

    public void setPoint(SpacePoint point) {
        this.point = point;
    }

    public double getDegrees() {
        return orbit.degrees;
    }

    public double getEccentricity() {
        return orbit.eccentricity;
    }

    public double getRotation() {
        return orbit.rotation;
    }

    public double getSemiMajorAxis() {
        return orbit.semiMajorAxis;
    }

    public void setDegrees(double degrees) {
        orbit.degrees = degrees;
    }

    public void setEccentricity(double eccentricity) {
        orbit.eccentricity = eccentricity;
    }

    public void setRotation(double rotation) {
        orbit.rotation = rotation;
    }

    public void setSemiMajorAxis(double semiMajorAxis) {
        orbit.semiMajorAxis = semiMajorAxis;
    }

    public void modDegrees(float degs) {
        orbit.modDegrees(degs);
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public double getOrbitalDistance() {
        return orbit.toPolarCoordinate().getDistance();
    }

    public Body getReferenceBody() {
        return referenceBody;
    }

    public void setReferenceBody(Body referenceBody) {
        this.referenceBody = referenceBody;
    }

    public UniversePath getUniversePath() {
        return new UniversePath();
    }
}
