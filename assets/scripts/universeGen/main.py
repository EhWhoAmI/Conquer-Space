# universeGen/main.py -- generates universe, loads it into xml file
# Get universe config, then parse the XML into file

from datetime import datetime
import random
import xml.etree.ElementTree as ET
from os.path import *
from os import *

# Import universe files
from ConquerSpace.game.universe import Universe, Sector, StarSystem, Star, Planet, PlanetTypes, StarTypes, GalaticLocation 

# universe size -- change this when universe sizes change
universeSize = {'Small': 2, 'Medium': 3, 'Large': 4}[universeConfig.getUniverseSize()]

# Universe Shape
universeShape = {'Spiral': 1, 'Irregular': 2, 'Elliptical': 3}[universeConfig.getUniverseShape()]

# Universe Age -- Doesnt really matter that much, but keep it anyway
universeHistory = {'Short': 1, 'Medium': 2, 'Long': 3, 'Ancient': 4}[universeConfig.getUniverseAge()]

# Civ count
civCount = {'Sparse': 1, 'Common': 2}[universeConfig.getCivilizationCount()]

# Planet count
planetCommonality = {'Common': 1, 'Sparse': 2}[universeConfig.getPlanetCommonality()]

# Random seed
seed = 0
# Seed random
random.seed(seed)

# Create universe generation object
universeObject = Universe()

# Create sectors
# Get sector count
for i in range(universeSize):
    # Set galatic location
    # random degrees, 360 degrees. Take 360 * 4, then select a random one.
    secdegs = (random.randint(0, 360 * 4)/4)
    secdist = random.randint(0, 100)
    sectorLoc = GalaticLocation(secdegs, secdist)
    
    # Sector
    sector = Sector(sectorLoc, i)
    
    starSystemCount = random.randint(10, 20)
    # Add star systems
    for r in range(starSystemCount):
        # Galatic location
        sysdegs = (random.randint(0, 360 * 4)/4)
        sysdist = random.randint(0, 100)
        systemLoc = GalaticLocation(sysdegs, sysdist)
        starSystem = StarSystem(r, systemLoc)
        # Create star
        starType = random.randint(0, 3)
        starSize = random.randint(1, 10)
        # 0 because there is only one star so far.
        star = Star(starType, starSize, 0)
        starSystem.addStar(star)
        
        planets = random.randint(0, 9)
        
        for n in range(planets):
            # Planets
            ptype = random.randint(0, 1)
            orbitalDistance = random.randint(1, 100)
            planetSize = random.randint(1, 50)
            planet = Planet(ptype, orbitalDistance, planetSize, n)
            starSystem.addPlanet(planet)
            
        sector.addStarSystem(starSystem)
        
    universeObject.addSector(sector)
