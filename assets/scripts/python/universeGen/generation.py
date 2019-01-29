import random
from ConquerSpace.game.universe.spaceObjects import PlanetTypes
def selectRandomSuitablePlanet(systemCount, civClimate, universe, limits):
    start = random.randint(0, systemCount - 2)
    while start < systemCount:
        # Select star system
        system = universe.getStarSystem(start)
        n = 0
        while n < system.getPlanetCount():
            if random.randint(1, 2) == 1:
                p = system.getPlanet(n)
                # Check if owned by any other civ
                
                if p.getPlanetType() == PlanetTypes.ROCK and p.getOrbitalDistance() < 300000000 and p.getPlanetSectorCount() > 4 and not(p.getUniversePath() in limits) :
                    value = [start, n]
                    return value
            n += 1
        start += 1
        if start >= systemCount:
            start = 0
            
def calculatePlanetSpacing(previous):
    if previous == 1:
        return 2
    amount = (float(random.randint(11, 25))/10)
    return long(previous*amount)