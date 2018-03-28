import random
from ConquerSpace.game.universe.spaceObjects import PlanetTypes
def selectRandomSuitablePlanet(sector, civClimate):
    start = random.randint(0, sector.getStarSystemCount() - 2)
    while start < sector.getStarSystemCount():
        # Select star system
        system = sector.getStarSystem(start)
        n = 0
        for n < system.getPlanetCount():
            if random.randint(1, 2) == 1:
                p = system.getPlanet(n)
                if n.getPlanetType() == PlanetTypes.ROCK and p.getOrbitalDistance() < 20:
                    value = [start, n]
                    return value
            n += 1
        start += 1